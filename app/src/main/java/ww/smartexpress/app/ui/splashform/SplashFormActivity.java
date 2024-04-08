package ww.smartexpress.app.ui.splashform;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import eu.davidea.flexibleadapter.databinding.BR;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.databinding.ActivitySplashFormBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.delivery.BookDeliveryActivity;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.index.IndexActivity;
import ww.smartexpress.app.ui.input.phone.PhoneActivity;
import ww.smartexpress.app.ui.trip.TripActivity;
import ww.smartexpress.app.ui.welcome.WelcomeActivity;

public class SplashFormActivity extends BaseActivity<ActivitySplashFormBinding, SplashFormViewModel> {
    BookingResponse bookingResponse;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash_form;
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
        // Delay for 5 seconds (5000 milliseconds)
        long delayMillis = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkLocationPermissions()) {
                    if(viewModel.checkToken()){
                        getCurrentBooking();
                    }else{
                        navigateToIndexActivity();
                    }
                } else {
                   navigateToWelcomeActivity();
                }
            }
        }, delayMillis);
    }

    public void getCurrentBooking(){
        viewModel.compositeDisposable.add(viewModel.getCurrentBooking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    viewModel.hideLoading();
                    if(response.isResult()){
                        bookingResponse = response.getData();
                        switch (bookingResponse.getState()){
                            case 0 : case 100:
                                navigateToBookActivity();
                                break;
                            case 200:
                                navigateToTripActivity();
                                break;
                            default:
                                break;
                        }
                    }else{
                        navigateToHomeActivity();
                    }
                }, err -> {
                    viewModel.hideLoading();
                    navigateToHomeActivity();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }

    public void navigateToBookActivity(){
        Intent intent = new Intent(SplashFormActivity.this, BookDeliveryActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.CUSTOMER_BOOKING_OBJECT, ApiModelUtils.toJson(bookingResponse));
//        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void navigateToHomeActivity(){
        Intent intent = new Intent(SplashFormActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void navigateToIndexActivity(){
        Intent intent = new Intent(SplashFormActivity.this, IndexActivity.class);
        startActivity(intent);
        finish();
    }

    public void navigateToWelcomeActivity(){
        Intent intent = new Intent(SplashFormActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void navigateToTripActivity(){
        Intent intent = new Intent(SplashFormActivity.this, TripActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BOOKING_STATE, 200);
        Log.d("TAG", "handleSocket: nende" );
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

}