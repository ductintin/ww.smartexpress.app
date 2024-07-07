package ww.smartexpress.app.ui.wallet.transaction.details;

import android.content.Intent;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.response.WalletTransaction;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.trip.detail.TripDetailActivity;

public class TransactionDetailsViewModel extends BaseViewModel {

    public ObservableField<WalletTransaction> transaction = new ObservableField<>();
    public TransactionDetailsViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void getTransactionDetails(Long id){
        showLoading();
        compositeDisposable.add(repository.getApiService().getTransactionDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.isResult()){
                        transaction.set(response.getData());
                    }else {
                        showErrorMessage(response.getMessage());
                    }
                    hideLoading();
                },error->{
                    hideLoading();
                    showErrorMessage(application.getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    public void navigateToBooking(){
        Intent intent = new Intent(application.getCurrentActivity(), TripDetailActivity.class);
        intent.putExtra(Constants.CUSTOMER_BOOKING_DETAIL_ID, transaction.get().getBooking().getId());
        application.getCurrentActivity().startActivity(intent);
    }
}
