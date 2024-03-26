package ww.smartexpress.app.ui.store;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.FoodDiscount;
import ww.smartexpress.app.data.model.api.response.FoodStore;
import ww.smartexpress.app.databinding.ActivityStoreBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.order.OrderActivity;
import ww.smartexpress.app.ui.store.adpater.FoodDiscountAdapter;
import ww.smartexpress.app.ui.store.adpater.FoodStoreAdapter;
import ww.smartexpress.app.ui.store.adpater.GridSpacingItemDecoration;

public class StoreActivity extends BaseActivity<ActivityStoreBinding, StoreViewModel> {
    
    FoodStoreAdapter foodStoreAdapter;
    FoodDiscountAdapter foodDiscountAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_store;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //
        viewBinding.layoutStore.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                viewBinding.layoutAddCart.setVisibility(View.GONE);
                return false;
            }
        });
        viewBinding.rcForYou.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                viewBinding.layoutAddCart.setVisibility(View.GONE);
                return false;
            }
        });
        viewBinding.rcPromoteFood.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                viewBinding.layoutAddCart.setVisibility(View.GONE);
                return false;
            }
        });

        loadFoodDiscount();
        loadFoodStore();
    }

    private void loadFoodDiscount(){
        //
        List<FoodDiscount> categories = new ArrayList<>();
        categories.add(new FoodDiscount("Ô tô"));
        categories.add(new FoodDiscount("Xe máy"));
        categories.add(new FoodDiscount("Giao hàng"));
        categories.add(new FoodDiscount("Đi chợ"));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this
                ,LinearLayoutManager.HORIZONTAL, false);
        viewBinding.rcPromoteFood.setLayoutManager(layoutManager);
        foodDiscountAdapter = new FoodDiscountAdapter(categories);
        viewBinding.rcPromoteFood.setAdapter(foodDiscountAdapter);

        foodDiscountAdapter.setOnItemClickListener(foodDiscount -> {
        });
    }

    private void loadFoodStore(){
        //
        List<FoodStore> foodStores = new ArrayList<>();
        foodStores .add(new FoodStore("Ô tô"));
        foodStores .add(new FoodStore("Xe máy"));
        foodStores .add(new FoodStore("Giao hàng"));
        foodStores .add(new FoodStore("Đi chợ"));
        foodStores .add(new FoodStore("Ô tô"));
        foodStores .add(new FoodStore("Xe máy"));
        foodStores .add(new FoodStore("Giao hàng"));
        foodStores .add(new FoodStore("Đi chợ"));
        foodStores .add(new FoodStore("Ô tô"));
        foodStores .add(new FoodStore("Xe máy"));
        foodStores .add(new FoodStore("Giao hàng"));
        foodStores .add(new FoodStore("Đi chợ"));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        viewBinding.rcForYou.setLayoutManager(layoutManager);
        foodStoreAdapter = new FoodStoreAdapter(foodStores);

        int spanCount = 2;
        int spacing = getResources().getDimensionPixelSize(R.dimen.space_item_horizontal);
        boolean includeEdge = false;
        viewBinding.rcForYou.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        viewBinding.rcForYou.setAdapter(foodStoreAdapter);

        foodStoreAdapter.setOnItemClickListener(new FoodStoreAdapter.OnItemClickListener() {
            @Override
            public void itemClick(FoodStore foodStore) {
                startActivity(new Intent(StoreActivity.this, OrderActivity.class));
            }
            @Override
            public void addCart(FoodStore foodStore) {
                int count = viewModel.count.get();
                int price = viewModel.price.get();
                viewModel.count.set(count+1);
                viewModel.price.set(price+30000);
            }
        });
    }

}
