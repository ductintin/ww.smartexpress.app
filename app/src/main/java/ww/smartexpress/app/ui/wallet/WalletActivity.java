package ww.smartexpress.app.ui.wallet;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.databinding.ActivityWalletBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class WalletActivity extends BaseActivity<ActivityWalletBinding, WalletViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }
}
