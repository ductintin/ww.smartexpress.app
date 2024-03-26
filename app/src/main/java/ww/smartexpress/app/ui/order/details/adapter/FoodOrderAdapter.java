package ww.smartexpress.app.ui.order.details.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.FoodOrder;
import ww.smartexpress.app.databinding.ItemFoodOrderBinding;

public class FoodOrderAdapter extends RecyclerView.Adapter<FoodOrderAdapter.FoodOrderViewHolder> {

    private List<FoodOrder> foodOrders;
    private OnItemClickListener onItemClickListener;
    public FoodOrderAdapter(List<FoodOrder> foodOrders) {
        this.foodOrders = foodOrders;
    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodOrderBinding itemFoodOrderBinding = ItemFoodOrderBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new FoodOrderViewHolder(itemFoodOrderBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodOrderViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return foodOrders == null ? 0 : foodOrders.size();
    }

    public void addItems(List<FoodOrder> foodOrders) {
        this.foodOrders = foodOrders;
        notifyDataSetChanged();
    }

    public void clearItems() {
        foodOrders.clear();
    }


    public class FoodOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemFoodOrderBinding mBinding;
        private OnItemClickListener onItemClickListener;
        private FoodOrder foodOrder;
        private int position;
        public FoodOrderViewHolder(ItemFoodOrderBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.position = position;
            this.foodOrder = foodOrders.get(position);
            mBinding.setVariable(BR.ivm, foodOrder);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            notifyDataSetChanged();
            this.onItemClickListener.itemClick(foodOrder);
        }
    }
    public interface OnItemClickListener{
        void itemClick(FoodOrder foodOrder);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
