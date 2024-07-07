package ww.smartexpress.app.data.remote;

import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ww.smartexpress.app.data.model.api.ResponseGeneric;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.ActiveCustomerRequest;
import ww.smartexpress.app.data.model.api.request.CancelBookingRequest;
import ww.smartexpress.app.data.model.api.request.ConfirmAccountNumberRequest;
import ww.smartexpress.app.data.model.api.request.CreateBookingRequest;
import ww.smartexpress.app.data.model.api.request.DepositRequest;
import ww.smartexpress.app.data.model.api.request.ForgetPasswordRequest;
import ww.smartexpress.app.data.model.api.request.LoginRequest;
import ww.smartexpress.app.data.model.api.request.PayoutRequest;
import ww.smartexpress.app.data.model.api.request.RatingBookingRequest;
import ww.smartexpress.app.data.model.api.request.RegisterRequest;
import ww.smartexpress.app.data.model.api.request.ResetPasswordRequest;
import ww.smartexpress.app.data.model.api.request.RetryOtpRegisterRequest;
import ww.smartexpress.app.data.model.api.request.UpdateProfileRequest;
import ww.smartexpress.app.data.model.api.response.BankAccountResponse;
import ww.smartexpress.app.data.model.api.response.BankListResponse;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.CategoryResponse;
import ww.smartexpress.app.data.model.api.response.CustomerIdResponse;
import ww.smartexpress.app.data.model.api.response.DriverBookingResponse;
import ww.smartexpress.app.data.model.api.response.DriverPosition;
import ww.smartexpress.app.data.model.api.response.LoginResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ww.smartexpress.app.data.model.api.response.NewsResponse;
import ww.smartexpress.app.data.model.api.response.NotificationResponse;
import ww.smartexpress.app.data.model.api.response.Payment;
import ww.smartexpress.app.data.model.api.response.PayoutTransaction;
import ww.smartexpress.app.data.model.api.response.ProfileResponse;
import ww.smartexpress.app.data.model.api.response.Promotion;
import ww.smartexpress.app.data.model.api.response.Room;
import ww.smartexpress.app.data.model.api.response.RoomResponse;
import ww.smartexpress.app.data.model.api.response.SearchLocationResponse;
import ww.smartexpress.app.data.model.api.response.ServiceResponse;
import ww.smartexpress.app.data.model.api.response.Setting;
import ww.smartexpress.app.data.model.api.response.UploadFileResponse;
import ww.smartexpress.app.data.model.api.response.WalletResponse;
import ww.smartexpress.app.data.model.api.response.WalletTransaction;

public interface ApiService {
    @POST("/v1/customer/login")
    @Headers({"IgnoreAuth:1"})
    Observable<ResponseWrapper<LoginResponse>> login(@Body LoginRequest request);
    @POST("/v1/customer/register")
    @Headers({"IgnoreAuth:1"})
    Observable<ResponseWrapper<CustomerIdResponse>> register(@Body RegisterRequest request);
    @GET("/v1/customer/profile")
    Observable<ResponseWrapper<ProfileResponse>> getProfile();
    @GET("/v1/category/auto-complete")
    Observable<ResponseWrapper<ResponseListObj<CategoryResponse>>> getCategoryAutoComplete(@Query("kind") Integer kind, @Query("name") String name,
                                                                                           @Query("categoryId") Long categoryId, @Query("status") Integer status);
    @GET("/v1/user-service/auto-complete")
    Observable<ResponseWrapper<ResponseListObj<ServiceResponse>>> getServiceAutoComplete(@Query("categoryId") Long categoryId, @Query("id") Long id,
                                                                                         @Query("kind") Integer kind, @Query("name") String name, @Query("status") Integer status);
    @PUT("/v1/customer/update-profile")
    Observable<ResponseWrapper<String>> updateProfile(@Body UpdateProfileRequest request);
    @POST("/v1/file/upload")
    @Headers({"isMedia:1"})
    Observable<ResponseWrapper<UploadFileResponse>> uploadFile(@Body RequestBody requestBody);

    @GET("/place/autocomplete/json")
    @Headers({"isSearchLocation:1"})
    Observable<SearchLocationResponse> searchLocation(@Query("input") String input, @Query("key") String api);

    @GET("/geocode/json")
    @Headers({"isSearchLocation:1"})
    Observable<JsonObject> getLocationInfo(@Query("place_id") String placeId, @Query("key") String api);
    @GET("/directions/json")
    @Headers({"isSearchLocation:1"})
    Observable<JsonObject> getMapDirection(@Query("destination") String destination, @Query("mode") String mode,
                                           @Query("origin") String origin, @Query("key") String api);
    @GET("/distancematrix/json")
    @Headers({"isSearchLocation:1"})
    Observable<JsonObject> getDistance(@Query("origins") String origins, @Query("destinations") String destinations, @Query("key") String api);
    @POST("/v1/booking/create")
    Observable<ResponseWrapper<BookingResponse>> createBooking(@Body CreateBookingRequest request);
    @GET("/v1/booking/my-current-booking")
    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getCurrentBooking(@Query("code") String code);
    @PUT("/v1/booking/cancel")
    Observable<ResponseWrapper<String>> cancelBooking(@Body CancelBookingRequest request);
    @GET("/v1/booking/my-booking")
    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getMyBooking(@Query("code") String code, @Query("id") Long id, @Query("page") Integer pageNumber, @Query("size") Integer pageSize);
    @GET("/v1/booking/detail-booking/{id}")
    Observable<ResponseWrapper<BookingResponse>> getBookingById(@Path("id") Long id);
    @GET("/v1/room/get/{id}")
    Observable<ResponseWrapper<RoomResponse>> getMyRoom(@Path("id") Long id);
    @POST("/v1/rating/create")
    Observable<ResponseWrapper<String>> ratingBooking(@Body RatingBookingRequest request);

    @GET("/geocode/json")
    @Headers({"isSearchLocation:1"})
    Observable<JsonObject> getLocationInfoByLatLng(@Query("latlng") String latlng, @Query("key") String api);
    @GET("/v1/promotion/client-list")
    Observable<ResponseWrapper<ResponseListObj<Promotion>>> getPromotions(@Query("serviceId") Long serviceId, @Query("page") Integer pageNumber, @Query("size") Integer pageSize);
    @GET("/v1/promotion/get/{id}")
    Observable<ResponseWrapper<Promotion>> getPromotionById(@Path("id") Integer id);
    @POST("/v1/customer/active")
    @Headers({"IgnoreAuth:1"})
    Observable<ResponseWrapper<String>> activeCustomer(@Body ActiveCustomerRequest request);
    @POST("/v1/customer/retry-otp-register")
    @Headers({"IgnoreAuth:1"})
    Observable<ResponseWrapper<CustomerIdResponse>> retryActiveCustomer(@Body RetryOtpRegisterRequest request);
    @POST("/v1/wallet/deposit")
    Observable<ResponseWrapper<Payment>> depositMomo(@Body DepositRequest request);
    @POST("/v1/wallet/deposit")
    Observable<ResponseWrapper<Payment>> depositPayos(@Body DepositRequest request);
    @POST("/v1/request-pay-out/create")
    Observable<ResponseGeneric> payout(@Body PayoutRequest request);

    @GET("/v1/wallet/my-wallet")
    Observable<ResponseWrapper<WalletResponse>> getMyWallet();

    @GET("/v2/banks")
    @Headers({"isBank:1"})
    Observable<BankListResponse> getBankList();

    @POST("/v2/lookup")
    @Headers({"isBank:1","X-Api-Key:5b310ff0-2683-4cb5-a3aa-227b8179aec3","X-Client-Id:72a88234-f90e-45af-b4f1-bafdc9d7a01a"})
    Observable<BankAccountResponse> accountLookup(@Body ConfirmAccountNumberRequest request);

    @GET("/v1/wallet-transaction/my-wallet-transaction")
    Observable<ResponseWrapper<ResponseListObj<WalletTransaction>>> getWalletTransaction(@Query("page") Integer pageNumber,
                                                                                         @Query("size") Integer pageSize);

    @GET("/v1/position/my-position")
    Observable<ResponseWrapper<ResponseListObj<DriverPosition>>> getDriverPosition(@Query("driverId") Long driverId);

    @POST("/v1/customer/request-forget-password")
    @Headers({"IgnoreAuth:1"})
    Observable<ResponseWrapper<CustomerIdResponse>> forgetPassword(@Body ForgetPasswordRequest request);

    @POST("/v1/customer/reset-password")
    @Headers({"IgnoreAuth:1"})
    Observable<ResponseWrapper<String>> resetPassword(@Body ResetPasswordRequest request);

    @GET("/v1/notification/my-notification")
    Observable<ResponseWrapper<ResponseListObj<NotificationResponse>>> getMyNotification(@Query("page") Integer pageNumber,
                                                                                         @Query("size") Integer pageSize);
    @GET("/v1/news/client-get/{id}")
    Observable<ResponseWrapper<NewsResponse>> getNews(@Path("id") Long id);

    @DELETE("/v1/request-pay-out/delete/{id}")
    Observable<ResponseWrapper<String>> deletePayoutRequest(@Path("id") Long id);

    @GET("/v1/request-pay-out/my-request")
    Observable<ResponseWrapper<ResponseListObj<PayoutTransaction>>> getMyPayoutRequest (@Query("customerId") Long customerId, @Query("state") Integer state);

    @GET("/v1/settings/get-by-key/{key}")
    Observable<ResponseWrapper<Setting>> getSetting(@Path("key") String key);

    @GET("v1/customer/confirm-password")
    Observable<ResponseWrapper<String>> confirmPassword(@Path("key") String key);
}
