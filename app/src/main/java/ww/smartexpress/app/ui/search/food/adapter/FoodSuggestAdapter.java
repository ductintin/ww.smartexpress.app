package ww.smartexpress.app.ui.search.food.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.FoodSuggest;
import ww.smartexpress.app.databinding.ItemFoodSuggestBinding;

public class FoodSuggestAdapter extends RecyclerView.Adapter<FoodSuggestAdapter.FoodSuggestViewHolder> {

    private List<FoodSuggest> foodSuggestList;

    private OnItemClickListener onItemClickListener;

    public FoodSuggestAdapter(List<FoodSuggest> foodSuggestList) {
        this.foodSuggestList = foodSuggestList;
    }

    @NonNull
    @Override
    public FoodSuggestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodSuggestBinding itemFoodSuggestBinding = ItemFoodSuggestBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new FoodSuggestViewHolder(itemFoodSuggestBinding,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodSuggestViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return foodSuggestList == null ? 0 : foodSuggestList.size();
    }

    public void addItems(List<FoodSuggest> foodSuggestList) {
        this.foodSuggestList = foodSuggestList;
        notifyDataSetChanged();
    }

    public void clearItems() {
        foodSuggestList.clear();
    }


    public class FoodSuggestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemFoodSuggestBinding mBinding;

        private OnItemClickListener onItemClickListener;
        private FoodSuggest foodSuggest;

        public FoodSuggestViewHolder(ItemFoodSuggestBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.foodSuggest = foodSuggestList.get(position);
            mBinding.setVariable(BR.ivm, foodSuggest);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(foodSuggest);
        }
    }

    public interface OnItemClickListener{
        void itemClick(FoodSuggest foodSuggest);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
