package ww.smartexpress.app.ui.trip.detail;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import ww.smartexpress.app.databinding.ItemZoomImageBinding;
import ww.smartexpress.app.ui.bank.BankActivity;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.rate.RateDriverActivity;

public class TripDetailViewModel extends BaseViewModel {
    public ObservableField<BookingResponse> bookingResponse = new ObservableField<>(null);
    public ObservableField<Boolean> isLoading = new ObservableField<>(true);
    public TripDetailViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getMyBooking(Long id) {
        return repository.getApiService().getMyBooking(null, id, null, null)
                .doOnNext(response -> {
                    if(response.isResult()){

                    }
                });
    }

    public void zoomImage(int option){
        Dialog dialog = new Dialog(getApplication().getCurrentActivity());
        ItemZoomImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getApplication().getCurrentActivity()), R.layout.item_zoom_image,null, false);
        if(option == 0){
            binding.setUrl(bookingResponse.get().getPickupImage());
        }else{
            binding.setUrl(bookingResponse.get().getDeliveryImage());
        }
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

    public void gotoRating(){
        Intent intent = new Intent(application.getCurrentActivity(), RateDriverActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.CUSTOMER_BOOKING_ID,bookingResponse.get().getId());
        bundle.putInt("FROM",1);
        intent.putExtras(bundle);

        application.getCurrentActivity().startActivity(intent);
    }
}
