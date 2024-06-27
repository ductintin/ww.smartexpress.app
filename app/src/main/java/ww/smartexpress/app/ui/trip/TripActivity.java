package ww.smartexpress.app.ui.trip;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.databinding.ActivityTripBinding;
import ww.smartexpress.app.databinding.BaseDialogBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.rate.RateDriverActivity;
import ww.smartexpress.app.ui.trip.cancel.TripCancelReasonActivity;
import ww.smartexpress.app.ui.trip.complete.TripCompleteActivity;
import ww.smartexpress.app.utils.NumberUtils;

public class TripActivity extends BaseActivity<ActivityTripBinding, TripViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_trip;
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Long id = bundle.getLong(Constants.CUSTOMER_BOOKING_DETAIL_ID);
            getMyBooking(id);
        }

        viewBinding.btnCancelBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handler.removeCallbacks(runnable);
                cancelBooking();
            }
        });
    }

    public void cancelBooking() {
        Dialog dialog = new Dialog(TripActivity.this);
        BaseDialogBinding dialogLogoutBinding = DataBindingUtil.inflate(LayoutInflater.from(TripActivity.this),R.layout.base_dialog,null, false);
        dialogLogoutBinding.setTitle(getString(R.string.cancel_trip_dialog_title));
        dialogLogoutBinding.setSubtitle("");
        dialogLogoutBinding.setDecision(getString(R.string.confirm));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(dialogLogoutBinding.getRoot());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCanceledOnTouchOutside(false);

        dialogLogoutBinding.btnLogout.setOnClickListener(view -> {
            Intent intent = new Intent(TripActivity.this, TripCancelReasonActivity.class);
            startActivity(intent);
            dialog.dismiss();
        });
        dialogLogoutBinding.btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }


    public void getMyBooking(Long id){
        viewModel.compositeDisposable.add(viewModel.getMyBooking(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if(response.isResult() && response.getData().getTotalElements() > 0){
                        viewModel.isLoading.set(false);
                        viewModel.bookingResponse.set(response.getData().getContent().get(0));
                    }else{
                        viewModel.showErrorMessage("Không tìm thấy đơn hàng");
                        Intent intent = new Intent(TripActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null){
            Long id = intent.getLongExtra("BOOKING_ID", 0L);
            if(viewModel.bookingResponse.get().getId().equals(id)){
                Intent intentToRating = new Intent(TripActivity.this, RateDriverActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.CUSTOMER_BOOKING_ID, viewModel.bookingResponse.get().getId());
                intentToRating.putExtras(bundle);
                startActivity(intentToRating);
                finish();
            }
        }
    }
}
