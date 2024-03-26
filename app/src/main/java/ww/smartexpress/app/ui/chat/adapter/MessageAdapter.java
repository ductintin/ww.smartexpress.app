package ww.smartexpress.app.ui.chat.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.data.model.api.response.ChatDetail;
import ww.smartexpress.app.databinding.ItemMessageBinding;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private List<ChatDetail> chatDetails;
    private OnItemClickListener onItemClickListener;
    public Long currentUserId;

    public ChatDetail lassChatDetail = new ChatDetail();

    public MessageAdapter() {
    }
    public void setMessages(List<ChatDetail> chatDetails){
        this.chatDetails = chatDetails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessageBinding binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new MessageViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return chatDetails == null ? 0 : chatDetails.size();
    }

    public void addItems(List<ChatDetail> chatDetails) {
        this.chatDetails = chatDetails;
        notifyDataSetChanged();
    }

    public void addItems(ChatDetail chatDetails) {
        this.chatDetails.add(chatDetails);
        notifyDataSetChanged();
    }

    public void addMessage(ChatDetail messageChat){
        if(chatDetails!= null && chatDetails.size()!=0){
            chatDetails.add(messageChat);
            notifyItemInserted(chatDetails.size());
        }else {
            chatDetails = new ArrayList<>();
            chatDetails.add(messageChat);
            notifyDataSetChanged();
        }

    }

    public void clearItems() {
        chatDetails.clear();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{
        private ItemMessageBinding mBinding;
        private ChatDetail chatDetail;
        private OnItemClickListener onItemClickListener;

        public MessageViewHolder(@NonNull ItemMessageBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.mBinding = binding;
            this.onItemClickListener = onItemClickListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void onBind(int position){
            this.chatDetail = chatDetails.get(position);
            if(position - 1>=0){
                lassChatDetail = chatDetails.get(position-1);
            }
            mBinding.setVariable(BR.ivm, chatDetail);
            mBinding.setUserId(currentUserId);
            mBinding.setLastMessage(lassChatDetail);
            Log.d("TAG", "onBind: "+currentUserId);
            Log.d("TAG", "onBind1: "+ chatDetail.getSender());
            Log.d("TAG", "onBind1: "+ lassChatDetail.getSender());
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            this.onItemClickListener.itemClick(chatDetail);
        }
    }

    public interface OnItemClickListener{
        void itemClick(ChatDetail chatDetail);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
