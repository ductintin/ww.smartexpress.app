package ww.smartexpress.app.ui.password.otp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
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
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Intent intent = getIntent();
        viewModel.userId.set(intent.getStringExtra(Constants.KEY_USER_ID));
        viewModel.kind.set(intent.getIntExtra(Constants.VERIFY_OPTION, 1));

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
                    if(!viewModel.otp1.get().isEmpty() && !viewModel.otp1.get().isEmpty() && !viewModel.otp1.get().isEmpty() && !viewModel.otp1.get().isEmpty()){
                        viewBinding.inputOTP4.clearFocus();
                        hideKeyboard();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}
