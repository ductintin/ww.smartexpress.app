package ww.smartexpress.app.ui.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;

import androidx.databinding.ObservableField;


import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.home.HomeActivity;

public class LoginViewModel extends BaseViewModel {
    public ObservableField<String> pw1 = new ObservableField<>();
    public ObservableField<String> pw2 = new ObservableField<>();
    public ObservableField<String> pw3 = new ObservableField<>();
    public ObservableField<String> pw4 = new ObservableField<>();
    public ObservableField<String> pw5 = new ObservableField<>();
    public ObservableField<String> pw6 = new ObservableField<>();
    public ObservableField<Boolean> isShown = new ObservableField<>();

    public LoginViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void forgetPassword(){}

    public void back(){
        application.getCurrentActivity().onBackPressed();
    }

    public void listen(Editable editable){
        if(!TextUtils.isEmpty(pw1.get()) && !TextUtils.isEmpty(pw2.get()) && !TextUtils.isEmpty(pw3.get()) && !TextUtils.isEmpty(pw4.get())
                && !TextUtils.isEmpty(pw5.get()) && !TextUtils.isEmpty(pw6.get())){
            Intent intent = new Intent(application.getCurrentActivity(), HomeActivity.class);
            application.getCurrentActivity().startActivity(intent);
            application.getCurrentActivity().finish();
        }
    }

}
