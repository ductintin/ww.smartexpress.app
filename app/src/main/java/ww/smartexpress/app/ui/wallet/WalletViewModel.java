package ww.smartexpress.app.ui.wallet;

import androidx.databinding.ObservableField;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class WalletViewModel extends BaseViewModel {
    public WalletViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
