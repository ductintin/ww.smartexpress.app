package ww.smartexpress.app.ui.qrcode;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class QrcodeViewModel extends BaseViewModel {
    public QrcodeViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void back(){
        application.getCurrentActivity().finish();
    }
}
