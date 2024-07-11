package ww.smartexpress.app.ui.payout.details;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.databinding.ActivityPayoutDetailsBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class PayoutDetailsActivity extends BaseActivity<ActivityPayoutDetailsBinding, PayoutDetailsViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_payout_details;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
        Long id = getIntent().getLongExtra("payoutId",0);
        viewModel.getTransactionDetails(id);
    }
}
