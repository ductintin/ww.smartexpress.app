package ww.smartexpress.app.ui.trip.complete;

import android.os.Bundle;

import androidx.annotation.Nullable;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.databinding.ActivityCompleteTripBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.home.HomeActivity;

public class TripCompleteActivity extends BaseActivity<ActivityCompleteTripBinding, TripCompleteViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_complete_trip;
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
            viewModel.isComplete.set(bundle.getBoolean(Constants.BOOKING_COMPLETE_STATE, false));
            viewModel.bookingId.set(bundle.getLong(Constants.CUSTOMER_BOOKING_ID));
        }
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(getApplication(), HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
//        finish();
        viewModel.onAgreeClick();
    }
}
