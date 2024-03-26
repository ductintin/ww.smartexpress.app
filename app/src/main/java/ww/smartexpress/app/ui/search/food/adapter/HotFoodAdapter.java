package ww.smartexpress.app.ui.search.food.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.HotFood;
import ww.smartexpress.app.databinding.ItemHotFoodBinding;

public class HotFoodAdapter extends RecyclerView.Adapter<HotFoodAdapter.HotFoodViewHolder> {

    private List<HotFood> hotFoodList;

    private OnItemClickListener onItemClickListener;

    public HotFoodAdapter(List<HotFood> hotFoodList) {
        this.hotFoodList = hotFoodList;
    }

    @NonNull
    @Override
    public HotFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHotFoodBinding itemHotFoodBinding = ItemHotFoodBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new HotFoodViewHolder(itemHotFoodBinding,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HotFoodViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return hotFoodList == null ? 0 : hotFoodList.size();
    }

    public void addItems(List<HotFood> hotFoodList) {
        this.hotFoodList = hotFoodList;
        notifyDataSetChanged();
    }

    public void clearItems() {
        hotFoodList.clear();
    }


    public class HotFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemHotFoodBinding mBinding;

        private OnItemClickListener onItemClickListener;
        private HotFood hotFood;

        public HotFoodViewHolder(ItemHotFoodBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.hotFood = hotFoodList.get(position);
            mBinding.setVariable(BR.ivm, hotFood);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(hotFood);
        }
    }

    public interface OnItemClickListener{
        void itemClick(HotFood hotFood);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
