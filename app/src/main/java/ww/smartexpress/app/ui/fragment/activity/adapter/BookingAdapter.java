package ww.smartexpress.app.ui.fragment.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.Order;
import ww.smartexpress.app.databinding.ItemBookingBinding;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder>{
    private List<BookingResponse> bookings;
    private OnItemClickListener onItemClickListener;

    public BookingAdapter() {
    }

    public BookingAdapter(List<BookingResponse> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookingBinding binding = ItemBookingBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new BookingViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return bookings == null ? 0 : bookings.size();
    }

    public void setBookings(List<BookingResponse> bookings) {
        this.bookings = bookings;
        notifyDataSetChanged();
    }

    public void clearItems() {
        bookings.clear();
        notifyDataSetChanged();
    }


    public class BookingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemBookingBinding mBinding;
        private BookingResponse booking;
        private OnItemClickListener onItemClickListener;

        public BookingViewHolder(@NonNull ItemBookingBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position){
            this.booking = bookings.get(position);
            mBinding.setVariable(BR.ivm, booking);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(booking);
        }
    }
    public interface OnItemClickListener{
        void itemClick(BookingResponse booking);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
