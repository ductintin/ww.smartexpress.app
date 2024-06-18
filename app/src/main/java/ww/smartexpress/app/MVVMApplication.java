package ww.smartexpress.app;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.Objects;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.response.ChatMessage;
import ww.smartexpress.app.data.model.api.response.DriverBookingResponse;
import ww.smartexpress.app.data.model.api.response.DriverPosition;
import ww.smartexpress.app.data.model.api.response.MessageOfTransaction;
import ww.smartexpress.app.data.model.api.response.TransactionMessage;
import ww.smartexpress.app.data.websocket.Command;
import ww.smartexpress.app.data.websocket.Message;
import ww.smartexpress.app.data.websocket.SocketEventModel;
import ww.smartexpress.app.data.websocket.SocketListener;
import ww.smartexpress.app.data.websocket.WebSocketLiveData;
import ww.smartexpress.app.databinding.ItemNotificationBinding;
import ww.smartexpress.app.di.component.AppComponent;
import ww.smartexpress.app.di.component.DaggerAppComponent;
import ww.smartexpress.app.others.MyTimberDebugTree;
import ww.smartexpress.app.others.MyTimberReleaseTree;
import ww.smartexpress.app.ui.chat.ChatActivity;
import ww.smartexpress.app.ui.delivery.BookDeliveryActivity;
import ww.smartexpress.app.ui.deposit.DepositActivity;
import ww.smartexpress.app.ui.fragment.search.SearchFragment;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.qrcode.QrcodeActivity;
import ww.smartexpress.app.ui.trip.TripActivity;
import ww.smartexpress.app.ui.trip.detail.TripDetailActivity;
import ww.smartexpress.app.utils.AES;
import ww.smartexpress.app.utils.DialogUtils;
import ww.smartexpress.app.utils.NumberUtils;

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

    @Getter
    private AES aes;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            aes = new AES();
            aes.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
                        navigateFromDriverAcceptToBookDeliveryActivity(socketEventModel);
                        break;
                    case Command.CMD_DRIVER_PICKUP_SUCCESS:
                        navigateFromDriverPickUpToBookDeliveryActivity(socketEventModel);
                        break;
                    case Command.CMD_DRIVER_DONE_BOOKING:
                        navigateFromBookingDoneToBookDeliveryActivity(socketEventModel);
                        break;
                    case Command.CM_SEND_MESSAGE:
                        navigateToChat(socketEventModel);
                        break;
                    case Command.CMD_DRIVER_CANCEL_BOOKING:
                        navigateFromDriverCancelBookingToBookDeliveryActivity(socketEventModel);
                        break;
                    case Command.CMD_NOT_FOUND_DRIVER:
                        navigateToHomeActivity(socketEventModel);
                        break;
                    case Command.CMD_DRIVER_UPDATE_POSITION:
                        navigateFromDriverUpdatePositionToBookDeliveryActivity(socketEventModel);
                        break;
                    case Command.CMD_CLIENT_RECEIVED_PUSH_NOTIFICATION:
                        transactionNotification(socketEventModel.getMessage());
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
        if(currentActivity instanceof ChatActivity){
            Message message = socketEventModel.getMessage();
            chatDetail = message.getDataObject(ChatMessage.class);
            Intent intent = new Intent(currentActivity, ChatActivity.class);
            if(chatDetail != null){
                intent.putExtra("BOOKING_CODE", chatDetail.getCodeBooking());
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                currentActivity.startActivity(intent);
            }
        }
    }

    public void navigateFromDriverAcceptToBookDeliveryActivity(SocketEventModel socketEventModel){
        Message message = socketEventModel.getMessage();
        createNotification(message, 1);

        if(currentActivity instanceof BookDeliveryActivity){
            DriverBookingResponse bookingResponse = message.getDataObject(DriverBookingResponse.class);
            driverBookingResponse = bookingResponse;
            Intent intent = new Intent(currentActivity, BookDeliveryActivity.class);
            intent.putExtra("BOOKING_CODE", bookingResponse.getCodeBooking());
            intent.putExtra("STATE_BOOKING", 1);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            currentActivity.startActivity(intent);
        }
    }

        public void navigateFromDriverPickUpToBookDeliveryActivity(SocketEventModel socketEventModel){
            Message message = socketEventModel.getMessage();
            createNotification(message, 2);

            if(currentActivity instanceof BookDeliveryActivity){
                Intent intent = new Intent(currentActivity, BookDeliveryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("BOOKING_ID", message.getDataObject(DriverBookingResponse.class).getBookingId());
                intent.putExtra("STATE_BOOKING", 3);
                currentActivity.startActivity(intent);
            }
        }

    public void navigateFromBookingDoneToBookDeliveryActivity(SocketEventModel socketEventModel){
        Message message = socketEventModel.getMessage();
        DriverBookingResponse response = message.getDataObject(DriverBookingResponse.class);
        if(currentActivity instanceof BookDeliveryActivity){
            Intent intent = new Intent(currentActivity, BookDeliveryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("BOOKING_ID",response.getBookingId());
            intent.putExtra("STATE_BOOKING", 4);
            currentActivity.startActivity(intent);
        }else if(currentActivity instanceof HomeActivity){
            HomeActivity homeActivity = ((HomeActivity) currentActivity);
            if(homeActivity.activeFragment instanceof SearchFragment){
                Log.d("TAG", "navigateFromBookingDoneToBookDeliveryActivity: in search fragment");
                Intent intent = new Intent(currentActivity, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                currentActivity.startActivity(intent);
            }
        }else if(currentActivity instanceof TripActivity){
            Intent intent = new Intent(currentActivity, TripActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("BOOKING_ID",response.getBookingId());
            currentActivity.startActivity(intent);
        }
        webSocketLiveData.removeBookingCode(response.getBookingId());
    }

    public void navigateFromDriverCancelBookingToBookDeliveryActivity(SocketEventModel socketEventModel){
        Message message = socketEventModel.getMessage();

//        Intent intent = new Intent(currentActivity, BookDeliveryActivity.class);
//        intent.putExtra(Constants.BOOKING_STATE, 0);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        currentActivity.startActivity(intent);

        if( currentActivity instanceof BookDeliveryActivity){
            Intent intent = new Intent(currentActivity, BookDeliveryActivity.class);
            intent.putExtra("STATE_BOOKING", 5);
            intent.putExtra("BOOKING_ID", message.getDataObject(DriverBookingResponse.class).getBookingId());
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            currentActivity.startActivity(intent);
        }
    }

    public void navigateToHomeActivity(SocketEventModel socketEventModel){
        Intent intent = new Intent(currentActivity, BookDeliveryActivity.class);
        intent.putExtra(Constants.BOOKING_STATE, -100);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        currentActivity.startActivity(intent);
    }

    public void navigateFromDriverUpdatePositionToBookDeliveryActivity(SocketEventModel socketEventModel){
        Message message = socketEventModel.getMessage();
        if( currentActivity instanceof BookDeliveryActivity){
            Intent intent = new Intent(currentActivity, BookDeliveryActivity.class);
            DriverPosition driverPosition =  message.getDataObject(DriverPosition.class);
            intent.putExtra("STATE_BOOKING", 2);
            intent.putExtra("BOOKING_CODE", driverPosition.getCodeBooking());
            intent.putExtra(Constants.DRIVER_POSITION, driverPosition.getLatitude() + "," + driverPosition.getLongitude());
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            currentActivity.startActivity(intent);
        }
    }

    public void createNotification(Message message, int option){
        String id = "SmartExpress";
        String title = "";
        String content = "";
        DriverBookingResponse bookingResponse = message.getDataObject(DriverBookingResponse.class);

        Intent notificationIntent = new Intent(getCurrentActivity(), BookDeliveryActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        switch (option){
            case 1: //accept
                title = "Đã tìm thấy tài xế giao hàng";
                content = "Đơn hàng: " + bookingResponse.getCodeBooking() + " .Tài xế đang trên đường đến lấy.";
                notificationIntent.putExtra("BOOKING_CODE", bookingResponse.getCodeBooking());
                notificationIntent.putExtra("STATE_BOOKING", 1);
                break;
            case 2: // pickup
                title = "Tài xế đã nhận đơn hàng";
                content = "Đơn hàng: " + bookingResponse.getBookingId() + " .Tài xế đang trên đường giao đến điểm nhận.";
                notificationIntent.putExtra("BOOKING_ID", bookingResponse.getBookingId());
                notificationIntent.putExtra("STATE_BOOKING", 3);
                break;
            case 3: // done
                title = "Đơn hàng giao thành công!";
                content = "Cảm ơn bạn đã chọn SmartExpress. Hẹn gặp lại lần sau!";
                notificationIntent = new Intent(getCurrentActivity(), TripDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.CUSTOMER_BOOKING_DETAIL_ID, bookingResponse.getBookingId());
                break;
        }


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = manager.getNotificationChannel(id);
            if(channel == null){
                channel = new NotificationChannel(id, "Channel Title", NotificationManager.IMPORTANCE_HIGH);

                channel.setDescription("[Channel Description]");
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100,1000,200,340});
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                manager.createNotificationChannel(channel);
            }
        }

        PendingIntent contentIntent = PendingIntent.getActivity(getCurrentActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id)
                .setSmallIcon(R.drawable.ic_icon_activity)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.car_vehicle))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigLargeIcon(null)
                )
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100,1000,200,340})
                .setAutoCancel(false)
                .setTicker("Notification");

        builder.setContentIntent(contentIntent);

        NotificationManagerCompat m = NotificationManagerCompat.from(getApplicationContext());
        m.notify(new Random().nextInt(), builder.build());


    }

    public void transactionNotification(Message message){
        TransactionMessage tm = message.getDataObject(TransactionMessage.class);
        Log.d("TAG", "transactionNotification: " + tm.toString());
        MessageOfTransaction mt = ApiModelUtils.fromJson(tm.getMessage(), MessageOfTransaction.class);
        ItemNotificationBinding itemNotificationBinding = ItemNotificationBinding.inflate(getCurrentActivity().getLayoutInflater());
        View layout = itemNotificationBinding.getRoot();
        itemNotificationBinding.setKind(tm.getKind());

        switch (tm.getKind()){
            case 1:
                itemNotificationBinding.setTitle("Nạp tiền thành công");
                itemNotificationBinding.setSubtitle("Bạn đã nạp thành công " + NumberUtils.formatCurrency(mt.getMoney()) + " vào ví SmartExpress.");
                break;
            case 2:
                itemNotificationBinding.setTitle("Rút tiền thành công");
                itemNotificationBinding.setSubtitle("Bạn đã rút thành công " + NumberUtils.formatCurrency(mt.getMoney()) + " về tài khoản ngân hàng.");
                break;
            case 3:
                itemNotificationBinding.setTitle("Yêu cầu rút tiền thất bại");
                itemNotificationBinding.setSubtitle("Yêu cầu rút tiền thất bại. Vui lòng thử lại sau");
                break;
            default:
                break;
        }

        Toast toast = new Toast(getCurrentActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 150);
        toast.setView(layout);
        toast.show();

        Intent intentDeposit = new Intent(getCurrentActivity(), DepositActivity.class);
        intentDeposit.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        getCurrentActivity().startActivity(intentDeposit);

        if(currentActivity instanceof QrcodeActivity){
            currentActivity.finish();
        }

    }
}
