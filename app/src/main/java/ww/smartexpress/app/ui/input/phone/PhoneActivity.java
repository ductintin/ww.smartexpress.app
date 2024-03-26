package ww.smartexpress.app.ui.input.phone;

import android.os.Bundle;

import eu.davidea.flexibleadapter.databinding.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.databinding.ActivityPhoneBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class PhoneActivity extends BaseActivity<ActivityPhoneBinding, PhoneViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_phone;
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
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}