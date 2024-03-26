package ww.smartexpress.app.ui.register;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.RegisterRequest;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class RegisterViewModel extends BaseViewModel {
    public ObservableField<Boolean> visibleBack = new ObservableField<>(true);
    public ObservableField<RegisterRequest> request = new ObservableField<>(new RegisterRequest());
    public ObservableField<String> name = new ObservableField<>("");
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<Boolean> isVisibility = new ObservableField<>(false);
    public RegisterViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    Observable<ResponseWrapper<String>> register(RegisterRequest registerRequest) {
        return repository.getApiService().register(registerRequest)
                .doOnNext(response -> {

                });
    }

    public void doBack(){

    }

    public void setIsVisibilityPassword(){
        isVisibility.set(!isVisibility.get());
    }
}
