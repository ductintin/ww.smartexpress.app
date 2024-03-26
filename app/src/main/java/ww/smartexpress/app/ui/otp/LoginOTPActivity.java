package ww.smartexpress.app.ui.otp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.databinding.ActivityLoginOtpBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

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

        viewBinding.edtOTP1.addTextChangedListener(new GenericTextWatcher(viewBinding.edtOTP1, viewBinding.edtOTP2));
        viewBinding.edtOTP2.addTextChangedListener(new GenericTextWatcher(viewBinding.edtOTP2, viewBinding.edtOTP3));
        viewBinding.edtOTP3.addTextChangedListener(new GenericTextWatcher(viewBinding.edtOTP3, viewBinding.edtOTP4));
        viewBinding.edtOTP4.addTextChangedListener(new GenericTextWatcher(viewBinding.edtOTP4, viewBinding.edtOTP5));
        viewBinding.edtOTP5.addTextChangedListener(new GenericTextWatcher(viewBinding.edtOTP5, viewBinding.edtOTP6));
        viewBinding.edtOTP6.addTextChangedListener(new GenericTextWatcher(viewBinding.edtOTP6, null));


        viewBinding.edtOTP1.setOnKeyListener(new GenericKeyEvent(viewBinding.edtOTP1, null));
        viewBinding.edtOTP2.setOnKeyListener(new GenericKeyEvent(viewBinding.edtOTP2, viewBinding.edtOTP1));
        viewBinding.edtOTP3.setOnKeyListener(new GenericKeyEvent(viewBinding.edtOTP3, viewBinding.edtOTP2));
        viewBinding.edtOTP4.setOnKeyListener(new GenericKeyEvent(viewBinding.edtOTP4, viewBinding.edtOTP3));
        viewBinding.edtOTP5.setOnKeyListener(new GenericKeyEvent(viewBinding.edtOTP5, viewBinding.edtOTP4));
        viewBinding.edtOTP6.setOnKeyListener(new GenericKeyEvent(viewBinding.edtOTP6, viewBinding.edtOTP5));

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
                    && currentView != viewBinding.edtOTP1 && currentView.getText().toString().isEmpty()) {
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
            if(text.length() < 1){
                viewBinding.btnVerifyLoginOTP.setEnabled(false);
            }

            switch (currentView.getId()) {
                case R.id.edtOTP1:
                case R.id.edtOTP2:
                case R.id.edtOTP3:
                case R.id.edtOTP4:
                case R.id.edtOTP5:
                    if (text.length() == 1) {
                        nextView.requestFocus();
                    }
                    break;
                case R.id.edtOTP6:
                    if(text.length()==1){
                        viewBinding.btnVerifyLoginOTP.setEnabled(true);
                    }
                    break;
            }

            if(!viewBinding.edtOTP1.getText().toString().isEmpty() && !viewBinding.edtOTP2.getText().toString().isEmpty() && !viewBinding.edtOTP3.getText().toString().isEmpty()
                    && !viewBinding.edtOTP4.getText().toString().isEmpty() && !viewBinding.edtOTP5.getText().toString().isEmpty() && !viewBinding.edtOTP6.getText().toString().isEmpty()){
                viewBinding.btnVerifyLoginOTP.setEnabled(true);
            }else{
                viewBinding.btnVerifyLoginOTP.setEnabled(false);
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

}
