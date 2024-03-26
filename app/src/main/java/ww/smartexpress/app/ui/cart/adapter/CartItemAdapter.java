package ww.smartexpress.app.ui.cart.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.CartItem;
import ww.smartexpress.app.databinding.ItemCartBinding;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartItem> cartItems;

    private OnItemClickListener onItemClickListener;

    public CartItemAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding itemCartItemBinding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new CartItemViewHolder(itemCartItemBinding,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return cartItems == null ? 0 : cartItems.size();
    }

    public void addItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    public void clearItems() {
        cartItems.clear();
    }


    public class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemCartBinding mBinding;

        private OnItemClickListener onItemClickListener;
        private CartItem cartItem;

        private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

        public CartItemViewHolder(ItemCartBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.cartItem = cartItems.get(position);
            mBinding.setVariable(BR.ivm, cartItem);
            mBinding.executePendingBindings();
            mBinding.imgDelete.setOnClickListener(view -> {
                Log.d("TAG", "onBind: Delete CartItem");
            });
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(cartItem);
        }
    }

    public interface OnItemClickListener{
        void itemClick(CartItem cartItem);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}
