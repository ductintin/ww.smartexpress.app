package ww.smartexpress.app.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.databinding.ActivityHomeBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.fragment.activity.ActivityFragment;
import ww.smartexpress.app.ui.fragment.home.HomeFragment;
import ww.smartexpress.app.ui.fragment.search.SearchFragment;
import ww.smartexpress.app.ui.profile.ProfileFragment;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel>
        implements NavigationBarView.OnItemSelectedListener{
    public Fragment activeFragment = new Fragment();

    private HomeFragment homeFragment;
    private ActivityFragment activityFragment;
    private ProfileFragment profileFragment;
    private SearchFragment searchFragment;

    private BookingResponse bookingResponse = new BookingResponse();
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
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

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        networkCallback = new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(Network network) {
                // Kết nối Internet đã khả dụng
                Log.d("TAG", "onAvailable: ");
            }

            @Override
            public void onLost(Network network) {
                // Kết nối Internet đã mất
                Log.d("TAG", "onLost: ");
            }

            @Override
            public void onUnavailable() {
                // Kết nối Internet không khả dụng
                Log.d("TAG", "onUnavailable: ");
            }
        };


        // Đăng ký callback kết nối mạng
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);

        //getCurrentBooking();
        viewBinding.navigationView.setOnItemSelectedListener(this);
        //getCurrentBooking();
        viewBinding.navigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                //switchToHomeFragment();
                switchToSearchFragment();
                return true;
            case R.id.activity:
                switchToActivityFragment();
                return true;
            case R.id.account:
                switchToProfileFragment();
                return true;
        }
        return false;
    }

    public void switchToHomeFragment(){
        if(homeFragment == null){
            homeFragment = new HomeFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameLayout, homeFragment)
                    .hide(activeFragment)
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(activeFragment)
                    .show(homeFragment)
                    .commit();
        }
        activeFragment = homeFragment;
    }
    public void switchToActivityFragment(){
        if(activityFragment == null){
            activityFragment = new ActivityFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameLayout, activityFragment)
                    .hide(activeFragment)
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(activeFragment)
                    .show(activityFragment)
                    .commit();
        }
        activeFragment = activityFragment;
    }

    public void switchToProfileFragment(){
        if(profileFragment == null){
            profileFragment = new ProfileFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameLayout, profileFragment)
                    .hide(activeFragment)
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(activeFragment)
                    .show(profileFragment)
                    .commit();
        }
        activeFragment = profileFragment;
    }


    public void switchToSearchFragment(){
        if(searchFragment == null){
            searchFragment = new SearchFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameLayout, searchFragment)
                    .hide(activeFragment)
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(activeFragment)
                    .show(searchFragment)
                    .commit();
        }
        activeFragment = searchFragment;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void getCurrentBooking(){
        viewModel.compositeDisposable.add(viewModel.getCurrentBooking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    viewModel.hideLoading();
                    if(response.isResult()){
                        bookingResponse = response.getData().getContent().get(0);
                        navigateToBookActivity();
                    }
                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }

    public void navigateToBookActivity(){
        Intent intent = new Intent(HomeActivity.this, BookCarActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CUSTOMER_BOOKING_OBJECT, ApiModelUtils.toJson(bookingResponse));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }
}
