package ww.smartexpress.app.ui.purchase;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.FoodPurchase;
import ww.smartexpress.app.databinding.ActivityPurchaseBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.purchase.adapter.FoodPurchaseAdapter;

public class PurchaseActivity extends BaseActivity<ActivityPurchaseBinding, PurchaseViewModel> {

    FoodPurchaseAdapter foodPurchaseAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_purchase;
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
        loadFoodPurchase();
    }
    private void loadFoodPurchase(){
        //
        List<FoodPurchase> foodPurchases = new ArrayList<>();
        foodPurchases.add(new FoodPurchase("Ô tô"));
        foodPurchases.add(new FoodPurchase("Xe máy"));
        foodPurchases.add(new FoodPurchase("Giao hàng"));
        foodPurchases.add(new FoodPurchase("Đi chợ"));
        foodPurchases.add(new FoodPurchase("Ô tô"));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        viewBinding.rcFoodPurchase.setLayoutManager(layoutManager);
        foodPurchaseAdapter = new FoodPurchaseAdapter(foodPurchases);
        viewBinding.rcFoodPurchase.setAdapter(foodPurchaseAdapter);

        foodPurchaseAdapter.setOnItemClickListener(new FoodPurchaseAdapter.OnItemClickListener() {
            @Override
            public void itemClick(FoodPurchase foodPurchase) {
            }
        });
    }
}
