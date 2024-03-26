package ww.smartexpress.app.ui.index;

import android.content.Intent;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.login.LoginActivity;
import ww.smartexpress.app.ui.register.RegisterActivity;
import ww.smartexpress.app.ui.signin.SignInActivity;

public class IndexViewModel extends BaseViewModel {
    public IndexViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void navigateToLogin(){
        Intent intent = new Intent(application.getCurrentActivity(), SignInActivity.class);
        application.getCurrentActivity().startActivity(intent);
    }

    public void navigateToRegister(){
        Intent intent = new Intent(application.getCurrentActivity(), RegisterActivity.class);
        application.getCurrentActivity().startActivity(intent);
    }
}
