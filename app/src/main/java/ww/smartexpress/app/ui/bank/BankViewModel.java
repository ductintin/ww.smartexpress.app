package ww.smartexpress.app.ui.bank;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.request.ConfirmAccountNumberRequest;
import ww.smartexpress.app.data.model.api.request.UpdateProfileRequest;
import ww.smartexpress.app.data.model.api.response.AccountName;
import ww.smartexpress.app.data.model.api.response.BankCard;
import ww.smartexpress.app.data.model.api.response.BankResponse;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.databinding.DialogBankBinding;
import ww.smartexpress.app.ui.bank.adapter.BankAdapter;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class BankViewModel extends BaseViewModel {

    public ObservableField<AccountName> accountName = new ObservableField<>();
    public ObservableField<String> accountNumber = new ObservableField<>();
    public ObservableField<String> brand = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>();
    public ObservableField<String> bankSearch = new ObservableField<>("");
    public ObservableField<Boolean> isVisibility = new ObservableField<>(false);
    public ObservableField<List<BankResponse>> bankResponseList = new ObservableField<>();
    public ObservableField<BankResponse> bank = new ObservableField<>();

    public ObservableField<UserEntity> user = new ObservableField<>();

    Dialog dialog;

    public BankViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public Repository getRepository(){
        return repository;
    }
    public void setIsVisibilityPassword(){
        isVisibility.set(!isVisibility.get());
    }

    public void back(){
        application.getCurrentActivity().finish();
    }

    public void bankDialog(){
        dialog = new Dialog(application.getCurrentActivity());
        DialogBankBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(application.getCurrentActivity()), R.layout.dialog_bank,null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(mBinding.getRoot());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCanceledOnTouchOutside(true);

        BankAdapter bankAdapter = new BankAdapter(application.getCurrentActivity());
        bankAdapter.setBankResponseList(bankResponseList.get());
        mBinding.rcBank.setLayoutManager(new LinearLayoutManager(application.getCurrentActivity(), LinearLayoutManager.VERTICAL, false));
        mBinding.rcBank.setAdapter(bankAdapter);

        bankAdapter.setOnItemClickListener(new BankAdapter.OnItemClickListener() {
            @Override
            public void itemClick(BankResponse bankResponse) {
                if(bank.get() != null && !bank.get().equals(bankResponse)){
                    accountNumber.set(null);
                    accountName.set(null);
                    brand.set(null);
                }
                bank.set(bankResponse);
                dialog.dismiss();
            }
        });

        mBinding.edtSearchFood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterListener(bankAdapter,mBinding.edtSearchFood.getText().toString());
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterListener(bankAdapter,mBinding.edtSearchFood.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterListener(bankAdapter,mBinding.edtSearchFood.getText().toString());
            }
        });
    }

    private void filterListener(BankAdapter bankAdapter, String text){
        if (text.isEmpty()) {
            bankAdapter.setBankResponseList(bankResponseList.get());
        } else {
            List<BankResponse> bankResponses = new ArrayList<>();
            String textS = text.toLowerCase();
            for (BankResponse bankResponse : bankResponseList.get()) {
                if (bankResponse.getShort_name().toLowerCase().contains(textS)) {
                    bankResponses.add(bankResponse);
                }
            }
            bankAdapter.setBankResponseList(bankResponses);

        }
    }

    public void deleteTextSearch(){
        bankSearch.set("");
    }



    public void getBankList(){
        showLoading();
        compositeDisposable.add(repository.getApiService().getBankList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    bankResponseList.set(response.getData());
                    //bị lỗi long và
                    Long userId = repository.getSharedPreferences().getLongVal(Constants.KEY_USER_ID);
                    if(userId != 0L){
                        if(user.get().getBankCard() != null){
                            BankCard bankCard = ApiModelUtils.fromJson(user.get().getBankCard(), BankCard.class);
                            for (BankResponse bank: response.getData()) {
                                if(bank.getShort_name().equals(bankCard.getBankName())){
                                    accountName.set(new AccountName(bankCard.getAccountName()));
                                    accountNumber.set(bankCard.getAccountNumber());
                                    brand.set(bankCard.getBranch());
                                    this.bank.set(bank);
                                    break;
                                }
                            }
                        }
                    }
                    bankDialog();
                    hideLoading();
                },error->{
                    hideLoading();
                    showErrorMessage(application.getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    public void getAccountName(){
        if(accountNumber.get() == null || accountNumber.get().isEmpty()){
            return;
        }
        showLoading();
        compositeDisposable.add(repository.getApiService().accountLookup(new ConfirmAccountNumberRequest(bank.get().getBin(),accountNumber.get()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (Objects.equals(response.getCode(), "00")){
                        accountName.set(response.getData());
                    }else {
                        showErrorMessage(response.getDesc());
                    }
                    hideLoading();
                },error->{
                    hideLoading();
                    showErrorMessage(application.getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    @SuppressLint("CheckResult")
    public void updateBankAccount(){
        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.setAvatar(user.get().getAvatar());
        updateProfileRequest.setName(user.get().getName());
        updateProfileRequest.setOldPassword(password.get());
        updateProfileRequest.setNewPassword(password.get());

        updateProfileRequest.setBankCard(ApiModelUtils.toJson(new BankCard(bank.get().getShort_name(),accountNumber.get(), accountName.get().getAccountName(),brand.get())));
        showLoading();
        compositeDisposable.add(repository.getApiService().updateProfile(updateProfileRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res->{
                            if(res.isResult()) {
                                user.get().setBankCard(ApiModelUtils.toJson(new BankCard(bank.get().getShort_name(),accountNumber.get(), accountName.get().getAccountName(),brand.get())));
                                repository.getRoomService().userDao().updateBankCard(user.get().getId(), user.get().getBankCard())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(()-> {

                                        }, throwable -> {

                                        });
                                application.getCurrentActivity().finish();
                                showSuccessMessage("Cập nhật tài khoản ngân hàng thành công");
                            }else {
                                getApplication().getErrorUtils().handelError(res.getCode());
                            }
                            hideLoading();
                        },err-> {
                            err.printStackTrace();
                            hideLoading();
                        }
                ));
    }

    public void showBankDialog(){
        if(dialog!=null){
            dialog.show();
        }
    }
}
