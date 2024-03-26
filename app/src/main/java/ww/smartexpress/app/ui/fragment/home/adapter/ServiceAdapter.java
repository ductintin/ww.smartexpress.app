package ww.smartexpress.app.ui.fragment.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.CategoryResponse;
import ww.smartexpress.app.data.model.api.response.ServiceResponse;
import ww.smartexpress.app.databinding.ItemServiceBinding;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>{
    private List<ServiceResponse> services;
    private OnItemClickListener onItemClickListener;

    public ServiceAdapter(List<ServiceResponse> services) {
        this.services = services;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemServiceBinding binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ServiceViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return services == null ? 0 : services.size();
    }

    public void addItems(List<ServiceResponse> services) {
        this.services = services;
        notifyDataSetChanged();
    }

    public void clearItems() {
        services.clear();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder implements View

            .OnClickListener{

        private ItemServiceBinding mBinding;
        private ServiceResponse service;
        private OnItemClickListener onItemClickListener;

        public ServiceViewHolder(@NonNull ItemServiceBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position){
            this.service = services.get(position);
            mBinding.setVariable(BR.ivm, service);
            mBinding.executePendingBindings();
        }
        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(service);
        }
    }

    public interface OnItemClickListener{
        void itemClick(ServiceResponse service);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
