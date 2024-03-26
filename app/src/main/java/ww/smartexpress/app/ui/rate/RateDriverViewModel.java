package ww.smartexpress.app.ui.rate;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.RatingBookingRequest;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.trip.TripActivity;

public class RateDriverViewModel extends BaseViewModel {
    public ObservableField<String> image = new ObservableField<>("");
    public ObservableField<String> note = new ObservableField<>("");
    public ObservableField<String> customerReview = new ObservableField<>("");
    public ObservableField<Float> rating = new ObservableField<>();
    public ObservableField<Long> bookingId = new ObservableField<>(0L);

    public ObservableField<Boolean> onClick = new ObservableField<>(false);

    public RateDriverViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void back(){
        getApplication().getCurrentActivity().onBackPressed();
    }

    public void sendRating(){
        Intent intent = new Intent(application.getCurrentActivity(), HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.BOOKING_CANCEL_STATE, false);
        bundle.putBoolean(Constants.BOOKING_COMPLETE_STATE, true);
        bundle.putLong(Constants.CUSTOMER_BOOKING_DETAIL_ID, bookingId.get());
        intent.putExtras(bundle);
        application.getCurrentActivity().startActivity(intent);
        application.getCurrentActivity().finish();
    }

    Observable<ResponseWrapper<String>> rating(RatingBookingRequest request) {
        return repository.getApiService().ratingBooking(request)
                .doOnNext(response -> {

                });
    }

    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getMyBooking(Long id) {
        return repository.getApiService().getMyBooking(null, id,null, null)
                .doOnNext(response -> {
                    if(response.isResult()){

                    }
                });
    }
}
