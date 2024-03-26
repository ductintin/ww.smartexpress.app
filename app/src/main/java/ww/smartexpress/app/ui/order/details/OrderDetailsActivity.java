package ww.smartexpress.app.ui.order.details;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.FoodOrder;
import ww.smartexpress.app.databinding.ActivityOrderDetailsBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.order.details.OrderDetailsActivity;
import ww.smartexpress.app.ui.order.details.adapter.FoodOrderAdapter;
import ww.smartexpress.app.ui.store.StoreActivity;

public class OrderDetailsActivity extends BaseActivity<ActivityOrderDetailsBinding, OrderDetailsViewModel> {

    FoodOrderAdapter foodOrderAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_order_details;
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
        viewModel.isPayment.set(intent.getBooleanExtra("payment",false));
        loadFoodOrder();
    }

    private void loadFoodOrder(){
        //
        List<FoodOrder> foodOrders = new ArrayList<>();
        foodOrders.add(new FoodOrder("Ô tô"));
        foodOrders.add(new FoodOrder("Xe máy"));
        foodOrders.add(new FoodOrder("Giao hàng"));
        foodOrders.add(new FoodOrder("Đi chợ"));
        foodOrders.add(new FoodOrder("Ô tô"));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        viewBinding.rcFoodOrder.setLayoutManager(layoutManager);
        foodOrderAdapter = new FoodOrderAdapter(foodOrders);
        viewBinding.rcFoodOrder.setAdapter(foodOrderAdapter);

        foodOrderAdapter.setOnItemClickListener(new FoodOrderAdapter.OnItemClickListener() {
            @Override
            public void itemClick(FoodOrder foodOrder) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!viewModel.isPayment.get()) {
            super.onBackPressed();
        }else {
            Intent intent = new Intent(this, StoreActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }
}
