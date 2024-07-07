package ww.smartexpress.app.ui.password.otp;

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
import ww.smartexpress.app.data.model.api.request.ForgetPasswordRequest;
import ww.smartexpress.app.data.model.api.response.CustomerIdResponse;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.password.forget.ResetForgetPasswordActivity;

public class VerifyForgetPasswordOTPViewModel extends BaseViewModel {
    public ObservableField<String> otp1 = new ObservableField<>("");
    public ObservableField<String> otp2 = new ObservableField<>("");
    public ObservableField<String> otp3 = new ObservableField<>("");
    public ObservableField<String> otp4 = new ObservableField<>("");

    public ObservableField<Integer> kind = new ObservableField<>(1);
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> userId = new ObservableField<>("");
    public ObservableField<Long>  milFinished = new ObservableField<>();
    public ObservableField<String> countdownOTP = new ObservableField<>();

    public CountDownTimer countDownTimer;

    public VerifyForgetPasswordOTPViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void verifyOTP(){
        String otp = otp1.get() + otp2.get() + otp3.get() + otp4.get();
        Intent intent = new Intent(getApplication().getCurrentActivity(), ResetForgetPasswordActivity.class);
        intent.putExtra(Constants.OTP, otp);
        intent.putExtra(Constants.KEY_USER_ID, userId.get());
        getApplication().getCurrentActivity().startActivity(intent);
        getApplication().getCurrentActivity().finish();
    }

    public void setCountdownOTP() {

        long OTPDurationInMillis = 60000; //60s
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

    Observable<ResponseWrapper<CustomerIdResponse>> requestForgetPassword(ForgetPasswordRequest request) {
        return repository.getApiService().forgetPassword(request)
                .doOnNext(response -> {

                });
    }
}
