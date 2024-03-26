package ww.smartexpress.app.ui.welcome;

import androidx.annotation.NonNull;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.databinding.ActivityWelcomeBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.input.phone.PhoneActivity;

public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding, WelcomeViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!checkLocationPermissions()) {
            requestLocationPermissions();
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
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
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.LOCATION_PERMISSION_CODE) {
            // Kiểm tra kết quả yêu cầu quyền truy cập vị trí
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                viewModel.checkLocationPermission.set(true);
            } else {
                Log.d("AAA", "onRequestPermissionsResult: ");
                finishAffinity();
                System.exit(0);
            }
        }
    }

}