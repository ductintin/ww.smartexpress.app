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
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.databinding.ActivitySplashFormBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.delivery.BookDeliveryActivity;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.signin.SignInActivity;
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
                        navigateToSignInActivity();
                    }
                } else {
                   navigateToWelcomeActivity();
                }
            }
        }, delayMillis);
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

    public void navigateToSignInActivity(){
        Intent intent = new Intent(SplashFormActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void navigateToWelcomeActivity(){
        Intent intent = new Intent(SplashFormActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

}