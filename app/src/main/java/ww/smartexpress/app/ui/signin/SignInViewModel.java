package ww.smartexpress.app.ui.signin;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.LoginRequest;
import ww.smartexpress.app.data.model.api.response.LoginResponse;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.utils.AES;

public class SignInViewModel extends BaseViewModel {
    public ObservableField<LoginRequest> request = new ObservableField<>(new LoginRequest());
    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<Boolean> visibleBack = new ObservableField<>(true);
    public ObservableField<Boolean> isVisibility = new ObservableField<>(false);
    public SignInViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    Observable<ResponseWrapper<LoginResponse>> login(LoginRequest request) {
        return repository.getApiService().login(request)
                .doOnNext(response -> {
                    if(response.isResult()){
                        AES aes = new AES();
                        aes.init();

                        repository.getSharedPreferences().setToken(response.getData().getAccessToken());
                        repository.getSharedPreferences().setLong(Constants.KEY_USER_ID, response.getData().getUserId());



                    }
                });
    }
    public void setIsVisibilityPassword(){
        isVisibility.set(!isVisibility.get());
    }

    Completable insertUser(UserEntity userEntity){
        return repository.getRoomService().userDao().insert(userEntity);
    }
}
