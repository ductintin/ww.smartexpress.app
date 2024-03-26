package ww.smartexpress.app.ui.coupon;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.Coupon;
import ww.smartexpress.app.databinding.ActivityCouponBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.coupon.adapter.CouponAdapter;

public class CouponActivity extends BaseActivity<ActivityCouponBinding, CouponViewModel> {
    CouponAdapter couponAdapter;
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
        loadCoupon();
    }

    public void loadCoupon(){
        List<Coupon> couponList = new ArrayList<>();
        couponList.add(new Coupon("1", "", "20.000", "50", "15:00, 20/09/2023"));
        couponList.add(new Coupon("1", "", "20.000", "50", "15:00, 20/09/2023"));
        couponList.add(new Coupon("1", "", "20.000", "50", "15:00, 20/09/2023"));
        couponList.add(new Coupon("1", "", "20.000", "50", "15:00, 20/09/2023"));
        couponList.add(new Coupon("1", "", "20.000", "50", "15:00, 20/09/2023"));
        couponList.add(new Coupon("1", "", "20.000", "50", "15:00, 20/09/2023"));
        couponList.add(new Coupon("1", "", "20.000", "50", "15:00, 20/09/2023"));
        couponList.add(new Coupon("1", "", "20.000", "50", "15:00, 20/09/2023"));
        couponList.add(new Coupon("1", "", "20.000", "50", "15:00, 20/09/2023"));
        couponList.add(new Coupon("1", "", "20.000", "50", "15:00, 20/09/2023"));

        viewBinding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);

        viewBinding.rcCoupon.setLayoutManager(layoutManager);
        viewBinding.rcCoupon.setItemAnimator(new DefaultItemAnimator());
        couponAdapter = new CouponAdapter(couponList);
        viewBinding.rcCoupon.setAdapter(couponAdapter);
        couponAdapter.setOnItemClickListener(coupon -> {

            Log.d("Click", "performDataBinding: ");
        });

    }
}
