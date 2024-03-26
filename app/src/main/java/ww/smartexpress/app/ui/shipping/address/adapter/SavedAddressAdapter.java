package ww.smartexpress.app.ui.shipping.address.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.SavedAddress;
import ww.smartexpress.app.databinding.ItemSavedAddressBinding;

public class SavedAddressAdapter extends RecyclerView.Adapter<SavedAddressAdapter.SavedAddressViewHolder>{
    private List<SavedAddress> savedAddressList;
    private OnItemClickListener onItemClickListener;

    public SavedAddressAdapter(List<SavedAddress> savedAddressList) {
        this.savedAddressList = savedAddressList;
    }

    @NonNull
    @Override
    public SavedAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSavedAddressBinding itemSavedAddressBinding = ItemSavedAddressBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new SavedAddressViewHolder(itemSavedAddressBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedAddressViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return savedAddressList == null ? 0 : savedAddressList.size();
    }

    public void addItems(List<SavedAddress> savedAddressList){
        this.savedAddressList = savedAddressList;
        notifyDataSetChanged();
    }

    public void clearItems() {
        savedAddressList.clear();
    }


    public class SavedAddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemSavedAddressBinding mBinding;
        private OnItemClickListener onItemClickListener;
        private SavedAddress savedAddress;

        public SavedAddressViewHolder(@NonNull ItemSavedAddressBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.savedAddress = savedAddressList.get(position);
            mBinding.setVariable(BR.ivm, savedAddress);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(savedAddress);
        }
    }

    public interface OnItemClickListener{
        void itemClick(SavedAddress Address);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
