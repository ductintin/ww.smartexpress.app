package ww.smartexpress.app.ui.store.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.FoodStore;
import ww.smartexpress.app.databinding.ItemFoodBinding;
import ww.smartexpress.app.databinding.ItemFoodStoreBinding;
import ww.smartexpress.app.databinding.ItemWinBikeBinding;

public class FoodStoreAdapter extends RecyclerView.Adapter<FoodStoreAdapter.FoodStoreViewHolder> {

    private List<FoodStore> foodStores;
    private OnItemClickListener onItemClickListener;
    public FoodStoreAdapter(List<FoodStore> foodStores) {
        this.foodStores = foodStores;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    private int selected =0;

    @NonNull
    @Override
    public FoodStoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodStoreBinding itemFoodStoreBinding = ItemFoodStoreBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new FoodStoreViewHolder(itemFoodStoreBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodStoreViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return foodStores == null ? 0 : foodStores.size();
    }

    public void addItems(List<FoodStore> foodStores) {
        this.foodStores = foodStores;
        notifyDataSetChanged();
    }

    public void clearItems() {
        foodStores.clear();
    }


    public class FoodStoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemFoodStoreBinding mBinding;
        private OnItemClickListener onItemClickListener;
        private FoodStore foodStore;
        private int position;
        public FoodStoreViewHolder(ItemFoodStoreBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.position = position;
            this.foodStore = foodStores.get(position);
            mBinding.setVariable(BR.ivm, foodStore);
            mBinding.executePendingBindings();
            mBinding.imgAdd.setOnClickListener(view -> {
                onItemClickListener.addCart(foodStore);
            });
        }

        @Override
        public void onClick(View view) {
            selected = position;
            notifyDataSetChanged();
            this.onItemClickListener.itemClick(foodStore);
        }
    }
    public interface OnItemClickListener{
        void itemClick(FoodStore foodStore);
        void addCart(FoodStore foodStore);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
