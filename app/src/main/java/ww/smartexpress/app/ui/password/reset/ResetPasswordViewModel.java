package ww.smartexpress.app.ui.password.reset;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.UpdateProfileRequest;
import ww.smartexpress.app.data.model.api.response.ProfileResponse;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class ResetPasswordViewModel extends BaseViewModel {
    public ObservableField<Boolean> visibleBack = new ObservableField<>(true);
    public ObservableField<String> oldPw = new ObservableField<>("");
    public ObservableField<Boolean> isOldPwVisibility = new ObservableField<>(false);
    public ObservableField<String> newPw = new ObservableField<>("");
    public ObservableField<Boolean> isNewPwVisibility = new ObservableField<>(false);
    public ObservableField<String> confirmNewPW = new ObservableField<>("");
    public ObservableField<Boolean> isCNewPwVisibility = new ObservableField<>(false);
    public ObservableField<UpdateProfileRequest> request = new ObservableField<>(new UpdateProfileRequest());
    public ObservableField<Long> userId = new ObservableField<>(0L);
    public ResetPasswordViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    Observable<ResponseWrapper<String>> updateProfile() {
        return repository.getApiService().updateProfile(request.get())
                .doOnNext(response -> {

                });
    }

    public void getProfileStored(){
        userId.set(repository.getSharedPreferences().getLongVal(Constants.KEY_USER_ID));
        if(userId.get() != 0){
            Single<UserEntity> userEntity = repository.getRoomService().userDao().findById(userId.get());
            if(userEntity != null){
                request.get().setAvatar(userEntity.blockingGet().getAvatar());
                request.get().setName(userEntity.blockingGet().getName());

            }
        }
    }

    public void setIsOldPwVisibility(){
        isOldPwVisibility.set(!isOldPwVisibility.get());
    }
    public void setIsNewPwVisibility(){
        isNewPwVisibility.set(!isNewPwVisibility.get());
    }
    public void setIsCNewPwVisibility(){
        isCNewPwVisibility.set(!isCNewPwVisibility.get());
    }


}
