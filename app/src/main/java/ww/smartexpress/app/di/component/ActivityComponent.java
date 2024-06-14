package ww.smartexpress.app.di.component;

import ww.smartexpress.app.di.module.ActivityModule;
import ww.smartexpress.app.di.scope.ActivityScope;
import ww.smartexpress.app.ui.cart.CartActivity;
import ww.smartexpress.app.ui.chat.ChatActivity;
import ww.smartexpress.app.ui.delivery.BookDeliveryActivity;
import ww.smartexpress.app.ui.delivery.order.DeliveryActivity;
import ww.smartexpress.app.ui.index.IndexActivity;
import ww.smartexpress.app.ui.map.MapActivity;
import ww.smartexpress.app.ui.order.details.OrderDetailsActivity;
import ww.smartexpress.app.ui.order.information.OrderInformationActivity;
import ww.smartexpress.app.ui.password.reset.ResetPasswordActivity;
import ww.smartexpress.app.ui.purchase.PurchaseActivity;
import ww.smartexpress.app.ui.register.RegisterActivity;
import ww.smartexpress.app.ui.search.food.SearchFoodActivity;
import ww.smartexpress.app.ui.shipping.address.ShippingAddressActivity;
import ww.smartexpress.app.ui.shipping.address.info.ShippingInfoActivity;
import ww.smartexpress.app.ui.shipping.address.search.SearchAddressActivity;
import ww.smartexpress.app.ui.signin.SignInActivity;
import ww.smartexpress.app.ui.store.StoreActivity;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.coupon.CouponActivity;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.login.LoginActivity;
import ww.smartexpress.app.ui.main.MainActivity;

import dagger.Component;
import ww.smartexpress.app.ui.note.NoteActivity;
import ww.smartexpress.app.ui.order.OrderActivity;
import ww.smartexpress.app.ui.otp.ForgetPasswordOTPActivity;
import ww.smartexpress.app.ui.otp.LoginOTPActivity;
import ww.smartexpress.app.ui.payment.PaymentActivity;
import ww.smartexpress.app.ui.profile.EditProfileActivity;
import ww.smartexpress.app.ui.rate.RateDriverActivity;
import ww.smartexpress.app.ui.search.SearchActivity;
import ww.smartexpress.app.ui.search.location.SearchLocationActivity;
import ww.smartexpress.app.ui.trip.TripActivity;
import ww.smartexpress.app.ui.trip.cancel.TripCancelReasonActivity;
import ww.smartexpress.app.ui.trip.complete.TripCompleteActivity;
import ww.smartexpress.app.ui.trip.detail.TripDetailActivity;
import ww.smartexpress.app.ui.wallet.WalletActivity;
import ww.smartexpress.app.ui.welcome.WelcomeActivity;
import ww.smartexpress.app.ui.input.phone.PhoneActivity;
import ww.smartexpress.app.ui.splashform.SplashFormActivity;

@ActivityScope
@Component(modules = {ActivityModule.class}, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(MainActivity activity);

    void inject(SplashFormActivity splashFormActivity);
    void inject(WelcomeActivity welcomeActivity);
    void inject(PhoneActivity phoneActivity);

    void inject(LoginOTPActivity loginOTPActivity);

    void inject(EditProfileActivity editProfileActivity);

    void inject(ForgetPasswordOTPActivity forgetPasswordOTPActivity);

    void inject(HomeActivity homeActivity);

    void inject(LoginActivity loginActivity);
    void inject(SearchActivity searchActivity);
    void inject(BookCarActivity bookCarActivity);
    void inject(PaymentActivity paymentActivity);
    void inject(NoteActivity noteActivity);

    void inject(CouponActivity couponActivity);

    void inject(SearchLocationActivity searchLocationActivity);
    void inject(TripActivity tripActivity);

    void inject(RateDriverActivity rateDriverActivity);
    void inject(OrderActivity orderActivity);
    void inject(StoreActivity storeActivity);

    void inject(CartActivity cartActivity);

    void inject(OrderDetailsActivity orderDetailsActivity);

    void inject(PurchaseActivity purchaseActivity);
    void inject(BookDeliveryActivity bookDeliveryActivity);

    void inject(ShippingAddressActivity shippingAddressActivity);

    void inject(OrderInformationActivity orderInformationActivity);

    void inject(SearchAddressActivity searchAddressActivity);

    void inject(TripCompleteActivity tripCompleteActivity);

    void inject(DeliveryActivity deliveryActivity);

    void inject(SearchFoodActivity searchFoodActivity);

    void inject(TripCancelReasonActivity tripCancelReasonActivity);
    void inject(RegisterActivity registerActivity);
    void inject(IndexActivity indexActivity);
    void inject(SignInActivity signInActivity);
    void inject(ResetPasswordActivity resetPasswordActivity);
    void inject(ChatActivity chatActivity);
    void inject(ShippingInfoActivity shippingInfoActivity);

    void inject(MapActivity mapActivity);
    void inject(TripDetailActivity tripDetailActivity);
    void inject(WalletActivity walletActivity);
}

