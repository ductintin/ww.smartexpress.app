package ww.smartexpress.app.ui.delivery;

import android.app.Dialog;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.request.CreateBookingRequest;
import ww.smartexpress.app.data.model.api.response.BookCar;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.DriverBookingResponse;
import ww.smartexpress.app.data.model.api.response.ServicePrice;
import ww.smartexpress.app.data.model.api.response.ServiceResponse;
import ww.smartexpress.app.data.model.api.response.ShippingInfo;
import ww.smartexpress.app.data.model.api.response.Size;
import ww.smartexpress.app.databinding.ActivityBookDeliveryBinding;
import ww.smartexpress.app.databinding.BaseDialogBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.bookcar.adpater.BookCarAdapter;
import ww.smartexpress.app.ui.home.HomeActivity;
import ww.smartexpress.app.ui.trip.cancel.TripCancelReasonActivity;

public class BookDeliveryActivity extends BaseActivity<ActivityBookDeliveryBinding, BookDeliveryViewModel> implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerDragListener {
    private GoogleMap mMap;
    BottomSheetBehavior sheetBehavior;
    BookCarAdapter bookCarAdapter;
    long delayMillis = 5000;
    Handler handler = new Handler();
    Runnable runnable;

    float zoomLevel = 0f;
    List<BookCar> bookCars = new ArrayList<>();
    List<ServiceResponse> serviceResponses = new ArrayList<>();
    //PolylineOptions polylineOptions;
    Polyline polyline;
    CreateBookingRequest bookingRequest = new CreateBookingRequest();
    BookingResponse bookingResponse;
    DriverBookingResponse driverBookingResponse;


    @Override
    public int getLayoutId() {
        return R.layout.activity_book_delivery;
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

            ShippingInfo shippingInfo = ApiModelUtils.fromJson(bundle.getString(Constants.SHIPPING_INFO), ShippingInfo.class);
            //viewModel.customerNote.set(getIntent().getStringExtra(Constants.CUSTOMER_BOOKING_NOTE));


            Log.d("TAG", "dd: " + shippingInfo.getOriginId() + " " + shippingInfo.getDestinationId());

            if(shippingInfo != null){
                viewModel.origin.set(shippingInfo.getOrigin());
                viewModel.originId.set(shippingInfo.getOriginId());
                viewModel.destination.set(shippingInfo.getDestination());
                viewModel.destinationId.set(shippingInfo.getDestinationId());
                viewModel.senderName.set(shippingInfo.getSenderName());
                viewModel.senderPhone.set(shippingInfo.getSenderPhone());
                viewModel.consigneeName.set(shippingInfo.getConsigneeName());
                viewModel.consigneePhone.set(shippingInfo.getConsigneePhone());
                viewModel.origin.set(shippingInfo.getOrigin());
                viewModel.destination.set(shippingInfo.getDestination());
                viewModel.customerNote.set(shippingInfo.getCustomerNote());
                viewModel.selectCOD.set(shippingInfo.getIsCod());
                viewModel.codPrice.set(shippingInfo.getCodPrice());

                bookingRequest.setPickupAddress(shippingInfo.getOrigin());
                bookingRequest.setDestinationAddress(shippingInfo.getDestination());
                bookingRequest.setSenderName(shippingInfo.getSenderName());
                bookingRequest.setSenderPhone(shippingInfo.getSenderPhone());
                bookingRequest.setConsigneeName(shippingInfo.getConsigneeName());
                bookingRequest.setConsigneePhone(shippingInfo.getConsigneePhone());
                bookingRequest.setCustomerNote(shippingInfo.getCustomerNote());
                bookingRequest.setIsCod(shippingInfo.getIsCod());
                bookingRequest.setCodPrice(shippingInfo.getCodPrice().doubleValue());

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapShip);
                mapFragment.getMapAsync(this);

                bottomSheetLayout();

                getOrigin();

            }
        }else{
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapShip);
            mapFragment.getMapAsync(this);

            getCurrentBooking();

        }

        viewBinding.bntBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findingDriver();
            }
        });

        viewBinding.bntCancelBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelBooking();
                //handler.removeCallbacks(runnable);
            }
        });

        viewBinding.btnCancelBookPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelBooking();
                //handler.removeCallbacks(runnable);
            }
        });


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                zoomLevel = mMap.getCameraPosition().zoom;
                Log.d("TAG", "onCameraIdle: " + zoomLevel);
            }
        });
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14.0f));
    }

    public void loadService(){
        viewModel.showLoading();
        viewModel.compositeDisposable.add(viewModel.getService()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    viewModel.hideLoading();
                    serviceResponses = response.getData().getContent().subList(4, response.getData().getContent().size());
                    loadBookCar();
                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }

    private void loadBookCar(){

        for(ServiceResponse sr: serviceResponses){
            ServicePrice servicePrice = ApiModelUtils.fromJson(sr.getPrice(), ServicePrice.class);
            Size size = ApiModelUtils.fromJson(sr.getSize(), Size.class);
            String textSize = size.getHeight() + "x" + size.getWidth() + "x" + size.getLength();
            bookCars.add(new BookCar(sr.getId(), sr.getName(), sr.getImage(), calculatePrice(viewModel.distance.get(), servicePrice), 0.0, textSize, sr.getWeight()));
        }

        bookingRequest.setPromotionMoney(0.0);
        bookingRequest.setServiceId(serviceResponses.get(0).getId());
        bookingRequest.setMoney(bookCars.get(0).getPrice());

        RecyclerView.LayoutManager layoutBookCar = new LinearLayoutManager(this
                ,LinearLayoutManager.VERTICAL, false);
        viewBinding.rcWinDelivery.setLayoutManager(layoutBookCar);
        viewBinding.rcWinDelivery.setItemAnimator(new DefaultItemAnimator());
        bookCarAdapter = new BookCarAdapter(bookCars);
        viewBinding.rcWinDelivery.setAdapter(bookCarAdapter);

        viewModel.deliveryMethod.set(bookCars.get(0).getName());
        viewModel.discount.set(bookCars.get(0).getDiscount().intValue());

        bookCarAdapter.setOnItemClickListener(bookCar -> {
            if(sheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bookingRequest.setServiceId(bookCar.getServiceId());
                bookingRequest.setMoney(bookCar.getPrice());
                bookingRequest.setPromotionMoney(bookCar.getDiscount());
                bookCarAdapter.setSelected(bookCars.indexOf(bookCar));
                viewModel.deliveryMethod.set(bookCar.getName());
                bookCarAdapter.notifyDataSetChanged();

                viewBinding.rcWinDelivery.smoothScrollToPosition(bookCars.indexOf(bookCar));
            }

        });
    }

    private void bottomSheetLayout(){
        sheetBehavior = BottomSheetBehavior.from(viewBinding.bottomLayout);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:

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
        sheetBehavior.addBottomSheetCallback(bottomSheetCallback);
    }

    public void findingDriver(){

        viewModel.compositeDisposable.add(viewModel.createBooking(bookingRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    viewModel.hideLoading();
                    if(response.isResult()){
                        viewModel.isBooking.set(true);
                        viewModel.bookingResponse.set(response.getData());
                        viewModel.getApplication().getWebSocketLiveData().setCodeBooking(response.getData().getCode());
                        viewModel.getApplication().getWebSocketLiveData().sendPing();
                        Log.d("TAG", "findingDriver: " + response.getData().getCode());


                    }else{
                        viewModel.hideLoading();
                        viewModel.showErrorMessage(response.getMessage());
                    }

                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));


    }

    public void cancelBooking(){

        Dialog dialog = new Dialog(BookDeliveryActivity.this);
        BaseDialogBinding dialogLogoutBinding = DataBindingUtil.inflate(LayoutInflater.from(BookDeliveryActivity.this),R.layout.base_dialog,null, false);
        dialogLogoutBinding.setTitle(getString(R.string.cancel_trip_dialog_title));
        dialogLogoutBinding.setSubtitle("");
        dialogLogoutBinding.setDecision(getString(R.string.confirm));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(dialogLogoutBinding.getRoot());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCanceledOnTouchOutside(false);

        dialogLogoutBinding.btnLogout.setOnClickListener(view -> {
            Intent intent = new Intent(BookDeliveryActivity.this, TripCancelReasonActivity.class);
            startActivity(intent);
            dialog.dismiss();
        });
        dialogLogoutBinding.btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    public void foundDriver(Handler handler, Runnable runnable){
        viewModel.isBooking.set(false);
        viewModel.isFound.set(true);
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {
        if(!viewModel.isFound.get() && !viewModel.isBooking.get() && !viewModel.isShipping.get()){
            super.onBackPressed();
        }else if(viewModel.isFound.get() || viewModel.isBooking.get() || viewModel.isShipping.get()){
            finishAffinity();
        }else{
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    public Double calculatePrice(Long distance, ServicePrice servicePrice){
        double money = 0;
        if(distance <= servicePrice.getStartPrice().getDistance()){
            return servicePrice.getStartPrice().getPrice().doubleValue();
        }else{
            money += servicePrice.getStartPrice().getPrice();
            if(servicePrice.getNextPrices() != null){
                money += 1.0*(distance - servicePrice.getStartPrice().getDistance())/1000 * servicePrice.getNextPrices();
            }else{
                for(ServicePrice.Prices sr: servicePrice.getPrices()){
                    if(sr.getTo() != null){
                        if(distance >= sr.getFrom() && distance <= sr.getTo()){
                            long compareDistance = distance - sr.getFrom();
                            money += 1.0 * compareDistance / 1000 * sr.getPrice();
                        }else if(distance >= sr.getTo()){
                            long compareDistance = sr.getTo() - sr.getFrom();
                            money += 1.0 * compareDistance / 1000 * sr.getPrice();
                        }
                    }else{
                        if(distance >= sr.getFrom()){
                            long compareDistance = distance - sr.getFrom();
                            money += 1.0 * compareDistance / 1000 * sr.getPrice();
                        }
                    }
                }
            }
        }

        return Math.ceil(money/1000) * 1000;
    }

    public void getOrigin(){
        if(!TextUtils.isEmpty(viewModel.originId.get()) && !TextUtils.isEmpty(viewModel.destinationId.get())){
            viewModel.compositeDisposable.add(viewModel.getLocationInfo(viewModel.originId.get())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        String status = response.get("status").getAsString();
                        if(status.equals("OK")){
                            JsonArray results = response.getAsJsonArray("results");
                            for(int i = 0; i< results.size(); i++){
                                JsonObject geometry = results.get(i).getAsJsonObject().getAsJsonObject("geometry");
                                JsonObject location = geometry.getAsJsonObject("location");
                                viewModel.originLat.set(location.get("lat").getAsDouble());
                                viewModel.originLng.set(location.get("lng").getAsDouble());
                                bookingRequest.setPickupLat(location.get("lat").getAsDouble());
                                bookingRequest.setPickupLong(location.get("lng").getAsDouble());
                                viewModel.originLatLng.set(String.valueOf(location.get("lat").getAsDouble())+","+String.valueOf(location.get("lng").getAsDouble()));
                            }
                            getDestination();
                        }

                    },error->{
                        viewModel.showErrorMessage(getString(R.string.network_error));
                        error.printStackTrace();
                    })
            );
        }else{
            loadMapDirection();
        }
    }

    public void getDestination(){
        viewModel.compositeDisposable.add(viewModel.getLocationInfo(viewModel.destinationId.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    String status = response.get("status").getAsString();
                    if(status.equals("OK")){
                        JsonArray results = response.getAsJsonArray("results");
                        for(int i = 0; i< results.size(); i++){
                            JsonObject geometry = results.get(i).getAsJsonObject().getAsJsonObject("geometry");
                            JsonObject location = geometry.getAsJsonObject("location");
                            viewModel.destinationLat.set(location.get("lat").getAsDouble());
                            viewModel.destinationLng.set(location.get("lng").getAsDouble());
                            bookingRequest.setDestinationLat(location.get("lat").getAsDouble());
                            bookingRequest.setDestinationLong(location.get("lng").getAsDouble());
                            viewModel.destinationLatLng.set(String.valueOf(location.get("lat").getAsDouble())+","+String.valueOf(location.get("lng").getAsDouble()));
                        }
                        loadMapDirection();
                    }

                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    public void loadMapDirection(){
        viewModel.compositeDisposable.add(viewModel.getMapDirection()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    String status = response.get("status").getAsString();

                    if(status.equals("OK")){

                        mMap.clear();
                        JsonArray routes = response.getAsJsonArray("routes");
                        ArrayList<LatLng> points;
                        PolylineOptions polylineOptions = null;

                        for(int i = 0; i< routes.size(); i++){
                            points = new ArrayList<>();
                            polylineOptions = new PolylineOptions();
                            JsonArray legs = routes.get(i).getAsJsonObject().getAsJsonArray("legs");
                            for(int j = 0; j < legs.size(); j++){
                                JsonArray steps = legs.get(j).getAsJsonObject().getAsJsonArray("steps");
                                JsonObject distance = legs.get(j).getAsJsonObject().getAsJsonObject("distance");
                                JsonObject duration = legs.get(j).getAsJsonObject().getAsJsonObject("duration");
                                viewModel.distance.set(distance.get("value").getAsLong());
                                viewModel.distanceKm.set(distance.get("text").getAsString());
                                viewModel.time.set(duration.get("text").getAsString());
                                viewModel.timeValue.set(duration.get("value").getAsLong());
                                bookingRequest.setDistance(distance.get("value").getAsDouble());

                                for(int k = 0; k < steps.size(); k++){
                                    String polyline = steps.get(k).getAsJsonObject().get("polyline").getAsJsonObject().get("points").getAsString();
                                    List<LatLng> latLngList = PolyUtil.decode(polyline);

                                    for(int l = 0; l < latLngList.size(); l++){
                                        LatLng position = new LatLng(latLngList.get(l).latitude, latLngList.get(l).longitude);
                                        points.add(position);
                                    }
                                }
                            }

                            polylineOptions.addAll(points);
                            polylineOptions.width(10);
                            polylineOptions.color(ContextCompat.getColor(this, R.color.item_background));
                            polylineOptions.geodesic(true);
                        }

                        // Add a marker in Sydney and move the camera

                        mMap.addPolyline(polylineOptions);

                        BitmapDescriptor originIc = BitmapDescriptorFactory.fromResource(R.drawable.origin_pin);
                        BitmapDescriptor desIc = BitmapDescriptorFactory.fromResource(R.drawable.location_flag);
                        LatLng origin = new LatLng(viewModel.originLat.get(), viewModel.originLng.get());
                        LatLng des = new LatLng(viewModel.destinationLat.get(), viewModel.destinationLng.get());
                        mMap.addMarker(new MarkerOptions().position(origin).title(viewModel.origin.get()).icon(originIc));
                        mMap.addMarker(new MarkerOptions().position(des).title(viewModel.destination.get()).icon(desIc));


                        LatLngBounds bounds = new LatLngBounds.Builder().include(origin).include(des).build();
                        Point point = new Point();
                        getWindowManager().getDefaultDisplay().getSize(point);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, point.x, 150, 10));

                        loadService();

                    }else{
                        viewModel.showErrorMessage("Địa chỉ không hợp lệ. Vui lòng chọn lại địa chỉ khác");
                        Intent intent = new Intent(BookDeliveryActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }, err -> {
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }

    public void loadMapDriverDirection(){
        viewModel.compositeDisposable.add(viewModel.getMapDriverDirection()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    String status = response.get("status").getAsString();

                    if(status.equals("OK")){
                        mMap.clear();
                        JsonArray routes = response.getAsJsonArray("routes");
                        ArrayList<LatLng> points;
                        PolylineOptions polylineOptions = null;

                        for(int i = 0; i< routes.size(); i++){
                            points = new ArrayList<>();
                            polylineOptions = new PolylineOptions();
                            JsonArray legs = routes.get(i).getAsJsonObject().getAsJsonArray("legs");

                            for(int j = 0; j < legs.size(); j++){
                                JsonArray steps = legs.get(j).getAsJsonObject().getAsJsonArray("steps");

                                for(int k = 0; k < steps.size(); k++){
                                    String polyline = steps.get(k).getAsJsonObject().get("polyline").getAsJsonObject().get("points").getAsString();
                                    List<LatLng> latLngList = PolyUtil.decode(polyline);

                                    for(int l = 0; l < latLngList.size(); l++){
                                        LatLng position = new LatLng(latLngList.get(l).latitude, latLngList.get(l).longitude);
                                        points.add(position);
                                    }
                                }
                            }

                            polylineOptions.addAll(points);
                            polylineOptions.width(10);
                            polylineOptions.color(ContextCompat.getColor(this, R.color.item_background));
                            polylineOptions.geodesic(true);
                        }

                        // Add a marker in Sydney and move the camera

                        mMap.addPolyline(polylineOptions);

                        BitmapDescriptor originIc = BitmapDescriptorFactory.fromResource(R.drawable.origin_pin);
                        LatLng origin = new LatLng(viewModel.originLat.get(), viewModel.originLng.get());
                        LatLng des = new LatLng(Double.valueOf(viewModel.driverLatLng.get().split(",")[0]), Double.valueOf(viewModel.driverLatLng.get().split(",")[1]));

                        if(bookingResponse.getService().getKind() == 1){
                            BitmapDescriptor desIc = BitmapDescriptorFactory.fromResource(R.drawable.car_vehicle);
                            mMap.addMarker(new MarkerOptions().position(des).title(viewModel.destination.get()).icon(desIc));
                        }else{
                            BitmapDescriptor desIc = BitmapDescriptorFactory.fromResource(R.drawable.motorcycle);
                            mMap.addMarker(new MarkerOptions().position(des).title(viewModel.destination.get()).icon(desIc));
                        }

                        mMap.addMarker(new MarkerOptions().position(origin).title(viewModel.origin.get()).icon(originIc));

                        LatLngBounds bounds = new LatLngBounds.Builder().include(origin).include(des).build();
                        Point point = new Point();
                        getWindowManager().getDefaultDisplay().getSize(point);

                        //mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, point.x, 150, 20));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(des,zoomLevel > 3.0f ? zoomLevel : 17.0f ));


                    }

                }, err -> {
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {

    }

    public void getCurrentBooking(){
        viewModel.compositeDisposable.add(viewModel.getCurrentBooking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    viewModel.hideLoading();
                    if(response.isResult()){
                        bookingResponse = response.getData();
                        viewModel.getApplication().getWebSocketLiveData().setCodeBooking(response.getData().getCode());
                        viewModel.getApplication().getWebSocketLiveData().sendPing();
                        viewModel.bookingResponse.set(response.getData());
                        if(bookingResponse.getRoom() != null){
                            viewModel.roomId.set(bookingResponse.getRoom().getId());
                        }
                        viewModel.originLat.set(bookingResponse.getPickupLat());
                        viewModel.originLng.set(bookingResponse.getPickupLong());
                        viewModel.destinationLat.set(bookingResponse.getDestinationLat());
                        viewModel.destinationLng.set(bookingResponse.getDestinationLong());
                        viewModel.originLatLng.set(viewModel.originLat.get() + "," + viewModel.originLng.get());
                        viewModel.destinationLatLng.set(viewModel.destinationLat.get() + "," + viewModel.destinationLng.get());
                        //viewModel.originLatLng.set(viewModel.originLat.get() + "," + viewModel.originLng.get());
                        viewModel.getApplication().getWebSocketLiveData().setCodeBooking(response.getData().getCode());

                        Log.d("TAG", "getCurrentBooking: " + bookingResponse.getState());
                        switch (bookingResponse.getState()){
                            case 0:
                                viewModel.isBooking.set(true);
                                viewModel.isFound.set(false);
                                break;
                            case 100:
                                viewModel.isBooking.set(false);
                                viewModel.isFound.set(true);
                                foundDriver();
                                break;
                            case 200:
                                viewModel.isFound.set(false);
                                viewModel.isShipping.set(true);
                                break;
                        }


                    }else{

                    }

                    getOrigin();

                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    err.printStackTrace();
                }));
    }

    public void foundDriver(){
        //handler.removeCallbacks(runnable);
        viewModel.driverName.set(bookingResponse.getDriver().getFullName());
        viewModel.driverAvatar.set(bookingResponse.getDriver().getAvatar());
        viewModel.rate.set(bookingResponse.getAverageRating().floatValue());
        viewModel.licensePlates.set(bookingResponse.getDriverVehicle().getPlate());
        viewModel.vehicle.set(bookingResponse.getDriverVehicle().getName());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent == null){
            return;
        }

        if(intent.getIntExtra(Constants.BOOKING_STATE, 1) == -100){
            viewModel.showErrorMessage(getString(R.string.not_found_driver));
            Log.d("TAG", "onNewIntent: bị hủy r");
            Intent intent1 = new Intent(BookDeliveryActivity.this, HomeActivity.class);
            startActivity(intent1);
            finish();
            return;
        }

        if(intent.getStringExtra(Constants.DRIVER_POSITION) != null){
            String driverPos = intent.getStringExtra(Constants.DRIVER_POSITION);
            viewModel.driverLatLng.set(driverPos);
            loadMapDriverDirection();

            Log.d("TAG", "onNewIntent: driverpos " + driverPos);
            Log.d("TAG", "onNewIntent: driverpos " + Double.valueOf(viewModel.driverLatLng.get().split(",")[0]));
            Log.d("TAG", "onNewIntent: driverpos " + Double.valueOf(viewModel.driverLatLng.get().split(",")[1]));
            return;
        }

        if(viewModel.getApplication().getDriverBookingResponse() != null){
            driverBookingResponse = viewModel.getApplication().getDriverBookingResponse();
            getCurrentBooking();
        }else {
            viewModel.isShipping.set(true);
            viewModel.isFound.set(false);
            viewModel.getApplication().getWebSocketLiveData().sendPing();
            Log.d("TAG", "onNewIntent: đã đoán");
        }


    }
}
