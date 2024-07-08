package ww.smartexpress.app.ui.otp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.request.ActiveCustomerRequest;
import ww.smartexpress.app.data.model.api.request.RetryOtpRegisterRequest;
import ww.smartexpress.app.databinding.ActivityLoginOtpBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.profile.EditProfileActivity;
import ww.smartexpress.app.ui.signin.SignInActivity;

public class LoginOTPActivity extends BaseActivity<ActivityLoginOtpBinding, LoginOTPViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_login_otp;
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
        setTheme(R.style.WhiteAppTheme);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent!= null){
            if(intent.getStringExtra("USER_ID") == null ){
                viewModel.phone.set(intent.getStringExtra("USER_PHONE"));
                retryOtpActiveCustomer();
            }else{
                viewModel.userId.set(intent.getStringExtra("USER_ID"));
                viewModel.phone.set(intent.getStringExtra("USER_PHONE"));
                viewModel.setCountdownOTP();
            }
        }

        viewBinding.fpOTP1.addTextChangedListener(new GenericTextWatcher(viewBinding.fpOTP1, viewBinding.fpOTP2));
        viewBinding.fpOTP2.addTextChangedListener(new GenericTextWatcher(viewBinding.fpOTP2, viewBinding.fpOTP3));
        viewBinding.fpOTP3.addTextChangedListener(new GenericTextWatcher(viewBinding.fpOTP3, viewBinding.fpOTP4));
        viewBinding.fpOTP4.addTextChangedListener(new GenericTextWatcher(viewBinding.fpOTP4, null));

        viewBinding.fpOTP1.setOnKeyListener(new GenericKeyEvent(viewBinding.fpOTP1, null));
        viewBinding.fpOTP2.setOnKeyListener(new GenericKeyEvent(viewBinding.fpOTP2, viewBinding.fpOTP1));
        viewBinding.fpOTP3.setOnKeyListener(new GenericKeyEvent(viewBinding.fpOTP3, viewBinding.fpOTP2));
        viewBinding.fpOTP4.setOnKeyListener(new GenericKeyEvent(viewBinding.fpOTP4, viewBinding.fpOTP3));

    }



    public class GenericKeyEvent implements View.OnKeyListener {
        private EditText currentView;
        private EditText previousView;

        public GenericKeyEvent(EditText currentView, EditText previousView) {
            this.currentView = currentView;
            this.previousView = previousView;
        }

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL
                    && currentView != viewBinding.fpOTP1 && currentView.getText().toString().isEmpty()) {
                previousView.setText(null);
                previousView.requestFocus();
                return true;
            }
            return false;
        }

    }

    public class GenericTextWatcher implements TextWatcher {
        private View currentView;
        private View nextView;

        public GenericTextWatcher(View currentView, View nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();

            switch (currentView.getId()) {
                case R.id.fpOTP1:
                case R.id.fpOTP2:
                case R.id.fpOTP3:
                    if (text.length() == 1) {
                        nextView.requestFocus();
                    }
                    break;
                case R.id.fpOTP4:
                    break;

            }

            if(!TextUtils.isEmpty(viewBinding.fpOTP1.getText().toString()) && !TextUtils.isEmpty(viewBinding.fpOTP2.getText().toString())
                    && !TextUtils.isEmpty(viewBinding.fpOTP2.getText().toString()) && !TextUtils.isEmpty(viewBinding.fpOTP4.getText().toString())){
//                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
//                startActivity(intent);
//                viewModel.countDownTimer.cancel();
//                finish();
                activeCustomer();
            }

        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // TODO: Implement as needed
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // TODO: Implement as needed1
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void activeCustomer(){
        String otp = viewModel.fpOTP1.get() + viewModel.fpOTP2.get() + viewModel.fpOTP3.get() + viewModel.fpOTP4.get();

        ActiveCustomerRequest request = ActiveCustomerRequest.builder()
                .otp(otp)
                .userId(viewModel.userId.get())
                .build();
        viewModel.showLoading();
        viewModel.compositeDisposable.add(viewModel.activeCustomer(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    viewModel.hideLoading();
                    if (response.isResult()){
                        viewModel.showSuccessMessage("Kích hoạt tài khoản thành công. Vui lòng đăng nhập lại!");
                        navigateToSignIn();
                    }else{
                        viewModel.getApplication().getErrorUtils().handelError(response.getCode());
                    }

                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    public void retryOtpActiveCustomer(){
        RetryOtpRegisterRequest request = new RetryOtpRegisterRequest(viewModel.phone.get());

        viewModel.showLoading();
        viewModel.compositeDisposable.add(viewModel.retryActiveCustomer(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    viewModel.hideLoading();
                    if (response.isResult()){
                        viewModel.showSuccessMessage("Gửi lại mã xác thực thành công");
                        viewModel.userId.set(response.getData().getUserId());
                        viewModel.setCountdownOTP();
                    }else{
                        viewModel.getApplication().getErrorUtils().handelError(response.getCode());
                    }

                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    public void navigateToSignIn(){
        Intent intent = new Intent(LoginOTPActivity.this, SignInActivity.class);
        startActivity(intent);
        viewModel.countDownTimer.cancel();
        finish();
    }

}
