package ww.smartexpress.app.ui.bookcar;

import android.content.Intent;
import android.net.Uri;
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
import ww.smartexpress.app.data.model.api.request.CancelBookingRequest;
import ww.smartexpress.app.data.model.api.request.CreateBookingRequest;
import ww.smartexpress.app.data.model.api.response.BookLocation;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.ProfileResponse;
import ww.smartexpress.app.data.model.api.response.ServiceResponse;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.chat.ChatActivity;
import ww.smartexpress.app.ui.coupon.CouponActivity;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.note.NoteActivity;
import ww.smartexpress.app.ui.payment.PaymentActivity;
import ww.smartexpress.app.ui.search.SearchActivity;
import ww.smartexpress.app.ui.trip.TripActivity;

public class BookCarViewModel extends BaseViewModel {
    public ObservableField<String> time = new ObservableField<>("6 phút");
    public ObservableField<String> vehicle = new ObservableField<>("");
    public ObservableField<String> licensePlates = new ObservableField<>("");
    public ObservableField<String> driverName = new ObservableField<>("");
    public ObservableField<String> driverAvatar = new ObservableField<>("");
    public ObservableField<String> image = new ObservableField<>("");
    public ObservableField<Float> rate = new ObservableField<>(4.9f);
    public ObservableField<Boolean> isBooking = new ObservableField<>(false);
    public ObservableField<Boolean> isFound = new ObservableField<>(false);
    public ObservableField<Long> categoryId = new ObservableField<>();
    public ObservableField<Double> originLat = new ObservableField<>();
    public ObservableField<Double> originLng = new ObservableField<>();
    public ObservableField<Double> destinationLat = new ObservableField<>();
    public ObservableField<Double> destinationLng = new ObservableField<>();
    public ObservableField<String> origin = new ObservableField<>("");
    public ObservableField<String> destination = new ObservableField<>("");
    public ObservableField<Long> serviceId = new ObservableField<>();
    public ObservableField<String> originId = new ObservableField<>("");
    public ObservableField<String> destinationId = new ObservableField<>("");
    public ObservableField<String> originLatLng = new ObservableField<>("");
    public ObservableField<String> destinationLatLng = new ObservableField<>("");
    public ObservableField<String> mode = new ObservableField<>("");
    public ObservableField<Long> distance = new ObservableField<>(0L);
    public ObservableField<String> distanceKm = new ObservableField<>("");
    public ObservableField<Long> duration = new ObservableField<>(0L);
    public ObservableField<Boolean> paused = new ObservableField<>(false);
    public ObservableField<BookLocation> bookLocation = new ObservableField<>(new BookLocation());
    public ObservableField<String> customerNote = new ObservableField<>("");
    public ObservableField<BookingResponse> bookingResponse = new ObservableField<>();
    public ObservableField<Boolean> statePickupAccepted = new ObservableField<>(false);
    public ObservableField<Long> roomId = new ObservableField<>(0L);

    public ObservableField<String> customerPos = new ObservableField<>("");
    public ObservableField<String> driverPos = new ObservableField<>("");

    public BookCarViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void selectPayment(){
        Intent intent = new Intent(getApplication().getCurrentActivity(), PaymentActivity.class);
        getApplication().getCurrentActivity().startActivity(intent);
    }
    public void selectDiscountCard(){
        Intent intent = new Intent(getApplication().getCurrentActivity(), CouponActivity.class);
        getApplication().getCurrentActivity().startActivity(intent);
    }
    public void doNote(){
        Intent intent = new Intent(getApplication().getCurrentActivity(), NoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("noteHint", 0);
        bundle.putString(Constants.CUSTOMER_BOOKING_NOTE, customerNote.get());
        intent.putExtras(bundle);
        getApplication().getCurrentActivity().startActivity(intent);
    }
    public void doBookWin(){
    }
    public void back(){
        getApplication().getCurrentActivity().onBackPressed();
    }

    public void cancelFinding(){}

    public void callDriver(){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + bookingResponse.get().getDriver().getPhone()));
        application.getCurrentActivity().startActivity(callIntent);
    }

    public void chatDriver(){
//        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//        sendIntent.setData(Uri.parse("sms:" + bookingResponse.get().getDriver().getPhone()));
//        sendIntent.putExtra("sms_body", "");
//        application.getCurrentActivity().startActivity(sendIntent);
        Intent intent = new Intent(application.getCurrentActivity(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.ROOM_ID, roomId.get());
        intent.putExtras(bundle);
        application.getCurrentActivity().startActivity(intent);
    }

    public void showTripActivity(){
        Intent intent = new Intent(application.getCurrentActivity(), TripActivity.class);
        Bundle bundle = new Bundle();
        //bundle.putString(Constants.CUSTOMER_BOOKING_OBJECT, ApiModelUtils.toJson(bookingResponse.get()));
        bundle.putBoolean("isCanceled", false);
        bundle.putBoolean("isCompleted", false);
        bundle.putLong(Constants.ROOM_ID, roomId.get());
        intent.putExtras(bundle);
        application.getCurrentActivity().startActivity(intent);
        //application.getCurrentActivity().finish();
    }

    public void getNote(){
        String note = repository.getSharedPreferences().getStringVal(Constants.CUSTOMER_BOOKING_NOTE);
        if(note != null){
            customerNote.set(note);
        }
    }

    public void clearNote(){
        repository.getSharedPreferences().removeKey(Constants.CUSTOMER_BOOKING_NOTE);
        customerNote.set("");
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

    Observable<JsonObject> getMapDriverDirection() {
        return repository.getApiService().getMapDirection(driverPos.get(), "driving", customerPos.get(), Constants.GEO_API_KEY)
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

    Observable<ResponseWrapper<BookingResponse>> getCurrentBooking() {
        return repository.getApiService().getCurrentBooking()
                .doOnNext(response -> {
                    if(response.isResult()){
                        if(response.getData().getRoom() != null){
                            repository.getSharedPreferences().setLong(Constants.ROOM_ID, response.getData().getRoom().getId());
                            roomId.set(response.getData().getRoom().getId());
                        }
                    }
                });
    }

    Observable<ResponseWrapper<String>> cancelBooking(CancelBookingRequest request) {
        return repository.getApiService().cancelBooking(request)
                .doOnNext(response -> {

                });
    }
}
