package ww.smartexpress.app.ui.splashform;

import android.content.Intent;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.index.IndexActivity;
import ww.smartexpress.app.ui.welcome.WelcomeViewModel;

public class SplashFormViewModel extends WelcomeViewModel {
    public ObservableField<Integer> state = new ObservableField<>();
    public SplashFormViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public boolean checkToken(){
        String token = repository.getSharedPreferences().getToken();
        Intent intent;
        if(token.equals("NULL") || token.isEmpty()){
            //intent = new Intent(getApplication().getCurrentActivity(), IndexActivity.class);
            return false;
        }else{
            return true;
            //intent = new Intent(getApplication().getCurrentActivity(), HomeActivity.class);
        }
//        getApplication().getCurrentActivity().startActivity(intent);
//        getApplication().getCurrentActivity().finish();
    }

    Observable<ResponseWrapper<BookingResponse>> getCurrentBooking() {
        return repository.getApiService().getCurrentBooking()
                .doOnNext(response -> {
                    if(response.isResult()){
                        state.set(response.getData().getState());
                    }
                });
    }
}
