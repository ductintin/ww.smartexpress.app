package ww.smartexpress.app.ui.shipping.address;

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

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.LocationType;
import ww.smartexpress.app.data.model.api.response.SavedAddress;
import ww.smartexpress.app.data.model.api.response.ShippingAddress;
import ww.smartexpress.app.databinding.ActivityShippingAddressBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.search.SearchActivity;
import ww.smartexpress.app.ui.search.location.adapter.LocationTypeAdapter;
import ww.smartexpress.app.ui.shipping.address.adapter.SavedAddressAdapter;
import ww.smartexpress.app.ui.shipping.address.adapter.ShippingAddressAdapter;

public class ShippingAddressActivity extends BaseActivity<ActivityShippingAddressBinding, ShippingAddressViewModel> {
    
    SavedAddressAdapter savedAddressAdapter;
    ShippingAddressAdapter shippingAddressAdapter;
    LocationTypeAdapter locationTypeAdapter;
    
    @Override
    public int getLayoutId() {
        return R.layout.activity_shipping_address;
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
        loadSavedAddress();
        loadLocationType();
        viewBinding.edtShippingAddress.addTextChangedListener(new GenericTextWatcher(viewBinding.edtShippingAddress));
    }

    public void loadSavedAddress(){
        List<SavedAddress> SavedAddressList = new ArrayList<>();
        SavedAddressList.add(new SavedAddress("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
        SavedAddressList.add(new SavedAddress("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
        SavedAddressList.add(new SavedAddress("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));

        viewBinding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);

        viewBinding.rcShippingAddress.setLayoutManager(layoutManager);
        viewBinding.rcShippingAddress.setItemAnimator(new DefaultItemAnimator());
        savedAddressAdapter = new SavedAddressAdapter(SavedAddressList);
        viewBinding.rcShippingAddress.setAdapter(savedAddressAdapter);

        savedAddressAdapter.setOnItemClickListener(location -> {
            viewModel.shippingAddress.set(location.getName());
        });
    }

    public void loadShippingAddress(){
        List<ShippingAddress> ShippingAddresses = new ArrayList<>();
        ShippingAddresses.add(new ShippingAddress("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
        ShippingAddresses.add(new ShippingAddress("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
        ShippingAddresses.add(new ShippingAddress("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
        ShippingAddresses.add(new ShippingAddress("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));
        ShippingAddresses.add(new ShippingAddress("1", "Masteri Thảo Điền", "Thảo Điền, Thủ Đức, Hồ Chí Minh"));

        viewBinding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);

        viewBinding.rcShippingAddress.setLayoutManager(layoutManager);
        viewBinding.rcShippingAddress.setItemAnimator(new DefaultItemAnimator());
        shippingAddressAdapter = new ShippingAddressAdapter(ShippingAddresses);
        viewBinding.rcShippingAddress.setAdapter(shippingAddressAdapter);

        shippingAddressAdapter.setOnItemClickListener(location -> {
            viewModel.shippingAddress.set(location.getName());
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

            if(!TextUtils.isEmpty(text)){
                loadShippingAddress();
            }else{
                loadSavedAddress();
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
}
