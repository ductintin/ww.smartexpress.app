package ww.smartexpress.app.ui.chat;

import android.text.TextUtils;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.ChatDetail;
import ww.smartexpress.app.data.model.api.response.ChatMessage;
import ww.smartexpress.app.data.model.api.response.Driver;
import ww.smartexpress.app.data.model.api.response.Room;
import ww.smartexpress.app.data.websocket.Command;
import ww.smartexpress.app.data.websocket.Message;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class ChatViewModel extends BaseViewModel {
    public ObservableField<String> message = new ObservableField<>();
    public ObservableField<Long> roomId = new ObservableField<>(0L);
    public ObservableField<String> driverName = new ObservableField<>("");
    public ObservableField<String> driverAvatar = new ObservableField<>("");
    public ObservableField<Long> driverId = new ObservableField<>(0L);
    public MutableLiveData<ChatDetail> messageChat = new MutableLiveData<>(null);

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
        chatMessage.setCodeBooking(application.getWebSocketLiveData().getCodeBooking());
        chatMessage.setMessage(message.get().trim());
        chatMessage.setMessageId(String.valueOf((new Date()).getTime()));

        Message message = new Message();
        message.setCmd(Command.CM_SEND_MESSAGE);
        message.setPlatform(0);
        message.setClientVersion("1.0");
        message.setLang("vi");
        message.setToken(repository.getSharedPreferences().getToken());
        message.setApp(Constants.APP_CUSTOMER);
        message.setData(chatMessage);
        application.getWebSocketLiveData().sendEvent(message);
        addMessage();
    }

    Observable<ResponseWrapper<Room>> getMyRoom(Long roomId) {
        return repository.getApiService().getMyRoom(roomId)
                .doOnNext(response -> {

                });
    }

    public void addMessage(){
//        messageChat.setValue(new MessageChat(null,"11/11/2023 11:10:11",Long.valueOf(1234567890),getUserId(),message.get()));
        ChatDetail messageChat1 = new ChatDetail();
        messageChat1.setSender(repository.getSharedPreferences().getLongVal(Constants.KEY_USER_ID));
        messageChat1.setMsg(message.get());
        messageChat.setValue(messageChat1);
        message.set("");
    }

    public void getRoomId(){
        roomId.set(repository.getSharedPreferences().getLongVal(Constants.ROOM_ID));
    }
}
