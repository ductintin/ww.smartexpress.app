package ww.smartexpress.app.ui.payout.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.PayoutTransaction;
import ww.smartexpress.app.databinding.ItemPayoutRequestBinding;


public class PayoutRequestAdapter extends RecyclerView.Adapter<PayoutRequestAdapter.PayoutRequestViewHolder>{
    private List<PayoutTransaction> payoutTransactions;
    private OnItemClickListener onItemClickListener;

    public PayoutRequestAdapter(List<PayoutTransaction> payoutTransactions) {
        this.payoutTransactions = payoutTransactions;
    }

    @NonNull
    @Override
    public PayoutRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPayoutRequestBinding binding = ItemPayoutRequestBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new PayoutRequestViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PayoutRequestViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return payoutTransactions == null ? 0 : payoutTransactions.size();
    }

    public class PayoutRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemPayoutRequestBinding mBinding;
        private PayoutTransaction payoutTransaction;
        private OnItemClickListener onItemClickListener;

        public PayoutRequestViewHolder(@NonNull ItemPayoutRequestBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position){
            this.payoutTransaction = payoutTransactions.get(position);
            mBinding.imvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.delete(payoutTransaction);
                }
            });
            mBinding.setVariable(BR.ivm, payoutTransaction);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(payoutTransaction);
        }
    }

    public interface OnItemClickListener{
        void itemClick(PayoutTransaction payoutTransaction);

        void delete(PayoutTransaction payoutTransaction);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
