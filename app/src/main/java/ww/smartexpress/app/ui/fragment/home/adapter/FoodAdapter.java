package ww.smartexpress.app.ui.fragment.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import eu.davidea.flexibleadapter.databinding.BR;
import ww.smartexpress.app.data.model.api.response.Food;
import ww.smartexpress.app.databinding.ItemFoodBinding;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<Food> foods;
    private OnItemClickListener onItemClickListener;
    public FoodAdapter(List<Food> foods) {
        this.foods = foods;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodBinding itemFoodBinding = ItemFoodBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new FoodViewHolder(itemFoodBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return foods == null ? 0 : foods.size();
    }

    public void addItems(List<Food> foods) {
//        this.permissions.add(new Permission("name","aaa"));
//        this.permissions.add(new Permission("hai","a"));
        this.foods = foods;
        notifyDataSetChanged();
    }

    public void clearItems() {
        foods.clear();
    }


    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ItemFoodBinding mBinding;
        private OnItemClickListener onItemClickListener;
        private Food food;
        public FoodViewHolder(ItemFoodBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            food = foods.get(position);
            mBinding.setVariable(BR.ivm, food);
            mBinding.executePendingBindings();
        }
        @Override
        public void onClick(View view) {
            onItemClickListener.itemClick(food);
        }
    }
    public interface OnItemClickListener{
        void itemClick(Food food);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
