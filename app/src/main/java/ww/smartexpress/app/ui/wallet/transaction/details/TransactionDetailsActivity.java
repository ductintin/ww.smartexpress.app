package ww.smartexpress.app.ui.wallet.transaction.details;

import android.os.Bundle;

import androidx.annotation.Nullable;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.databinding.ActivityTransactionDetailBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class TransactionDetailsActivity extends BaseActivity<ActivityTransactionDetailBinding, TransactionDetailsViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_transaction_detail;
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
        Long id = getIntent().getLongExtra("transactionId",0);
        viewModel.getTransactionDetails(id);
    }
}
