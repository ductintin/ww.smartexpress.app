package ww.smartexpress.app.ui.payment;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.PaymentMethod;
import ww.smartexpress.app.data.model.api.response.ServiceResponse;
import ww.smartexpress.app.data.model.api.response.WalletResponse;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.search.location.SearchLocationActivity;

public class PaymentViewModel extends BaseViewModel {
    public MutableLiveData<Integer> paymentMethod = new MutableLiveData<>(0);
    public ObservableField<PaymentMethod> payment = new ObservableField<>(new PaymentMethod());
    public ObservableField<WalletResponse> wallet = new ObservableField<>(new WalletResponse());
    public ObservableField<Double> money = new ObservableField<>(0.0);
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
        payment.get().setPaymentKind(2);
    }
    public void payCash(){
        paymentMethod.setValue(0);
        payment.get().setPaymentKind(1);
    }

    public void confirm(){
       if(paymentMethod.getValue() == 1 && wallet.get().getBalance() < money.get()){
           showWarningMessage("Số dư không đủ, vui lòng nạp thêm");
       }else{
           EventBus.getDefault().postSticky(payment.get());
           getApplication().getCurrentActivity().onBackPressed();
       }
    }

    Observable<ResponseWrapper<WalletResponse>> getMyWallet() {
        return repository.getApiService().getMyWallet()
                .doOnNext(response -> {

                });
    }

}
