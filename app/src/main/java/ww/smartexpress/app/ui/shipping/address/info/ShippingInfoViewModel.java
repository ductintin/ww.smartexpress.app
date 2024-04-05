package ww.smartexpress.app.ui.shipping.address.info;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.ObservableField;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.response.ShippingInfo;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.coupon.CouponActivity;
import ww.smartexpress.app.ui.order.information.OrderInformationActivity;
import ww.smartexpress.app.ui.shipping.address.search.SearchAddressActivity;

public class ShippingInfoViewModel extends BaseViewModel {
    public ObservableField<Long> categoryId = new ObservableField<>();
    public ObservableField<Double> originLat = new ObservableField<>();
    public ObservableField<Double> originLng = new ObservableField<>();
    public ObservableField<Double> destinationLat = new ObservableField<>();
    public ObservableField<Double> destinationLng = new ObservableField<>();
    public ObservableField<String> origin = new ObservableField<>("");
    public ObservableField<String> destination = new ObservableField<>("");
    public ObservableField<Long> serviceId = new ObservableField<>();
    public ObservableField<String> originId = new ObservableField<>("");
    public ObservableField<String> destinationId = new ObservableField<>("");
    public ObservableField<String> originLatLng = new ObservableField<>("");
    public ObservableField<String> destinationLatLng = new ObservableField<>("");
    public ObservableField<String> mode = new ObservableField<>("");
    public ObservableField<Long> distance = new ObservableField<>(0L);
    public ObservableField<String> distanceKm = new ObservableField<>("");

    public ObservableField<String> consigneeName = new ObservableField<>("");
    public ObservableField<String> consigneePhone = new ObservableField<>("");
    public ObservableField<String> senderName = new ObservableField<>("");
    public ObservableField<String> senderPhone = new ObservableField<>("");
    public ObservableField<Boolean> isCod = new ObservableField<>(false);
    public ObservableField<Integer> codPrice = new ObservableField<>(0);

    public ShippingInfoViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void toCouponActivity(){
        Intent intent = new Intent(application.getCurrentActivity(), CouponActivity.class);
        application.getCurrentActivity().startActivity(intent);
    }

    public void toShippingDetailActivity(){
        Intent intent = new Intent(application.getCurrentActivity(), OrderInformationActivity.class);
        ShippingInfo shippingInfo = ShippingInfo.builder()
                .origin(origin.get())
                .destination(destination.get())
                .senderName(senderName.get())
                .senderPhone(senderPhone.get())
                .consigneeName(consigneeName.get())
                .consigneePhone(consigneePhone.get())
                .isCod(false)
                .codPrice(0)
                .build();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SHIPPING_INFO, ApiModelUtils.toJson(shippingInfo));
        intent.putExtras(bundle);
        application.getCurrentActivity().startActivity(intent);
    }
}
