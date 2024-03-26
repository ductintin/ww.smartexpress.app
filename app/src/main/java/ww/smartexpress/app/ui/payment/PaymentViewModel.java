package ww.smartexpress.app.ui.payment;

import androidx.lifecycle.MutableLiveData;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.search.location.SearchLocationActivity;

public class PaymentViewModel extends BaseViewModel {
    public MutableLiveData<Integer> paymentMethod = new MutableLiveData<>(0);
    public PaymentViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public void doBack(){
        getApplication().getCurrentActivity().onBackPressed();
    }
    public void doAddMethod(){

    }
    public void payCredit(){
        paymentMethod.setValue(1);
    }
    public void payCash(){
        paymentMethod.setValue(0);
    }

    public void confirm(){
        getApplication().getCurrentActivity().onBackPressed();
    }

}
