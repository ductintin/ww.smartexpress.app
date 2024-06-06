package ww.smartexpress.app.ui.fragment.search;

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
import io.reactivex.rxjava3.core.Completable;
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
import ww.smartexpress.app.data.model.api.response.ProfileResponse;
import ww.smartexpress.app.data.model.api.response.SearchLocation;
import ww.smartexpress.app.data.model.api.response.SearchLocationResponse;
import ww.smartexpress.app.data.model.room.AddressEntity;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.data.model.room.UserWithAddresses;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.base.fragment.BaseFragmentViewModel;
import ww.smartexpress.app.ui.bookcar.BookCarActivity;
import ww.smartexpress.app.ui.shipping.address.info.ShippingInfoActivity;

public class SearchFragmentViewModel extends BaseFragmentViewModel {
    public ObservableField<Integer> totalBookingElements = new ObservableField<>(0);
    public ObservableField<ProfileResponse> profile = new ObservableField<>(new ProfileResponse());
    public SearchFragmentViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }


    Observable<ResponseWrapper<ResponseListObj<BookingResponse>>> getCurrentBooking() {
        return repository.getApiService().getCurrentBooking(null)
                .doOnNext(response -> {

                });
    }

    Observable<ResponseWrapper<ProfileResponse>> getProfile() {
        return repository.getApiService().getProfile();
    }

    Completable insertUser(UserEntity userEntity){
        return repository.getRoomService().userDao().insert(userEntity);
    }

    Completable updateUser(Long id, String avatar, String name, String phone, String email){
        return repository.getRoomService().userDao().updateFull(id, avatar, name, phone, email);
    }

}
