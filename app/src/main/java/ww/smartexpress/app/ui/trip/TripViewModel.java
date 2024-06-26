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
    public ObservableField<Boolean> isLoading = new ObservableField<>(true);
    public ObservableField<BookingResponse> bookingResponse = new ObservableField<>();

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


    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getMyBooking(Long id) {
        return repository.getApiService().getMyBooking(null, id, null, null)
                .doOnNext(response -> {
                    if(response.isResult()){

                    }
                });
    }

    public void chatDriver(){
        Intent intent = new Intent(application.getCurrentActivity(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.ROOM_ID, bookingResponse.get().getRoom().getId());
        bundle.putLong("BOOKING_ID", bookingResponse.get().getId());
        bundle.putString("BOOKING_CODE", bookingResponse.get().getCode());
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
