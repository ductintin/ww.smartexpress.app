package ww.smartexpress.app.ui.cart;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.CartItem;
import ww.smartexpress.app.databinding.ActivityCartBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.cart.adapter.CartItemAdapter;

public class CartActivity extends BaseActivity<ActivityCartBinding, CartViewModel> {
    
    CartItemAdapter cartItemAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_cart;
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
        loadCart();
    }
    private void loadCart(){
        //
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem("Ô tô"));
        cartItems.add(new CartItem("Xe máy"));
        cartItems.add(new CartItem("Giao hàng"));
        cartItems.add(new CartItem("Đi chợ"));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this
                ,LinearLayoutManager.VERTICAL, false);
        viewBinding.rcCart.setLayoutManager(layoutManager);
        cartItemAdapter = new CartItemAdapter(cartItems);
        viewBinding.rcCart.setAdapter(cartItemAdapter);

        cartItemAdapter.setOnItemClickListener(cartItem -> {
        });
    }
}
