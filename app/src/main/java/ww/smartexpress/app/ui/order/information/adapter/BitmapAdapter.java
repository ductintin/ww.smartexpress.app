package ww.smartexpress.app.ui.order.information.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import ww.smartexpress.app.databinding.ItemPictureBinding;

public class BitmapAdapter extends RecyclerView.Adapter<BitmapAdapter.BitmapViewHolder>{
    private List<Bitmap> bitmaps;
    private OnItemClickListener onItemClickListener;

    public BitmapAdapter(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BitmapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPictureBinding binding = ItemPictureBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new BitmapViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BitmapViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return bitmaps == null ? 0 : bitmaps.size();
    }

    public void addItems(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        notifyDataSetChanged();
    }

    public void clearItems() {
        bitmaps.clear();
    }

    public void clearItem(int position){
        this.bitmaps.remove(position);
        notifyDataSetChanged();
    }

    public void addPicture(Bitmap bitmap){
        if(bitmaps!= null && bitmaps.size()!=0){
            bitmaps.add(bitmap);
            notifyItemInserted(bitmaps.size());
        }else {
            bitmaps = new ArrayList<>();
            bitmaps.add(bitmap);
            notifyDataSetChanged();
        }

    }

    public class BitmapViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemPictureBinding mBinding;
        private Bitmap bitmap;
        private OnItemClickListener onItemClickListener;

        public BitmapViewHolder(@NonNull ItemPictureBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position){
            this.bitmap = bitmaps.get(position);
//            mBinding.setVariable(BR.ivm, bitmaps);
            mBinding.picture.setImageBitmap(bitmap);
            mBinding.executePendingBindings();
            mBinding.deletePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.remove(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener{
        void itemClick(int position);

        void remove(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
