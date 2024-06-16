package ww.smartexpress.app.ui.payment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.PaymentMethod;
import ww.smartexpress.app.data.model.api.response.Promotion;
import ww.smartexpress.app.databinding.ActivityPaymentBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class PaymentActivity extends BaseActivity<ActivityPaymentBinding, PaymentViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_payment;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setLifecycleOwner(this);
        Intent intent = getIntent();
        if(intent.getDoubleExtra("BOOKING_PRICE", 0L) != 0){
            viewModel.money.set(intent.getDoubleExtra("BOOKING_PRICE", 0L));
        }
        getMyWallet();
    }

    public void getMyWallet(){
        viewModel.showLoading();
        viewModel.compositeDisposable.add(viewModel.getMyWallet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    viewModel.hideLoading();
                    if(response.isResult()){
                        viewModel.wallet.set(response.getData());
                    }else {

                    }
                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(application.getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyWallet();
    }
}
