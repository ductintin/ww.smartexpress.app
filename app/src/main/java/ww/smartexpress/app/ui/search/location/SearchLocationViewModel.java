package ww.smartexpress.app.ui.search.location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.room.Transaction;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.BookLocation;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.SearchLocation;
import ww.smartexpress.app.data.model.api.response.SearchLocationResponse;
import ww.smartexpress.app.data.model.room.AddressEntity;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.search.SearchActivity;
import ww.smartexpress.app.ui.shipping.address.info.ShippingInfoActivity;

public class SearchLocationViewModel extends BaseViewModel {

    public ObservableField<String> location = new ObservableField<>("");
    public ObservableField<String> searchLocation = new ObservableField<>("");
    public ObservableField<Long> categoryId = new ObservableField<>(0L);
    public ObservableField<Long> serviceId = new ObservableField<>(0L);
    public ObservableField<String> originId = new ObservableField<>("");
    public ObservableField<String> destinationId = new ObservableField<>("");
    public ObservableField<SearchLocation> origin = new ObservableField<>(null);
    public ObservableField<SearchLocation> destination = new ObservableField<>(new SearchLocation());
    public ObservableField<String> latlng = new ObservableField<>("");

    public ObservableField<BookLocation> bookLocation = new ObservableField<>(new BookLocation());
    public ObservableField<Boolean> hasBooking = new ObservableField<>(false);
    public SearchLocationViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }


    public void getSavedLocation(){}

    public void getHomeLocation(){}

    public void getCompanyLocation(){}

    public void addNewLocation(){}

    public void deleteLocation(){
        location.set("");
        origin.set(null);
        originId.set("");
    }

    public void deleteSearchLocation(){
        searchLocation.set("");
        destinationId.set("");
    }

    public void back() {
        application.getCurrentActivity().onBackPressed();
    }

    public void getNotifications(){

    }

    @Transaction
    public void nextBooking(){
        if(originId.get().equals(destinationId.get())){
            showErrorMessage(application.getString(R.string.same_booking_location));
        }else{
            Long userId = repository.getSharedPreferences().getLongVal(Constants.KEY_USER_ID);
            Log.d("TAG", "insertAddress: us" + userId);
            List<AddressEntity> addressEntitiesToInsert = new ArrayList<>();
            if(origin.get() != null){
                addressEntitiesToInsert.add(
                        AddressEntity.builder()
                                .place_id(originId.get())
                                .description(origin.get().getDescription())
                                .main_text(origin.get().getStructured_formatting().getMain_text())
                                .userId(userId)
                                .build());
            }
            addressEntitiesToInsert.add(
                    AddressEntity.builder()
                            .place_id(destinationId.get())
                            .description(destination.get().getDescription())
                            .main_text(destination.get().getStructured_formatting().getMain_text())
                            .userId(userId)
                            .build());

            Log.d("TAG", "nextBooking: " + addressEntitiesToInsert);
            repository.getRoomService().addressDao().loadAllAddressByUserId(userId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<AddressEntity>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onSuccess(List<AddressEntity> addressEntities) {
                            Log.d("TAG", "onSuccess: load" + addressEntities.toString());
                            if(addressEntities.size() > 0){
                                int size = addressEntities.size();
                                for (AddressEntity address : addressEntitiesToInsert) {
                                    int count =  repository.getRoomService().addressDao().getAddressCount(address.getPlace_id());
                                    Log.d("TAG", "onSuccess: count" + count);
                                    if(count > 0)
                                        continue;
                                    else{
                                        if(size >= 6){
                                            deleteAddressByIdAndInsert(addressEntities.get(0).getId(), address);
                                        }else {
                                            insertAddress(address);
                                        }
                                    }
                                }
                            }else{
                                insertAddress(addressEntitiesToInsert);
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                    });

            Intent intent = new Intent(application.getCurrentActivity(), ShippingInfoActivity.class);
            Bundle bundle = new Bundle();
            //bundle.putLong(Constants.KEY_CATEGORY_ID, categoryId.get());
            bundle.putLong(Constants.KEY_SERVICE_ID, serviceId.get());
            bundle.putString(Constants.KEY_ORIGIN_ID, originId.get());
            bundle.putString(Constants.KEY_DESTINATION_ID, destinationId.get());
            bundle.putString(Constants.KEY_ORIGIN_NAME, origin.get() == null ? location.get() : origin.get().getDescription());
            bundle.putString(Constants.KEY_DESTINATION_NAME, destination.get().getDescription());
            intent.putExtras(bundle);
            application.getCurrentActivity().startActivity(intent);
        }

    }

    Single<List<AddressEntity>> getAllSavedAddress(){
        Long userId = repository.getSharedPreferences().getLongVal(Constants.KEY_USER_ID);
        //List<SearchLocation> searchLocations = new ArrayList<>();
        return repository.getRoomService().addressDao().loadAllAddressByUserId(userId);
    }

    public void deleteAddressByIdAndInsert(Long id, AddressEntity address){
        repository.getRoomService().addressDao().deleteAddressById(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        insertAddress(address);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
    public void insertAddress(AddressEntity address){
        repository.getRoomService().addressDao().insert(address).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void insertAddress(List<AddressEntity> addresses){
        repository.getRoomService().addressDao().insertAll(addresses).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    Observable<SearchLocationResponse> searchLocation(String location) {
        return repository.getApiService().searchLocation(location + " Việt Nam", Constants.GEO_API_KEY)
                .doOnNext(response -> {
                    Log.d("TAG", "searchLocation: " + response.toString());
                });
    }

    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getCurrentBooking() {
        return repository.getApiService().getCurrentBooking(null)
                .doOnNext(response -> {
                    if(response.isResult()){
                        hasBooking.set(true);
                    }
                });
    }

    Observable<JsonObject> getLocationInfo() {
        return repository.getApiService().getLocationInfoByLatLng(latlng.get(), Constants.GEO_API_KEY)
                .doOnNext(response -> {

                });
    }

}
