package ww.smartexpress.app;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.response.BookingDoneResponse;
import ww.smartexpress.app.data.model.api.response.ChatMessage;
import ww.smartexpress.app.data.model.api.response.DriverBookingResponse;
import ww.smartexpress.app.data.model.api.response.DriverPosition;
import ww.smartexpress.app.data.websocket.Command;
import ww.smartexpress.app.data.websocket.Message;
import ww.smartexpress.app.data.websocket.SocketEventModel;
import ww.smartexpress.app.data.websocket.SocketListener;
import ww.smartexpress.app.data.websocket.WebSocketLiveData;
import ww.smartexpress.app.di.component.AppComponent;
import ww.smartexpress.app.di.component.DaggerAppComponent;
import ww.smartexpress.app.others.MyTimberDebugTree;
import ww.smartexpress.app.others.MyTimberReleaseTree;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.chat.ChatActivity;
import ww.smartexpress.app.ui.delivery.BookDeliveryActivity;
import ww.smartexpress.app.ui.trip.TripActivity;
import ww.smartexpress.app.ui.trip.complete.TripCompleteActivity;
import ww.smartexpress.app.utils.DialogUtils;

public class MVVMApplication extends Application implements LifecycleObserver, SocketListener {
    @Setter
    @Getter
    private AppCompatActivity currentActivity;

    @Getter
    private AppComponent appComponent;
    private Boolean inBackground;

    @Getter
    private WebSocketLiveData webSocketLiveData;

    @Getter
    @Setter
    private Boolean paused = false;

    @Getter
    @Setter
    private DriverBookingResponse driverBookingResponse;

    @Getter
    @Setter
    private ChatMessage chatDetail;

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable firebase log
        FirebaseCrashlytics firebaseCrashlytics = FirebaseCrashlytics.getInstance();
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(true);


        if (BuildConfig.DEBUG) {
            Timber.plant(new MyTimberDebugTree());
        } else {
            Timber.plant(new MyTimberReleaseTree(firebaseCrashlytics));
        }

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();
        appComponent.inject(this);

        // Init Toasty
        Toasty.Config.getInstance()
                .allowQueue(false)
                .apply();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
//        insertMock();
//        startOrderSchedule();

        //websocket
        webSocketLiveData = WebSocketLiveData.getInstance();
        webSocketLiveData.setSocketListener(this);
        webSocketLiveData.setAppOnline(true);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        //App in background
        Timber.d("APP IN BACKGROUND");
        inBackground = true;
        webSocketLiveData.setAppOnline(false);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        // App in foreground
        Timber.d("APP IN FOREGROUND");
        inBackground = false;
        webSocketLiveData.setAppOnline(true);
    }

    public PublishSubject<Integer> showDialogNoInternetAccess() {
        final PublishSubject<Integer> subject = PublishSubject.create();
        currentActivity.runOnUiThread(() ->
                DialogUtils.dialogConfirm(currentActivity, currentActivity.getResources().getString(R.string.network_error),
                        currentActivity.getResources().getString(R.string.newtwork_error_button_retry),
                        (dialogInterface, i) -> subject.onNext(1), currentActivity.getResources().getString(R.string.newtwork_error_button_exit),
                        (dialogInterface, i) -> System.exit(0))
        );
        return subject;
    }

    public void startSocket(String token){
        webSocketLiveData.setSession(token);
        webSocketLiveData.startSocket();
    }

    public void stopSocket(){
        webSocketLiveData.stopSocket();
    }

    @Override
    public void onMessage(SocketEventModel socketEventModel) {
        if (paused){
            Timber.d("APP PAUSED, IGNORE DATA RECEIVE");
            return;
        }
        currentActivity.runOnUiThread(()->handleSocket(socketEventModel));
    }

    public void handleSocket(SocketEventModel socketEventModel){
        Message message;
        if(!webSocketLiveData.isAppOnline()){
            return ;
        }
        if(Objects.equals(socketEventModel.getEvent(), SocketEventModel.EVENT_MESSAGE)){
            if(socketEventModel.getMessage().getResponseCode() == 200){
                switch (socketEventModel.getMessage().getCmd()){
                    case Command.CMD_DRIVER_ACCEPTED:
                        navigateToBookCarActivity(socketEventModel);
                        break;
                    case Command.CMD_DRIVER_PICKUP_SUCCESS:
                        navigateToTripActivity(socketEventModel);
                        break;
                    case Command.CMD_DRIVER_DONE_BOOKING:
                        navigateToTripCompleteActivity(socketEventModel);
                        break;
                    case Command.CM_SEND_MESSAGE:
                        navigateToChat(socketEventModel);
                        break;
                    case Command.CMD_DRIVER_CANCEL_BOOKING:
                        navigateToBookCarCancelActivity(socketEventModel);
                        break;
                    case Command.CMD_NOT_FOUND_DRIVER:
                        navigateToHomeActivity(socketEventModel);
                        break;
                    case Command.CMD_DRIVER_UPDATE_POSITION:
                        navigateToBookCarAcceptActivity(socketEventModel);
                        break;
                    default:
                        break;
                }
            }else {

            }
        }

    }
    @Override
    public void onTimeout(Message message) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onClosed() {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onSessionExpire() {

    }

    @Override
    public void onPingFailure() {

    }

    public void navigateToChat(SocketEventModel socketEventModel){
        Message message = socketEventModel.getMessage();
        chatDetail = message.getDataObject(ChatMessage.class);
        Intent intent = new Intent(currentActivity, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        currentActivity.startActivity(intent);
    }

    public void navigateToBookCarActivity(SocketEventModel socketEventModel){
        Message message = socketEventModel.getMessage();
        DriverBookingResponse bookingResponse = message.getDataObject(DriverBookingResponse.class);
        driverBookingResponse = bookingResponse;

        Intent intent = new Intent(currentActivity, BookDeliveryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        currentActivity.startActivity(intent);
    }

    public void navigateToTripActivity(SocketEventModel socketEventModel){
        Message message = socketEventModel.getMessage();
//        DriverBookingResponse bookingResponse = message.getDataObject(DriverBookingResponse.class);
//        driverBookingResponse = bookingResponse;

        Intent intent = new Intent(currentActivity, BookDeliveryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        currentActivity.startActivity(intent);
    }

    public void navigateToTripCompleteActivity(SocketEventModel socketEventModel){
        Message message = socketEventModel.getMessage();
        Intent intent = new Intent(currentActivity, TripCompleteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.BOOKING_COMPLETE_STATE, true);
        bundle.putString(Constants.CUSTOMER_BOOKING_ID, message.getDataObject(BookingDoneResponse.class).getBookingId());
        intent.putExtras(bundle);
        currentActivity.startActivity(intent);
    }

    public void navigateToBookCarCancelActivity(SocketEventModel socketEventModel){
        Intent intent = new Intent(currentActivity, BookDeliveryActivity.class);
        intent.putExtra(Constants.BOOKING_STATE, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        currentActivity.startActivity(intent);
    }

    public void navigateToHomeActivity(SocketEventModel socketEventModel){
        Intent intent = new Intent(currentActivity, BookDeliveryActivity.class);
        intent.putExtra(Constants.BOOKING_STATE, -100);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        currentActivity.startActivity(intent);
    }

    public void navigateToBookCarAcceptActivity(SocketEventModel socketEventModel){
        Message message = socketEventModel.getMessage();
        if( currentActivity instanceof BookDeliveryActivity){
            Intent intent = new Intent(currentActivity, BookDeliveryActivity.class);
            DriverPosition driverPosition = new DriverPosition();
            driverPosition = message.getDataObject(DriverPosition.class);
            intent.putExtra(Constants.DRIVER_POSITION, driverPosition.getLatitude() + "," + driverPosition.getLongitude());
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            currentActivity.startActivity(intent);
        }
    }
}
