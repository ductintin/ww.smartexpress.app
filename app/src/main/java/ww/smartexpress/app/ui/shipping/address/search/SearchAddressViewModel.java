package ww.smartexpress.app.ui.shipping.address.search;

import android.content.Intent;

import androidx.databinding.ObservableField;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.order.information.OrderInformationActivity;
import ww.smartexpress.app.ui.shipping.address.ShippingAddressActivity;

public class SearchAddressViewModel extends BaseViewModel {

    public ObservableField<String> location = new ObservableField<>("");
    public ObservableField<String> searchLocation = new ObservableField<>("");
    public SearchAddressViewModel(Repository repository, MVVMApplication application) {
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
    public void searchAddress(){
        Intent intent = new Intent(getApplication().getCurrentActivity(), ShippingAddressActivity.class);
        getApplication().getCurrentActivity().startActivity(intent);
    }

    public void nextOrderInformation(){
        Intent intent = new Intent(getApplication().getCurrentActivity(), OrderInformationActivity.class);
        getApplication().getCurrentActivity().startActivity(intent);
    }
}
