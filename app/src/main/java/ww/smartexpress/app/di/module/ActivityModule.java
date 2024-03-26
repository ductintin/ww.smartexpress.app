package ww.smartexpress.app.di.module;

import android.content.Context;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.ViewModelProviderFactory;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.di.scope.ActivityScope;
import ww.smartexpress.app.ui.cart.CartViewModel;
import ww.smartexpress.app.ui.chat.ChatViewModel;
import ww.smartexpress.app.ui.delivery.BookDeliveryViewModel;
import ww.smartexpress.app.ui.delivery.order.DeliveryViewModel;
import ww.smartexpress.app.ui.index.IndexViewModel;
import ww.smartexpress.app.ui.order.OrderViewModel;
import ww.smartexpress.app.ui.order.details.OrderDetailsViewModel;
import ww.smartexpress.app.ui.order.information.OrderInformationViewModel;
import ww.smartexpress.app.ui.password.reset.ResetPasswordViewModel;
import ww.smartexpress.app.ui.purchase.PurchaseViewModel;
import ww.smartexpress.app.ui.register.RegisterViewModel;
import ww.smartexpress.app.ui.search.food.SearchFoodViewModel;
import ww.smartexpress.app.ui.shipping.address.ShippingAddressViewModel;
import ww.smartexpress.app.ui.shipping.address.search.SearchAddressViewModel;
import ww.smartexpress.app.ui.signin.SignInViewModel;
import ww.smartexpress.app.ui.store.StoreViewModel;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.bookcar.BookCarViewModel;
import ww.smartexpress.app.ui.coupon.CouponViewModel;
import ww.smartexpress.app.ui.home.HomeViewModel;
import ww.smartexpress.app.ui.input.phone.PhoneViewModel;
import ww.smartexpress.app.ui.login.LoginViewModel;
import ww.smartexpress.app.ui.main.MainViewModel;
import ww.smartexpress.app.ui.note.NoteViewModel;
import ww.smartexpress.app.ui.otp.ForgetPasswordOTPViewModel;
import ww.smartexpress.app.ui.otp.LoginOTPViewModel;
import ww.smartexpress.app.ui.payment.PaymentViewModel;
import ww.smartexpress.app.ui.profile.EditProfileViewModel;
import ww.smartexpress.app.ui.rate.RateDriverViewModel;
import ww.smartexpress.app.ui.search.SearchViewModel;
import ww.smartexpress.app.ui.search.location.SearchLocationViewModel;
import ww.smartexpress.app.ui.trip.cancel.TripCancelReasonViewModel;
import ww.smartexpress.app.ui.trip.complete.TripCompleteViewModel;
import ww.smartexpress.app.ui.trip.TripViewModel;
import ww.smartexpress.app.ui.welcome.WelcomeViewModel;
import ww.smartexpress.app.ui.splashform.SplashFormViewModel;
import ww.smartexpress.app.utils.GetInfo;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final BaseActivity<?, ?> activity;

    public ActivityModule(BaseActivity<?, ?> activity) {
        this.activity = activity;
    }

    @Named("access_token")
    @Provides
    @ActivityScope
    String provideToken(Repository repository) {
        return repository.getToken();
    }

    @Named("device_id")
    @Provides
    @ActivityScope
    String provideDeviceId(Context applicationContext) {
        return GetInfo.getAll(applicationContext);
    }


    @Provides
    @ActivityScope
    MainViewModel provideMainViewModel(Repository repository, Context application) {
        Supplier<MainViewModel> supplier = () -> new MainViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<MainViewModel> factory = new ViewModelProviderFactory<>(MainViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(MainViewModel.class);
    }

    @Provides
    @ActivityScope
    LoginOTPViewModel provideLoginOTPViewModel(Repository repository, Context application) {
        Supplier<LoginOTPViewModel> supplier = () -> new LoginOTPViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<LoginOTPViewModel> factory = new ViewModelProviderFactory<>(LoginOTPViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(LoginOTPViewModel.class);
    }

    @Provides
    @ActivityScope
    EditProfileViewModel provideEditProfileViewModel(Repository repository, Context application) {
        Supplier<EditProfileViewModel> supplier = () -> new EditProfileViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<EditProfileViewModel> factory = new ViewModelProviderFactory<>(EditProfileViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(EditProfileViewModel.class);
    }

    @Provides
    @ActivityScope
    ForgetPasswordOTPViewModel provideForgetPasswordOTPViewModel(Repository repository, Context application) {
        Supplier<ForgetPasswordOTPViewModel> supplier = () -> new ForgetPasswordOTPViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<ForgetPasswordOTPViewModel> factory = new ViewModelProviderFactory<>(ForgetPasswordOTPViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ForgetPasswordOTPViewModel.class);
    }

    @Provides
    @ActivityScope
    SplashFormViewModel provideSplashFormViewModel(Repository repository, Context application) {
        Supplier<SplashFormViewModel> supplier = () -> new SplashFormViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<SplashFormViewModel> factory = new ViewModelProviderFactory<>(SplashFormViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SplashFormViewModel.class);
    }

    @Provides
    @ActivityScope
    WelcomeViewModel provideWelcomeViewModel(Repository repository, Context application) {
        Supplier<WelcomeViewModel> supplier = () -> new WelcomeViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<WelcomeViewModel> factory = new ViewModelProviderFactory<>(WelcomeViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(WelcomeViewModel.class);
    }

    @Provides
    @ActivityScope
    PhoneViewModel providePhoneViewModel(Repository repository, Context application) {
        Supplier<PhoneViewModel> supplier = () -> new PhoneViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<PhoneViewModel> factory = new ViewModelProviderFactory<>(PhoneViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(PhoneViewModel.class);
    }

    @Provides
    @ActivityScope
    HomeViewModel provideHomeViewModel(Repository repository, Context application) {
        Supplier<HomeViewModel> supplier = () -> new HomeViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<HomeViewModel> factory = new ViewModelProviderFactory<>(HomeViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(HomeViewModel.class);
    }

    @Provides
    @ActivityScope
    SearchViewModel provideSearchViewModel(Repository repository, Context application) {
        Supplier<SearchViewModel> supplier = () -> new SearchViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<SearchViewModel> factory = new ViewModelProviderFactory<>(SearchViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SearchViewModel.class);
    }

    @Provides
    @ActivityScope
    LoginViewModel provideLoginViewModel(Repository repository, Context application) {
        Supplier<LoginViewModel> supplier = () -> new LoginViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<LoginViewModel> factory = new ViewModelProviderFactory<>(LoginViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(LoginViewModel.class);
    }

    @Provides
    @ActivityScope
    BookCarViewModel provideBookCarViewModel(Repository repository, Context application) {
        Supplier<BookCarViewModel> supplier = () -> new BookCarViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<BookCarViewModel> factory = new ViewModelProviderFactory<>(BookCarViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(BookCarViewModel.class);
    }

    @Provides
    @ActivityScope
    PaymentViewModel providePaymentViewModel(Repository repository, Context application) {
        Supplier<PaymentViewModel> supplier = () -> new PaymentViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<PaymentViewModel> factory = new ViewModelProviderFactory<>(PaymentViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(PaymentViewModel.class);
    }
    @Provides
    @ActivityScope
    NoteViewModel provideNoteViewModel(Repository repository, Context application) {
        Supplier<NoteViewModel> supplier = () -> new NoteViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<NoteViewModel> factory = new ViewModelProviderFactory<>(NoteViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(NoteViewModel.class);
    }

    @Provides
    @ActivityScope
    CouponViewModel provideCouponViewModel(Repository repository, Context application) {
        Supplier<CouponViewModel> supplier = () -> new CouponViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<CouponViewModel> factory = new ViewModelProviderFactory<>(CouponViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(CouponViewModel.class);
    }

    @Provides
    @ActivityScope
    SearchLocationViewModel provideSearchLocationViewModel(Repository repository, Context application) {
        Supplier<SearchLocationViewModel> supplier = () -> new SearchLocationViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<SearchLocationViewModel> factory = new ViewModelProviderFactory<>(SearchLocationViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SearchLocationViewModel.class);
    }

    @Provides
    @ActivityScope
    RateDriverViewModel provideRateDriverViewModel(Repository repository, Context application) {
        Supplier<RateDriverViewModel> supplier = () -> new RateDriverViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<RateDriverViewModel> factory = new ViewModelProviderFactory<>(RateDriverViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(RateDriverViewModel.class);
    }
    @Provides
    @ActivityScope
    TripViewModel provideTripViewModel(Repository repository, Context application) {
        Supplier<TripViewModel> supplier = () -> new TripViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<TripViewModel> factory = new ViewModelProviderFactory<>(TripViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(TripViewModel.class);
    }

    @Provides
    @ActivityScope
    OrderViewModel provideOrderViewModel(Repository repository, Context application) {
        Supplier<OrderViewModel> supplier = () -> new OrderViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<OrderViewModel> factory = new ViewModelProviderFactory<>(OrderViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(OrderViewModel.class);
    }

    @Provides
    @ActivityScope
    StoreViewModel provideStoreViewModel(Repository repository, Context application) {
        Supplier<StoreViewModel> supplier = () -> new StoreViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<StoreViewModel> factory = new ViewModelProviderFactory<>(StoreViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(StoreViewModel.class);
    }
    @Provides
    @ActivityScope
    CartViewModel provideCartViewModel(Repository repository, Context application) {
        Supplier<CartViewModel> supplier = () -> new CartViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<CartViewModel> factory = new ViewModelProviderFactory<>(CartViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(CartViewModel.class);
    }
    @Provides
    @ActivityScope
    OrderDetailsViewModel provideOrderDetailsViewModel(Repository repository, Context application) {
        Supplier<OrderDetailsViewModel> supplier = () -> new OrderDetailsViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<OrderDetailsViewModel> factory = new ViewModelProviderFactory<>(OrderDetailsViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(OrderDetailsViewModel.class);
    }
    @Provides
    @ActivityScope
    PurchaseViewModel providePurchaseViewModel(Repository repository, Context application) {
        Supplier<PurchaseViewModel> supplier = () -> new PurchaseViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<PurchaseViewModel> factory = new ViewModelProviderFactory<>(PurchaseViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(PurchaseViewModel.class);
    }
    @Provides
    @ActivityScope
    BookDeliveryViewModel provideBookDeliveryViewModel(Repository repository, Context application) {
        Supplier<BookDeliveryViewModel> supplier = () -> new BookDeliveryViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<BookDeliveryViewModel> factory = new ViewModelProviderFactory<>(BookDeliveryViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(BookDeliveryViewModel.class);
    }

    @Provides
    @ActivityScope
    ShippingAddressViewModel provideShippingAddressViewModel(Repository repository, Context application) {
        Supplier<ShippingAddressViewModel> supplier = () -> new ShippingAddressViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<ShippingAddressViewModel> factory = new ViewModelProviderFactory<>(ShippingAddressViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ShippingAddressViewModel.class);
    }

    @Provides
    @ActivityScope
    OrderInformationViewModel provideOrderInformationViewModel(Repository repository, Context application) {
        Supplier<OrderInformationViewModel> supplier = () -> new OrderInformationViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<OrderInformationViewModel> factory = new ViewModelProviderFactory<>(OrderInformationViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(OrderInformationViewModel.class);
    }
    @Provides
    @ActivityScope
    SearchAddressViewModel provideSearchAddressViewModel(Repository repository, Context application) {
        Supplier<SearchAddressViewModel> supplier = () -> new SearchAddressViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<SearchAddressViewModel> factory = new ViewModelProviderFactory<>(SearchAddressViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SearchAddressViewModel.class);
    }
    @Provides
    @ActivityScope
    TripCompleteViewModel provideTripCompleteViewModel(Repository repository, Context application) {
        Supplier<TripCompleteViewModel> supplier = () -> new TripCompleteViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<TripCompleteViewModel> factory = new ViewModelProviderFactory<>(TripCompleteViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(TripCompleteViewModel.class);
    }
    @Provides
    @ActivityScope
    DeliveryViewModel provideDeliveryViewModel(Repository repository, Context application) {
        Supplier<DeliveryViewModel> supplier = () -> new DeliveryViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<DeliveryViewModel> factory = new ViewModelProviderFactory<>(DeliveryViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(DeliveryViewModel.class);
    }

    @Provides
    @ActivityScope
    SearchFoodViewModel  provideSearchFoodViewModel(Repository repository, Context application) {
        Supplier< SearchFoodViewModel> supplier = () -> new  SearchFoodViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory< SearchFoodViewModel> factory = new ViewModelProviderFactory<>( SearchFoodViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get( SearchFoodViewModel.class);
    }
    @Provides
    @ActivityScope
    TripCancelReasonViewModel provideTripCancelReasonViewModel(Repository repository, Context application) {
        Supplier<TripCancelReasonViewModel> supplier = () -> new TripCancelReasonViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<TripCancelReasonViewModel> factory = new ViewModelProviderFactory<>(TripCancelReasonViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(TripCancelReasonViewModel.class);
    }
    @Provides
    @ActivityScope
    RegisterViewModel provideRegisterViewModel(Repository repository, Context application) {
        Supplier<RegisterViewModel> supplier = () -> new RegisterViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<RegisterViewModel> factory = new ViewModelProviderFactory<>(RegisterViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(RegisterViewModel.class);
    }

    @Provides
    @ActivityScope
    IndexViewModel provideIndexViewModel(Repository repository, Context application) {
        Supplier<IndexViewModel> supplier = () -> new IndexViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<IndexViewModel> factory = new ViewModelProviderFactory<>(IndexViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(IndexViewModel.class);
    }
    @Provides
    @ActivityScope
    SignInViewModel provideSignInViewModel(Repository repository, Context application) {
        Supplier<SignInViewModel> supplier = () -> new SignInViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<SignInViewModel> factory = new ViewModelProviderFactory<>(SignInViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SignInViewModel.class);
    }
    @Provides
    @ActivityScope
    ResetPasswordViewModel provideResetPasswordViewModel(Repository repository, Context application) {
        Supplier<ResetPasswordViewModel> supplier = () -> new ResetPasswordViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<ResetPasswordViewModel> factory = new ViewModelProviderFactory<>(ResetPasswordViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ResetPasswordViewModel.class);
    }

    @Provides
    @ActivityScope
    ChatViewModel provideChatViewModel(Repository repository, Context application) {
        Supplier<ChatViewModel> supplier = () -> new ChatViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<ChatViewModel> factory = new ViewModelProviderFactory<>(ChatViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ChatViewModel.class);
    }
}
