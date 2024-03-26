package ww.smartexpress.app.ui.delivery;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.BookCar;
import ww.smartexpress.app.databinding.ActivityBookDeliveryBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.bookcar.adpater.BookCarAdapter;
import ww.smartexpress.app.ui.home.HomeActivity;
public class BookDeliveryActivity extends BaseActivity<ActivityBookDeliveryBinding, BookDeliveryViewModel> implements OnMapReadyCallback {
    private GoogleMap mMap;
    BottomSheetBehavior sheetBehavior;
    BookCarAdapter bookCarAdapter;
    long delayMillis = 5000;
    Handler handler = new Handler();
    Runnable runnable;

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

        loadBookCar();
        bottomSheetLayout();

        viewBinding.bntBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findingDriver();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14.0f));
    }

    private void loadBookCar(){
        //
        List<BookCar> bookCars = new ArrayList<>();
        bookCars.add(new BookCar(1L, "Giao nhanh",null,100000.0,10.0));
        bookCars.add(new BookCar(2L,"Giao tiết kiệm",null,100000.0,10.0));
        bookCars.add(new BookCar(3L,"Hỏa tốc",null,100000.0,10.0));


        RecyclerView.LayoutManager layoutBookCar = new LinearLayoutManager(this
                ,LinearLayoutManager.VERTICAL, false);
        viewBinding.rcWinDelivery.setLayoutManager(layoutBookCar);
        viewBinding.rcWinDelivery.setItemAnimator(new DefaultItemAnimator());
        bookCarAdapter = new BookCarAdapter(bookCars);
        viewBinding.rcWinDelivery.setAdapter(bookCarAdapter);

        viewModel.deliveryMethod.set(bookCars.get(0).getName());
        viewModel.discount.set(bookCars.get(0).getDiscount().intValue());

        bookCarAdapter.setOnItemClickListener(bookCar -> {
            viewModel.deliveryMethod.set(bookCar.getName());
            viewModel.discount.set(bookCar.getDiscount().intValue());
            if(sheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bookCars.remove(bookCar);
                bookCars.add(0,bookCar);
                bookCarAdapter.setSelected(0);
                bookCarAdapter.notifyDataSetChanged();
                viewBinding.rcWinDelivery.smoothScrollToPosition(0);
            }
            Log.d("Click", "performDataBinding: ");
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
        viewModel.isBooking.set(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        runnable = new Runnable() {
            @Override
            public void run() {
                foundDriver(handler, this::run);
            }
        };

        handler.postDelayed(runnable, delayMillis);

        viewBinding.bntCancelBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.isBooking.set(false);
                handler.removeCallbacks(runnable);
            }
        });
    }

    public void foundDriver(Handler handler, Runnable runnable){
        viewModel.isBooking.set(false);
        viewModel.isFound.set(true);
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {
        if(!viewModel.isFound.get()){
            super.onBackPressed();
        }else{
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }
}
