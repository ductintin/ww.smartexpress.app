package ww.smartexpress.app.ui.coupon;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class CouponViewModel extends BaseViewModel {
    public CouponViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void back() {
        application.getCurrentActivity().onBackPressed();
    }
}
