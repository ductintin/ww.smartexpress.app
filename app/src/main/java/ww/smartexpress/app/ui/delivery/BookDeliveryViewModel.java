package ww.smartexpress.app.ui.delivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.ObservableField;

import com.google.gson.JsonObject;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.CreateBookingRequest;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.ServicePromotion;
import ww.smartexpress.app.data.model.api.response.ServiceResponse;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.chat.ChatActivity;
import ww.smartexpress.app.ui.coupon.CouponActivity;
import ww.smartexpress.app.ui.payment.PaymentActivity;
import ww.smartexpress.app.ui.trip.TripActivity;

public class BookDeliveryViewModel extends BaseViewModel {
    public ObservableField<Boolean> isLoading = new ObservableField<>(true);
    public ObservableField<Long> bookingId = new ObservableField<>(0L);
    public ObservableField<String> bookingCode = new ObservableField<>("");

    public ObservableField<String> location = new ObservableField<>("150/17 Đinh Tiên Hoàng, Phường 26, Quận 3");
    public ObservableField<Double> discount = new ObservableField<>();
    public ObservableField<String> deliveryMethod = new ObservableField<>();
    public ObservableField<String> vehicle = new ObservableField<>("Honda SH Mode");
    public ObservableField<String> licensePlates = new ObservableField<>("59-S2 57301");
    public ObservableField<String> driverName = new ObservableField<>("Lý Tiểu Long");
    public ObservableField<String> image = new ObservableField<>("");
    public ObservableField<Float> rate = new ObservableField<>(4.9f);
    public ObservableField<Boolean> isChecked = new ObservableField<>(false);
    public ObservableField<Boolean> isBooking = new ObservableField<>(false);
    public ObservableField<Boolean> isFound = new ObservableField<>(false);
    public ObservableField<Boolean> isShipping = new ObservableField<>(false);

    public ObservableField<Boolean> selectCOD = new ObservableField<>(false);
    public ObservableField<Double> originLat = new ObservableField<>();
    public ObservableField<Double> originLng = new ObservableField<>();
    public ObservableField<Double> destinationLat = new ObservableField<>();
    public ObservableField<Double> destinationLng = new ObservableField<>();

    public ObservableField<String> origin = new ObservableField<>("");
    public ObservableField<String> originId = new ObservableField<>("");
    public ObservableField<String> originLatLng = new ObservableField<>("");
    public ObservableField<String> destination = new ObservableField<>("");
    public ObservableField<String> destinationId = new ObservableField<>("");
    public ObservableField<String> destinationLatLng = new ObservableField<>("");
    public ObservableField<String> driverLatLng = new ObservableField<>("");
    public ObservableField<String> consigneeName = new ObservableField<>("");
    public ObservableField<String> consigneePhone = new ObservableField<>("");
    public ObservableField<String> senderName = new ObservableField<>("");
    public ObservableField<String> senderPhone = new ObservableField<>("");
    public ObservableField<String> customerNote = new ObservableField<>("");
    public ObservableField<Boolean> isCod = new ObservableField<>(false);
    public ObservableField<Integer> codPrice = new ObservableField<>(0);

    public ObservableField<Long> distance = new ObservableField<>(0L);
    public ObservableField<String> distanceKm = new ObservableField<>("");

    public ObservableField<String> time = new ObservableField<>("6 phút");
    public ObservableField<Long> timeValue = new ObservableField<>(0L);

    public ObservableField<String> driverAvatar = new ObservableField<>("");

    public ObservableField<BookingResponse> bookingResponse = new ObservableField<>();

    public ObservableField<Long> roomId = new ObservableField<>(0L);

    public ObservableField<ServicePromotion> selectedService = new ObservableField<>(null);
    public ObservableField<Integer> selectedServiceIndex = new ObservableField<>(0);
    public ObservableField<Integer> paymentKind = new ObservableField<>(1);
    public ObservableField<CreateBookingRequest> bookingRequest = new ObservableField<>(new CreateBookingRequest());

    public BookDeliveryViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void back(){
        getApplication().getCurrentActivity().onBackPressed();
    }
    public void deleteDestination(){}
    public void selectPayment(){
        Intent intent = new Intent(getApplication().getCurrentActivity(), PaymentActivity.class);
        intent.putExtra("BOOKING_PRICE", bookingRequest.get().getMoney());
        getApplication().getCurrentActivity().startActivity(intent);
    }
    public void selectDiscountCard(){
        Intent intent = new Intent(getApplication().getCurrentActivity(), CouponActivity.class);
        intent.putExtra("SERVICE", ApiModelUtils.toJson(selectedService.get()));
        getApplication().getCurrentActivity().startActivity(intent);
    }
    public void timeSetUp(){}
    public void setIsChecked(){
        isChecked.set(!isChecked.get());
    }
    public void doBookWinDelivery(){}
    public void cancelFinding(){}
    public void showDeliveryDetail(){
        Intent intent = new Intent(application.getCurrentActivity(), TripActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.CUSTOMER_BOOKING_DETAIL_ID, bookingId.get());
        intent.putExtras(bundle);
        application.getCurrentActivity().startActivity(intent);
    }
    public void callDriver(){}
    public void chatDriver(){
        Intent intent = new Intent(application.getCurrentActivity(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.ROOM_ID, roomId.get());
        bundle.putLong("BOOKING_ID", bookingId.get());
        bundle.putString("BOOKING_CODE", bookingCode.get());
        intent.putExtras(bundle);
        application.getCurrentActivity().startActivity(intent);
    }

    Observable<ResponseWrapper<ResponseListObj<ServiceResponse>>> getService() {
        return repository.getApiService().getServiceAutoComplete(null, null, null, null, null)
                .doOnNext(response -> {

                });
    }

    Observable<JsonObject> getLocationInfo(String id) {
        return repository.getApiService().getLocationInfo(id, Constants.GEO_API_KEY)
                .doOnNext(response -> {

                });
    }

    Observable<JsonObject> getMapDirection() {
        return repository.getApiService().getMapDirection(destinationLatLng.get(), "driving", originLatLng.get(), Constants.GEO_API_KEY)
                .doOnNext(response -> {

                });
    }

    Observable<JsonObject> getMapDriverDirection(String latLng) {
        return repository.getApiService().getMapDirection(driverLatLng.get(), "driving", latLng, Constants.GEO_API_KEY)
                .doOnNext(response -> {

                });
    }

    Observable<JsonObject> getDistance() {
        return repository.getApiService().getDistance(originLatLng.get(),  destinationLatLng.get(), Constants.GEO_API_KEY)
                .doOnNext(response -> {

                });
    }

    Observable<ResponseWrapper<BookingResponse>> createBooking(CreateBookingRequest request) {
        return repository.getApiService().createBooking(request)
                .doOnNext(response -> {

                });
    }

    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getCurrentBooking() {
        return repository.getApiService().getCurrentBooking(bookingCode.get())
                .doOnNext(response -> {
                    if(response.isResult() && response.getData().getTotalElements() > 0){
                        if(response.getData().getContent().get(0).getRoom() != null){
                            repository.getSharedPreferences().setLong(Constants.ROOM_ID, response.getData().getContent().get(0).getRoom().getId());

                        }
                    }
                });
    }

}
