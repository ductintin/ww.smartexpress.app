package ww.smartexpress.app.ui.otp;

import android.content.Intent;

import androidx.databinding.ObservableField;


import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.profile.EditProfileActivity;

public class LoginOTPViewModel extends BaseViewModel {


    public ObservableField<String> otp1 = new ObservableField<>();
    public ObservableField<String> otp2 = new ObservableField<>();
    public ObservableField<String> otp3 = new ObservableField<>();
    public ObservableField<String> otp4 = new ObservableField<>();
    public ObservableField<String> otp5 = new ObservableField<>();
    public ObservableField<String> otp6 = new ObservableField<>();
    public ObservableField<Boolean> isShown = new ObservableField<>(false);


    public LoginOTPViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }


    public void verifyOTP(){
        Intent intent = new Intent(application.getCurrentActivity(), HomeActivity.class);
        application.getCurrentActivity().startActivity(intent);
    }

    public void showPw(){
        boolean a = isShown.get();
        isShown.set(!a);
    }

    public void back(){
        application.getCurrentActivity().onBackPressed();
    }

}
