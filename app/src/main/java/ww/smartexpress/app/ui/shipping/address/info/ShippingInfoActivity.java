package ww.smartexpress.app.ui.shipping.address.info;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.Note;
import ww.smartexpress.app.data.model.api.response.ShippingInfo;
import ww.smartexpress.app.databinding.ActivityShippingInfoBinding;
import ww.smartexpress.app.databinding.LayoutBottomSheetServiceBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.delivery.BookDeliveryActivity;
import ww.smartexpress.app.ui.delivery.order.DeliveryActivity;

public class ShippingInfoActivity extends BaseActivity<ActivityShippingInfoBinding, ShippingInfoViewModel> {


    BottomSheetBehavior sheetBehavior;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shipping_info;
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

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            viewModel.originId.set(bundle.getString(Constants.KEY_ORIGIN_ID, ""));
            viewModel.destinationId.set(bundle.getString(Constants.KEY_DESTINATION_ID, ""));
            viewModel.origin.set(bundle.getString(Constants.KEY_ORIGIN_NAME, ""));
            viewModel.destination.set(bundle.getString(Constants.KEY_DESTINATION_NAME, ""));
        }

        bottomSheetLayout();

    }

    public void showDialog(){
//        final Dialog dialog = new Dialog(this);
//
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        LayoutBottomSheetServiceBinding bottomSheetServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(ShippingInfoActivity.this), R.layout.layout_bottom_sheet_service, null, false);
//
//        dialog.setContentView(bottomSheetServiceBinding.getRoot());
//
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        dialog.getWindow().setWindowAnimations(R.style.BottomSheetService);
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
//        dialog.getWindow().setElevation(100);
//
//        dialog.show();
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void bottomSheetLayout(){
        sheetBehavior = BottomSheetBehavior.from(viewBinding.bottomLayout);

        BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        //Bắt đầu kéo View
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        };

        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.addBottomSheetCallback(bottomSheetCallback);
    }

    // Trong màn hình A
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDataEvent(ShippingInfo event) {


        if(event != null){
            viewModel.senderName.set(event.getSenderName());
            viewModel.senderPhone.set(event.getSenderPhone());
            viewModel.consigneeName.set(event.getConsigneeName());
            viewModel.consigneePhone.set(event.getConsigneePhone());
            viewModel.customerNote.set(event.getCustomerNote());
            viewModel.isCod.set(event.getIsCod());
            viewModel.codPrice.set(event.getCodPrice());
        }
        Log.d("TAG", "onDataEvent: " + viewModel.consigneeName.get());
        // Xử lý dữ liệu ở đây
    }


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().removeStickyEvent(Note.class);
    }

    public void toDelivery(){
        ShippingInfo shippingInfo = ShippingInfo.builder()
                .origin(viewModel.origin.get())
                .originId(viewModel.originId.get())
                .destination(viewModel.destination.get())
                .destinationId(viewModel.destinationId.get())
                .senderName(viewModel.senderName.get())
                .senderPhone(viewModel.senderPhone.get())
                .consigneeName(viewModel.consigneeName.get())
                .consigneePhone(viewModel.consigneePhone.get())
                .customerNote(viewModel.customerNote.get())
                .isCod(viewModel.isCod.get())
                .codPrice(viewModel.codPrice.get())
                .build();

        Intent intent = new Intent(this, BookDeliveryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SHIPPING_INFO, ApiModelUtils.toJson(shippingInfo));
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
