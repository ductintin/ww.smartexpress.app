package ww.smartexpress.app.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.databinding.ActivityLoginBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
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

        viewBinding.edtPW1.addTextChangedListener(new GenericTextWatcher(viewBinding.edtPW1, viewBinding.edtPW2));
        viewBinding.edtPW2.addTextChangedListener(new GenericTextWatcher(viewBinding.edtPW2, viewBinding.edtPW3));
        viewBinding.edtPW3.addTextChangedListener(new GenericTextWatcher(viewBinding.edtPW3, viewBinding.edtPW4));
        viewBinding.edtPW4.addTextChangedListener(new GenericTextWatcher(viewBinding.edtPW4, viewBinding.edtPW5));
        viewBinding.edtPW5.addTextChangedListener(new GenericTextWatcher(viewBinding.edtPW5, viewBinding.edtPW6));
        viewBinding.edtPW6.addTextChangedListener(new GenericTextWatcher(viewBinding.edtPW6, null));


        viewBinding.edtPW1.setOnKeyListener(new GenericKeyEvent(viewBinding.edtPW1, null));
        viewBinding.edtPW2.setOnKeyListener(new GenericKeyEvent(viewBinding.edtPW2, viewBinding.edtPW1));
        viewBinding.edtPW3.setOnKeyListener(new GenericKeyEvent(viewBinding.edtPW3, viewBinding.edtPW2));
        viewBinding.edtPW4.setOnKeyListener(new GenericKeyEvent(viewBinding.edtPW4, viewBinding.edtPW3));
        viewBinding.edtPW5.setOnKeyListener(new GenericKeyEvent(viewBinding.edtPW5, viewBinding.edtPW4));
        viewBinding.edtPW6.setOnKeyListener(new GenericKeyEvent(viewBinding.edtPW6, viewBinding.edtPW5));
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
                    && currentView != viewBinding.edtPW1 && currentView.getText().toString().isEmpty()) {
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
                case R.id.edtPW1:
                case R.id.edtPW2:
                case R.id.edtPW3:
                case R.id.edtPW4:
                case R.id.edtPW5:
                    if (text.length() == 1) {
                        nextView.requestFocus();
                    }
                    break;
                case R.id.edtPW6:
                    break;
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
}
