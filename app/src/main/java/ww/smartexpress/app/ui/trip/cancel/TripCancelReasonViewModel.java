package ww.smartexpress.app.ui.trip.cancel;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.CancelBookingRequest;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.trip.complete.TripCompleteActivity;

public class TripCancelReasonViewModel extends BaseViewModel {
    public ObservableField<Boolean> isSelected1 = new ObservableField<>(false);
    public ObservableField<Boolean> isSelected2 = new ObservableField<>(false);
    public ObservableField<String> textNote = new ObservableField<>("");
    public ObservableField<String> customerNote = new ObservableField<>("");
    public ObservableField<String> textRate = new ObservableField<>("");
    public ObservableField<Boolean> onClick = new ObservableField<>(false);
    public TripCancelReasonViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void back(){
        getApplication().getCurrentActivity().onBackPressed();
    }

    public void onAgreeClick(){
        Intent intent = new Intent(getApplication().getCurrentActivity(), TripCompleteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isComplete", false);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getApplication().getCurrentActivity().startActivity(intent);
        getApplication().getCurrentActivity().finish();
    }

    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getCurrentBooking() {
        return repository.getApiService().getCurrentBooking(null)
                .doOnNext(response -> {
                    if(response.isResult()){

                    }
                });
    }

    Observable<ResponseWrapper<String>> cancelBooking(CancelBookingRequest request) {
        return repository.getApiService().cancelBooking(request)
                .doOnNext(response -> {
                    clearDataBooking();
                });
    }

    public void clearDataBooking(){
        repository.getSharedPreferences().removeKey(Constants.CUSTOMER_BOOKING_NOTE);
    }
}
