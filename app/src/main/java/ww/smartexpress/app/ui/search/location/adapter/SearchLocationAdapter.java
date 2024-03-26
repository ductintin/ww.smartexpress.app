package ww.smartexpress.app.ui.search.location.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.SearchLocation;
import ww.smartexpress.app.databinding.ItemSearchLocationBinding;

public class SearchLocationAdapter extends RecyclerView.Adapter<SearchLocationAdapter.SearchLocationViewHolder>{
    private List<SearchLocation> searchLocations;
    private OnItemClickListener onItemClickListener;

    public SearchLocationAdapter(List<SearchLocation> searchLocations) {
        this.searchLocations = searchLocations;
    }

    @NonNull
    @Override
    public SearchLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchLocationBinding binding = ItemSearchLocationBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new SearchLocationViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchLocationViewHolder holder, int position) {
        holder.onBind(position);
    }

    public void addItems(List<SearchLocation> searchLocations){
        this.searchLocations = searchLocations;
        notifyDataSetChanged();
    }

    public void clearItems() {
        searchLocations.clear();
    }

    @Override
    public int getItemCount() {
        return searchLocations == null ? 0 : searchLocations.size();
    }

    public class SearchLocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemSearchLocationBinding mBinding;
        private OnItemClickListener onItemClickListener;
        private SearchLocation searchLocation;

        public SearchLocationViewHolder(@NonNull ItemSearchLocationBinding binding, OnItemClickListener onItemClickListener) {
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
