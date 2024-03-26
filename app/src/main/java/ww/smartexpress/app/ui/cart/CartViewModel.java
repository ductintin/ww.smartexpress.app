package ww.smartexpress.app.ui.cart;

import android.content.Intent;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.purchase.PurchaseActivity;

public class CartViewModel extends BaseViewModel {
    public CartViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public void back(){
        getApplication().getCurrentActivity().onBackPressed();
    }

    public void doPurchase(){
        Intent intent = new Intent(getApplication().getCurrentActivity(), PurchaseActivity.class);
        getApplication().getCurrentActivity().startActivity(intent);
    }
}
