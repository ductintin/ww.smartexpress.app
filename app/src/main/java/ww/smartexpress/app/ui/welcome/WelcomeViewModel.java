package ww.smartexpress.app.ui.welcome;

import android.content.Intent;

import androidx.databinding.ObservableBoolean;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.index.IndexActivity;
import ww.smartexpress.app.ui.input.phone.PhoneActivity;

public class WelcomeViewModel extends BaseViewModel {

    public ObservableBoolean checkLocationPermission = new ObservableBoolean(false);
    public WelcomeViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public void openInputPhone(){
        String token = repository.getSharedPreferences().getToken();
        Intent intent;
        if(token.equals("NULL") || token.isEmpty()){
            intent = new Intent(getApplication().getCurrentActivity(), IndexActivity.class);
        }else{
            intent = new Intent(getApplication().getCurrentActivity(), HomeActivity.class);
        }
        getApplication().getCurrentActivity().startActivity(intent);
        getApplication().getCurrentActivity().finish();
    }


}
