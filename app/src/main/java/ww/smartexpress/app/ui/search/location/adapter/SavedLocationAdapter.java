package ww.smartexpress.app.ui.search.location.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.SavedLocation;
import ww.smartexpress.app.data.model.api.response.SearchLocation;
import ww.smartexpress.app.databinding.ItemSavedLocationBinding;

public class SavedLocationAdapter extends RecyclerView.Adapter<SavedLocationAdapter.SavedLocationViewHolder>{
    private List<SearchLocation> searchLocations;
    private OnItemClickListener onItemClickListener;

    public SavedLocationAdapter(List<SearchLocation> searchLocations) {
        this.searchLocations = searchLocations;
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
        return searchLocations == null ? 0 : searchLocations.size();
    }

    public void addItems(List<SearchLocation> searchLocations){
        this.searchLocations = searchLocations;
        notifyDataSetChanged();
    }

    public void clearItems() {
        searchLocations.clear();
    }


    public class SavedLocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemSavedLocationBinding mBinding;
        private OnItemClickListener onItemClickListener;
        private SearchLocation searchLocation;

        public SavedLocationViewHolder(@NonNull ItemSavedLocationBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.searchLocation = searchLocations.get(position);
            mBinding.setVariable(BR.ivm, searchLocation);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(searchLocation);
        }
    }

    public interface OnItemClickListener{
        void itemClick(SearchLocation location);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
