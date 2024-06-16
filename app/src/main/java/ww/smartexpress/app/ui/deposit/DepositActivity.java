package ww.smartexpress.app.ui.deposit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.annotation.Nullable;

import eu.davidea.flexibleadapter.databinding.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.databinding.ActivityDepositBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.utils.NumberUtils;

public class DepositActivity extends BaseActivity<ActivityDepositBinding, DepositViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_deposit;
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
        viewBinding.edtMoney.addTextChangedListener(new TextWatcher() {
            private String current = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    if (!charSequence.toString().equals(current)) {

                        String cleanString = charSequence.toString().replaceAll("[.]", "");
                        viewModel.money.set(cleanString);
                        Log.d("TAG", "onTextChanged: "+ viewModel.money.get());
                        double parsed = Double.parseDouble(cleanString);

                        String formated = NumberUtils.formatEdtTextCurrency(parsed);

                        current = formated;

                        viewBinding.edtMoney.setText(formated);
                        viewBinding.edtMoney.setSelection(formated.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void clickMoney(String money){
        viewModel.money.set(money);
        viewBinding.edtMoney.setText(money);
    }

}
