package ww.smartexpress.app.ui.rate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.request.RatingBookingRequest;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.RateOption;
import ww.smartexpress.app.databinding.ActivityRatingDriverBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.rate.adapter.RateOptionAdapter;

public class RateDriverActivity extends BaseActivity<ActivityRatingDriverBinding, RateDriverViewModel> {
    RateOptionAdapter rateOptionAdapter;
    RatingBookingRequest request = new RatingBookingRequest();
    BookingResponse bookingResponse = new BookingResponse();
    List<String> ratingNote = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_rating_driver;
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
        viewBinding.setA(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            viewModel.bookingId.set((bundle.getLong(Constants.CUSTOMER_BOOKING_ID)));
            viewModel.from.set((bundle.getInt("FROM")));
            Log.d("TAG", "onCreate: " + bundle.getLong(Constants.CUSTOMER_BOOKING_ID));
            Log.d("TAG", "onCreate: " + viewModel.bookingId.get());
            getMyBooking(viewModel.bookingId.get());
        }
        viewBinding.ratingBarDriver.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                viewModel.rating.set(v);
            }
        });


        loadRateOption();

    }

    public void loadRateOption(){
        List<RateOption> rateOptionList = new ArrayList<>();
        rateOptionList.add(new RateOption("1", "Nhanh chóng", false));
        rateOptionList.add(new RateOption("1", "Cẩn thận", false));
        rateOptionList.add(new RateOption("1", "Thuận tiện", false));

        viewBinding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.HORIZONTAL, false);
        viewBinding.rcRateOption.setLayoutManager(layoutManager);
        viewBinding.rcRateOption.setItemAnimator(new DefaultItemAnimator());
        rateOptionAdapter = new RateOptionAdapter(rateOptionList);
        viewBinding.rcRateOption.setAdapter(rateOptionAdapter);

        rateOptionAdapter.setOnItemClickListener(pos -> {
            viewModel.onClick.set(rateOptionAdapter.handleClick(pos));
            if(ratingNote.contains(rateOptionList.get(pos).getName())){
                ratingNote.remove(rateOptionList.get(pos).getName());
            }else{
                ratingNote.add(rateOptionList.get(pos).getName());
            }
        });

    }
    @Override
    public void onBackPressed() {
        if(viewModel.from != null && viewModel.from.get() == 1){
            super.onBackPressed();
        }else{
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    public void prepareRequest(){
        String note = ratingNote.stream().collect(Collectors.joining(", "));
        if(!TextUtils.isEmpty(viewModel.customerReview.get())){
            request.setMessage(note + ", " + viewModel.customerReview.get().trim());
        }else{
            request.setMessage(note);
        }
        request.setStar(viewModel.rating.get().intValue());
        request.setBookingId(viewModel.bookingId.get());
    }

    public void createRating(){
        prepareRequest();
        viewModel.showLoading();
        viewModel.compositeDisposable.add(viewModel.rating(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    viewModel.hideLoading();
                    if(response.isResult()){
                        viewModel.sendRating();
                        viewModel.showSuccessMessage(response.getMessage());
                    }else{
                        viewModel.getApplication().getErrorUtils().handelError(response.getCode());
                    }

                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
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
                        viewModel.image.set(bookingResponse.getDriver().getAvatar());
                    }else{
                        viewModel.getApplication().getErrorUtils().handelError(response.getCode());
                        Intent intent = new Intent(RateDriverActivity.this, HomeActivity.class);
                        startActivity(intent);
                        this.finish();
                    }
                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }

}
