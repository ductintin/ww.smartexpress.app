package ww.smartexpress.app.ui.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.OrderOption;
import ww.smartexpress.app.data.model.api.response.OrderOptionDetail;
import ww.smartexpress.app.databinding.ItemOrderDetailOptionBinding;

public class OrderOptionAdapter extends RecyclerView.Adapter<OrderOptionAdapter.OrderOptionViewHolder>{
    private List<OrderOption> orderOptions;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public OrderOptionAdapter(List<OrderOption> orderOptions, Context context) {
        this.orderOptions = orderOptions;
        this.context = context;
    }

    public OrderOptionAdapter(List<OrderOption> orderOptions) {
        this.orderOptions = orderOptions;
    }

    @NonNull
    @Override
    public OrderOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailOptionBinding binding = ItemOrderDetailOptionBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new OrderOptionViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderOptionViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return orderOptions == null ? 0 : orderOptions.size();
    }

    public void addItems(List<OrderOption> orderOptions){
        this.orderOptions = orderOptions;
        notifyDataSetChanged();
    }

    public void clearItems() {
        orderOptions.clear();
    }

    public class OrderOptionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemOrderDetailOptionBinding mBinding;
        private OnItemClickListener onItemClickListener;
        private OrderOptionDetailAdapter orderOptionDetailAdapter;
        private OrderOption orderOption;

        public OrderOptionViewHolder(@NonNull ItemOrderDetailOptionBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position){
            this.orderOption = orderOptions.get(position);
            orderOptionDetailAdapter = new OrderOptionDetailAdapter(orderOption.getOptionDetailList(), orderOption.getIsOptional());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context
                    ,LinearLayoutManager.VERTICAL, false);
            mBinding.rcOrderOptionDetail.setLayoutManager(layoutManager);
//
//            orderOptionDetailAdapter.setOnItemClickListener(optionDetail -> {
////                optionDetail.setIsSelected(!optionDetail.getIsSelected());
////                orderOptionDetailAdapter.notifyDataSetChanged();
//            });

            orderOptionDetailAdapter.setOnItemClickListener(pos -> {
                orderOptionDetailAdapter.handelRadioButtonCheck(pos);
            });

            mBinding.rcOrderOptionDetail.setAdapter(orderOptionDetailAdapter);
            mBinding.setVariable(BR.ivm, orderOption);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(orderOption);
        }
    }

    public interface OnItemClickListener{
        void itemClick(OrderOption orderOption);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
