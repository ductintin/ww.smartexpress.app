package ww.smartexpress.app.ui.coupon;

import androidx.databinding.ObservableField;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.Promotion;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class CouponViewModel extends BaseViewModel {
    public ObservableField<Integer> page = new ObservableField<>(0);
    public ObservableField<Integer> size = new ObservableField<>(10);
    public ObservableField<Integer> totalPage = new ObservableField<>(0);
    public ObservableField<Boolean> isLoading = new ObservableField<>(true);
    public CouponViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void back() {
        application.getCurrentActivity().onBackPressed();
    }

    Observable<ResponseWrapper<ResponseListObj<Promotion>>> getPromotion() {
        return repository.getApiService().getPromotions(page.get(), size.get())
                .doOnNext(response -> {

                });
    }
}
