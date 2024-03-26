package ww.smartexpress.app.ui.order.information.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.Commodity;
import ww.smartexpress.app.databinding.ItemCommodityBinding;

public class CommodityAdapter extends RecyclerView.Adapter<CommodityAdapter.CommodityViewHolder> {

    private List<Commodity> commodityList;

    private OnItemClickListener onItemClickListener;

    private Integer selected;
    private int lastCheckedPosition = 0;

    public CommodityAdapter(List<Commodity> commodityList) {
        this.commodityList = commodityList;
    }

    @NonNull
    @Override
    public CommodityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommodityBinding itemCommodityBinding = ItemCommodityBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new CommodityViewHolder(itemCommodityBinding,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CommodityViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return commodityList == null ? 0 : commodityList.size();
    }

    public void addItems(List<Commodity> CommodityList) {
        this.commodityList = CommodityList;
        notifyDataSetChanged();
    }

    public void clearItems() {
        commodityList.clear();
    }


    public class CommodityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemCommodityBinding mBinding;

        private OnItemClickListener onItemClickListener;
        private Commodity commodity;
        private int position;

        public CommodityViewHolder(ItemCommodityBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.position = position;
            this.commodity = commodityList.get(position);
            mBinding.setVariable(BR.ivm, commodity);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            selected = position;
            commodityList.get(lastCheckedPosition).setIsSelected(false);
            commodityList.get(getAdapterPosition()).setIsSelected(true);
            lastCheckedPosition = getAdapterPosition();
            notifyDataSetChanged();
            this.onItemClickListener.itemClick(commodity);
        }
    }

    public interface OnItemClickListener{
        void itemClick(Commodity Commodity);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}
