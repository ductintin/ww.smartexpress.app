package ww.smartexpress.app.ui.shipping.address.info;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.databinding.ActivityShippingInfoBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class ShippingInfoActivity extends BaseActivity<ActivityShippingInfoBinding, ShippingInfoViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_shipping_info;
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
