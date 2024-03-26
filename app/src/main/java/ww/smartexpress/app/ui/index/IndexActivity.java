package ww.smartexpress.app.ui.index;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.databinding.ActivityIndexBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.home.HomeActivity;

public class IndexActivity extends BaseActivity<ActivityIndexBinding, IndexViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_index;
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
    }
}
