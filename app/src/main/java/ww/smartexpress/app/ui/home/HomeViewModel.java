package ww.smartexpress.app.ui.home;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.CreateBookingRequest;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class HomeViewModel extends BaseViewModel {
    public ObservableField<Integer> state = new ObservableField<>();
    public HomeViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getCurrentBooking() {
        return repository.getApiService().getCurrentBooking(null)
                .doOnNext(response -> {
                    if(response.isResult()){
                        state.set(response.getData().getContent().get(0).getState());
                    }
                });
    }
}
