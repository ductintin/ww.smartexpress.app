package ww.smartexpress.app.ui.profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.local.prefs.PreferencesService;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.PayoutTransaction;
import ww.smartexpress.app.data.model.api.response.ProfileResponse;
import ww.smartexpress.app.data.model.api.response.Setting;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.databinding.BaseDialogBinding;
import ww.smartexpress.app.ui.base.fragment.BaseFragmentViewModel;
import ww.smartexpress.app.ui.index.IndexActivity;
import ww.smartexpress.app.ui.password.reset.ResetPasswordActivity;
import ww.smartexpress.app.ui.register.RegisterActivity;
import ww.smartexpress.app.ui.signin.SignInActivity;
import ww.smartexpress.app.ui.wallet.WalletActivity;

public class ProfileFragmentViewModel extends BaseFragmentViewModel {

    public ObservableField<UserEntity> profile = new ObservableField<>(new UserEntity());
    public ObservableField<String> lang = new ObservableField<>("Tiếng Việt");
    public ObservableField<String> token = new ObservableField<>("");
    public ObservableField<Long> userId = new ObservableField<>();
    public ObservableField<Boolean> hasBiometric = new ObservableField<>(false);
    public ObservableField<String> hotline = new ObservableField<>("");

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
    Observable<ResponseWrapper<ProfileResponse>> getProfile() {
        return repository.getApiService().getProfile()
                .doOnNext(response -> {
                    if(response.isResult()) {
                        ProfileResponse profileResponse = response.getData();
                        UserEntity.builder()
                                .id(profileResponse.getId())
                                .email(profileResponse.getEmail())
                                .avatar(profileResponse.getAvatar())
                                .name(profileResponse.getName())
                                .phone(profileResponse.getPhone())
                                .isBiometric(false)
                                .build();
                    }
                });
    }

    Single<UserEntity> loadProfile(){
        userId.set(repository.getSharedPreferences().getLongVal(Constants.KEY_USER_ID));
        return repository.getRoomService().userDao().findById(userId.get());
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
            Long userId = repository.getSharedPreferences().getLongVal(Constants.KEY_USER_ID);
            repository.getRoomService().addressDao().deleteAddressByUserId(userId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            clearToken();
                            Intent intent = new Intent(application.getCurrentActivity(), SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            application.getCurrentActivity().startActivity(intent);
                            application.getCurrentActivity().finish();
                            dialog.dismiss();
                            Log.d("TAG", "onComplete: ooo" );
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            e.printStackTrace();
                        }
                    });

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
        repository.getRoomService().userDao().deleteAllExceptId(userId.get()).blockingAwait();
        repository.getRoomService().addressDao().deleteAddressByUserId(userId.get()).blockingAwait();
    }

    public void gotoWallet(){
        Intent intent = new Intent(application.getCurrentActivity(), WalletActivity.class);
        application.getCurrentActivity().startActivity(intent);
    }

    public Completable updateBiometric(Boolean isBiometric){
        return repository.getRoomService().userDao().updateBiometric(userId.get(), isBiometric);
    }

    public Observable<ResponseWrapper<Setting>> getHotlineSetting(){
        return repository.getApiService().getSetting("support-hotline");
    }

    public void gotoHotline(){
        if(!TextUtils.isEmpty(hotline.get())){
           try{
               Intent callIntent = new Intent(Intent.ACTION_DIAL);
               callIntent.setData(Uri.parse("tel:" + hotline.get()));
               application.getCurrentActivity().startActivity(callIntent);
           }catch (Exception e){
               e.printStackTrace();
               showErrorMessage("Thiết bị không hỗ trợ");
           }
        }
    }
}
