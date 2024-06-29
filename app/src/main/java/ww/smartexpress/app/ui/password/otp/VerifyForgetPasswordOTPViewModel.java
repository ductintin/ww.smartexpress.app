package ww.smartexpress.app.ui.password.otp;

import android.content.Intent;

import androidx.databinding.ObservableField;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
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
}
