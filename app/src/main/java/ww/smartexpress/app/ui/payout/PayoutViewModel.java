package ww.smartexpress.app.ui.payout;

import android.content.Intent;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.request.PayoutRequest;
import ww.smartexpress.app.data.model.api.response.BankCard;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.ui.bank.BankActivity;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class PayoutViewModel extends BaseViewModel {

    public ObservableField<String> money = new ObservableField<>();
    public ObservableField<Double> balance = new ObservableField<>(0.0);
    public ObservableField<UserEntity> user = new ObservableField<>();
    public ObservableField<BankCard> bankCard = new ObservableField<>();
    public PayoutViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public Repository getRepository(){
        return repository;
    }

    public void back(){
        application.getCurrentActivity().finish();
    }

    public void doDone(){
        if(money.get() != null && Integer.valueOf(money.get())<50000){
            showErrorMessage("Số tiền rút tối thiểu là 50.000đ");
            return;
        }
        if(money.get() != null && Double.valueOf(money.get()) > balance.get()){
            showErrorMessage("Số tiền rút vượt quá số dư trong ví");
            return;
        }
        showLoading();
            compositeDisposable.add(repository.getApiService().payout(new PayoutRequest(user.get().getBankCard(),Integer.valueOf(money.get())))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                if(response.isResult()){
                                    hideLoading();
                                    showSuccessMessage(response.getMessage());
                                }else {
                                    hideLoading();
                                    showErrorMessage(response.getMessage());
                                }
                            },error->{
                                hideLoading();
                                showErrorMessage(application.getString(R.string.network_error));
                                error.printStackTrace();
                            })
            );
    }

    public void navigateBank(){
        Intent intent = new Intent(application.getCurrentActivity(), BankActivity.class);
        application.getCurrentActivity().startActivity(intent);
    }
}
