package ww.smartexpress.app.ui.payout;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.request.PayoutRequest;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class PayoutViewModel extends BaseViewModel {

    public ObservableField<String> money = new ObservableField<>();
    public ObservableField<Integer> balance = new ObservableField<>(0);
    public ObservableField<UserEntity> user = new ObservableField<>();
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
        if(money.get() == null && Integer.valueOf(money.get())>50000){
            showErrorMessage("Số tiền rút không hợp lệ");
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
}
