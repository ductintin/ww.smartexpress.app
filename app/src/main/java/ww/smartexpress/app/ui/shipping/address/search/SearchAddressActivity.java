package ww.smartexpress.app.ui.shipping.address.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.LocationType;
import ww.smartexpress.app.databinding.ActivitySearchDeliveryAddressBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.search.location.adapter.LocationTypeAdapter;

public class SearchAddressActivity extends BaseActivity<ActivitySearchDeliveryAddressBinding, SearchAddressViewModel> {
    LocationTypeAdapter locationTypeAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_search_delivery_address;
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

        Intent intent = getIntent();
        viewModel.searchLocation.set(intent.getStringExtra("shippingAddress"));
        loadLocationType();
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
}
