package ww.smartexpress.app.data.remote;

import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.CancelBookingRequest;
import ww.smartexpress.app.data.model.api.request.CreateBookingRequest;
import ww.smartexpress.app.data.model.api.request.LoginRequest;
import ww.smartexpress.app.data.model.api.request.RatingBookingRequest;
import ww.smartexpress.app.data.model.api.request.RegisterRequest;
import ww.smartexpress.app.data.model.api.request.UpdateProfileRequest;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.CategoryResponse;
import ww.smartexpress.app.data.model.api.response.LoginResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ww.smartexpress.app.data.model.api.response.ProfileResponse;
import ww.smartexpress.app.data.model.api.response.Room;
import ww.smartexpress.app.data.model.api.response.SearchLocationResponse;
import ww.smartexpress.app.data.model.api.response.ServiceResponse;
import ww.smartexpress.app.data.model.api.response.UploadFileResponse;

public interface ApiService {
    @POST("/v1/customer/login")
    @Headers({"IgnoreAuth:1"})
    Observable<ResponseWrapper<LoginResponse>> login(@Body LoginRequest request);
    @POST("/v1/customer/register")
    @Headers({"IgnoreAuth:1"})
    Observable<ResponseWrapper<String>> register(@Body RegisterRequest request);
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
    @GET("/v1/room/get/{id}")
    Observable<ResponseWrapper<Room>> getMyRoom(@Path("id") Long id);
    @POST("/v1/rating/create")
    Observable<ResponseWrapper<String>> ratingBooking(@Body RatingBookingRequest request);

    @GET("/geocode/json")
    @Headers({"isSearchLocation:1"})
    Observable<JsonObject> getLocationInfoByLatLng(@Query("latlng") String latlng, @Query("key") String api);
}
