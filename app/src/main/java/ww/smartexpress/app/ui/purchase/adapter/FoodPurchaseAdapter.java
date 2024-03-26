package ww.smartexpress.app.ui.purchase.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.FoodPurchase;
import ww.smartexpress.app.databinding.ItemFoodPurchaseBinding;

public class FoodPurchaseAdapter extends RecyclerView.Adapter<FoodPurchaseAdapter.FoodPurchaseViewHolder> {

    private List<FoodPurchase> foodPurchases;
    private OnItemClickListener onItemClickListener;
    public FoodPurchaseAdapter(List<FoodPurchase> foodPurchases) {
        this.foodPurchases = foodPurchases;
    }

    @NonNull
    @Override
    public FoodPurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodPurchaseBinding itemFoodPurchaseBinding = ItemFoodPurchaseBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new FoodPurchaseViewHolder(itemFoodPurchaseBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodPurchaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return foodPurchases == null ? 0 : foodPurchases.size();
    }

    public void addItems(List<FoodPurchase> foodPurchases) {
        this.foodPurchases = foodPurchases;
        notifyDataSetChanged();
    }

    public void clearItems() {
        foodPurchases.clear();
    }


    public class FoodPurchaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemFoodPurchaseBinding mBinding;
        private OnItemClickListener onItemClickListener;
        private FoodPurchase foodPurchase;
        private int position;
        public FoodPurchaseViewHolder(ItemFoodPurchaseBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.position = position;
            this.foodPurchase = foodPurchases.get(position);
            mBinding.setVariable(BR.ivm, foodPurchase);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            notifyDataSetChanged();
            this.onItemClickListener.itemClick(foodPurchase);
        }
    }
    public interface OnItemClickListener{
        void itemClick(FoodPurchase foodPurchase);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
