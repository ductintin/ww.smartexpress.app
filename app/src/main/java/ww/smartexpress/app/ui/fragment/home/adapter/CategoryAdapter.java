package ww.smartexpress.app.ui.fragment.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.Category;
import ww.smartexpress.app.data.model.api.response.CategoryResponse;
import ww.smartexpress.app.databinding.ItemCategoryBinding;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryResponse> categories;

    private OnItemClickListener onItemClickListener;

    public CategoryAdapter(List<CategoryResponse> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding itemCategoryBinding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new CategoryViewHolder(itemCategoryBinding,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    public void addItems(List<CategoryResponse> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void clearItems() {
        categories.clear();
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemCategoryBinding mBinding;

        private OnItemClickListener onItemClickListener;
        private CategoryResponse category;

        public CategoryViewHolder(ItemCategoryBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.category = categories.get(position);
            mBinding.setVariable(BR.ivm, category);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(category);
        }
    }

    public interface OnItemClickListener{
        void itemClick(CategoryResponse category);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}
