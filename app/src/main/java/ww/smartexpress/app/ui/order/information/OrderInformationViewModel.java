package ww.smartexpress.app.ui.order.information;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;


import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.delivery.BookDeliveryActivity;

public class OrderInformationViewModel extends BaseViewModel {
    public MutableLiveData<Integer> bonusService = new MutableLiveData<>(0);

    public MutableLiveData<Integer> itemSize = new MutableLiveData<>(0);
    public OrderInformationViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void doBack(){
        getApplication().getCurrentActivity().onBackPressed();
    }

    public void selectCOD(){
        bonusService.setValue(0);
    }

    public void selectHandDelivery(){
        bonusService.setValue(1);
    }
    public void doNext(){
        Intent intent = new Intent(getApplication().getCurrentActivity(), BookDeliveryActivity.class);
        getApplication().getCurrentActivity().startActivity(intent);
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
