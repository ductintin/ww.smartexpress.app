package ww.smartexpress.app.ui.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
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
import ww.smartexpress.app.data.model.api.response.Coupon;
import ww.smartexpress.app.data.model.api.response.Promotion;
import ww.smartexpress.app.data.model.api.response.ServicePromotion;
import ww.smartexpress.app.databinding.ActivityCouponBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.coupon.adapter.CouponAdapter;
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
        couponAdapter.setOnItemClickListener(coupon -> {
            if(!coupon.getIsExpired() && !coupon.getIsInValid()){
                Log.d("Click", "performDataBinding: ");
                EventBus.getDefault().postSticky(coupon);
                onBackPressed();
            }

        });

    }

    public Promotion checkPromotion(Promotion pr){
        if(!pr.getServices().contains(servicePromotion.getService())){
            pr.setIsInValid(true);
            Log.d("TAG", "checkPromotion: ko co");
            return pr;
        }

        if(DateUtils.convertStringToDate(pr.getEndDate()).before(date)){
            pr.setIsExpired(true);
            Log.d("TAG", "checkPromotion: het han");
            return pr;
        }

        if(DateUtils.convertStringToDate(pr.getStartDate()).after(date)){
            pr.setIsInValid(true);
            Log.d("TAG", "checkPromotion: chua toi");
            return pr;
        }


        if(servicePromotion.getMoney() < pr.getLimitValueMin()){
            pr.setIsInValid(true);
            Log.d("TAG", "checkPromotion: it tien hon limit");
            return pr;
        }

        if(servicePromotion.getSelectedId()!=null){
            if(servicePromotion.getSelectedId() == pr.getId()){
                pr.setIsSelected(true);
            }
        }

        pr.setIsExpired(false);
        pr.setIsInValid(false);
        return pr;
    }
}
