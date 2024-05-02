package ww.smartexpress.app.ui.trip.detail;

import android.os.Bundle;

import androidx.annotation.Nullable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.databinding.ActivityTripDetailBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class TripDetailActivity extends BaseActivity<ActivityTripDetailBinding, TripDetailViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_trip_detail;
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

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Long id = bundle.getLong(Constants.CUSTOMER_BOOKING_DETAIL_ID);
            getMyBooking(id);
        }

    }

    public void getMyBooking(Long id){
        viewModel.compositeDisposable.add(viewModel.getMyBooking(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if(response.isResult() && response.getData().getContent() != null){
                        viewModel.isLoading.set(false);
                        viewModel.bookingResponse.set(response.getData().getContent().get(0));
                    }
                }, err -> {
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }
}
