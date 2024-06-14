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
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.databinding.ActivitySignInBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.home.HomeActivity;
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

//        executor = ContextCompat.getMainExecutor(this);
//        biometricPrompt = new BiometricPrompt(SignInActivity.this,
//                executor, new BiometricPrompt.AuthenticationCallback() {
//            @Override
//            public void onAuthenticationError(int errorCode,
//                                              @NonNull CharSequence errString) {
//                super.onAuthenticationError(errorCode, errString);
//                Toast.makeText(getApplicationContext(),
//                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
//                        .show();
//
//            }
//
//            @Override
//            public void onAuthenticationSucceeded(
//                    @NonNull BiometricPrompt.AuthenticationResult result) {
//                super.onAuthenticationSucceeded(result);
//                Toast.makeText(getApplicationContext(),
//                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAuthenticationFailed() {
//                super.onAuthenticationFailed();
//                Toast.makeText(getApplicationContext(), "Authentication failed",
//                                Toast.LENGTH_SHORT)
//                        .show();
//            }
//        });
//
//        promptInfo = new BiometricPrompt.PromptInfo.Builder()
//                .setTitle("Xác thực vân tay cho đăng nhập ứng dụng")
//                .setNegativeButtonText("Sử dụng mật khẩu")
//                .build();



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
//
//                        viewModel.hideLoading();
//                        viewModel.showSuccessMessage(getString(R.string.login_success));
//
//                        biometricPrompt.authenticate(promptInfo);
//
//                        UserEntity userEntity = UserEntity.builder()
//                                .id(response.getData().getUserId())
//                                .encryptedPassword(aes.encrypt(viewModel.password.get()))
//                                .build();
//
//                        viewModel.insertUser(userEntity)
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new CompletableObserver() {
//                                    @Override
//                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
//
//                                    }
//
//                                    @Override
//                                    public void onComplete() {
//                                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
//                                        startActivity(intent);
//                                    }
//
//                                    @Override
//                                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
//
//                                    }
//                                });
                        //navigateToHome();
                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                        String en = viewModel.getApplication().getAes().encrypt(viewModel.password.get());
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

//        try {
//            navigateToHome();
//        }catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    public void navigateToHome() throws Exception {
        Intent intent = new Intent(this, HomeActivity.class);
        //byte[] bytes = viewModel.password.get().getBytes();
        AES aes = new AES();
        aes.init();

        String abc = aes.encrypt(viewModel.password.get());
        String bcb = aes.decrypt(abc);
//        File file = new File(getFilesDir(), "secret.text");
//        if(!file.exists()){
//            file.createNewFile();
//        }
//
//        FileOutputStream fos = new FileOutputStream(file);
//        String abc = CryptoUtils.encrypt(bytes, fos).toString();
//
//        FileInputStream fis = new FileInputStream(file);
//        String bcb = new String(CryptoUtils.decrypt(fis), StandardCharsets.UTF_8);
        Log.d("TAG", "navigateToHome: encrypt" + abc);
        Log.d("TAG", "navigateToHome: decrypt" +  bcb);
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
}
