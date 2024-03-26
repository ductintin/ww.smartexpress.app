package ww.smartexpress.app.ui.order.information;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.Commodity;
import ww.smartexpress.app.databinding.ActivityOrderInformationBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.order.information.adapter.CommodityAdapter;

public class OrderInformationActivity extends BaseActivity<ActivityOrderInformationBinding, OrderInformationViewModel> {
    
    CommodityAdapter commodityAdapter;
    CommodityAdapter commoditySizeAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_order_information;
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
        viewBinding.setLifecycleOwner(this);
        loadCommodity();
        loadSize();
    }

    private void loadCommodity(){
        //
        List<Commodity> CommodityList = new ArrayList<>();
        CommodityList.add(new Commodity("Thực phẩm",false));
        CommodityList.add(new Commodity("Thuốc men",false));
        CommodityList.add(new Commodity("Sách vở",false));
        CommodityList.add(new Commodity("Quần áo",false));
        CommodityList.add(new Commodity("Dễ vỡ",false));
        CommodityList.add(new Commodity("Khác...",false));


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        viewBinding.rcCommodity.setLayoutManager(layoutManager);
        commodityAdapter = new CommodityAdapter(CommodityList);
        viewBinding.rcCommodity.setAdapter(commodityAdapter);
        commodityAdapter.setOnItemClickListener(commodity -> {
        });
    }

    public void loadSize(){
        List<Commodity> CommodityList = new ArrayList<>();
        CommodityList.add(new Commodity("Nhỏ",false));
        CommodityList.add(new Commodity("Trung bình",false));
        CommodityList.add(new Commodity("Lớn",false));


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        viewBinding.rcSize.setLayoutManager(layoutManager);
        commoditySizeAdapter = new CommodityAdapter(CommodityList);
        viewBinding.rcSize.setAdapter(commoditySizeAdapter);
        commoditySizeAdapter.setOnItemClickListener(commodity -> {
        });
    }

}
