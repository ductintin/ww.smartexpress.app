package ww.smartexpress.app.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;

import com.google.android.material.tabs.TabLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.request.LoginRequest;
import ww.smartexpress.app.data.model.api.request.RegisterRequest;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.databinding.ActivitySignInBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.otp.LoginOTPActivity;

public class SignInActivity extends BaseActivity<ActivitySignInBinding, SignInViewModel> {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private static final int REQ_CODE = 101011;
    private Pattern phoneNumberPatter;

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

        phoneNumberPatter = Pattern.compile(Constants.PHONE_NUMBER_REGEX);

        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());
//        if (BiometricManager.from(getApplicationContext()).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS ||
//                BiometricManager.from(getApplicationContext()).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS) {
//            viewModel.hasBiometric.set(true);
//        }

        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.BIOMETRIC_WEAK)){
            case BiometricManager.BIOMETRIC_SUCCESS:
                viewModel.hasBiometric.set(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
//                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG| BiometricManager.Authenticators.DEVICE_CREDENTIAL);
//                startActivityForResult(enrollIntent, REQ_CODE);
//                break;
        }

        executor = ContextCompat.getMainExecutor(getApplicationContext());
        biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @lombok.NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.d("TAG", "onAuthenticationError: " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(
                    @lombok.NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                try{
                    viewModel.authPassword.set(viewModel.getApplication().getAes().decrypt(viewModel.user.get().getEncryptedPassword()));
                    doLogin(2);
                }catch (Exception e){
                    e.printStackTrace();
                    viewModel.showErrorMessage("Đăng nhập thất bại, vui lòng thử lại");
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d("TAG", "onAuthenticationFailed: ");
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Xác thực vân tay cho đăng nhập ứng dụng")
                .setNegativeButtonText("Sử dụng mật khẩu")
                .build();

        loadCurrentUser();

        viewModel.isVisibilityS.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(!viewModel.isVisibilityS.get()){
                    viewBinding.edtPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    viewBinding.edtPw.setTransformationMethod(null);;
                }

                viewBinding.edtPw.setSelection(viewBinding.edtPw.length());
            }
        });

        viewModel.isVisibilityR.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(!viewModel.isVisibilityR.get()){
                    viewBinding.edtPWR.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    viewBinding.edtPWR.setTransformationMethod(null);;
                }

                viewBinding.edtPWR.setSelection(viewBinding.edtPWR.length());
            }
        });

        viewBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        viewModel.tabIndex.set(0);
                        viewBinding.edtPhone.requestFocus();
                        viewModel.phoneS.set("");
                        viewModel.passwordS.set("");
                        viewBinding.edtPhone.setError(null);
                        viewBinding.edtPw.setError(null);
                        break;
                    case 1:
                        viewModel.tabIndex.set(1);
                        viewModel.phoneR.set("");
                        viewModel.name.set("");
                        viewModel.email.set("");
                        viewModel.passwordR.set("");
                        viewBinding.edtName.requestFocus();
                        viewBinding.edtPhoneR.setError(null);
                        viewBinding.edtMail.setError(null);
                        viewBinding.edtPWR.setError(null);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewBinding.imvBiometricLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG| BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED){
//                    final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
//                    enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG| BiometricManager.Authenticators.DEVICE_CREDENTIAL);
//                    startActivityForResult(enrollIntent, REQ_CODE);
//                    return;
                }else{
                    biometricPrompt.authenticate(promptInfo);
                }
            }
        });

        validating();
    }

    public void validating(){
        viewBinding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = charSequence.toString().trim();
                if(viewBinding.edtPhone.isFocused()){
                    if(!TextUtils.isEmpty(phone) && !phoneNumberPatter.matcher(phone).matches()){
                        viewBinding.edtPhone.setError("Số điện thoại không hợp lệ");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        viewBinding.tipPwS.setHelperText("Mật khẩu tối thiểu 6 ký tự");
        viewBinding.edtPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        viewBinding.edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        viewBinding.edtMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = charSequence.toString().trim();
                if(viewBinding.edtMail.isFocused()){
                    if(!TextUtils.isEmpty(email) && !EmailValidator.getInstance().isValid(email)){
                        viewBinding.edtMail.setError("Email không đúng dịnh dạng");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        viewBinding.edtPhoneR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = charSequence.toString().trim();
                if(viewBinding.edtPhoneR.isFocused()){
                    if(!TextUtils.isEmpty(phone) && !phoneNumberPatter.matcher(phone).matches()){
                        viewBinding.edtPhoneR.setError("Số điện thoại không hợp lệ");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        viewBinding.edtPWR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString().trim();
                if(viewBinding.edtPWR.isFocused()){
                    if(password.contains(" ")){
                        viewBinding.tipPwR.setPasswordVisibilityToggleEnabled(false);
                        viewBinding.edtPWR.setError("Mật khẩu khÔng chứa khoảng trắng");
                        return;
                    }
                    if(!TextUtils.isEmpty(password) && password.length() < 6){
                        viewBinding.tipPwR.setPasswordVisibilityToggleEnabled(false);
                        viewBinding.edtPWR.setError("Mật khẩu tối thiểu 6 ký tự");
                        return;
                    }

                    viewBinding.tipPwR.setPasswordVisibilityToggleEnabled(true);
                }else{
                    viewBinding.tipPwR.setPasswordVisibilityToggleEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public LoginRequest prepareLoginRequest(){
        viewModel.loginRequest.get().setPhone(viewModel.user.get().getId() != null ? viewModel.user.get().getPhone() : viewModel.phoneS.get().trim());
        viewModel.loginRequest.get().setPassword(viewModel.authPassword.get());
        return viewModel.loginRequest.get();
    }

    public boolean isValidLoginRequest(LoginRequest request){
        String phoneRegex = "^(0[3|5|7|8|9])+([0-9]{8})$";
        Pattern phonePatter = Pattern.compile(Constants.PHONE_NUMBER_REGEX);

        if(TextUtils.isEmpty(request.getPhone()) || request.getPhone().length() != 10 || !phonePatter.matcher(request.getPhone()).matches()){
            viewBinding.edtPhone.setError(application.getString(R.string.phone_wrong_formatted));
            viewBinding.edtPhone.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(request.getPassword()) || request.getPassword().length() < 6){
            viewBinding.edtPw.setError(application.getString(R.string.pw_wrong_formatted));
            viewBinding.edtPw.requestFocus();
            return false;
        }else if(request.getPassword().contains(" ")){
            viewBinding.edtPw.setError(application.getString(R.string.invalid_pw));
            viewBinding.edtPw.requestFocus();
            return false;
        }
        return true;
    }

    public void checkLogin(int option){
        if(option == 1){
            //login ko biometric
            viewModel.authPassword.set(viewModel.passwordS.get().trim());
            doLogin(1);
        }else{
            biometricPrompt.authenticate(promptInfo);
        }

    }

    public void doLogin(int option){
        LoginRequest loginRequest = prepareLoginRequest();
        if(!isValidLoginRequest(loginRequest)){
            viewModel.hideLoading();
            return;
        }

        viewModel.showLoading();

        viewModel.compositeDisposable.add(viewModel.login(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    viewModel.hideLoading();
                    if(response.isResult()){
                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                        intent.putExtra("FROM_SIGNIN", "1");
                        try{
                            intent.putExtra("ENCRYPTED_PW", viewModel.getApplication().getAes().encrypt(viewModel.authPassword.get()));
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                        finish();
                    }else{
                        viewModel.hideLoading();
                        viewModel.getApplication().getErrorUtils().handelError(response.getCode());
//                        Log.d("TAG", "doLogin: " + response.getCode());
//                        Log.d("TAG", "doLogin: " + String.valueOf(response.getCode() == Constants.CUSTOMER_ERROR_STATUS_PENDING));
//                        Log.d("TAG", "doLogin: " + String.valueOf(response.getCode().equals(Constants.CUSTOMER_ERROR_STATUS_PENDING)));
//                        Log.d("TAG", "doLogin: " + String.valueOf(Constants.CUSTOMER_ERROR_STATUS_PENDING == response.getCode()));
                        if(response.getCode().equals(Constants.CUSTOMER_ERROR_STATUS_PENDING)){
                            Intent intent = new Intent(SignInActivity.this, LoginOTPActivity.class);
                            intent.putExtra("USER_PHONE", viewModel.phoneS.get());
                            startActivity(intent);
                        }
                    }
                }, err -> {

                    viewModel.hideLoading();
                    viewModel.showErrorMessage(application.getString(R.string.network_error));
                }));
    }

    public RegisterRequest prepareRegisterRequest(){
        viewModel.registerRequest.get().setName(viewModel.name.get().trim());
        viewModel.registerRequest.get().setEmail(viewModel.email.get().trim());
        viewModel.registerRequest.get().setPhone(viewModel.phoneR.get().trim());
        viewModel.registerRequest.get().setPassword(viewModel.passwordR.get().trim());
        return viewModel.registerRequest.get();
    }

    public Boolean isValidRegisterRequest(RegisterRequest request){
        String phoneRegex = "^(0[3|5|7|8|9])+([0-9]{8})$";
        Pattern phonePatter = Pattern.compile(Constants.PHONE_NUMBER_REGEX);

        if(TextUtils.isEmpty(request.getName())){
            viewBinding.edtName.setError(application.getString(R.string.please_fill_name));
            viewBinding.edtName.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(request.getEmail())){
            viewBinding.edtMail.setError(application.getString(R.string.please_fill_email));
            viewBinding.edtMail.requestFocus();
            return false;
        }else if(!EmailValidator.getInstance().isValid(request.getEmail())){
            viewBinding.edtMail.setError(application.getString(R.string.invalid_email));
            viewBinding.edtMail.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(request.getPhone()) || request.getPhone().length() != 10 || !phonePatter.matcher(request.getPhone()).matches()){
            viewBinding.edtPhoneR.setError(application.getString(R.string.phone_wrong_formatted));
            viewBinding.edtPhoneR.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(request.getPassword()) || request.getPassword().length() < 6){
            viewBinding.edtPWR.setError(application.getString(R.string.pw_wrong_formatted));
            viewBinding.edtPWR.requestFocus();
            return false;
        }else if(request.getPassword().contains(" ")){
            viewBinding.edtPWR.setError(application.getString(R.string.invalid_pw));
            viewBinding.edtPWR.requestFocus();
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
                            //navigateToLogin();
                            navigateToOtp(response.getData().getUserId());
                        }else{
                            viewModel.getApplication().getErrorUtils().handelError(response.getCode());
                        }
                    }, err ->{
                        viewModel.hideLoading();
                        viewModel.showErrorMessage(err.getMessage());
                    }));
        }
    }

    public void navigateToOtp(String userId){
        Intent intent = new Intent(this, LoginOTPActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USER_PHONE", viewModel.phoneR.get());
        startActivity(intent);
        finish();
    }


    public void doNext(){
        if(viewModel.tabIndex.get().equals(0)){
            checkLogin(1);
        }else{
            doRegister();
        }
    }

    public void loadCurrentUser(){
        viewModel.getCurrentUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<UserEntity>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<UserEntity> userEntities) {
                        Log.d("TAG", "onSuccess: " + userEntities.size());
                        if(userEntities.size() > 0){
                            Log.d("TAG", "onSuccess: " + userEntities.get(0).getIsBiometric());
                            viewModel.user.set(userEntities.get(userEntities.size() - 1));
                            viewBinding.edtPw.requestFocus();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }

    public void doAnotherLogin(){
        viewModel.user.set(new UserEntity());
        viewBinding.edtPhone.requestFocus();
        viewModel.passwordS.set("");
        viewBinding.edtPw.setError(null);
    }
}
