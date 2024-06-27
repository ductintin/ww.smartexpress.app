package ww.smartexpress.app.ui.trip.detail;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.databinding.ItemZoomImageBinding;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

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
}
