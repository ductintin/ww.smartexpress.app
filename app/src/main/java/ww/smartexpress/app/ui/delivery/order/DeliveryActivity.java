package ww.smartexpress.app.ui.delivery.order;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.databinding.ActivityDeliveryBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class DeliveryActivity extends BaseActivity<ActivityDeliveryBinding, DeliveryViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_delivery;
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
