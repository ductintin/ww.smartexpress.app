package ww.smartexpress.app.ui.password.forget;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.databinding.ActivityResetForgetPasswordBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class ResetForgetPasswordActivity extends BaseActivity<ActivityResetForgetPasswordBinding, ResetForgetPasswordViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_forget_password;
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
        viewModel.otp.set(intent.getStringExtra(Constants.OTP));
    }
}
