package ww.smartexpress.app.ui.order.information;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.response.ShippingInfo;
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

    public ObservableField<String> codPrice = new ObservableField<>("");


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
}
