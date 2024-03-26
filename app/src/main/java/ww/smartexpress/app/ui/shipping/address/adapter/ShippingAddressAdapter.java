package ww.smartexpress.app.ui.shipping.address.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.ShippingAddress;
import ww.smartexpress.app.databinding.ItemShippingAddressBinding;

public class ShippingAddressAdapter extends RecyclerView.Adapter<ShippingAddressAdapter.ShippingAddressViewHolder>{
    private List<ShippingAddress> shippingAddresses;
    private OnItemClickListener onItemClickListener;

    public ShippingAddressAdapter(List<ShippingAddress> shippingAddresses) {
        this.shippingAddresses = shippingAddresses;
    }

    @NonNull
    @Override
    public ShippingAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemShippingAddressBinding binding = ItemShippingAddressBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ShippingAddressViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShippingAddressViewHolder holder, int position) {
        holder.onBind(position);
    }

    public void addItems(List<ShippingAddress> shippingAddresses){
        this.shippingAddresses = shippingAddresses;
        notifyDataSetChanged();
    }

    public void clearItems() {
        shippingAddresses.clear();
    }

    @Override
    public int getItemCount() {
        return shippingAddresses == null ? 0 : shippingAddresses.size();
    }

    public class ShippingAddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemShippingAddressBinding mBinding;
        private OnItemClickListener onItemClickListener;
        private ShippingAddress shippingAddress;

        public ShippingAddressViewHolder(@NonNull ItemShippingAddressBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.shippingAddress = shippingAddresses.get(position);
            mBinding.setVariable(BR.ivm, shippingAddress);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(shippingAddress);
        }
    }

    public interface OnItemClickListener{
        void itemClick(ShippingAddress location);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
