package ww.smartexpress.app.ui.password.forget;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.databinding.ActivityForgetPasswordBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class ForgetPasswordActivity extends BaseActivity<ActivityForgetPasswordBinding, ForgetPasswordViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_password;
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
        viewModel.kind.set(intent.getIntExtra(Constants.VERIFY_OPTION, 1));

        viewBinding.layoutHeader.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(viewModel.kind.get() == 1){
            InputFilter[] filters = new InputFilter[1];
            filters[0] = new InputFilter.LengthFilter(10);
            viewBinding.edtEmail.setFilters(filters);
        }
    }
}
