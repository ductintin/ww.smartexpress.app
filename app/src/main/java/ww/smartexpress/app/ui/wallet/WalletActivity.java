package ww.smartexpress.app.ui.wallet;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.response.BankCard;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.databinding.ActivityWalletBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class WalletActivity extends BaseActivity<ActivityWalletBinding, WalletViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userId = String.valueOf(viewModel.getRepository().getSharedPreferences().getLongVal(Constants.KEY_USER_ID));
        if(userId != null){
            viewModel.getRepository().getRoomService().userDao().findById(Long.valueOf(userId)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<UserEntity>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull UserEntity userEntity) {
                            viewModel.user.set(userEntity);
                            Log.d("TAG", "onSuccess: " + userEntity);
                            if(viewModel.user.get()!=null && viewModel.user.get().getBankCard() != null){
                                viewModel.bankCard.set(ApiModelUtils.fromJson(viewModel.user.get().getBankCard(), BankCard.class));
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getMyWallet();
    }

}
