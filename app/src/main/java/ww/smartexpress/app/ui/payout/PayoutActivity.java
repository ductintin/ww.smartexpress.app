package ww.smartexpress.app.ui.payout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import ww.smartexpress.app.databinding.ActivityPayoutBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.utils.NumberUtils;

public class PayoutActivity extends BaseActivity<ActivityPayoutBinding, PayoutViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_payout;
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
        Intent intent = getIntent();
        viewModel.balance.set(intent.getIntExtra("balance",0));

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
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
        }
        viewBinding.edtMoney.addTextChangedListener(new TextWatcher() {
            private String current = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    if (!charSequence.toString().equals(current)) {

                        String cleanString = charSequence.toString().replaceAll("[.]", "");
                        viewModel.money.set(cleanString);
                        Log.d("TAG", "onTextChanged: "+ viewModel.money.get());
                        double parsed = Double.parseDouble(cleanString);

                        String formated = NumberUtils.formatEdtTextCurrency(parsed);

                        current = formated;

                        viewBinding.edtMoney.setText(formated);
                        viewBinding.edtMoney.setSelection(formated.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void clickMoney(String money){
        viewModel.money.set(money);
        viewBinding.edtMoney.setText(money);
    }
}
