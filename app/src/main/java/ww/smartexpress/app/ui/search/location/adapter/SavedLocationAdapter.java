package ww.smartexpress.app.ui.search.location.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.SavedLocation;
import ww.smartexpress.app.databinding.ItemSavedLocationBinding;

public class SavedLocationAdapter extends RecyclerView.Adapter<SavedLocationAdapter.SavedLocationViewHolder>{
    private List<SavedLocation> savedLocationList;
    private OnItemClickListener onItemClickListener;

    public SavedLocationAdapter(List<SavedLocation> savedLocationList) {
        this.savedLocationList = savedLocationList;
    }

    @NonNull
    @Override
    public SavedLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSavedLocationBinding itemSavedLocationBinding = ItemSavedLocationBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new SavedLocationViewHolder(itemSavedLocationBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedLocationViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return savedLocationList == null ? 0 : savedLocationList.size();
    }

    public void addItems(List<SavedLocation> savedLocationList){
        this.savedLocationList = savedLocationList;
        notifyDataSetChanged();
    }

    public void clearItems() {
        savedLocationList.clear();
    }


    public class SavedLocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemSavedLocationBinding mBinding;
        private OnItemClickListener onItemClickListener;
        private SavedLocation savedLocation;

        public SavedLocationViewHolder(@NonNull ItemSavedLocationBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.savedLocation = savedLocationList.get(position);
            mBinding.setVariable(BR.ivm, savedLocation);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(savedLocation);
        }
    }

    public interface OnItemClickListener{
        void itemClick(SavedLocation location);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
