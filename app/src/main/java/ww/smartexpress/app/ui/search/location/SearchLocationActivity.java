package ww.smartexpress.app.ui.search.location;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.response.LocationType;
import ww.smartexpress.app.data.model.api.response.SavedLocation;
import ww.smartexpress.app.data.model.api.response.SearchLocation;
import ww.smartexpress.app.databinding.ActivitySearchLocationBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.profile.EditProfileActivity;
import ww.smartexpress.app.ui.search.SearchActivity;
import ww.smartexpress.app.ui.search.location.adapter.LocationTypeAdapter;
import ww.smartexpress.app.ui.search.location.adapter.SavedLocationAdapter;
import ww.smartexpress.app.ui.search.location.adapter.SearchLocationAdapter;

public class SearchLocationActivity extends BaseActivity<ActivitySearchLocationBinding, SearchLocationViewModel> {
    SavedLocationAdapter savedLocationAdapter;
    SearchLocationAdapter searchLocationAdapter;
    LocationTypeAdapter locationTypeAdapter;
    List<SearchLocation> fromLocations = new ArrayList<>();
    List<SearchLocation> toLocations  = new ArrayList<>();

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

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            //viewModel.categoryId.set(bundle.getLong(Constants.KEY_CATEGORY_ID, 0L));
            viewModel.serviceId.set(bundle.getLong(Constants.KEY_SERVICE_ID, 0L));
        }

        loadSavedLocation();
        //loadSearchLocation();
        loadLocationType();

        viewBinding.edtDeparture.addTextChangedListener(new GenericTextWatcher(viewBinding.edtDeparture));
        viewBinding.edtSearchLocation.addTextChangedListener(new GenericTextWatcher(viewBinding.edtSearchLocation));

    }

    public void loadSavedLocation(){
        List<SavedLocation> savedLocationList = new ArrayList<>();
        savedLocationList.add(new SavedLocation("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
        savedLocationList.add(new SavedLocation("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
        savedLocationList.add(new SavedLocation("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));

        viewBinding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);

        viewBinding.rcSavedLocation.setLayoutManager(layoutManager);
        viewBinding.rcSavedLocation.setItemAnimator(new DefaultItemAnimator());
        savedLocationAdapter = new SavedLocationAdapter(savedLocationList);
        viewBinding.rcSavedLocation.setAdapter(savedLocationAdapter);

        savedLocationAdapter.setOnItemClickListener(location -> {
            viewModel.searchLocation.set(location.getAddress());
        });
    }

    public void loadSearchLocation(){
        loadSearch();
        viewBinding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);

        viewBinding.rcSavedLocation.setLayoutManager(layoutManager);
        viewBinding.rcSavedLocation.setItemAnimator(new DefaultItemAnimator());
        searchLocationAdapter = new SearchLocationAdapter(toLocations);
        viewBinding.rcSavedLocation.setAdapter(searchLocationAdapter);

        searchLocationAdapter.setOnItemClickListener(location -> {
            viewModel.searchLocation.set(location.getDescription());
            viewModel.destinationId.set(location.getPlace_id());
        });
    }

    public void loadDepartureLocation(){
        loadDeparture();
        viewBinding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);

        viewBinding.rcSavedLocation.setLayoutManager(layoutManager);
        viewBinding.rcSavedLocation.setItemAnimator(new DefaultItemAnimator());
        searchLocationAdapter = new SearchLocationAdapter(fromLocations);
        viewBinding.rcSavedLocation.setAdapter(searchLocationAdapter);

        searchLocationAdapter.setOnItemClickListener(location -> {
            viewModel.location.set(location.getDescription());
            viewModel.originId.set(location.getPlace_id());
        });
    }

    public void loadLocationType(){
        List<LocationType> locationTypeList = new ArrayList<>();
        locationTypeList.add(new LocationType("1", "Nhà riêng", ""));
        locationTypeList.add(new LocationType("1", "Công ty", ""));
        locationTypeList.add(new LocationType("1", "Thêm địa điểm", ""));

        viewBinding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.HORIZONTAL, false);

        viewBinding.rcLocationType.setLayoutManager(layoutManager);
        viewBinding.rcLocationType.setItemAnimator(new DefaultItemAnimator());
        locationTypeAdapter = new LocationTypeAdapter(locationTypeList);
        viewBinding.rcLocationType.setAdapter(locationTypeAdapter);

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
                        loadSavedLocation();
                    }
                    break;
                case R.id.edtDeparture:
                    if(!TextUtils.isEmpty(text)){
                        loadDepartureLocation();
                    }else{
                        loadSavedLocation();
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
}
