package ww.smartexpress.app.ui.fragment.search;


import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.AddressMap;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.LocationType;
import ww.smartexpress.app.data.model.api.response.Note;
import ww.smartexpress.app.data.model.api.response.SearchLocation;
import ww.smartexpress.app.data.model.room.AddressEntity;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.databinding.FragmentSearchBinding;
import ww.smartexpress.app.di.component.FragmentComponent;
import ww.smartexpress.app.ui.base.fragment.BaseFragment;
import ww.smartexpress.app.ui.delivery.BookDeliveryActivity;
import ww.smartexpress.app.ui.fragment.activity.adapter.BookingAdapter;
import ww.smartexpress.app.ui.map.MapActivity;
import ww.smartexpress.app.ui.search.location.SearchLocationActivity;
import ww.smartexpress.app.ui.search.location.adapter.LocationTypeAdapter;
import ww.smartexpress.app.ui.search.location.adapter.SavedLocationAdapter;
import ww.smartexpress.app.ui.search.location.adapter.SearchLocationAdapter;

public class SearchFragment extends BaseFragment<FragmentSearchBinding, SearchFragmentViewModel> implements LocationListener {
    BookingAdapter bookingAdapter;
    List<BookingResponse> currentBookingList = new ArrayList<>();
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void performDataBinding() {
        binding.setA(this);
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadProfile();
        loadCurrentBooking();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    public void loadProfile(){
        viewModel.compositeDisposable.add(viewModel.getProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if(response.isResult()){

//                        UserEntity userEntity = UserEntity.builder()
//                                .id(response.getData().getId())
//                                .avatar(response.getData().getAvatar())
//                                .name(response.getData().getName())
//                                .phone(response.getData().getPhone())
//                                .email(response.getData().getEmail())
//                                .build();

                        viewModel.updateUser(
                                        response.getData().getId(),
                                        response.getData().getAvatar(),
                                        response.getData().getName(),
                                        response.getData().getPhone(),
                                        response.getData().getEmail()
                                )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        viewModel.profile.set(response.getData());
                                    }

                                    @Override
                                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                    }
                                });
                    }
                }, err -> {
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }

    public void loadCurrentBooking(){
        viewModel.compositeDisposable.add(viewModel.getCurrentBooking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    if(response.isResult()){
                        if(response.getData().getTotalElements() > 0){
                            currentBookingList = response.getData().getContent();
                            bookingAdapter = new BookingAdapter(currentBookingList);

                            Map<Long, String> codeBooking = new HashMap<>();
                            for(BookingResponse br: currentBookingList){
                                codeBooking.put(br.getId(),br.getCode());
                            }

                            viewModel.getApplication().getWebSocketLiveData().setCodeBooking(codeBooking);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext()
                                    ,LinearLayoutManager.VERTICAL, false);

                            binding.rcCurrentBooking.setLayoutManager(layoutManager);
                            binding.rcCurrentBooking.setItemAnimator(new DefaultItemAnimator());

                            binding.rcCurrentBooking.setAdapter(bookingAdapter);

                            bookingAdapter.setOnItemClickListener(new BookingAdapter.OnItemClickListener() {
                                @Override
                                public void itemClick(BookingResponse booking) {
                                    goToShippingActivity(booking);
                                }
                            });

                        }else{
                            if(bookingAdapter != null){
                                bookingAdapter.clearItems();
                            }
                        }
                    }else{

                    }
                }, err -> {
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }


    public void goToShippingActivity(BookingResponse booking){
        Intent intent = new Intent(getActivity(), BookDeliveryActivity.class);
        intent.putExtra("BOOKING_ID", booking.getId());
        intent.putExtra("BOOKING_CODE", booking.getCode());
        startActivity(intent);
    }

    public void goToSearchLocationActivity(){
        if(currentBookingList != null && currentBookingList.size() < 10){
            Intent intent = new Intent(getActivity(), SearchLocationActivity.class);
            startActivity(intent);
        }else{
            viewModel.showErrorMessage("Bạn đã đạt giới hạn 10 đơn hàng cùng lúc!");
        }
    }
}
