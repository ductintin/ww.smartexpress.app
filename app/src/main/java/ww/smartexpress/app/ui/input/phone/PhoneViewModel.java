package ww.smartexpress.app.ui.input.phone;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.ObservableField;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.LoginRequest;
import ww.smartexpress.app.data.model.api.request.RegisterRequest;
import ww.smartexpress.app.data.model.api.response.LoginResponse;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.login.LoginActivity;
import ww.smartexpress.app.ui.otp.ForgetPasswordOTPActivity;
import ww.smartexpress.app.ui.otp.LoginOTPActivity;
import ww.smartexpress.app.ui.welcome.WelcomeActivity;

public class PhoneViewModel extends BaseViewModel {

    public ObservableField<String> phoneNumber = new ObservableField<>();
    public PhoneViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void doBack(){
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        phoneNumber.set(s.toString());
    }

    public void clickButton(){
        Intent intent;
        if (phoneNumber.get().equals("111111111")){
            intent = new Intent(getApplication().getCurrentActivity(), ForgetPasswordOTPActivity.class);
        }else{
            Bundle bundle = new Bundle();
            bundle.putString(Constants.LOGIN_PHONE_NUMBER, "0" + phoneNumber.get());
            intent = new Intent(getApplication().getCurrentActivity(), LoginActivity.class);
        }
        getApplication().getCurrentActivity().startActivity(intent);
    }

}
