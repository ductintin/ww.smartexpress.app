package ww.smartexpress.app.ui.password.reset;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.databinding.ActivityResetPasswordBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class ResetPasswordActivity extends BaseActivity<ActivityResetPasswordBinding, ResetPasswordViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_password;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
         buildComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding.layoutHeader.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewModel.isOldPwVisibility.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(!viewModel.isOldPwVisibility.get()){
                    viewBinding.edtOldPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    viewBinding.edtOldPw.setTransformationMethod(null);;
                }

                viewBinding.edtOldPw.setSelection(viewBinding.edtOldPw.length());
            }
        });

        viewModel.isNewPwVisibility.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(!viewModel.isNewPwVisibility.get()){
                    viewBinding.edtNewPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    viewBinding.edtNewPw.setTransformationMethod(null);;
                }

                viewBinding.edtNewPw.setSelection(viewBinding.edtNewPw.length());
            }
        });

        viewModel.isCNewPwVisibility.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(!viewModel.isCNewPwVisibility.get()){
                    viewBinding.edtCNewPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    viewBinding.edtCNewPw.setTransformationMethod(null);;
                }

                viewBinding.edtCNewPw.setSelection(viewBinding.edtCNewPw.length());
            }
        });
    }

    public boolean prepareUpdateRequest(){
        viewModel.getProfileStored();
        if(viewModel.oldPw.get().equals(viewModel.newPw.get())){
            viewModel.showWarningMessage(getString(R.string.same_reset_pw));
            return false;
        }else{
            viewModel.request.get().setOldPassword(viewModel.oldPw.get());
            viewModel.request.get().setNewPassword(viewModel.newPw.get());
            return true;
        }
    }

    public void doUpdatePassword(){
        if(prepareUpdateRequest()){
            viewModel.compositeDisposable.add(viewModel.updateProfile()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        viewModel.hideLoading();
                        if(response.isResult()){
                            viewModel.showSuccessMessage(getString(R.string.update_password_success));
                            updateEncryptedPassword();
                        }else{
                            viewModel.showErrorMessage(response.getMessage());
                        }
                    }, err -> {
                        viewModel.hideLoading();
                        viewModel.showErrorMessage(getString(R.string.network_error));
                    }));
        }
        }

    public void updateEncryptedPassword() throws Exception {
        viewModel.updateEncryptedPassword()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}
