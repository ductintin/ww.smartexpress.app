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
import ww.smartexpress.app.ui.trip.cancel.TripCancelReasonActivity;
import ww.smartexpress.app.ui.trip.complete.TripCompleteActivity;
import ww.smartexpress.app.utils.NumberUtils;

public class TripActivity extends BaseActivity<ActivityTripBinding, TripViewModel> {
    long delayMillis = 5000;
    Handler handler = new Handler();
    Runnable runnable;
    BookingResponse bookingResponse;
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
        handler.removeCallbacks(runnable);

        if(viewModel.isCanceled.get() || viewModel.isCompleted.get() ){
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            viewModel.isCanceled.set(bundle.getBoolean(Constants.BOOKING_CANCEL_STATE));
            viewModel.isCompleted.set(bundle.getBoolean(Constants.BOOKING_COMPLETE_STATE));
            viewModel.bookingState.set(bundle.getInt(Constants.BOOKING_STATE));
            viewModel.roomId.set(bundle.getLong(Constants.ROOM_ID));
            if(!viewModel.isCanceled.get() && !viewModel.isCompleted.get()){
                getCurrentBooking();
            }else{
                Long id = bundle.getLong(Constants.CUSTOMER_BOOKING_DETAIL_ID);
                getMyBooking(id);
            }
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

    public void delivering(){
        runnable = new Runnable() {
            @Override
            public void run() {
                completeTrip(handler, this::run);
            }
        };
        viewBinding.btnCancelBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                Intent intent = new Intent(getApplication(), TripCancelReasonActivity.class);
                startActivity(intent);
            }
        });
        handler.postDelayed(runnable, delayMillis);
    }

    public void completeTrip(Handler handler, Runnable runnable){
        Intent intent = new Intent(this, TripCompleteActivity.class);
        startActivity(intent);
        finish();
    }

    public void getCurrentBooking(){
        viewModel.compositeDisposable.add(viewModel.getCurrentBooking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    viewModel.hideLoading();
                    if(response.isResult()){
                        viewModel.bookingResponse.set(response.getData().getContent().get(0));
                        viewModel.driverName.set(response.getData().getContent().get(0).getDriver().getFullName());
                        viewModel.driverAvatar.set(response.getData().getContent().get(0).getDriver().getAvatar());
                        viewModel.driverLicense.set(response.getData().getContent().get(0).getDriverVehicle().getPlate());
                        viewModel.driverVehicle.set(response.getData().getContent().get(0).getDriverVehicle().getName());

                        if(response.getData().getContent().get(0).getState() != 300){
                            viewModel.driverAvgRate.set((response.getData().getContent().get(0).getAverageRating().floatValue()));
                        }
                        viewModel.getApplication().getWebSocketLiveData().setCodeBooking(response.getData().getContent().get(0).getCode());
                        viewModel.getApplication().getWebSocketLiveData().sendPing();


                    }else{

                    }
                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }

    public void loadBooking(){
        viewModel.bookingResponse.set(bookingResponse);
        if(bookingResponse.getDriver() != null){
            viewModel.driverName.set(bookingResponse.getDriver().getFullName());
            viewModel.driverAvatar.set(bookingResponse.getDriver().getAvatar());
            viewModel.driverLicense.set(bookingResponse.getDriverVehicle().getPlate());
            viewModel.driverVehicle.set(bookingResponse.getDriverVehicle().getName());
            if(bookingResponse.getState() != 300 || bookingResponse.getRating() == null){
                viewModel.driverAvgRate.set(bookingResponse.getAverageRating().floatValue());
                Log.d("TAG", "loadBooking: " +300);
            }

        }

        if(bookingResponse.getRating() != null){
            viewModel.driverRate.set(bookingResponse.getRating().getStar());
            viewModel.rating.set(bookingResponse.getRating().getStar().floatValue());
            viewModel.ratingMsg.set(bookingResponse.getRating().getMessage());
        }

        viewModel.money.set(bookingResponse.getMoney());
        viewModel.distance.set(bookingResponse.getDistance());
        viewModel.origin.set(bookingResponse.getPickupAddress());
        viewModel.destination.set(bookingResponse.getDestinationAddress());
        viewModel.senderName.set(bookingResponse.getSenderName());
        viewModel.senderPhone.set(bookingResponse.getSenderPhone());
        viewModel.consigneeName.set(bookingResponse.getConsigneeName());
        viewModel.consigneePhone.set(bookingResponse.getConsigneePhone());
        viewModel.customerNote.set(bookingResponse.getCustomerNote());
        viewModel.isCod.set(bookingResponse.getIsCod());
        viewModel.codPrice.set(bookingResponse.getCodPrice());
        viewModel.createdDate.set(bookingResponse.getCreatedDate());
        viewModel.code.set(bookingResponse.getCode());
        viewModel.pickupImage.set(bookingResponse.getPickupImage());
        viewModel.deliveryImage.set(bookingResponse.getDeliveryImage());
        viewModel.bookingState.set(bookingResponse.getState());
    }

    public void getMyBooking(Long id){
        viewModel.showLoading();
        viewModel.compositeDisposable.add(viewModel.getMyBooking(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    viewModel.hideLoading();
                    if(response.isResult()){
                        bookingResponse = response.getData().getContent().get(0);
                        viewModel.getApplication().getWebSocketLiveData().setCodeBooking(response.getData().getContent().get(0).getCode());
                        viewModel.getApplication().getWebSocketLiveData().sendPing();
                        loadBooking();
                    }else{
                        bookingResponse = new BookingResponse();
                        loadBooking();
                        viewModel.showErrorMessage(response.getMessage());
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
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                getMyBooking(Long.valueOf(bundle.getString(Constants.CUSTOMER_BOOKING_ID)));
                viewModel.bookingState.set(200);
            }
        }
    }
}
