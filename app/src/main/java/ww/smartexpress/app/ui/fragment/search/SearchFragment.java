package ww.smartexpress.app.ui.fragment.search;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.response.BookLocation;
import ww.smartexpress.app.data.model.api.response.LocationType;
import ww.smartexpress.app.data.model.api.response.SavedLocation;
import ww.smartexpress.app.data.model.api.response.SearchLocation;
import ww.smartexpress.app.databinding.FragmentSearchBinding;
import ww.smartexpress.app.di.component.FragmentComponent;
import ww.smartexpress.app.ui.base.fragment.BaseFragment;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.search.location.SearchLocationActivity;
import ww.smartexpress.app.ui.search.location.adapter.LocationTypeAdapter;
import ww.smartexpress.app.ui.search.location.adapter.SavedLocationAdapter;
import ww.smartexpress.app.ui.search.location.adapter.SearchLocationAdapter;

public class SearchFragment extends BaseFragment<FragmentSearchBinding, SearchFragmentViewModel> implements LocationListener {
    SavedLocationAdapter savedLocationAdapter;
    SearchLocationAdapter searchLocationAdapter;
    LocationTypeAdapter locationTypeAdapter;
    List<SearchLocation> fromLocations = new ArrayList<>();
    List<SearchLocation> toLocations  = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationClient;

    private LatLng currentLocation;


    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void performDataBinding() {
        binding.setA(this);
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getCurrentBooking();
        //loadSavedLocation();
        //loadSearchLocation();
        //loadLocationType();

        //binding.setLifecycleOwner(this);

        binding.edtDeparture.addTextChangedListener(new GenericTextWatcher(binding.edtDeparture));
        binding.edtSearchLocation.addTextChangedListener(new GenericTextWatcher(binding.edtSearchLocation));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        viewModel.latlng.set(String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude()));
                        viewModel.compositeDisposable.add(viewModel.getLocationInfo()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(response -> {
                                    String status = response.get("status").getAsString();
                                    Log.d("TAG", status);

                                    JsonArray results = response.getAsJsonArray("results");
                                    int index = results.size() - 1;

                                    String component = results.get(0).getAsJsonObject().get("formatted_address").getAsString();
                                    String id = results.get(0).getAsJsonObject().get("place_id").getAsString();
                                    Log.d("TAG", "onLocationChanged: " + component);

                                    viewModel.location.set(component);
                                    viewModel.originId.set(id);
                                    binding.edtSearchLocation.requestFocus();
//
                                },error->{
                                    viewModel.hideLoading();
                                    viewModel.showErrorMessage(getString(R.string.network_error));
                                    error.printStackTrace();
                                })
                        );
                    });
        }

    }

    public void loadSavedLocation(){
        List<SavedLocation> savedLocationList = new ArrayList<>();
        savedLocationList.add(new SavedLocation("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
        savedLocationList.add(new SavedLocation("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
        savedLocationList.add(new SavedLocation("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));

        binding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);

        binding.rcSavedLocation.setLayoutManager(layoutManager);
        binding.rcSavedLocation.setItemAnimator(new DefaultItemAnimator());
        savedLocationAdapter = new SavedLocationAdapter(savedLocationList);
        binding.rcSavedLocation.setAdapter(savedLocationAdapter);

        savedLocationAdapter.setOnItemClickListener(location -> {
            viewModel.searchLocation.set(location.getAddress());
        });
    }

    public void loadSearchLocation(){
        loadSearch();
        binding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);

        binding.rcSavedLocation.setLayoutManager(layoutManager);
        binding.rcSavedLocation.setItemAnimator(new DefaultItemAnimator());
        searchLocationAdapter = new SearchLocationAdapter(toLocations);
        binding.rcSavedLocation.setAdapter(searchLocationAdapter);

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
        binding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);

        binding.rcSavedLocation.setLayoutManager(layoutManager);
        binding.rcSavedLocation.setItemAnimator(new DefaultItemAnimator());
        searchLocationAdapter = new SearchLocationAdapter(fromLocations);
        binding.rcSavedLocation.setAdapter(searchLocationAdapter);

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

    public void loadLocationType(){
        List<LocationType> locationTypeList = new ArrayList<>();
        locationTypeList.add(new LocationType("1", "Nhà riêng", ""));
        locationTypeList.add(new LocationType("1", "Công ty", ""));
        locationTypeList.add(new LocationType("1", "Thêm địa điểm", ""));

        binding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()
                ,LinearLayoutManager.HORIZONTAL, false);

        binding.rcLocationType.setLayoutManager(layoutManager);
        binding.rcLocationType.setItemAnimator(new DefaultItemAnimator());
        locationTypeAdapter = new LocationTypeAdapter(locationTypeList);
        binding.rcLocationType.setAdapter(locationTypeAdapter);

        locationTypeAdapter.setOnItemClickListener(locationType -> {

        });
    }

    public class GenericTextWatcher implements TextWatcher {
        private View currentView;

        public GenericTextWatcher(View currentView) {
            this.currentView = currentView;

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();

            switch (currentView.getId()){
                case R.id.edtSearchLocation:
                    if(!TextUtils.isEmpty(text)){
                        loadSearchLocation();
                    }else{
                        //loadSavedLocation();
                        toLocations.clear();
                        searchLocationAdapter.clearItems();
                        Log.d("TAG", "afterTextChanged:1 ");
                    }
                    break;
                case R.id.edtDeparture:
                    if(!TextUtils.isEmpty(text)){
                        loadDepartureLocation();
                    }else{
                        //loadSavedLocation();
                        fromLocations.clear();
                        searchLocationAdapter.clearItems();
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
        viewModel.location.set("");
        viewModel.searchLocation.set("");
        viewModel.originId.set("");
        viewModel.destinationId.set("");
        binding.edtDeparture.requestFocus();
        binding.edtSearchLocation.clearFocus();

    }
}
