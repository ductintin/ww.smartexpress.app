package ww.smartexpress.app.ui.delivery.order;

import androidx.databinding.ObservableField;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class DeliveryViewModel extends BaseViewModel {

    public ObservableField<Boolean> isCanceled = new ObservableField<>(false);
    public ObservableField<Boolean> isCompleted = new ObservableField<>(false);
    public DeliveryViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void openChat(){

    }
    public void callDriver(){

    }

    public void back(){
        application.getCurrentActivity().onBackPressed();
    }
}
