package ww.smartexpress.app.ui.order.details;

import android.content.Intent;

import androidx.databinding.ObservableBoolean;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.store.StoreActivity;

public class OrderDetailsViewModel extends BaseViewModel {
    public ObservableBoolean isPayment = new ObservableBoolean(false);
    public OrderDetailsViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public void doBack(){
        if(!isPayment.get()) {
            getApplication().getCurrentActivity().onBackPressed();
        }else {
            Intent intent = new Intent(getApplication().getCurrentActivity(), StoreActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            getApplication().getCurrentActivity().startActivity(intent);
            getApplication().getCurrentActivity().finish();
        }
    }
    public void statusOrder(){

    }
}
