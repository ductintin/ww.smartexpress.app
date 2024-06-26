package ww.smartexpress.app.ui.signin;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.LoginRequest;
import ww.smartexpress.app.data.model.api.request.RegisterRequest;
import ww.smartexpress.app.data.model.api.response.CustomerIdResponse;
import ww.smartexpress.app.data.model.api.response.LoginResponse;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.utils.AES;

public class SignInViewModel extends BaseViewModel {
    public ObservableField<LoginRequest> loginRequest = new ObservableField<>(new LoginRequest());
    public ObservableField<String> phoneS = new ObservableField<>("");
    public ObservableField<String> passwordS = new ObservableField<>("");
    public ObservableField<Boolean> visibleBack = new ObservableField<>(true);
    public ObservableField<Boolean> isVisibilityS = new ObservableField<>(false);
    public ObservableField<Integer> tabIndex = new ObservableField<>(0);

    public ObservableField<RegisterRequest> registerRequest = new ObservableField<>(new RegisterRequest());
    public ObservableField<String> name = new ObservableField<>("");
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> phoneR = new ObservableField<>("");
    public ObservableField<String> passwordR = new ObservableField<>("");
    public ObservableField<Boolean> isVisibilityR = new ObservableField<>(false);

    public SignInViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    Observable<ResponseWrapper<LoginResponse>> login(LoginRequest request) {
        return repository.getApiService().login(request)
                .doOnNext(response -> {
                    if(response.isResult()){
                        repository.getSharedPreferences().setToken(response.getData().getAccessToken());
                        repository.getSharedPreferences().setLong(Constants.KEY_USER_ID, response.getData().getUserId());
                    }
                });
    }


    Observable<ResponseWrapper<CustomerIdResponse>> register(RegisterRequest registerRequest) {
        return repository.getApiService().register(registerRequest)
                .doOnNext(response -> {

                });
    }

    public void setIsVisibilityPasswordS(){
        isVisibilityS.set(!isVisibilityS.get());
    }

    public void setIsVisibilityPasswordR(){
        isVisibilityR.set(!isVisibilityR.get());
    }

    Completable insertUser(UserEntity userEntity){
        return repository.getRoomService().userDao().insert(userEntity);
    }
}
