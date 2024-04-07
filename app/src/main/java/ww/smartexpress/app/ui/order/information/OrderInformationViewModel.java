package ww.smartexpress.app.ui.order.information;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.ProfileResponse;
import ww.smartexpress.app.data.model.api.response.ShippingInfo;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.delivery.BookDeliveryActivity;

public class OrderInformationViewModel extends BaseViewModel {
    public MutableLiveData<Integer> bonusService = new MutableLiveData<>(0);

    public MutableLiveData<Integer> itemSize = new MutableLiveData<>(0);
    public ObservableField<Integer> size = new ObservableField<>(0);
    public ObservableField<List<Bitmap>> bitmaps= new ObservableField<>(new ArrayList<>());

    public ObservableField<Boolean> selectCOD = new ObservableField<>(false);

    public ObservableField<String> origin = new ObservableField<>("");
    public ObservableField<String> destination = new ObservableField<>("");
    public ObservableField<String> consigneeName = new ObservableField<>("");
    public ObservableField<String> consigneePhone = new ObservableField<>("");
    public ObservableField<String> senderName = new ObservableField<>("");
    public ObservableField<String> senderPhone = new ObservableField<>("");
    public ObservableField<String> customerNote = new ObservableField<>("");
    public ObservableField<String> codPriceText = new ObservableField<>("");
    public ObservableField<Integer> codPrice = new ObservableField<>(0);


    public OrderInformationViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void doBack(){
        getApplication().getCurrentActivity().onBackPressed();
    }

    public void selectCOD(){
        bonusService.setValue(0);
        selectCOD.set(!selectCOD.get());
    }


    public void selectHandDelivery(){
        bonusService.setValue(1);
    }

    public void smallSize(){
        itemSize.setValue(1);
    }
    public void mediumSize(){
        itemSize.setValue(2);
    }
    public void bigSize(){
        itemSize.setValue(3);
    }

    Observable<ResponseWrapper<ProfileResponse>> getProfile() {
        return repository.getApiService().getProfile()
                .doOnNext(response -> {

                });
    }
}
