package ww.smartexpress.app.ui.fragment.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.ObservableField;

import com.google.gson.JsonObject;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.BookLocation;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.SearchLocation;
import ww.smartexpress.app.data.model.api.response.SearchLocationResponse;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.base.fragment.BaseFragmentViewModel;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.shipping.address.info.ShippingInfoActivity;

public class SearchFragmentViewModel extends BaseFragmentViewModel {
    public ObservableField<String> location = new ObservableField<>("");
    public ObservableField<String> searchLocation = new ObservableField<>("");
    public ObservableField<Long> categoryId = new ObservableField<>(0L);
    public ObservableField<Long> serviceId = new ObservableField<>(0L);
    public ObservableField<String> originId = new ObservableField<>("");
    public ObservableField<String> destinationId = new ObservableField<>("");
    public ObservableField<SearchLocation> origin = new ObservableField<>(null);
    public ObservableField<SearchLocation> destination = new ObservableField<>(new SearchLocation());
    public ObservableField<String> latlng = new ObservableField<>("");

    public ObservableField<BookLocation> bookLocation = new ObservableField<>(new BookLocation());
    public ObservableField<Boolean> hasBooking = new ObservableField<>(false);
    public SearchFragmentViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }


    public void getSavedLocation(){}

    public void getHomeLocation(){}

    public void getCompanyLocation(){}

    public void addNewLocation(){}

    public void deleteLocation(){
        location.set("");
        origin.set(null);
        originId.set("");
    }

    public void deleteSearchLocation(){
        searchLocation.set("");
        destinationId.set("");
    }

    public void back() {
        application.getCurrentActivity().onBackPressed();
    }

    public void getNotifications(){

    }
    public void nextBooking(){
        if(originId.get().equals(destinationId.get())){
            showErrorMessage(application.getString(R.string.same_booking_location));
        }else{
            Intent intent = new Intent(application.getCurrentActivity(), ShippingInfoActivity.class);
            Bundle bundle = new Bundle();
            //bundle.putLong(Constants.KEY_CATEGORY_ID, categoryId.get());
            bundle.putLong(Constants.KEY_SERVICE_ID, serviceId.get());
            bundle.putString(Constants.KEY_ORIGIN_ID, originId.get());
            bundle.putString(Constants.KEY_DESTINATION_ID, destinationId.get());
            bundle.putString(Constants.KEY_ORIGIN_NAME, origin.get() == null ? location.get() : origin.get().getDescription());
            bundle.putString(Constants.KEY_DESTINATION_NAME, destination.get().getDescription());
            intent.putExtras(bundle);
            application.getCurrentActivity().startActivity(intent);
        }
        
    }

    Observable<SearchLocationResponse> searchLocation(String location) {
        return repository.getApiService().searchLocation(location + " Việt Nam", Constants.GEO_API_KEY)
                .doOnNext(response -> {
                    Log.d("TAG", "searchLocation: " + response.toString());
                });
    }

    Observable<ResponseWrapper<BookingResponse>> getCurrentBooking() {
        return repository.getApiService().getCurrentBooking()
                .doOnNext(response -> {
                    if(response.isResult()){
                        hasBooking.set(true);
                    }
                });
    }

    Observable<JsonObject> getLocationInfo() {
        return repository.getApiService().getLocationInfoByLatLng(latlng.get(), Constants.GEO_API_KEY)
                .doOnNext(response -> {

                });
    }
}
