package ww.smartexpress.app.ui.payout;

import android.content.Intent;

import androidx.databinding.ObservableField;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.ConfirmPasswordRequest;
import ww.smartexpress.app.data.model.api.request.PayoutRequest;
import ww.smartexpress.app.data.model.api.response.BankCard;
import ww.smartexpress.app.data.model.api.response.NotificationResponse;
import ww.smartexpress.app.data.model.api.response.PayoutTransaction;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.ui.bank.BankActivity;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class PayoutViewModel extends BaseViewModel {

    public ObservableField<String> money = new ObservableField<>();
    public ObservableField<Double> balance = new ObservableField<>(0.0);
    public ObservableField<UserEntity> user = new ObservableField<>();
    public ObservableField<BankCard> bankCard = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<List<PayoutTransaction>> payoutTransactionList = new ObservableField<>(new ArrayList<>());
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

        showLoading();
            compositeDisposable.add(repository.getApiService().payout(new PayoutRequest(user.get().getBankCard(),Integer.valueOf(money.get())))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                if(response.isResult()){
                                    hideLoading();
                                    showSuccessMessage("Tạo yêu cầu rút tiền thành công");
                                    getApplication().getCurrentActivity().onBackPressed();
                                }else {
                                    hideLoading();
                                    showErrorMessage(getApplication().getErrorUtils().handelError(response.getCode()));
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

    public Observable<ResponseWrapper<String>> deletePayoutRequest(Long id){
        return repository.getApiService().deletePayoutRequest(id);
    }

    public Observable<ResponseWrapper<ResponseListObj<PayoutTransaction>>> getMyPayoutRequest(){
        Long userId = repository.getSharedPreferences().getLongVal(Constants.KEY_USER_ID);
        return repository.getApiService().getMyPayoutRequest(userId, 0);
    }

    public Observable<ResponseWrapper<String>> confirmPassword(String password){
        ConfirmPasswordRequest confirmPasswordRequest = new ConfirmPasswordRequest(password);
        return repository.getApiService().confirmPassword(confirmPasswordRequest);
    }
}
