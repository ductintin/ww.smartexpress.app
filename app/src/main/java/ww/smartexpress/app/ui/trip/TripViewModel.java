package ww.smartexpress.app.ui.trip;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.SearchLocation;
import ww.smartexpress.app.databinding.ItemZoomImageBinding;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.chat.ChatActivity;
import ww.smartexpress.app.ui.search.location.SearchLocationActivity;

public class TripViewModel extends BaseViewModel {
    public ObservableField<Boolean> isCanceled = new ObservableField<>(false);
    public ObservableField<Boolean> isCompleted = new ObservableField<>(false);
    public ObservableField<BookingResponse> bookingResponse = new ObservableField<>();
    public ObservableField<String> driverName = new ObservableField<>("");
    public ObservableField<String> driverAvatar = new ObservableField<>("");
    public ObservableField<String> driverLicense = new ObservableField<>("");
    public ObservableField<String> driverVehicle = new ObservableField<>("");
    public ObservableField<Integer> driverRate = new ObservableField<>(0);
    public ObservableField<Float> rating = new ObservableField<>(0.0f);
    public ObservableField<String> ratingMsg = new ObservableField<>("");
    public ObservableField<Float> driverAvgRate = new ObservableField<>(0.0f);
    public ObservableField<Double> money = new ObservableField<>(0.0);
    public ObservableField<String> createdDate = new ObservableField<>();
    public ObservableField<Double> distance = new ObservableField<>(0.0);
    public ObservableField<String> origin = new ObservableField<>("");
    public ObservableField<String> destination = new ObservableField<>("");
    public ObservableField<String> senderName = new ObservableField<>("");
    public ObservableField<String> senderPhone = new ObservableField<>("");
    public ObservableField<String> consigneeName = new ObservableField<>("");
    public ObservableField<String> consigneePhone = new ObservableField<>("");
    public ObservableField<String> customerNote = new ObservableField<>("");
    public ObservableField<Boolean> isCod = new ObservableField<>(false);
    public ObservableField<Double> codPrice = new ObservableField<>(0.0);
    public ObservableField<String> code = new ObservableField<>("");
    public ObservableField<String> pickupImage = new ObservableField<>("");
    public ObservableField<String> deliveryImage = new ObservableField<>("");
    public ObservableField<Long> bookingId = new ObservableField<>(0L);
    public ObservableField<Long> roomId = new ObservableField<>(0L);
    public ObservableField<Integer> bookingState = new ObservableField<>(-1);
    public TripViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void openChat(){

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + bookingResponse.get().getDriver().getPhone()));
        sendIntent.putExtra("sms_body", "");
        application.getCurrentActivity().startActivity(sendIntent);
    }
    public void callDriver(){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + bookingResponse.get().getDriver().getPhone()));
        application.getCurrentActivity().startActivity(callIntent);
    }

    public void back(){
        application.getCurrentActivity().onBackPressed();
    }

    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getCurrentBooking() {
        return repository.getApiService().getCurrentBooking(null)
                .doOnNext(response -> {
                    if(response.isResult()){
                        bookingResponse.set(response.getData().getContent().get(0));
                        bookingState.set(bookingResponse.get().getState());
                        driverName.set(bookingResponse.get().getDriver().getFullName());
                        money.set(bookingResponse.get().getMoney());
                        distance.set(bookingResponse.get().getDistance());
                        origin.set(bookingResponse.get().getPickupAddress());
                        destination.set(bookingResponse.get().getDestinationAddress());
                        createdDate.set(bookingResponse.get().getCreatedDate());
                        code.set(bookingResponse.get().getCode());
                        if(response.getData().getContent().get(0).getDriver() != null){
                            driverAvatar.set(response.getData().getContent().get(0).getDriver().getAvatar());
                        }

                        isCod.set(bookingResponse.get().getIsCod());
                        codPrice.set(bookingResponse.get().getCodPrice());
                        senderName.set(bookingResponse.get().getSenderName());
                        senderPhone.set(bookingResponse.get().getSenderPhone());
                        consigneeName.set(bookingResponse.get().getConsigneeName());
                        consigneePhone.set(bookingResponse.get().getConsigneePhone());
                        pickupImage.set(bookingResponse.get().getPickupImage());
                        deliveryImage.set(bookingResponse.get().getDeliveryImage());
                    }
                });
    }

    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getMyBooking(Long id) {
        return repository.getApiService().getMyBooking(null, id, null, null)
                .doOnNext(response -> {
                    if(response.isResult()){

                    }
                });
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

    public void zoomImage(String url){
        Dialog dialog = new Dialog(getApplication().getCurrentActivity());
        ItemZoomImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getApplication().getCurrentActivity()), R.layout.item_zoom_image,null, false);
        binding.setUrl(url);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(binding.getRoot());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }
}
