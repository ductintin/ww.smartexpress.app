package ww.smartexpress.app.ui.trip.cancel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.request.CancelBookingRequest;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.CancelReason;
import ww.smartexpress.app.databinding.ActivityTripCancelReasonBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.trip.adapter.CancelReasonAdapter;
import ww.smartexpress.app.ui.trip.complete.TripCompleteActivity;

public class TripCancelReasonActivity extends BaseActivity<ActivityTripCancelReasonBinding, TripCancelReasonViewModel> {
    private CancelReasonAdapter cancelReasonAdapter1;
    private CancelReasonAdapter cancelReasonAdapter2;
    BookingResponse bookingResponse;
    @Override
    public int getLayoutId() {
        return R.layout.activity_trip_cancel_reason;
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
        getCurrentBooking();
        load();
    }

    public void load(){
        List<CancelReason> cancelReasonList1 = new ArrayList<>();
        cancelReasonList1.add(new CancelReason("1", "Tìm được chỗ", false));
        cancelReasonList1.add(new CancelReason("2", "Tìm được chỗ gần hơn", false));
        cancelReasonList1.add(new CancelReason("3", "Có việc gấp", false));
        //cancelReasonList1.add(new CancelReason("3", "Chưa áp mã giảm giá", false));

        List<CancelReason> cancelReasonList2 = new ArrayList<>();
        cancelReasonList2.add(new CancelReason("1", "Tuyệt vời", false));
        cancelReasonList2.add(new CancelReason("2", "Tạm ổn", false));
        cancelReasonList2.add(new CancelReason("3", "Quá tệ", false));

        cancelReasonAdapter1 = new CancelReasonAdapter(this,0, cancelReasonList1);
        cancelReasonAdapter2 = new CancelReasonAdapter(this,0, cancelReasonList2);

        viewBinding.autoCompleteCR1.setAdapter(cancelReasonAdapter1);
        viewBinding.autoCompleteCR2.setAdapter(cancelReasonAdapter2);


        viewBinding.autoCompleteCR1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.isSelected1.set(true);
                viewModel.textNote.set(viewBinding.autoCompleteCR1.getText().toString());
                Log.d("TAG", "onItemClick: " + viewModel.textNote.get());
            }
        });


        viewBinding.autoCompleteCR2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.isSelected2.set(true);
                viewModel.textRate.set(viewBinding.autoCompleteCR2.getText().toString());
                Log.d("TAG", "onItemClick: " + viewModel.textRate.get());
            }
        });


    }

    public void getCurrentBooking(){
        viewModel.compositeDisposable.add(viewModel.getCurrentBooking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    viewModel.hideLoading();
                    if(response.isResult()){
                        bookingResponse = response.getData();
                        //navigateToBookActivity();
                    }else{
                        //navigateToHomeActivity();
                    }
                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }

    public void cancelTrip(){
        CancelBookingRequest request = new CancelBookingRequest();
        request.setId(bookingResponse.getId());
        request.setNote(viewModel.customerNote.get().trim() + " - " + viewModel.textNote.get());
        viewModel.compositeDisposable.add(viewModel.cancelBooking(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    viewModel.hideLoading();
                    if(response.isResult()){
                        Intent intent = new Intent(TripCancelReasonActivity.this, TripCompleteActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constants.BOOKING_COMPLETE_STATE, false);
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }else{
                        viewModel.showErrorMessage(response.getMessage());
                    }

                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }
}
