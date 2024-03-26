package ww.smartexpress.app.ui.fragment.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.Banner;
import ww.smartexpress.app.databinding.ItemBannerBinding;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private List<Banner> banners;
    private OnItemClickListener onItemClickListener;
    public BannerAdapter(List<Banner> banners) {
        this.banners = banners;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBannerBinding itemBannerBinding = ItemBannerBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new BannerViewHolder(itemBannerBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return banners == null ? 0 : banners.size();
    }

    public void addItems(List<Banner> banners) {
        this.banners = banners;
        notifyDataSetChanged();
    }

    public void clearItems() {
        banners.clear();
    }


    public class BannerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemBannerBinding mBinding;
        private OnItemClickListener onItemClickListener;

        private Banner banner;
        public BannerViewHolder(ItemBannerBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.banner = banners.get(position);
            mBinding.setVariable(BR.ivm, banner);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(banner);
        }
    }
    public interface OnItemClickListener{
        void itemClick(Banner banner);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
