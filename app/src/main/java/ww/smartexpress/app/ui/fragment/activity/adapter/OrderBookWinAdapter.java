package ww.smartexpress.app.ui.fragment.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.Order;
import ww.smartexpress.app.databinding.ItemOrderBookAllwinBinding;

public class OrderBookWinAdapter extends RecyclerView.Adapter<OrderBookWinAdapter.OrderBookWinViewHolder> {

    private List<Order> orders;
    private OnItemClickListener onItemClickListener;
    public OrderBookWinAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderBookWinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBookAllwinBinding itemOrderBookAllwinBinding = ItemOrderBookAllwinBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new OrderBookWinViewHolder(itemOrderBookAllwinBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderBookWinViewHolder holder, int position) {
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


    public class OrderBookWinViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemOrderBookAllwinBinding mBinding;
        private OnItemClickListener onItemClickListener;

        private Order Order;
        public OrderBookWinViewHolder(ItemOrderBookAllwinBinding binding, OnItemClickListener onItemClickListener) {
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
