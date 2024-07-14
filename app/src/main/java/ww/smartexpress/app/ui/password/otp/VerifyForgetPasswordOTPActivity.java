package ww.smartexpress.app.ui.password.otp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.request.ForgetPasswordRequest;
import ww.smartexpress.app.databinding.ActivityVerifyForgetPasswordOtpBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.signin.SignInActivity;

public class VerifyForgetPasswordOTPActivity extends BaseActivity<ActivityVerifyForgetPasswordOtpBinding, VerifyForgetPasswordOTPViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_verify_forget_password_otp;
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
        Intent intent = getIntent();
        viewModel.userId.set(intent.getStringExtra(Constants.KEY_USER_ID));
        viewModel.kind.set(intent.getIntExtra(Constants.VERIFY_OPTION, 1));
        viewModel.email.set(intent.getStringExtra("EMAIL"));

        viewModel.setCountdownOTP();
        viewBinding.layoutHeader.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerifyForgetPasswordOTPActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });

        setupOTP();
    }

    public void setupOTP(){
        viewBinding.inputOTP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TAG", "onTextChanged: " + charSequence.toString());
                if(!charSequence.toString().trim().isEmpty()){
                    viewBinding.inputOTP2.requestFocus();
                    Log.d("TAG", "onTextChanged: ");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("TAG", "afterTextChanged: " + editable.toString());
            }
        });
        viewBinding.inputOTP2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    viewBinding.inputOTP3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        viewBinding.inputOTP3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    viewBinding.inputOTP4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        viewBinding.inputOTP4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    if(!viewModel.otp1.get().isEmpty() && !viewModel.otp2.get().isEmpty() && !viewModel.otp3.get().isEmpty() && !viewModel.otp4.get().isEmpty()){
                        Log.d("TAG", "onTextChanged: ok");
                        viewBinding.inputOTP4.clearFocus();
                        VerifyForgetPasswordOTPActivity.this.hideKeyboard();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void retryOtp(){
        ForgetPasswordRequest request = ForgetPasswordRequest.builder()
                .email(viewModel.email.get())
                .kind(viewModel.kind.get())
                .build();
        viewModel.showLoading();

        viewModel.compositeDisposable.add(viewModel.requestForgetPassword(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    viewModel.hideLoading();
                    if(response.isResult() && response.getData() != null){
                        viewModel.setCountdownOTP();
                    }else if(!response.isResult()){
                        viewModel.showErrorMessage(viewModel.getApplication().getErrorUtils().handelError(response.getCode()));
                    }
                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage("Không tìm thấy tài khoản. Vui lòng thử lại");
                }));

    }

}
