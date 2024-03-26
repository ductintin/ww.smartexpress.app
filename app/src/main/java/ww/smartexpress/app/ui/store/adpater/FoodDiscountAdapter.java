package ww.smartexpress.app.ui.store.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.FoodDiscount;
import ww.smartexpress.app.databinding.ItemFoodDiscountBinding;

public class FoodDiscountAdapter extends RecyclerView.Adapter<FoodDiscountAdapter.FoodDiscountViewHolder> {

    private List<FoodDiscount> foodDiscounts;
    private OnItemClickListener onItemClickListener;
    public FoodDiscountAdapter(List<FoodDiscount> foodDiscounts) {
        this.foodDiscounts = foodDiscounts;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    private int selected =0;

    @NonNull
    @Override
    public FoodDiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodDiscountBinding itemFoodDiscountBinding = ItemFoodDiscountBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new FoodDiscountViewHolder(itemFoodDiscountBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodDiscountViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return foodDiscounts == null ? 0 : foodDiscounts.size();
    }

    public void addItems(List<FoodDiscount> foodDiscounts) {
        this.foodDiscounts = foodDiscounts;
        notifyDataSetChanged();
    }

    public void clearItems() {
        foodDiscounts.clear();
    }


    public class FoodDiscountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemFoodDiscountBinding mBinding;
        private OnItemClickListener onItemClickListener;
        private FoodDiscount FoodDiscount;

        private int position;
        public FoodDiscountViewHolder(ItemFoodDiscountBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.position = position;
            this.FoodDiscount = foodDiscounts.get(position);
            mBinding.setVariable(BR.ivm, FoodDiscount);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            selected = position;
            notifyDataSetChanged();
            this.onItemClickListener.itemClick(FoodDiscount);
        }
    }
    public interface OnItemClickListener{
        void itemClick(FoodDiscount FoodDiscount);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
