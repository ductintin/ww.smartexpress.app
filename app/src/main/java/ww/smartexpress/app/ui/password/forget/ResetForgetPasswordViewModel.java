package ww.smartexpress.app.ui.password.forget;

import android.content.Intent;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.ResetPasswordRequest;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.signin.SignInActivity;
import ww.smartexpress.app.ui.signin.SignInViewModel;

public class ResetForgetPasswordViewModel extends BaseViewModel {
    public ObservableField<String> otp = new ObservableField<>("");
    public ObservableField<String> userId = new ObservableField<>("");
    public ObservableField<String> newPassword = new ObservableField<>("");
    public ObservableField<String> newCPassword = new ObservableField<>("");

    public ObservableField<Boolean> isVisibilityN = new ObservableField<>(false);
    public ObservableField<Boolean> isVisibilityC = new ObservableField<>(false);

    public ResetForgetPasswordViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void setIsVisibilityPasswordC(){
        isVisibilityC.set(!isVisibilityC.get());
    }

    public void setIsVisibilityPasswordN(){
        isVisibilityN.set(!isVisibilityN.get());
    }

    Observable<ResponseWrapper<String>> resetPassword(ResetPasswordRequest request) {
        return repository.getApiService().resetPassword(request)
                .doOnNext(response -> {

                });
    }

    public void resetPassword(){
        if(newPassword.get().contains(" ")){
            showErrorMessage(application.getString(R.string.invalid_pw));
            return;
        }

        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .newPassword(newPassword.get())
                .otp(otp.get())
                .userId(userId.get())
                .build();

        compositeDisposable.add(resetPassword(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    hideLoading();
                    if(response.isResult()){
                        showSuccessMessage("Đổi mật khẩu thành công");
                        Intent intent = new Intent(getApplication().getCurrentActivity(), SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        getApplication().getCurrentActivity().startActivity(intent);
                        getApplication().getCurrentActivity().finish();
                    }else{
                        showErrorMessage(getApplication().getErrorUtils().handelError(response.getCode()));
                    }
                }, err -> {
                    hideLoading();
                    showErrorMessage(application.getString(R.string.network_error));
                }));
    }
}
