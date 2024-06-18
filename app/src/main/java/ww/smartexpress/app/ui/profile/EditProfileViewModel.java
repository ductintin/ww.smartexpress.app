package ww.smartexpress.app.ui.profile;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.RequestBody;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.UpdateProfileRequest;
import ww.smartexpress.app.data.model.api.response.ProfileResponse;
import ww.smartexpress.app.data.model.api.response.UploadFileResponse;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.input.phone.PhoneActivity;
import ww.smartexpress.app.ui.welcome.WelcomeActivity;

public class EditProfileViewModel extends BaseViewModel {

    public ObservableField<String> avatar = new ObservableField<>("");
    public ObservableField<String> fullName = new ObservableField<>("");
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> confirmPassword = new ObservableField<>("");
    public ObservableField<String> bankCard = new ObservableField<>("");
    public ObservableField<String> encryptedPassword = new ObservableField<>("");

    public ObservableField<ProfileResponse> profile = new ObservableField<>(new ProfileResponse());
    public ObservableField<UpdateProfileRequest> request = new ObservableField<>(new UpdateProfileRequest());
    public ObservableField<Long> userId = new ObservableField<>(0L);
    public ObservableField<UserEntity> userEntityObservableField = new ObservableField<>(new UserEntity());
    public ObservableField<Boolean> isVisibility = new ObservableField<>(false);

    public EditProfileViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void back(){
        application.getCurrentActivity().onBackPressed();
    }

    public void setProfile(ProfileResponse response){
        profile.set(response);
        fullName.set(profile.get().getName());
        email.set(profile.get().getEmail());
        avatar.set(profile.get().getAvatar());
        storeProfile(userId.get(), avatar.get(), fullName.get());
    }

    Observable<ResponseWrapper<ProfileResponse>> getProfileObserve() {
        return repository.getApiService().getProfile()
                .doOnNext(response -> {
                    if(response.isResult()){
                        setProfile(response.getData());
                    }
                });
    }


    Observable<ResponseWrapper<String>> updateProfile(UpdateProfileRequest request) {
        return repository.getApiService().updateProfile(request)
                .doOnNext(response -> {
                    password.set("");
                });
    }


    public void setIsVisibilityPassword(){
        isVisibility.set(!isVisibility.get());
    }

    public Observable<ResponseWrapper<UploadFileResponse>> uploadAvatar(RequestBody requestBody){
        return repository.getApiService().uploadFile(requestBody);
    }

    Single<UserEntity> getProfileLocal(){
        userId.set(repository.getSharedPreferences().getLongVal(Constants.KEY_USER_ID));
        return repository.getRoomService().userDao().findById(userId.get());
    }

    public void storeProfile(Long id, String avatar, String name){
        repository.getRoomService().userDao().update(id, avatar, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}
