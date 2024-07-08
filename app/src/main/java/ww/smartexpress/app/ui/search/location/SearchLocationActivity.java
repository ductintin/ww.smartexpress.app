package ww.smartexpress.app.ui.search.location;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.response.AddressMap;
import ww.smartexpress.app.data.model.api.response.LocationType;
import ww.smartexpress.app.data.model.api.response.Note;
import ww.smartexpress.app.data.model.api.response.SavedLocation;
import ww.smartexpress.app.data.model.api.response.SearchLocation;
import ww.smartexpress.app.data.model.room.AddressEntity;
import ww.smartexpress.app.databinding.ActivitySearchLocationBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.map.MapActivity;
import ww.smartexpress.app.ui.profile.EditProfileActivity;
import ww.smartexpress.app.ui.search.SearchActivity;
import ww.smartexpress.app.ui.search.location.adapter.LocationTypeAdapter;
import ww.smartexpress.app.ui.search.location.adapter.SavedLocationAdapter;
import ww.smartexpress.app.ui.search.location.adapter.SearchLocationAdapter;

public class SearchLocationActivity extends BaseActivity<ActivitySearchLocationBinding, SearchLocationViewModel> implements LocationListener {
    SavedLocationAdapter savedLocationAdapter;
    SearchLocationAdapter searchLocationAdapter;
    LocationTypeAdapter locationTypeAdapter;
    List<SearchLocation> fromLocations = new ArrayList<>();
    List<SearchLocation> toLocations  = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationClient;

    private LatLng currentLocation;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_location;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding.edtDeparture.addTextChangedListener(new GenericTextWatcher(viewBinding.edtDeparture));
        viewBinding.edtSearchLocation.addTextChangedListener(new GenericTextWatcher(viewBinding.edtSearchLocation));
        viewBinding.edtDeparture.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("TAG", "onFocusChange: depar" + b);
                if(b){
                    fromLocations.clear();
                    if(searchLocationAdapter != null){
                        searchLocationAdapter.clearItems();
                    }

                    if(savedLocationAdapter != null){
                        viewBinding.tvSavedLocation.setVisibility(View.GONE);
                        savedLocationAdapter.clearItems();
                        if(TextUtils.isEmpty(viewModel.location.get())){
                            loadSavedLocationForOrigin();
                        }
                    }
                }
            }
        });

        viewBinding.edtSearchLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("TAG", "onFocusChange: se" + b);
                if(b){
                    toLocations.clear();
                    if(searchLocationAdapter != null){
                        searchLocationAdapter.clearItems();
                    }

                    if(savedLocationAdapter != null){
                        viewBinding.tvSavedLocation.setVisibility(View.GONE);
                        savedLocationAdapter.clearItems();
                        if(TextUtils.isEmpty(viewModel.searchLocation.get())){
                            loadSavedLocationForDestination();
                        }
                    }
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String component = bundle.getString("PLACE_NAME");
            String id = bundle.getString("PLACE_ID");
            if(fromLocations.size() == 0 && viewModel.destinationId.get().isEmpty()){
                viewModel.location.set(component);
                viewModel.originId.set(id);
                viewBinding.edtSearchLocation.requestFocus();
                loadSavedLocationForDestination();
            }

        }else{
            loadSavedLocationForOrigin();
        }

    }




    public void loadSavedLocationForOrigin(){
        List<SearchLocation> savedLocationList = new ArrayList<>();
//        savedLocationList.add(new SavedLocation("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
//        savedLocationList.add(new SavedLocation("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
//        savedLocationList.add(new SavedLocation("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));

        viewBinding.setLifecycleOwner(this);

        viewModel.getAllSavedAddress().subscribeOn(Schedulers.io()) // Chọn Scheduler để thực thi trên luồng I/O
                .observeOn(AndroidSchedulers.mainThread()) // Chọn Scheduler để xử lý kết quả trên luồng chính (UI thread)
                .subscribe(new SingleObserver<List<AddressEntity>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<AddressEntity> addressEntities) {
                        if(addressEntities.size() > 0){
                            for (AddressEntity address : addressEntities) {
                                savedLocationList.add(SearchLocation
                                        .builder()
                                        .place_id(address.getPlace_id())
                                        .description(address.getDescription())
                                        .structured_formatting(
                                                SearchLocation.Structure.
                                                        builder()
                                                        .main_text(address.getMain_text())
                                                        .build())
                                        .build()
                                );

                            }
                            Log.d("TAG", "onSuccess: " + addressEntities.toString());

                            Log.d("TAG", "loadSavedLocationForOrigin: " + savedLocationList);

                            viewBinding.tvSavedLocation.setVisibility(View.VISIBLE);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                                    ,LinearLayoutManager.VERTICAL, false);

                            viewBinding.rcSavedLocation.setLayoutManager(layoutManager);
                            viewBinding.rcSavedLocation.setItemAnimator(new DefaultItemAnimator());
                            savedLocationAdapter = new SavedLocationAdapter(savedLocationList);
                            viewBinding.rcSavedLocation.setAdapter(savedLocationAdapter);

                            savedLocationAdapter.setOnItemClickListener(location -> {
                                viewModel.location.set(location.getDescription());
                                viewModel.origin.set(location);
                                viewModel.bookLocation.get().setOrigin(location);
                                viewModel.originId.set(location.getPlace_id());
                                fromLocations.clear();
                            });
                        }else{
                            viewBinding.tvSavedLocation.setVisibility(View.GONE);

                        }
                    }


                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });

    }

    public void loadSavedLocationForDestination(){
        List<SearchLocation> savedLocationList = new ArrayList<>();
//        savedLocationList.add(new SavedLocation("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
//        savedLocationList.add(new SavedLocation("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
//        savedLocationList.add(new SavedLocation("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));

        viewBinding.setLifecycleOwner(this);
        viewModel.getAllSavedAddress().subscribeOn(Schedulers.io()) // Chọn Scheduler để thực thi trên luồng I/O
                .observeOn(AndroidSchedulers.mainThread()) // Chọn Scheduler để xử lý kết quả trên luồng chính (UI thread)
                .subscribe(new SingleObserver<List<AddressEntity>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<AddressEntity> addressEntities) {
                        if(addressEntities.size() > 0){
                            for (AddressEntity address : addressEntities) {
                                savedLocationList.add(SearchLocation
                                        .builder()
                                        .place_id(address.getPlace_id())
                                        .description(address.getDescription())
                                        .structured_formatting(
                                                SearchLocation.Structure.
                                                        builder()
                                                        .main_text(address.getMain_text())
                                                        .build())
                                        .build()
                                );

                            }
                            Log.d("TAG", "onSuccess: " + addressEntities.toString());
                            Log.d("TAG", "loadSavedLocationForDestination: " + savedLocationList);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                                    ,LinearLayoutManager.VERTICAL, false);

                            viewBinding.tvSavedLocation.setVisibility(View.VISIBLE);

                            viewBinding.rcSavedLocation.setLayoutManager(layoutManager);
                            viewBinding.rcSavedLocation.setItemAnimator(new DefaultItemAnimator());
                            savedLocationAdapter = new SavedLocationAdapter(savedLocationList);
                            viewBinding.rcSavedLocation.setAdapter(savedLocationAdapter);

                            savedLocationAdapter.setOnItemClickListener(location -> {
                                viewModel.searchLocation.set(location.getDescription());
                                viewModel.destination.set(location);
                                viewModel.bookLocation.get().setDestination(location);
                                viewModel.destinationId.set(location.getPlace_id());
                                toLocations.clear();
                            });
                        }else{
                            viewBinding.tvSavedLocation.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });


    }

    public void loadSearchLocation(){
        loadSearch();

        viewBinding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);


        viewBinding.tvSavedLocation.setVisibility(View.GONE);
        viewBinding.rcSavedLocation.setLayoutManager(layoutManager);
        viewBinding.rcSavedLocation.setItemAnimator(new DefaultItemAnimator());
        searchLocationAdapter = new SearchLocationAdapter(toLocations);
        viewBinding.rcSavedLocation.setAdapter(searchLocationAdapter);

        searchLocationAdapter.setOnItemClickListener(location -> {
            viewModel.searchLocation.set(location.getDescription());
            viewModel.destination.set(location);
            viewModel.bookLocation.get().setDestination(location);
            viewModel.destinationId.set(location.getPlace_id());
            toLocations.clear();
        });
    }

    public void loadDepartureLocation(){
        loadDeparture();

        viewBinding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);

        viewBinding.tvSavedLocation.setVisibility(View.GONE);
        viewBinding.rcSavedLocation.setLayoutManager(layoutManager);
        viewBinding.rcSavedLocation.setItemAnimator(new DefaultItemAnimator());
        searchLocationAdapter = new SearchLocationAdapter(fromLocations);
        viewBinding.rcSavedLocation.setAdapter(searchLocationAdapter);

        searchLocationAdapter.setOnItemClickListener(location -> {
            viewModel.location.set(location.getDescription());
            viewModel.origin.set(location);
            viewModel.bookLocation.get().setOrigin(location);
            viewModel.originId.set(location.getPlace_id());
            fromLocations.clear();
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        viewModel.latlng.set(String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude()));
        viewModel.compositeDisposable.add(viewModel.getLocationInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    String status = response.get("status").getAsString();
                    if(status.equals("0K")){
                        JsonArray results = response.getAsJsonArray("results");
                        int index = results.size() - 1;

                        String component = results.get(index).getAsJsonObject().get("formatted_address").getAsString();

                        Log.d("TAG", "onLocationChanged: " + component);
                    }
                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    error.printStackTrace();
                })
        );


    }

public class GenericTextWatcher implements TextWatcher {
    private View currentView;

    public GenericTextWatcher(View currentView) {
        this.currentView = currentView;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString().trim();

        switch (currentView.getId()){
            case R.id.edtSearchLocation:
                if(!TextUtils.isEmpty(text)){
                    loadSearchLocation();
                }else{
                    loadSavedLocationForDestination();
                    Log.d("TAG", "afterTextChanged:1 ");
                }
                break;
            case R.id.edtDeparture:
                if(!TextUtils.isEmpty(text)){
                    loadDepartureLocation();
                }else{
                    loadSavedLocationForOrigin();
                    Log.d("TAG", "afterTextChanged:2 ");
                }
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // TODO: Implement as needed
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // TODO: Implement as needed1
    }

}

    public void loadSearch(){
        viewModel.compositeDisposable.add(viewModel.searchLocation(viewModel.searchLocation.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    toLocations = response.getPredictions();
                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    public void loadDeparture(){
        viewModel.compositeDisposable.add(viewModel.searchLocation(viewModel.location.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    fromLocations = response.getPredictions();

                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    public void getCurrentBooking(){
        viewModel.compositeDisposable.add(viewModel.getCurrentBooking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    viewModel.hideLoading();
                    if(response.isResult()){

                    }else{
                        viewModel.getApplication().getErrorUtils().handelError(response.getCode());
                    }
                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    public void openMap(int kind){
        Intent intent = new Intent(SearchLocationActivity.this, MapActivity.class);
        if(kind == 1 && !TextUtils.isEmpty(viewModel.originId.get())){
            intent.putExtra("ORIGIN_ID", viewModel.originId.get());
            intent.putExtra("ORIGIN_DESCRIPTION", viewModel.location.get());
        }else if(kind == 2 && !TextUtils.isEmpty(viewModel.destinationId.get())){
            intent.putExtra("ORIGIN_ID", viewModel.destinationId.get());
            intent.putExtra("ORIGIN_DESCRIPTION", viewModel.searchLocation.get());
        }
        intent.putExtra("KIND", kind);
        startActivity(intent);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDataEvent(AddressMap event) {
        if(event != null){
            SearchLocation searchLocation = SearchLocation.builder()
                    .description(event.getDescription())
                    .place_id(event.getId())
                    .structured_formatting(
                            SearchLocation.Structure.builder()
                                    .main_text(event.getDescription())
                                    .build())
                    .build();


            if(event.getKind() == 1){
                viewModel.origin.set(searchLocation);
                viewModel.location.set(event.getDescription());
                viewModel.originId.set(event.getId());
            }else{
                viewModel.destination.set(searchLocation);
                viewModel.searchLocation.set(event.getDescription());
                viewModel.destinationId.set(event.getId());
            }
            Log.d("TAG", "onDataEvent: " + event.getDescription());
        }
        // Xử lý dữ liệu ở đây
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().removeStickyEvent(AddressMap.class);
    }
}
