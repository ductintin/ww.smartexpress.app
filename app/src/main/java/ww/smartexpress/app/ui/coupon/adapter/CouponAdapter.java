package ww.smartexpress.app.ui.coupon.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.Category;
import ww.smartexpress.app.data.model.api.response.Coupon;
import ww.smartexpress.app.databinding.ItemCouponBinding;
import ww.smartexpress.app.ui.fragment.home.adapter.CategoryAdapter;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder>{

    List<Coupon> coupons;

    private OnItemClickListener onItemClickListener;

    public CouponAdapter(List<Coupon> coupons){
        this.coupons = coupons;
    }

    @NonNull
    @Override
    public CouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCouponBinding itemCouponBinding = ItemCouponBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new CouponViewHolder(itemCouponBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return coupons == null ? 0 : coupons.size();
    }

    public void addItems(List<Coupon> coupons){
        this.coupons = coupons;
        notifyDataSetChanged();
    }

    public void clearItems() {
        coupons.clear();
    }

    public class CouponViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemCouponBinding mBinding;
        private OnItemClickListener onItemClickListener;
        private Coupon coupon;


        public CouponViewHolder(@NonNull ItemCouponBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.coupon = coupons.get(position);
            mBinding.setVariable(BR.ivm, coupon);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(coupon);
        }
    }

    public interface OnItemClickListener{
        void itemClick(Coupon coupon);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
