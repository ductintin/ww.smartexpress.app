package ww.smartexpress.app.ui.chat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.ChatDetail;
import ww.smartexpress.app.data.model.api.response.ChatMessage;
import ww.smartexpress.app.data.model.api.response.Driver;
import ww.smartexpress.app.data.model.api.response.MessageChat;
import ww.smartexpress.app.data.model.api.response.Room;
import ww.smartexpress.app.data.model.api.response.RoomResponse;
import ww.smartexpress.app.data.model.api.response.UploadFileResponse;
import ww.smartexpress.app.data.websocket.Command;
import ww.smartexpress.app.data.websocket.Message;
import ww.smartexpress.app.databinding.ItemZoomImageBinding;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class ChatViewModel extends BaseViewModel {
    public ObservableField<String> message = new ObservableField<>();
    public ObservableField<Long> roomId = new ObservableField<>(0L);
    public ObservableField<Long> bookingId = new ObservableField<>(0L);
    public ObservableField<String> driverName = new ObservableField<>("");
    public ObservableField<String> codeBooking = new ObservableField<>("");
    public ObservableField<String> driverAvatar = new ObservableField<>("");
    public ObservableField<String> customerAvatar = new ObservableField<>("");
    public ObservableField<Long> driverId = new ObservableField<>(0L);
    public MutableLiveData<MessageChat> messageChat = new MutableLiveData<>(null);
    public MutableLiveData<RoomResponse> room = new MutableLiveData<>(new RoomResponse());

    public ObservableField<List<MessageChat>> messageChatList = new ObservableField<>();

    public ObservableField<Boolean> isImageLoaded = new ObservableField<>(false);
    public ObservableField<Boolean> isBottomSheetExpanded = new ObservableField<>(false);
    public ObservableField<Boolean> isImageSelected = new ObservableField<>(false);

    public ObservableField<Boolean> isCropView = new ObservableField<>(false);

    public ChatViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public Long getUserId(){
        return repository.getSharedPreferences().getLongVal(Constants.KEY_USER_ID);
    }

    public void back(){
        application.getCurrentActivity().onBackPressed();
    }

    public void sendMessage(){
        if(TextUtils.isEmpty(message.get())){
            return;
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setCodeBooking(codeBooking.get());
        chatMessage.setBookingId(bookingId.get().toString());
        chatMessage.setRoomId(roomId.get().toString());
        chatMessage.setMessage(message.get().trim());
        chatMessage.setMessageId(String.valueOf((new Date()).getTime()));
        chatMessage.setAvatar(customerAvatar.get());

        Message message1 = new Message();
        message1.setCmd(Command.CM_SEND_MESSAGE);
        message1.setPlatform(0);
        message1.setClientVersion("1.0");
        message1.setLang("vi");
        message1.setToken(repository.getSharedPreferences().getToken());
        message1.setApp(Constants.APP_CUSTOMER);
        message1.setData(chatMessage);
        application.getWebSocketLiveData().sendEvent(message1);
        addMessage();
        message.set("");
    }


    public void getRoomId(){
        roomId.set(repository.getSharedPreferences().getLongVal(Constants.ROOM_ID));
    }

    public void callDriver(){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + room.getValue().getDriver().getPhone()));
        application.getCurrentActivity().startActivity(callIntent);
    }

    public void sendImageMessage(String image){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setCodeBooking(codeBooking.get());
        chatMessage.setMessage(image);
        chatMessage.setRoomId(String.valueOf(roomId.get()));
        chatMessage.setBookingId(String.valueOf(bookingId.get()));
        chatMessage.setMessageId(String.valueOf((new Date()).getTime()));
        chatMessage.setAvatar("avatar");

        Message message = new Message();
        message.setCmd(Command.CM_SEND_MESSAGE);
        message.setPlatform(0);
        message.setClientVersion("1.0");
        message.setLang("vi");
        message.setToken(repository.getSharedPreferences().getToken());
        message.setApp(Constants.APP_CUSTOMER);
        message.setData(chatMessage);
        application.getWebSocketLiveData().sendEvent(message);

        addImageMessage(image);
    }
    public void addMessage(){
        MessageChat messageChat1 = new MessageChat();
        messageChat1.setSender(getUserId());
        messageChat1.setMsg(message.get());
        messageChat.setValue(messageChat1);
    }

    public void addImageMessage(String image){
        MessageChat messageChat1 = new MessageChat();
        messageChat1.setSender(getUserId());
        messageChat1.setMsg(image);
        messageChat.setValue(messageChat1);
    }

    public Observable<ResponseWrapper<RoomResponse>> getRoomChat(Long roomId){
        return repository.getApiService().getMyRoom(roomId)
                .doOnNext(res ->{
                    messageChatList.set(res.getData().getChatDetails());
                });
    }

    public Observable<ResponseWrapper<UploadFileResponse>> uploadImage(RequestBody requestBody){
        return repository.getApiService().uploadFile(requestBody);
    }

    public void zoomImage(String url){
        Dialog dialog = new Dialog(getApplication().getCurrentActivity());
        ItemZoomImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getApplication().getCurrentActivity()), R.layout.item_zoom_image,null, false);
        binding.setUrl(url);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(binding.getRoot());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }
}
