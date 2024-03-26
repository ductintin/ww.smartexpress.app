package ww.smartexpress.app.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.request.LoginRequest;
import ww.smartexpress.app.databinding.ActivitySignInBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.home.HomeActivity;

public class SignInActivity extends BaseActivity<ActivitySignInBinding, SignInViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_sign_in;
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

        viewModel.isVisibility.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(!viewModel.isVisibility.get()){
                    viewBinding.edtPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    viewBinding.edtPw.setTransformationMethod(null);;
                }

                viewBinding.edtPw.setSelection(viewBinding.edtPw.length());
            }
        });
    }

    public LoginRequest prepareLoginRequest(){
        viewModel.request.get().setPhone(viewModel.phone.get().trim());
        viewModel.request.get().setPassword(viewModel.password.get().trim());
        return viewModel.request.get();
    }

    public boolean isValidLoginRequest(LoginRequest request){
        String phoneRegex = "^(0[3|5|7|8|9])+([0-9]{8})$";
        Pattern phonePatter = Pattern.compile(phoneRegex);

        if(TextUtils.isEmpty(request.getPhone()) || request.getPhone().length() != 10 || !phonePatter.matcher(request.getPhone()).matches()){
            viewModel.showErrorMessage(application.getString(R.string.phone_wrong_formatted));
            return false;
        }
        if(TextUtils.isEmpty(request.getPassword()) || request.getPassword().length() < 6){
            viewModel.showErrorMessage(application.getString(R.string.pw_wrong_formatted));
            return false;
        }else if(request.getPassword().contains(" ")){
            viewModel.showErrorMessage(application.getString(R.string.invalid_pw));
            return false;
        }
        return true;
    }
    public void doLogin(){
        viewModel.showLoading();
        LoginRequest loginRequest = prepareLoginRequest();
        if(!isValidLoginRequest(loginRequest)){
            viewModel.hideLoading();
            return;
        }
        viewModel.compositeDisposable.add(viewModel.login(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.isResult()){
                        viewModel.hideLoading();
                        viewModel.showSuccessMessage(getString(R.string.login_success));
                        navigateToHome();
                    }else{
                        viewModel.hideLoading();
                        viewModel.showErrorMessage(response.getMessage());
                    }
                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(application.getString(R.string.network_error));
                }));
    }

    public void navigateToHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
