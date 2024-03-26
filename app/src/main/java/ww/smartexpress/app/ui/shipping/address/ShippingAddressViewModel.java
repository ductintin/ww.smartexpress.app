package ww.smartexpress.app.ui.shipping.address;

import android.content.Intent;

import androidx.databinding.ObservableField;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.order.information.OrderInformationActivity;
import ww.smartexpress.app.ui.shipping.address.search.SearchAddressActivity;

public class ShippingAddressViewModel extends BaseViewModel {

    public ObservableField<String> location = new ObservableField<>("150/17 Đinh Tiên Hoàng, Phường 26, Quận 3");
    public ObservableField<String> shippingAddress = new ObservableField<>();
    public ShippingAddressViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void getSavedLocation(){}

    public void getHomeLocation(){}

    public void getCompanyLocation(){}

    public void addNewLocation(){}

    public void back() {
        application.getCurrentActivity().onBackPressed();
    }

    public void getNotifications(){

    }
    public void nextDelivery(){
        Intent intent = new Intent(application.getCurrentActivity(), SearchAddressActivity.class);
        intent.putExtra("shippingAddress", shippingAddress.get());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        application.getCurrentActivity().startActivity(intent);
        application.getCurrentActivity().finish();
    }
}
