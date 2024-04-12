package ww.smartexpress.app.ui.fragment.activity;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.ui.base.fragment.BaseFragmentViewModel;

public class ActivityFragmentViewModel extends BaseFragmentViewModel {

    public MutableLiveData<Integer> orderStatus = new MutableLiveData<>(0);
    public ObservableField<Boolean> isEmpty = new ObservableField<>(false);
    public MutableLiveData<List<BookingResponse>> bookingList = new MutableLiveData<>();
    public ObservableField<Integer> pageNumber = new ObservableField<>(0);
    public ObservableField<Integer> pageSize = new ObservableField<>(10);
    public ObservableField<Integer> pageTotal = new ObservableField<>();
    public ActivityFragmentViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void doBack(){
        application.getCurrentActivity().onBackPressed();
    }

    public void bookCarOrder(){
        orderStatus.setValue(0);
    }
    public void deliveryOrder(){
        orderStatus.setValue(1);
    }
    public void shoppingOrder(){
        orderStatus.setValue(2);
    }

    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getMyBooking() {
        return repository.getApiService().getMyBooking(null, null,  pageNumber.get(),pageSize.get())
                .doOnNext(response -> {
                });
    }

    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getBooking() {
        return repository.getApiService().getMyBooking(null, null,  pageNumber.get(),pageSize.get())
                .doOnNext(response -> {
                });
    }

}
