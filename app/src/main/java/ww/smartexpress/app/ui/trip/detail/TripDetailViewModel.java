package ww.smartexpress.app.ui.trip.detail;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class TripDetailViewModel extends BaseViewModel {
    public ObservableField<BookingResponse> bookingResponse = new ObservableField<>(null);
    public ObservableField<Boolean> isLoading = new ObservableField<>(true);
    public TripDetailViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getMyBooking(Long id) {
        return repository.getApiService().getMyBooking(null, id, null, null)
                .doOnNext(response -> {
                    if(response.isResult()){

                    }
                });
    }
}
