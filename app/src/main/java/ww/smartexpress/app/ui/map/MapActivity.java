package ww.smartexpress.app.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.response.AddressMap;
import ww.smartexpress.app.databinding.ActivityMapBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.utils.LocationUtils;

public class MapActivity extends BaseActivity<ActivityMapBinding, MapViewModel> implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerDragListener {
    private GoogleMap mMap;
    private Geocoder geocoder;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng currentLatLng;

    private Marker marker;
    private MarkerOptions markerOptions;
    private static final int REQUEST_CHECK_SETTINGS = 1001;
    LocationRequest request;

    LocationManager locMan;

    @Override
    public int getLayoutId() {
        return R.layout.activity_map;
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

        Intent intent = getIntent();
        viewModel.kind.set(intent.getIntExtra("KIND", 1));

        locMan = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapMark);
        mapFragment.getMapAsync(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (LocationUtils.isLocationEnabled(getApplicationContext())) {

                geocoder = new Geocoder(this);

                getLocation();

            } else {
                //requestLocationPermissions();
                request = LocationRequest.create();
                request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(request);

                Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());
                result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                        try {
                            LocationSettingsResponse response = task.getResult(ApiException.class);
                            Log.d("TAG", "onComplete: enabel");
                            // GPS đã được bật, có thể tiếp tục với logic của bạn ở đây
                        } catch (ApiException exception) {
                            if (exception.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                                // GPS chưa được bật, hiển thị cửa sổ yêu cầu bật GPS
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                Log.d("TAG", "onComplete: open");
                                try {
                                    resolvable.startResolutionForResult(MapActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException e) {
                                    // Xử lý nếu không thể bật GPS
                                }
                            }
                        }
                    }
                });
                Log.d("TAG", "onCreate: ");
            }
        }

    }

    @SuppressWarnings("MissingPermission")
    public void getLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(MapActivity.this, location -> {
                    if(location!=null){
                        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        markerOptions = new MarkerOptions();
                        markerOptions.position(currentLatLng).title("").draggable(true);
                        marker = mMap.addMarker(markerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17.0f));
                    }
                });

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
        Log.d("TAG", "onMarkerDragStart: ");
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
        Log.d("TAG", "onMarkerDrag: ");
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        Log.d("TAG", "onMarkerDragEnd: ");
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Log.d("TAG", "onMapReady: ");
        mMap.setMyLocationEnabled(true);

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                Log.d("TAG", "onMapLongClick: " + mMap.getCameraPosition().target.toString());
                viewModel.description.set("");
                viewModel.placeId.set("");
                viewModel.mainText.set("");
                currentLatLng = mMap.getCameraPosition().target;
                marker.setPosition(currentLatLng);
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Log.d("TAG", "setOnCameraIdleListener: " + mMap.getCameraPosition().target.toString());
                viewModel.latlng.set(mMap.getCameraPosition().target.latitude+","+mMap.getCameraPosition().target.longitude);

                viewModel.compositeDisposable.add(viewModel.getLocationInfo()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            String status = response.get("status").getAsString();
                            Log.d("TAG", status);
                            if(status.equals("OK")){
                                JsonArray results = response.getAsJsonArray("results");
                                String component = results.get(0).getAsJsonObject().get("formatted_address").getAsString();
                                String id = results.get(0).getAsJsonObject().get("place_id").getAsString();
                                String locationType = results.get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location_type").getAsString();
                                JsonArray jsonArray = results.get(0).getAsJsonObject().get("types").getAsJsonArray();
                                List<String> types = new ArrayList<>();
                                for (JsonElement element : jsonArray) {
                                    types.add(element.getAsString());
                                }

                                Log.d("TAG", "setOnCameraIdleListener: com " + component);
                                Log.d("TAG", "setOnCameraIdleListener: id "  + id);
                                Log.d("TAG", "setOnCameraIdleListener: location_type "  + locationType);

                                marker.setTitle(component);
                                viewModel.description.set(component);
                                viewModel.mainText.set(component);
                                viewModel.placeId.set(id);
                            }

                        },error->{
                            viewModel.hideLoading();
                            viewModel.showErrorMessage(getString(R.string.network_error));
                            error.printStackTrace();
                        })
                );
            }
        });

//        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(@NonNull LatLng latLng) {
//                Log.d("TAG", "onMapLongClick: " + latLng.toString());
//                currentLatLng = new LatLng(latLng.latitude, latLng.longitude);
//                marker.remove();
//                marker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title("").draggable(true));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17.0f));
//            }
//        });
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
        Log.d("TAG", "onProviderEnabled: ");
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
        Log.d("TAG", "onProviderDisabled: ");
    }

    @Override
    public void requestLocationPermissions() {
        super.requestLocationPermissions();
        Log.d("TAG", "requestLocationPermissions: ");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // GPS đã được bật, có thể tiếp tục với logic của bạn ở đây
                Log.d("TAG", "onActivityResult: OK");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                LocationServices.getFusedLocationProviderClient(MapActivity.this)
                        .requestLocationUpdates(request, new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                //LocationServices.getFusedLocationProviderClient(MapActivity.this).removeLocationUpdates(this);

                                if(locationResult != null && locationResult.getLocations().size() > 0){
                                    int index = locationResult.getLocations().size() - 1;
                                    Log.d("TAG", "onLocationResult: " + locationResult.getLocations().get(index).getLatitude() + locationResult.getLocations().get(index).getLongitude());;
                                    currentLatLng = new LatLng(locationResult.getLocations().get(index).getLatitude(), locationResult.getLocations().get(index).getLongitude());
                                    marker = mMap.addMarker(new MarkerOptions().position(currentLatLng)
                                            .title(""));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17.0f));
                                }
                            }
                        }, null);

            } else if (resultCode == RESULT_CANCELED) {
                // Người dùng đã từ chối bật GPS, xử lý tương ứng (ví dụ: thông báo cho người dùng)
                Log.d("TAG", "onActivityResult: cancel");
                onBackPressed();
            }
        }
    }

    public void chooseAddress(){
        AddressMap addressMap = AddressMap.builder()
                .description(viewModel.description.get())
                .id(viewModel.placeId.get())
                .kind(viewModel.kind.get())
                .build();

        Log.d("TAG", "chooseAddress: " + addressMap.getDescription());
        EventBus.getDefault().postSticky(addressMap);
        onBackPressed();
    }
}
