package ww.smartexpress.app.ui.signin;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.concurrent.Executor;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.NonNull;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.request.LoginRequest;
import ww.smartexpress.app.data.model.api.request.RegisterRequest;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.databinding.ActivitySignInBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.otp.LoginOTPActivity;
import ww.smartexpress.app.utils.AES;

public class SignInActivity extends BaseActivity<ActivitySignInBinding, SignInViewModel> {
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    private static final int REQ_ONE_TAP = 1000;

//    private Executor executor;
//    private BiometricPrompt biometricPrompt;
//    private BiometricPrompt.PromptInfo promptInfo;
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

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId("405398622435-m56nrqnva119q94oaqt0qssbsabsjiuf.apps.googleusercontent.com")
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(false)
                .build();



        viewBinding.imvLoginGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener(SignInActivity.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                try {
                                    startIntentSenderForResult(
                                            result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                            null, 0, 0, 0);
                                } catch (IntentSender.SendIntentException e) {
                                    Log.e("TAG", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                                }
                            }
                        })
                        .addOnFailureListener(SignInActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // No saved credentials found. Launch the One Tap sign-up flow, or
                                // do nothing and continue presenting the signed-out UI.
                                Log.d("TAG", e.getLocalizedMessage());
                            }
                        });
            }
        });
        Log.d("TAG", "onCreate: " + signInRequest.toString());

        viewBinding.layoutHeader.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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
                        break;
                    case 1:
                        viewModel.tabIndex.set(1);
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
    }

    public LoginRequest prepareLoginRequest(){
        viewModel.loginRequest.get().setPhone(viewModel.phoneS.get().trim());
        viewModel.loginRequest.get().setPassword(viewModel.passwordS.get().trim());
        return viewModel.loginRequest.get();
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
                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                        String en = viewModel.getApplication().getAes().encrypt(viewModel.passwordS.get());
                        intent.putExtra("ENCRYPTED_PW", en);
                        Log.d("TAG", "doLogin: " + en);
                        startActivity(intent);
                    }else{
                        viewModel.hideLoading();
                        viewModel.showErrorMessage(response.getMessage());
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
                            //navigateToLogin();
                            navigateToOtp(response.getData().getUserId());
                        }else{
                            viewModel.showErrorMessage(response.getMessage());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    String username = credential.getId();
                    String password = credential.getPassword();

                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with your backend.
                        Log.d("TAG", "Got ID token. " + idToken);
                        navigateToCreatePassword();

                    } else if (password != null) {
                        // Got a saved username and password. Use them to authenticate
                        // with your backend.
                        Log.d("TAG", "Got password.");
                    }
                } catch (ApiException e) {
                    // ...
                    viewModel.showErrorMessage("Lỗi kết nối. Vui lòng thử lại!");
                    e.printStackTrace();
                }
                break;
        }
    }


    public void navigateToCreatePassword(){

    }

    public void doNext(){
        if(viewModel.tabIndex.get().equals(0)){
            doLogin();
        }else{
            doRegister();
        }
    }
}
