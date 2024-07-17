package ww.smartexpress.app.ui.otp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.databinding.ObservableField;


import java.util.Locale;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.ActiveCustomerRequest;
import ww.smartexpress.app.data.model.api.request.LoginRequest;
import ww.smartexpress.app.data.model.api.request.RetryOtpRegisterRequest;
import ww.smartexpress.app.data.model.api.response.CustomerIdResponse;
import ww.smartexpress.app.data.model.api.response.LoginResponse;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.profile.EditProfileActivity;
import ww.smartexpress.app.utils.AES;

public class LoginOTPViewModel extends BaseViewModel {


    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> countdownOTP = new ObservableField<>();
    public ObservableField<String> fpOTP1 = new ObservableField<>();
    public ObservableField<String> fpOTP2 = new ObservableField<>();
    public ObservableField<String> fpOTP3 = new ObservableField<>();
    public ObservableField<String> fpOTP4 = new ObservableField<>();
    public ObservableField<Long>  milFinished = new ObservableField<>();

    public CountDownTimer countDownTimer;

    public ObservableField<String> userId = new ObservableField<>("");

    public LoginOTPViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void setCountdownOTP() {

        long OTPDurationInMillis = 3*60*1000; //60s
        long intervalInMillis = 1000; //1s tick

        milFinished.set(-1L);

        countDownTimer = new CountDownTimer(OTPDurationInMillis, intervalInMillis) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / (60 * 1000)) % 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                String countdownText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                countdownOTP.set(countdownText);
                //Log.d("TAG", "onTick: " + countdownOTP.get());
                Log.d("TAG", "onTick: " + millisUntilFinished);
            }
            @Override
            public void onFinish() {
                milFinished.set(0L);
            }
        };

        countDownTimer.start();
    }

    public void back(){
        application.getCurrentActivity().onBackPressed();
    }


    Observable<ResponseWrapper<String>> activeCustomer(ActiveCustomerRequest request) {
        return repository.getApiService().activeCustomer(request)
                .doOnNext(response -> {
                });
    }

    Observable<ResponseWrapper<CustomerIdResponse>> retryActiveCustomer(RetryOtpRegisterRequest request) {
        return repository.getApiService().retryActiveCustomer(request)
                .doOnNext(response -> {
                });
    }

}
