package ww.smartexpress.app.ui.rate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.RateOption;
import ww.smartexpress.app.databinding.ItemRateOptionBinding;
import ww.smartexpress.app.ui.order.adapter.OrderOptionDetailAdapter;

public class RateOptionAdapter extends RecyclerView.Adapter<RateOptionAdapter.RateOptionViewMHolder>{
    private List<RateOption> rateOptions;
    private OnItemClickListener onItemClickListener;

    public RateOptionAdapter(List<RateOption> rateOptions) {
        this.rateOptions = rateOptions;
    }

    @NonNull
    @Override
    public RateOptionViewMHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRateOptionBinding binding = ItemRateOptionBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new RateOptionViewMHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RateOptionViewMHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return rateOptions == null ? 0 : rateOptions.size();
    }

    public class RateOptionViewMHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemRateOptionBinding mBinding;
        private RateOption rateOption;
        private OnItemClickListener onItemClickListener;

        public RateOptionViewMHolder(@NonNull ItemRateOptionBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position){
            this.rateOption = rateOptions.get(position);
            mBinding.setVariable(BR.ivm, rateOption);
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

    public boolean handleClick(int pos){
        rateOptions.get(pos).setIsSelected(!rateOptions.get(pos).getIsSelected());
        notifyDataSetChanged();
        for(int i = 0; i < rateOptions.size(); i++){
            if (rateOptions.get(i).getIsSelected()){
                return true;
            }
        }
        return false;
    }
}
