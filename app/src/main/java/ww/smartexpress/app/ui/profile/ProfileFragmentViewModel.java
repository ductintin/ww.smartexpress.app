package ww.smartexpress.app.ui.profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.local.prefs.PreferencesService;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.ProfileResponse;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.databinding.BaseDialogBinding;
import ww.smartexpress.app.ui.base.fragment.BaseFragmentViewModel;
import ww.smartexpress.app.ui.index.IndexActivity;
import ww.smartexpress.app.ui.password.reset.ResetPasswordActivity;
import ww.smartexpress.app.ui.register.RegisterActivity;
import ww.smartexpress.app.ui.signin.SignInActivity;

public class ProfileFragmentViewModel extends BaseFragmentViewModel {

    public ObservableField<ProfileResponse> profile = new ObservableField<>(new ProfileResponse());
    public ObservableField<String> avatar = new ObservableField<>("");
    public ObservableField<String> fullName = new ObservableField<>("Tran Nguyen");
    public ObservableField<String> email = new ObservableField<>("Nguyen@gmail.com");

    public ObservableField<String> lang = new ObservableField<>("Tiếng Việt");
    public ObservableField<String> token = new ObservableField<>("");
    public ProfileFragmentViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
        token.set(repository.getToken());
        //loadProfile();
    }

    public void navigateToEditProfile(){
        Intent intent = new Intent(application.getCurrentActivity(), EditProfileActivity.class);
        application.getCurrentActivity().startActivity(intent);
    }

    public void getStoredLocation(){

    }

    public void getPaymentMethod(){

    }

    public void getInformation(){

    }

    public void getLanguage(){

    }

    public void doLogin(){
        Intent intent = new Intent(application.getCurrentActivity(), SignInActivity.class);
        application.getCurrentActivity().startActivity(intent);
    }

    public void doRegister(){
        Intent intent = new Intent(application.getCurrentActivity(), RegisterActivity.class);
        application.getCurrentActivity().startActivity(intent);
    }
    Observable<ResponseWrapper<ProfileResponse>> setProfile() {
        return repository.getApiService().getProfile()
                .doOnNext(response -> {
                    if(response.isResult()){
                        profile.set(response.getData());

                        UserEntity userEntity = new UserEntity();
                        userEntity.setId(response.getData().getId());
                        userEntity.setName(response.getData().getName());
                        userEntity.setAvatar(response.getData().getAvatar());
                        userEntity.setPhone(response.getData().getPhone());
                        userEntity.setEmail(response.getData().getEmail());
                        userEntity.setStatus(response.getData().getStatus());
                        repository.getRoomService().userDao().insert(userEntity)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {

                                }, throwable -> {

                                });

                    }
                });
    }

//    public void loadProfile(){
//        showLoading();
//        String token = repository.getToken();
//        if(token.equals("NULL") || token.isEmpty()){
//            Intent intent = new Intent(application.getCurrentActivity(), SignInActivity.class);
//            application.getCurrentActivity().startActivity(intent);
//        }
//
//        compositeDisposable.add(repository.getApiService().getProfile()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//                    if(response.isResult()){
//                        profile.set(response.getData());
//                        hideLoading();
//                    }else {
//                        hideLoading();
//                        showErrorMessage(response.getMessage());
//                    }
//                },error->{
//                    hideLoading();
//                    showErrorMessage(application.getString(R.string.network_error));
//                    error.printStackTrace();
//                })
//        );
//    }

    public boolean loadProfile(){
        Long userId = repository.getSharedPreferences().getLongVal(Constants.KEY_USER_ID);
        if(userId != 0){
            Single<UserEntity> userEntity = repository.getRoomService().userDao().findById(userId);
            if(userEntity.blockingGet() != null){
                ProfileResponse profileResponse = new ProfileResponse();
                profileResponse.setId(userEntity.blockingGet().getId());
                profileResponse.setName(userEntity.blockingGet().getName());
                profileResponse.setPhone(userEntity.blockingGet().getPhone());
                profileResponse.setAvatar(userEntity.blockingGet().getAvatar());
                profileResponse.setEmail(userEntity.blockingGet().getEmail());
                profile.set(profileResponse);
                return true;
            }
        }
        return false;
    }

    public void doLogout(){

        Dialog dialog = new Dialog(application.getCurrentActivity());
        BaseDialogBinding dialogLogoutBinding = DataBindingUtil.inflate(LayoutInflater.from(application.getCurrentActivity()),R.layout.base_dialog,null, false);
        dialogLogoutBinding.setTitle(application.getCurrentActivity().getString(R.string.logout_title));
        dialogLogoutBinding.setSubtitle(application.getCurrentActivity().getString(R.string.logout_description));
        dialogLogoutBinding.setDecision(application.getCurrentActivity().getString(R.string.logout_all_win));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(dialogLogoutBinding.getRoot());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCanceledOnTouchOutside(false);

        dialogLogoutBinding.btnLogout.setOnClickListener(view -> {
            clearToken();
            Intent intent = new Intent(application.getCurrentActivity(), IndexActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            application.getCurrentActivity().startActivity(intent);
            application.getCurrentActivity().finish();
            dialog.dismiss();
        });
        dialogLogoutBinding.btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    public void doResetPassword(){
        Intent intent = new Intent(application.getCurrentActivity(), ResetPasswordActivity.class);
        application.getCurrentActivity().startActivity(intent);
    }
    public void clearToken(){
        repository.getSharedPreferences().removeKey(PreferencesService.KEY_BEARER_TOKEN);
    }
}
