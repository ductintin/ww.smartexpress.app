package ww.smartexpress.app.ui.splashform;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonArray;

import java.util.List;

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
import ww.smartexpress.app.utils.LocationUtils;

public class SplashFormActivity extends BaseActivity<ActivitySplashFormBinding, SplashFormViewModel> {
    List<BookingResponse> bookingResponse;
    private FusedLocationProviderClient fusedLocationClient;

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
                        //getCurrentBooking();
                        navigateToHomeActivity();
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
                    if(response.isResult() && response.getData().getTotalElements() > 0 && response.getData().getTotalElements() <= 10){
                        bookingResponse = response.getData().getContent();
//                        switch (bookingResponse.getState()){
//                            case 0 : case 100:
//                                navigateToBookActivity();
//                                break;
//                            case 200:
//                                navigateToTripActivity();
//                                break;
//                            default:
//                                break;
//                        }
                        navigateToBookActivity();
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(SplashFormActivity.this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (LocationUtils.isLocationEnabled(getApplicationContext())) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(SplashFormActivity.this, location -> {
                            if(location != null){
                                viewModel.latlng.set(String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
                                viewModel.compositeDisposable.add(viewModel.getLocationInfo()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(response -> {
                                            String status = response.get("status").getAsString();
                                            Log.d("TAG", status);

                                            JsonArray results = response.getAsJsonArray("results");
                                            int index = results.size() - 1;

                                            String component = results.get(0).getAsJsonObject().get("formatted_address").getAsString();
                                            String id = results.get(0).getAsJsonObject().get("place_id").getAsString();
                                            Log.d("TAG", "onLocationChanged: " + component);

                                            Intent intent = new Intent(SplashFormActivity.this, HomeActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("PLACE_NAME", component);
                                            bundle.putString("PLACE_ID", id);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            finish();

                                        }, error -> {
                                            viewModel.hideLoading();
                                            viewModel.showErrorMessage(getString(R.string.network_error));
                                            error.printStackTrace();
                                        })
                                );
                            }else{

                                Intent intent = new Intent(SplashFormActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }else{
                Intent intent = new Intent(SplashFormActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
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