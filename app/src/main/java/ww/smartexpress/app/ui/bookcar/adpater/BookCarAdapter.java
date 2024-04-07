package ww.smartexpress.app.ui.bookcar.adpater;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.BookCar;
import ww.smartexpress.app.databinding.ItemWinBikeBinding;

public class BookCarAdapter extends RecyclerView.Adapter<BookCarAdapter.BookCarViewHolder> {

    private List<BookCar> bookCars;
    private OnItemClickListener onItemClickListener;
    public BookCarAdapter(List<BookCar> bookCars) {
        this.bookCars = bookCars;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    private int selected =0;

    @NonNull
    @Override
    public BookCarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWinBikeBinding itemWinBikeBinding = ItemWinBikeBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new BookCarViewHolder(itemWinBikeBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookCarViewHolder holder, int position) {
        holder.onBind(position);
        if(position==selected){
            holder.mBinding.itemLayout.setBackgroundResource(R.drawable.item_win_bike_background);
        }else{
            holder.mBinding.itemLayout.setBackgroundResource(R.drawable.bg_green_light_elevation);
        }

        if(bookCars.get(position).getDiscount()!=0){
            holder.mBinding.txtWinBikeDiscount.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return bookCars == null ? 0 : bookCars.size();
    }

    public void addItems(List<BookCar> bookCars) {
        this.bookCars = bookCars;
        notifyDataSetChanged();
    }

    public void clearItems() {
        bookCars.clear();
    }


    public class BookCarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemWinBikeBinding mBinding;
        private OnItemClickListener onItemClickListener;
        private BookCar bookCar;

        private int position;
        public BookCarViewHolder(ItemWinBikeBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position) {
            this.position = position;
            this.bookCar = bookCars.get(position);
            mBinding.setVariable(BR.ivm, bookCar);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            selected = position;
            notifyDataSetChanged();
            this.onItemClickListener.itemClick(bookCar);
        }
    }
    public interface OnItemClickListener{
        void itemClick(BookCar bookCar);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
