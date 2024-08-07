package ww.smartexpress.app.ui.fragment.notification.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.Notification;
import ww.smartexpress.app.databinding.ItemNotificationListBinding;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{
    private List<Notification> notifications;
    private OnItemClickListener onItemClickListener;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationListBinding binding = ItemNotificationListBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new NotificationViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return notifications == null ? 0 : notifications.size();
    }

    public void addItems(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    public void clearItems() {
        notifications.clear();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{
        private ItemNotificationListBinding mBinding;
        private Notification notification;
        private OnItemClickListener onItemClickListener;

        public NotificationViewHolder(@NonNull ItemNotificationListBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position){
            this.notification = notifications.get(position);
            mBinding.setVariable(BR.ivm, notification);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(notification);
        }
    }

    public interface OnItemClickListener{
        void itemClick(Notification notification);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
