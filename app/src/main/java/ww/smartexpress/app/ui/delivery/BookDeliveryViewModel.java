package ww.smartexpress.app.ui.delivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.ObservableField;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.trip.TripActivity;

public class BookDeliveryViewModel extends BaseViewModel {
    public ObservableField<String> location = new ObservableField<>("150/17 Đinh Tiên Hoàng, Phường 26, Quận 3");
    public ObservableField<String> destination = new ObservableField<>("Masteri Thảo Điền - T5");
    public ObservableField<Integer> discount = new ObservableField<>();
    public ObservableField<String> deliveryMethod = new ObservableField<>();
    public ObservableField<String> vehicle = new ObservableField<>("Honda SH Mode");
    public ObservableField<String> licensePlates = new ObservableField<>("59-S2 57301");
    public ObservableField<String> driverName = new ObservableField<>("Lý Tiểu Long");
    public ObservableField<String> image = new ObservableField<>("");
    public ObservableField<Float> rate = new ObservableField<>(4.9f);
    public ObservableField<Boolean> isChecked = new ObservableField<>(false);
    public ObservableField<Boolean> isBooking = new ObservableField<>(false);
    public ObservableField<Boolean> isFound = new ObservableField<>(false);
    public BookDeliveryViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void back(){
        getApplication().getCurrentActivity().onBackPressed();
    }
    public void deleteDestination(){}
    public void selectPayment(){}
    public void selectDiscountCard(){}
    public void timeSetUp(){}
    public void setIsChecked(){
        isChecked.set(!isChecked.get());
    }
    public void doBookWinDelivery(){}
    public void cancelFinding(){}
    public void showDeliveryDetail(){
        Intent intent = new Intent(application.getCurrentActivity(), TripActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isCanceled", false);
        bundle.putBoolean("isCompleted", false);
        intent.putExtras(bundle);
        application.getCurrentActivity().startActivity(intent);
        application.getCurrentActivity().finish();
    }
    public void callDriver(){}
    public void chatDriver(){}
}
