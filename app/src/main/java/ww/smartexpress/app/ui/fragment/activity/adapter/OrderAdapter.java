package ww.smartexpress.app.ui.fragment.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.Order;
import ww.smartexpress.app.databinding.ItemOrderBinding;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private OnItemClickListener onItemClickListener;
    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding itemOrderBinding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new OrderViewHolder(itemOrderBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

    public void addItems(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public void clearItems() {
        orders.clear();
    }


    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemOrderBinding mBinding;
        private OnItemClickListener onItemClickListener;

        private Order Order;
        public OrderViewHolder(ItemOrderBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.Order = orders.get(position);
            mBinding.setVariable(BR.ivm, Order);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(Order);
        }
    }
    public interface OnItemClickListener{
        void itemClick(Order Order);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
