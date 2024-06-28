package ww.smartexpress.app.ui.profile;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.BuildConfig;
import ww.smartexpress.app.R;

import ww.smartexpress.app.data.model.api.response.ProfileResponse;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.databinding.FragmentProfileBinding;
import ww.smartexpress.app.di.component.FragmentComponent;
import ww.smartexpress.app.ui.base.fragment.BaseFragment;
import ww.smartexpress.app.ui.chat.ChatActivity;
import ww.smartexpress.app.ui.index.IndexActivity;
import ww.smartexpress.app.ui.password.reset.ResetPasswordActivity;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileFragmentViewModel> {


    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void performDataBinding() {
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//        if (Build.VERSION.SDK_INT >= 19) {
//            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
//        if (Build.VERSION.SDK_INT >= 21) {
//            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        clearStatusBarFlags();
    }

    public void clearStatusBarFlags() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            // Xóa flags fullscreen đã thiết lập
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setA(this);
        getProfile();
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfile();
    }

    public void getProfile(){
        viewModel.loadProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserEntity>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull UserEntity userEntity) {
                        ProfileResponse profileResponse = ProfileResponse.builder()
                                .id(userEntity.getId())
                                .avatar(userEntity.getAvatar())
                                .email(userEntity.getEmail())
                                .name(userEntity.getName())
                                .phone(userEntity.getPhone())
                                .build();

                        Log.d("TAG", "onSuccess: " + userEntity.getEncryptedPassword());
                        viewModel.profile.set(profileResponse);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        loadProfile();
                    }
                });
    }

    public void loadProfile(){
        viewModel.compositeDisposable.add(viewModel.getProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.isResult()){
                        viewModel.hideLoading();
                    }else {
                        viewModel.hideLoading();
                        viewModel.showErrorMessage(response.getMessage());
                    }
                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getActivity().getString(R.string.network_error));
                    error.printStackTrace();
                })
        );

    }

    public void createnoti() {
        final Bitmap[] bitmap = {null};

        Glide.with(getContext())
                .asBitmap()
                .load(BuildConfig.MEDIA_URL + "/v1/file/download" + "/6783713748123648/AVATAR/AVATAR_XkdibfQk0l.jpg")
                .into(new CustomTarget<Bitmap>() {
                          @Override
                          public void onResourceReady(@io.reactivex.rxjava3.annotations.NonNull Bitmap resource, @io.reactivex.rxjava3.annotations.Nullable Transition<? super Bitmap> transition) {

                              bitmap[0] = resource;
                              String id = "SmartExpress Chat";
                              String title = "Nguyễn An";
                              String subtitle = "Hello dạo nhay co adad Hello dạo nhay co adad Hello dạo nhay co adad Hello dạo nhay co adad Hello dạo nhay co adad Hello dạo nhay co adad";


                              RemoteViews notificationLayout = new RemoteViews(getActivity().getPackageName(), R.layout.item_shipping_notification);


                              if (bitmap != null) {
                                  notificationLayout.setImageViewBitmap(R.id.imgIcon, bitmap[0]);
                              }

                              notificationLayout.setTextViewText(R.id.tvNotificationTitle, title);
                              notificationLayout.setTextViewText(R.id.tvNotificationSubtitle, subtitle);

                              NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                  NotificationChannel channel = manager.getNotificationChannel(id);
                                  if (channel == null) {
                                      channel = new NotificationChannel(id, "Channel Title", NotificationManager.IMPORTANCE_HIGH);

                                      channel.setDescription("[Channel Description]");
                                      channel.enableVibration(true);
                                      channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
                                      channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                                      manager.createNotificationChannel(channel);
                                  }
                              }

                              NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), id)
                                      .setSmallIcon(R.drawable.ic_icon_ship)
                                      .setCustomContentView(notificationLayout)
                                      .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                                      .setPriority(NotificationCompat.PRIORITY_HIGH)
                                      .setVibrate(new long[]{100, 1000, 200, 340})
                                      .setAutoCancel(false)
                                      .setTicker("Notification");


                              NotificationManagerCompat m = NotificationManagerCompat.from(getContext());

                              m.notify(0, builder.build());

                              // TODO Do some work: pass this bitmap
                          }

                          @Override
                          public void onLoadCleared(@io.reactivex.rxjava3.annotations.Nullable Drawable placeholder) {
                          }
                      }
                );
    }
}
