package ww.smartexpress.app.ui.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.response.ChatDetail;
import ww.smartexpress.app.data.model.api.response.ChatMessage;
import ww.smartexpress.app.databinding.ActivityChatBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.chat.adapter.MessageAdapter;

public class ChatActivity extends BaseActivity<ActivityChatBinding, ChatViewModel> {

    MessageAdapter messageAdapter;
    List<ChatDetail> chatDetails = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageAdapter = new MessageAdapter();
        messageAdapter.currentUserId = viewModel.getUserId();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            viewModel.roomId.set(bundle.getLong(Constants.ROOM_ID));
            viewModel.bookingId.set(bundle.getLong("BOOKING_ID"));
            viewModel.codeBooking.set(bundle.getString("BOOKING_CODE"));
            getMessage();
        }else{
            viewModel.getRoomId();
            getMessage();
        }

        viewModel.messageChat.observe(this, messageChat -> {
            messageAdapter.addMessage(messageChat);
            if(messageAdapter.getItemCount() > 0){
                viewBinding.rcChat.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });

    }

    public void loadMessages(){

        messageAdapter.setMessages(chatDetails);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this
                ,LinearLayoutManager.VERTICAL, false);
        viewBinding.rcChat.setLayoutManager(layout);
        viewBinding.rcChat.setItemAnimator(new DefaultItemAnimator());
        viewBinding.rcChat.setAdapter(messageAdapter);
        if(messageAdapter.getItemCount() > 0){
            viewBinding.rcChat.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
        }
        messageAdapter.setOnItemClickListener(trip -> {

        });
    }


    public void getMessage(){
        viewModel.showLoading();
        viewModel.compositeDisposable.add(viewModel.getMyRoom(viewModel.roomId.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    viewModel.hideLoading();
                    if(response.isResult()){
                        if(response.getData().getChatDetails() != null){
                            chatDetails = response.getData().getChatDetails();
                        }
                        viewModel.driverName.set(response.getData().getDriver().getFullName());
                        viewModel.driverAvatar.set(response.getData().getDriver().getAvatar());
                        viewModel.driverId.set(response.getData().getDriver().getId());
                        viewModel.customerAvatar.set(response.getData().getCustomer().getAvatar());
                        loadMessages();
                    }else{
                        viewModel.hideLoading();
                        viewModel.showErrorMessage(response.getMessage());
                    }

                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));

    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        ChatMessage chatDetail = viewModel.getApplication().getChatDetail();
//        if(chatDetail != null){
//            ChatDetail messageChat1 = new ChatDetail();
//            messageChat1.setMsg(chatDetail.getMessage());
//            messageChat1.setSender(viewModel.driverId.get());
//            messageChat1.setSenderAvatar(viewModel.driverAvatar.get());
//            viewModel.messageChat.setValue(messageChat1);
//            viewModel.getApplication().setChatDetail(null);
//        }
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent == null){
            return;
        }

        if(intent.getStringExtra("BOOKING_CODE").equals(viewModel.codeBooking.get())){
            ChatMessage chatDetail = viewModel.getApplication().getChatDetail();
            if(chatDetail != null){
                ChatDetail messageChat1 = new ChatDetail();
                messageChat1.setMsg(chatDetail.getMessage());
                messageChat1.setSender(viewModel.driverId.get());
                messageChat1.setSenderAvatar(viewModel.driverAvatar.get());
                viewModel.messageChat.setValue(messageChat1);
                viewModel.getApplication().setChatDetail(null);
            }
        }
    }
}
