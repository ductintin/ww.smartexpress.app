package ww.smartexpress.app.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.request.RegisterRequest;
import ww.smartexpress.app.databinding.ActivityRegisterBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.input.phone.PhoneActivity;
import ww.smartexpress.app.ui.login.LoginActivity;
import ww.smartexpress.app.ui.main.MainActivity;
import ww.smartexpress.app.ui.signin.SignInActivity;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, RegisterViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
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

    public RegisterRequest prepareRegisterRequest(){
        viewModel.request.get().setName(viewModel.name.get().trim());
        viewModel.request.get().setEmail(viewModel.email.get().trim());
        viewModel.request.get().setPhone(viewModel.phone.get().trim());
        viewModel.request.get().setPassword(viewModel.password.get().trim());
        return viewModel.request.get();
    }

    public Boolean isValidRegisterRequest(RegisterRequest request){
        String phoneRegex = "^(0[3|5|7|8|9])+([0-9]{8})$";
        Pattern phonePatter = Pattern.compile(phoneRegex);

        if(TextUtils.isEmpty(request.getName())){
            viewModel.showErrorMessage(application.getString(R.string.please_fill_name));
            return false;
        }
        if(TextUtils.isEmpty(request.getEmail())){
            viewModel.showErrorMessage(application.getString(R.string.please_fill_email));
            return false;
        }else if(!EmailValidator.getInstance().isValid(request.getEmail())){
            viewModel.showErrorMessage(application.getString(R.string.invalid_email));
            return false;
        }
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
    public void doRegister(){
        viewModel.showLoading();
        RegisterRequest request = prepareRegisterRequest();
        if(!isValidRegisterRequest(request)){
            viewModel.hideLoading();
        }else{
            viewModel.compositeDisposable.add(viewModel.register(prepareRegisterRequest())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        viewModel.hideLoading();
                        if(response.isResult()){
                            viewModel.showSuccessMessage(getString(R.string.register_success));
                            navigateToLogin();
                        }else{
                            viewModel.showErrorMessage(response.getMessage());
                        }
                    }, err ->{
                        viewModel.hideLoading();
                        viewModel.showErrorMessage(err.getMessage());
                    }));
        }
    }

    public void navigateToLogin(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

}
