package ww.smartexpress.app.ui.order.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.OrderOptionDetail;
import ww.smartexpress.app.databinding.ItemRadioOrderDetailOptionBinding;

public class OrderOptionDetailAdapter extends RecyclerView.Adapter<OrderOptionDetailAdapter.OrderOptionDetailViewHolder>{
    private List<OrderOptionDetail> optionDetailList;
    private OnItemClickListener onItemClickListener;
    private boolean isRequired;
    private boolean isNewRadioButtonChecked = false;
    private int lastCheckedPosition = 0;

    public OrderOptionDetailAdapter(List<OrderOptionDetail> optionDetailList, boolean isRequired) {
        this.optionDetailList = optionDetailList;
        this.isRequired = isRequired;
    }

    @NonNull
    @Override
    public OrderOptionDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRadioOrderDetailOptionBinding binding = ItemRadioOrderDetailOptionBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new OrderOptionDetailViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderOptionDetailViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return optionDetailList == null ? 0 : optionDetailList.size();
    }

    public class OrderOptionDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemRadioOrderDetailOptionBinding mBinding;
        private OrderOptionDetail optionDetail;
        private OnItemClickListener onItemClickListener;

        public OrderOptionDetailViewHolder(@NonNull ItemRadioOrderDetailOptionBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position){
            this.optionDetail = optionDetailList.get(position);
            if (isRequired && lastCheckedPosition == position){
                optionDetail.setIsSelected(true);
            }
            if(position == optionDetailList.size() - 1){
                mBinding.detailOptionLine.setVisibility(View.INVISIBLE);
            }
            mBinding.setVariable(BR.ivm, optionDetail);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener{
        void itemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void handelRadioButtonCheck(int pos){
        if(isRequired){
            isNewRadioButtonChecked = true;
            optionDetailList.get(lastCheckedPosition).setIsSelected(false);
            optionDetailList.get(pos).setIsSelected(true);
            this.lastCheckedPosition = pos;
        }else{
            optionDetailList.get(pos).setIsSelected(!optionDetailList.get(pos).getIsSelected());
        }
        notifyDataSetChanged();
    }

}
