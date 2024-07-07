package ww.smartexpress.app.ui.payout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.helpers.EmptyViewHelper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.request.PayoutRequest;
import ww.smartexpress.app.data.model.api.response.BankCard;
import ww.smartexpress.app.data.model.api.response.PayoutTransaction;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.databinding.ActivityPayoutBinding;
import ww.smartexpress.app.databinding.BaseDialogBinding;
import ww.smartexpress.app.databinding.DialogPayoutRequestBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.dialog.PasswordDialog;
import ww.smartexpress.app.ui.order.adapter.OrderOptionAdapter;
import ww.smartexpress.app.ui.payout.adapter.PayoutRequestAdapter;
import ww.smartexpress.app.ui.view.ProgressItem;
import ww.smartexpress.app.utils.NumberUtils;

public class PayoutActivity extends BaseActivity<ActivityPayoutBinding, PayoutViewModel> implements PasswordDialog.PasswordListener{
    private PayoutRequestAdapter payoutRequestAdapter;

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
        viewModel.balance.set((intent.getDoubleExtra("balance",0L)));

        getMyPayoutRequest();

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
                            if(viewModel.user.get()!=null && viewModel.user.get().getBankCard() != null){
                                viewModel.bankCard.set(ApiModelUtils.fromJson(viewModel.user.get().getBankCard(), BankCard.class));
                            }
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

    public void getMyPayoutRequest(){
        viewModel.showLoading();
        viewModel.compositeDisposable.add(viewModel.getMyPayoutRequest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    viewModel.hideLoading();
                    if(response.isResult() && response.getData().getTotalElements() > 0){

                        viewModel.payoutTransactionList.set(response.getData().getContent());
                        dialogPayoutRequest(response.getData().getContent().get(0));

//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
//                                ,LinearLayoutManager.VERTICAL, false);
//                        viewBinding.rcPayoutRequest.setLayoutManager(layoutManager);
//                        viewBinding.rcPayoutRequest.setItemAnimator(new DefaultItemAnimator());
//                        payoutRequestAdapter = new PayoutRequestAdapter(viewModel.payoutTransactionList.get());
//                        viewBinding.rcPayoutRequest.setAdapter(payoutRequestAdapter);
//
//                        payoutRequestAdapter.setOnItemClickListener(new PayoutRequestAdapter.OnItemClickListener() {
//                            @Override
//                            public void itemClick(PayoutTransaction payoutTransaction) {
//
//                            }
//
//                            @Override
//                            public void delete(PayoutTransaction payoutTransaction) {
//                                deletePayoutRequest(payoutTransaction);
//                            }
//                        });
                    }
                },error->{
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    error.printStackTrace();
                    viewModel.hideLoading();
                })
        );
    }

    public void dialogPayoutRequest(PayoutTransaction payoutTransaction){
        Dialog dialog = new Dialog(PayoutActivity.this);
        DialogPayoutRequestBinding dialogLogoutBinding = DataBindingUtil.inflate(LayoutInflater.from(PayoutActivity.this),R.layout.dialog_payout_request,null, false);
        dialogLogoutBinding.setIvm(payoutTransaction);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(dialogLogoutBinding.getRoot());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCanceledOnTouchOutside(false);

        dialogLogoutBinding.btnLogout.setOnClickListener(view -> {
            dialog.dismiss();
            viewModel.showLoading();
            viewModel.compositeDisposable.add(viewModel.deletePayoutRequest(payoutTransaction.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        viewModel.hideLoading();
                        if(response.isResult()){
                            //viewModel.payoutTransactionList.get().remove(payoutTransaction);
                            viewModel.payoutTransactionList.set(new ArrayList<>());
                            Log.d("TAG", "dialogPayoutRequest: " + viewModel.payoutTransactionList.get().size());
                            viewModel.showSuccessMessage("Xóa yêu cầu rút tiền thành công");
                        }else {
                            viewModel.showErrorMessage(response.getMessage());
                        }
                    },error->{
                        viewModel.showErrorMessage(getString(R.string.network_error));
                        error.printStackTrace();
                        viewModel.hideLoading();
                    })
            );
        });
        dialogLogoutBinding.btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
            onBackPressed();
        });
        dialog.show();
    }

    public void deletePayoutRequest(PayoutTransaction payoutTransaction){
        Dialog dialog = new Dialog(PayoutActivity.this);
        BaseDialogBinding dialogLogoutBinding = DataBindingUtil.inflate(LayoutInflater.from(PayoutActivity.this),R.layout.base_dialog,null, false);
        dialogLogoutBinding.setTitle("Xóa yêu cầu rút tiền?");
        dialogLogoutBinding.setSubtitle("");
        dialogLogoutBinding.setDecision("Xóa");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(dialogLogoutBinding.getRoot());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCanceledOnTouchOutside(false);

        dialogLogoutBinding.btnLogout.setOnClickListener(view -> {
            dialog.dismiss();
            viewModel.showLoading();
            viewModel.compositeDisposable.add(viewModel.deletePayoutRequest(payoutTransaction.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        viewModel.hideLoading();
                        if(response.isResult()){
                            viewModel.payoutTransactionList.get().remove(payoutTransaction);
                            payoutRequestAdapter.notifyDataSetChanged();

                        }else {
                            viewModel.showErrorMessage(response.getMessage());
                        }
                    },error->{
                        viewModel.showErrorMessage(getString(R.string.network_error));
                        error.printStackTrace();
                        viewModel.hideLoading();
                    })
            );
        });
        dialogLogoutBinding.btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void confirm(String password) {
        viewModel.compositeDisposable.add(viewModel.confirmPassword(password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    viewModel.hideLoading();
                    if(response.isResult()){
                        viewModel.doDone();
                    }else {
                        viewModel.showSuccessMessage(response.getCode());
                    }
                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(application.getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    public void navigateToConfirmPassword(){
        if(viewModel.money.get() != null && Integer.valueOf(viewModel.money.get())<50000){
            viewModel.showErrorMessage("Số tiền rút tối thiểu là 50.000đ");
            return;
        }
        if(viewModel.money.get() != null && Double.valueOf(viewModel.money.get()) > viewModel.balance.get()){
            viewModel.showErrorMessage("Số tiền rút vượt quá số dư trong ví");
            return;
        }
        PasswordDialog passwordDialog = new PasswordDialog();
        passwordDialog.show(getSupportFragmentManager(),"passwordDialog");
    }
}
