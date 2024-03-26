package ww.smartexpress.app.ui.search.location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.response.SearchLocationResponse;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.search.SearchActivity;

public class SearchLocationViewModel extends BaseViewModel {

    public ObservableField<String> location = new ObservableField<>("");
    public ObservableField<String> searchLocation = new ObservableField<>("");
    public ObservableField<Long> categoryId = new ObservableField<>(0L);
    public ObservableField<Long> serviceId = new ObservableField<>(0L);
    public ObservableField<String> originId = new ObservableField<>("");
    public ObservableField<String> destinationId = new ObservableField<>("");
    public SearchLocationViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void getSavedLocation(){}

    public void getHomeLocation(){}

    public void getCompanyLocation(){}

    public void addNewLocation(){}

    public void deleteLocation(){
        location.set("");
    }

    public void deleteSearchLocation(){
        searchLocation.set("");
    }

    public void back() {
        application.getCurrentActivity().onBackPressed();
    }

    public void getNotifications(){

    }
    public void nextBooking(){
        Intent intent = new Intent(application.getCurrentActivity(), BookCarActivity.class);
        Bundle bundle = new Bundle();
        //bundle.putLong(Constants.KEY_CATEGORY_ID, categoryId.get());
        bundle.putLong(Constants.KEY_SERVICE_ID, serviceId.get());
        bundle.putString(Constants.KEY_ORIGIN_ID, originId.get());
        bundle.putString(Constants.KEY_DESTINATION_ID, destinationId.get());
        Log.d("TAG", "nextBooking: " + originId.get());
        Log.d("TAG", "nextBooking: " + destinationId.get());
        intent.putExtras(bundle);
        application.getCurrentActivity().startActivity(intent);
    }

    Observable<SearchLocationResponse> searchLocation(String location) {
        return repository.getApiService().searchLocation(location + " Việt Nam", Constants.GEO_API_KEY)
                .doOnNext(response -> {
                    Log.d("TAG", "searchLocation: " + response.toString());
                });
    }

}
