package ww.smartexpress.app.ui.search.location.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.LocationType;
import ww.smartexpress.app.databinding.ItemLocationTypeBinding;

public class LocationTypeAdapter extends RecyclerView.Adapter<LocationTypeAdapter.LocationTypeViewHolder>{
    private List<LocationType> locationTypeList;
    private OnItemClickListener onItemClickListener;

    public LocationTypeAdapter(List<LocationType> locationTypeList){
        this.locationTypeList = locationTypeList;
    }
    @NonNull
    @Override
    public LocationTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLocationTypeBinding binding = ItemLocationTypeBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new LocationTypeViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationTypeViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return locationTypeList == null ? 0 : locationTypeList.size();
    }

    public void addItems(List<LocationType> locationTypeList){
        this.locationTypeList = locationTypeList;
        notifyDataSetChanged();
    }

    public void clearItems(){
        locationTypeList.clear();
    }

    public class LocationTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemLocationTypeBinding mBinding;
        private LocationType locationType;
        private OnItemClickListener onItemClickListener;

        public LocationTypeViewHolder(@NonNull ItemLocationTypeBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position){
            this.locationType = locationTypeList.get(position);
            mBinding.setVariable(BR.ivm, locationType);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(locationType);
        }
    }

    public interface OnItemClickListener{
        void itemClick(LocationType locationType);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
