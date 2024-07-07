package ww.smartexpress.app.ui.trip.complete;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.ObservableField;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.rate.RateDriverActivity;

public class TripCompleteViewModel extends BaseViewModel {
    public ObservableField<Boolean> isComplete = new ObservableField<>(true);
    public ObservableField<Long> bookingId = new ObservableField<>(0L);
    public TripCompleteViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void onAgreeClick(){
        Intent intent;
        if(isComplete.get()){
            intent = new Intent(getApplication().getCurrentActivity(), RateDriverActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.CUSTOMER_BOOKING_ID, bookingId.get());
            intent.putExtras(bundle);
        }else{
            intent = new Intent(getApplication().getCurrentActivity(), HomeActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getApplication().getCurrentActivity().startActivity(intent);
        getApplication().getCurrentActivity().finish();
    }

}
