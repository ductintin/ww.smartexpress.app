package ww.smartexpress.app.ui.trip.cancel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.request.CancelBookingRequest;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.CancelReason;
import ww.smartexpress.app.data.model.api.response.RateOption;
import ww.smartexpress.app.databinding.ActivityTripCancelReasonBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.rate.adapter.RateOptionAdapter;
import ww.smartexpress.app.ui.trip.adapter.CancelReasonAdapter;
import ww.smartexpress.app.ui.trip.complete.TripCompleteActivity;

public class TripCancelReasonActivity extends BaseActivity<ActivityTripCancelReasonBinding, TripCancelReasonViewModel> {
    private CancelReasonAdapter cancelReasonAdapter1;
    private CancelReasonAdapter cancelReasonAdapter2;

    List<String> cancelReason = new ArrayList<>();
    RateOptionAdapter cancelOptionAdapter;
    Long id;
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
        id = getIntent().getLongExtra("BOOKING_ID", 0L);
        if(id != 0L){
            load();
        }else{
            viewModel.showErrorMessage("Không tìm thấy đơn hàng!");
            Intent intent = new Intent(TripCancelReasonActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        //getCurrentBooking();
        //load();
        loadCancelOption();
    }

    public void loadCancelOption(){
        List<RateOption> rateOptionList = new ArrayList<>();
        rateOptionList.add(new RateOption("1", "Có việc", false));
        rateOptionList.add(new RateOption("2", "Tìm được dịch vụ khác", false));
        rateOptionList.add(new RateOption("3", "Chi phí đắt", false));
        rateOptionList.add(new RateOption("4", "Lý do khách quan", false));

        viewBinding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.HORIZONTAL, false);
        viewBinding.rcRateOption.setLayoutManager(layoutManager);
        viewBinding.rcRateOption.setItemAnimator(new DefaultItemAnimator());
        cancelOptionAdapter = new RateOptionAdapter(rateOptionList);
        viewBinding.rcRateOption.setAdapter(cancelOptionAdapter);

        cancelOptionAdapter.setOnItemClickListener(pos -> {
            viewModel.onClick.set(cancelOptionAdapter.handleClick(pos));
            if(cancelReason.contains(rateOptionList.get(pos).getName())){
                cancelReason.remove(rateOptionList.get(pos).getName());
            }else{
                cancelReason.add(rateOptionList.get(pos).getName());
            }
        });

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



    public void cancelTrip(){
        CancelBookingRequest request = new CancelBookingRequest();
        request.setId(id);
        String note = cancelReason.stream().collect(Collectors.joining(", "));
        if(!TextUtils.isEmpty(viewModel.customerNote.get())){
            request.setNote(note + ", " + viewModel.customerNote.get().trim());
        }else{
            request.setNote(note);
        }
        //request.setNote(viewModel.customerNote.get().trim() + " - " + viewModel.textNote.get());
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
                        viewModel.getApplication().getErrorUtils().handelError(response.getCode());
                    }

                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }
}
