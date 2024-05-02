package ww.smartexpress.app.ui.splashform;

import android.content.Intent;

import androidx.databinding.ObservableField;

import com.google.gson.JsonObject;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.index.IndexActivity;
import ww.smartexpress.app.ui.welcome.WelcomeViewModel;

public class SplashFormViewModel extends WelcomeViewModel {
    public ObservableField<Integer> state = new ObservableField<>();
    public ObservableField<String> latlng = new ObservableField<>("");
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

    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getCurrentBooking() {
        return repository.getApiService().getCurrentBooking(null)
                .doOnNext(response -> {
                    if(response.isResult()){
                        state.set(response.getData().getContent().get(0).getState());
                    }
                });
    }

    Observable<JsonObject> getLocationInfo() {
        return repository.getApiService().getLocationInfoByLatLng(latlng.get(), Constants.GEO_API_KEY)
                .doOnNext(response -> {

                });
    }
}
