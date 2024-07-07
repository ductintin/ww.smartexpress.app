package ww.smartexpress.app.ui.password.forget;

import android.content.Intent;
import android.util.Log;

import androidx.databinding.ObservableField;

import org.apache.commons.validator.routines.EmailValidator;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.ForgetPasswordRequest;
import ww.smartexpress.app.data.model.api.response.CustomerIdResponse;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.password.otp.VerifyForgetPasswordOTPActivity;

public class ForgetPasswordViewModel extends BaseViewModel {
    public ObservableField<Integer> kind = new ObservableField<>(1);
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> userId = new ObservableField<>("");
    public ForgetPasswordViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    Observable<ResponseWrapper<CustomerIdResponse>> requestForgetPassword(ForgetPasswordRequest request) {
        return repository.getApiService().forgetPassword(request)
                .doOnNext(response -> {

                });
    }

    public void doNext(){
        if(kind.get() == 1){
            Log.d("TAG", "doNext: " + email.get().trim().length());
            if(email.get().trim().length() != 10){
                showErrorMessage("Số điện thoại không hợp lệ");
                return;
            }
        }else{
            if(!EmailValidator.getInstance().isValid(email.get().trim())){
                showErrorMessage("Email không hợp lệ");
                return;
            }
        }

        ForgetPasswordRequest request = ForgetPasswordRequest.builder()
                .email(email.get())
                .kind(kind.get())
                .build();

        showLoading();

        compositeDisposable.add(requestForgetPassword(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    hideLoading();
                    if(response.isResult() && response.getData() != null){
                        Intent intent = new Intent(getApplication().getCurrentActivity(), VerifyForgetPasswordOTPActivity.class);
                        intent.putExtra(Constants.KEY_USER_ID, response.getData().getUserId());
                        intent.putExtra("EMAIL", email.get());
                        intent.putExtra(Constants.VERIFY_OPTION, kind.get());
                        getApplication().getCurrentActivity().startActivity(intent);
                        getApplication().getCurrentActivity().finish();
                    }else{
                        showErrorMessage("Không tìm thấy người dùng!");
                    }
                }, err -> {
                    hideLoading();
                    showErrorMessage("Không tìm thấy tài khoản. Vui lòng thử lại");
                }));


    }
}
