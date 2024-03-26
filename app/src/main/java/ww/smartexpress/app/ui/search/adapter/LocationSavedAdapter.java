package ww.smartexpress.app.ui.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.LocationSaved;
import ww.smartexpress.app.databinding.ItemLocationSavedBinding;

public class LocationSavedAdapter extends RecyclerView.Adapter<LocationSavedAdapter.LocationSavedViewHolder> {

    private List<LocationSaved> locationSavedList;

    private OnItemClickListener onItemClickListener;

    public LocationSavedAdapter(List<LocationSaved> locationSavedList) {
        this.locationSavedList = locationSavedList;
    }

    @NonNull
    @Override
    public LocationSavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLocationSavedBinding itemLocationSavedBinding = ItemLocationSavedBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new LocationSavedViewHolder(itemLocationSavedBinding,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationSavedViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return locationSavedList == null ? 0 : locationSavedList.size();
    }

    public void addItems(List<LocationSaved> locationSavedList) {
        this.locationSavedList = locationSavedList;
        notifyDataSetChanged();
    }

    public void clearItems() {
        locationSavedList.clear();
    }


    public class LocationSavedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemLocationSavedBinding mBinding;

        private OnItemClickListener onItemClickListener;
        private LocationSaved locationSaved;

        public LocationSavedViewHolder(ItemLocationSavedBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.locationSaved = locationSavedList.get(position);
            mBinding.setVariable(BR.ivm, locationSaved);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(locationSaved);
        }
    }

    public interface OnItemClickListener{
        void itemClick(LocationSaved LocationSaved);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}
