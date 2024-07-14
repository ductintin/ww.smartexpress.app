package ww.smartexpress.app.ui.coupon;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.response.ApplyPromotion;
import ww.smartexpress.app.data.model.api.response.Coupon;
import ww.smartexpress.app.data.model.api.response.Promotion;
import ww.smartexpress.app.data.model.api.response.ServicePromotion;
import ww.smartexpress.app.databinding.ActivityCouponBinding;
import ww.smartexpress.app.databinding.BaseDialogBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.coupon.adapter.CouponAdapter;
import ww.smartexpress.app.ui.trip.cancel.TripCancelReasonActivity;
import ww.smartexpress.app.utils.DateUtils;

public class CouponActivity extends BaseActivity<ActivityCouponBinding, CouponViewModel> {
    CouponAdapter couponAdapter;
    List<Promotion> couponList = new ArrayList<>();
    ServicePromotion servicePromotion;

    Date date = new Date();
    @Override
    public int getLayoutId() {
        return R.layout.activity_coupon;
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
        servicePromotion = ApiModelUtils.fromJson(intent.getStringExtra("SERVICE"), ServicePromotion.class);
        if(servicePromotion != null){
            Log.d("TAG", "onCreate: " + servicePromotion.getMoney());
            Log.d("TAG", "onCreate: date " + date);
            Log.d("TAG", "onCreate: id " + servicePromotion.getSelectedId());
            viewModel.serviceId.set(servicePromotion.getService().getId());
            getPromotion();
        }
    }

    public void getPromotion(){
        viewModel.compositeDisposable.add(viewModel.getPromotion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    viewModel.isLoading.set(false);
                    if(response.isResult() && response.getData().getTotalElements() > 0){
                        couponList = response.getData().getContent();
                        loadCoupon();
                    }else if(!response.isResult()){
                        viewModel.showErrorMessage(viewModel.getApplication().getErrorUtils().handelError(response.getCode()));
                    }
                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));

    }

    public void loadCoupon(){
        couponList = couponList.stream().map(pr -> checkPromotion(pr)).collect(Collectors.toList());

        viewBinding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);

        viewBinding.rcCoupon.setLayoutManager(layoutManager);
        viewBinding.rcCoupon.setItemAnimator(new DefaultItemAnimator());
        couponAdapter = new CouponAdapter(couponList);
        viewBinding.rcCoupon.setAdapter(couponAdapter);
        couponAdapter.setOnItemClickListener(new CouponAdapter.OnItemClickListener() {
            @Override
            public void itemClick(Promotion coupon) {

            }

            @Override
            public void useClick(Promotion promotion) {
                if(servicePromotion.getSelectedId()!=null){
                    if(servicePromotion.getSelectedId().equals(promotion.getId())){
                        // hiện dialog hủy coupon
                        showDialogRemovePromotion(promotion);

                    }else if(!promotion.getIsInValid()){
                        Log.d("Click", "performDataBinding: ");
                        ApplyPromotion applyPromotion = ApplyPromotion.builder()
                                .promotion(promotion)
                                .kindApply(1)
                                .build();
                        EventBus.getDefault().postSticky(applyPromotion);
                        onBackPressed();
                    }
                }else if(!promotion.getIsInValid()){
                    Log.d("Click", "performDataBinding: ");
                    ApplyPromotion applyPromotion = ApplyPromotion.builder()
                            .promotion(promotion)
                            .kindApply(1)
                            .build();
                    EventBus.getDefault().postSticky(applyPromotion);
                    onBackPressed();
                }
            }
        });

    }

    public Promotion checkPromotion(Promotion pr){
        if(servicePromotion.getSelectedId()!=null){
            if(servicePromotion.getSelectedId().equals(pr.getId())){
                pr.setIsSelected(true);
            }
        }


        if(pr.getLimitValueMin() != null && servicePromotion.getMoney() < pr.getLimitValueMin()){
            pr.setIsInValid(true);
            Log.d("TAG", "checkPromotion: it tien hon limit");
            return pr;
        }

        pr.setIsInValid(false);
        return pr;
    }

    public void showDialogRemovePromotion(Promotion promotion){
        Dialog dialog = new Dialog(CouponActivity.this);
        BaseDialogBinding dialogLogoutBinding = DataBindingUtil.inflate(LayoutInflater.from(CouponActivity.this),R.layout.base_dialog,null, false);
        dialogLogoutBinding.setTitle("Hủy áp dụng khuyến mãi");
        dialogLogoutBinding.setSubtitle("");
        dialogLogoutBinding.setDecision(getString(R.string.confirm));
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
            ApplyPromotion applyPromotion = ApplyPromotion.builder()
                    .promotion(promotion)
                    .kindApply(0)
                    .build();
            EventBus.getDefault().postSticky(applyPromotion);
            onBackPressed();
            dialog.dismiss();
        });
        dialogLogoutBinding.btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }


}
