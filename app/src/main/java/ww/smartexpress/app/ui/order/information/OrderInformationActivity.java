package ww.smartexpress.app.ui.order.information;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;

import org.apache.commons.validator.routines.EmailValidator;
import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.Commodity;
import ww.smartexpress.app.data.model.api.response.ShippingInfo;
import ww.smartexpress.app.databinding.ActivityOrderInformationBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.order.information.adapter.BitmapAdapter;
import ww.smartexpress.app.ui.order.information.adapter.CommodityAdapter;

public class OrderInformationActivity extends BaseActivity<ActivityOrderInformationBinding, OrderInformationViewModel> {
    
    CommodityAdapter commodityAdapter;
    CommodityAdapter commoditySizeAdapter;

    BitmapAdapter bitmapAdapter;
    private Bitmap updatedAvatar;
    private List<Bitmap> bitmaps;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    String cameraPermission[];
    String storagePermission[];

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_information;
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
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        bitmaps = new ArrayList<>();

        loadTaskPicture();

        viewBinding.setLifecycleOwner(this);
        loadCommodity();
        loadSize();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            ShippingInfo shippingInfo = ApiModelUtils.fromJson(bundle.getString(Constants.SHIPPING_INFO), ShippingInfo.class);
            //viewModel.customerNote.set(getIntent().getStringExtra(Constants.CUSTOMER_BOOKING_NOTE));


            Log.d("TAG", "dd: " + shippingInfo.toString());

           if(shippingInfo != null){
               viewModel.senderName.set(shippingInfo.getSenderName());
               viewModel.senderPhone.set(shippingInfo.getSenderPhone());
               viewModel.consigneeName.set(shippingInfo.getConsigneeName());
               viewModel.consigneePhone.set(shippingInfo.getConsigneePhone());
               viewModel.origin.set(shippingInfo.getOrigin());
               viewModel.destination.set(shippingInfo.getDestination());
               viewModel.selectCOD.set(shippingInfo.getIsCod());
               viewModel.codPrice.set(String.valueOf(shippingInfo.getCodPrice()));
               getProfile();
           }
        }
    }

    public void getProfile(){
        viewModel.showLoading();
        viewModel.compositeDisposable.add(viewModel.getProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.isResult()){
                        viewModel.senderName.set(response.getData().getName());
                        viewModel.senderPhone.set(response.getData().getPhone());
                        viewModel.hideLoading();
                    }else {
                        viewModel.hideLoading();
                        viewModel.showErrorMessage(response.getMessage());
                    }
                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    private void loadCommodity(){
        //
        List<Commodity> CommodityList = new ArrayList<>();
        CommodityList.add(new Commodity("Thực phẩm",false));
        CommodityList.add(new Commodity("Thuốc men",false));
        CommodityList.add(new Commodity("Sách vở",false));
        CommodityList.add(new Commodity("Quần áo",false));
        CommodityList.add(new Commodity("Dễ vỡ",false));
        CommodityList.add(new Commodity("Khác...",false));


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        viewBinding.rcCommodity.setLayoutManager(layoutManager);
        commodityAdapter = new CommodityAdapter(CommodityList);
        viewBinding.rcCommodity.setAdapter(commodityAdapter);
        commodityAdapter.setOnItemClickListener(commodity -> {
        });
    }

    public void loadSize(){
        List<Commodity> CommodityList = new ArrayList<>();
        CommodityList.add(new Commodity("Nhỏ",false));
        CommodityList.add(new Commodity("Trung bình",false));
        CommodityList.add(new Commodity("Lớn",false));


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        viewBinding.rcSize.setLayoutManager(layoutManager);
        commoditySizeAdapter = new CommodityAdapter(CommodityList);
        viewBinding.rcSize.setAdapter(commoditySizeAdapter);
        commoditySizeAdapter.setOnItemClickListener(commodity -> {
        });
    }

    private void loadTaskPicture(){

        bitmapAdapter = new BitmapAdapter(viewModel.bitmaps.get());
        viewModel.size.set(bitmapAdapter.getItemCount());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        viewBinding.rcTaskPicture.setLayoutManager(layoutManager);
        viewBinding.rcTaskPicture.setAdapter(bitmapAdapter);

        bitmapAdapter.setOnItemClickListener(new BitmapAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position) {

            }

            @Override
            public void remove(int position) {
                bitmapAdapter.clearItem(position);
                viewModel.size.set(bitmapAdapter.getItemCount());
            }
        });
    }

    public void getNewAvatar() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        takeFromCamera();
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        takeFromGallery();
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void takeFromCamera() {
        ImagePicker.with(this)
                .cameraOnly()
                .cropSquare()
                .start();
    }

    private void takeFromGallery() {
        ImagePicker.with(this)
                .galleryOnly()
                .cropSquare()
                .start();
    }

    public Uri getUriFromBitmap(Context context, Bitmap bitmap) {
        Uri uri = null;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        uri = Uri.parse(path);
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri resultUri = data.getData();
                if(resultUri == null) return;
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(resultUri);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                final Bitmap photo = BitmapFactory.decodeStream(imageStream);
//                viewBinding.imageViewAva.setImageBitmap(photo)
//                bitmaps = viewModel.bitmaps.get();
//                bitmaps.add(photo);
//                viewModel.bitmaps.set(bitmaps);
                bitmapAdapter.addPicture(photo);
                viewModel.size.set(bitmapAdapter.getItemCount());
                updatedAvatar = photo;
            }
        }

    }

    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    // checking storage permissions
    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // checking camera permissions
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        takeFromCamera();
                    } else {
                        viewModel.showErrorMessage("Please Enable Camera and Storage Permissions");
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        takeFromGallery();
                    } else {
                        viewModel.showErrorMessage("Please Enable Storage Permissions");
                    }
                }
            }
            break;
        }
    }

    public Boolean isValidRegisterRequest(ShippingInfo request){
        String phoneRegex = "^(0[3|5|7|8|9])+([0-9]{8})$";
        Pattern phonePatter = Pattern.compile(phoneRegex);

        if(TextUtils.isEmpty(request.getSenderName())){
            viewModel.showErrorMessage("Vui lòng nhập tên người nhận");
            return false;
        }
        if(TextUtils.isEmpty(request.getSenderPhone()) || request.getSenderPhone().length() != 10 || !phonePatter.matcher(request.getSenderPhone()).matches()){
            viewModel.showErrorMessage("Số điện thoại người gửi không hợp lệ");
            return false;
        }


        if(TextUtils.isEmpty(request.getConsigneeName())){
            viewModel.showErrorMessage("Vui lòng nhập tên người nhận");
            return false;
        }

        if(TextUtils.isEmpty(request.getConsigneePhone()) || request.getConsigneePhone().length() != 10 || !phonePatter.matcher(request.getConsigneePhone()).matches()){
            viewModel.showErrorMessage("Số điện thoại người nhận không hợp lệ");
            return false;
        }

        if(request.getIsCod()){
            if(request.getCodPrice() == 0){
                viewModel.showErrorMessage("Vui lòng nhập số tiền thu hộ");
                return false;
            }
        }

        return true;
    }
    public void doNext(){
        try {
            ShippingInfo shippingInfo = ShippingInfo.builder()
                    .senderName(viewModel.senderName.get().trim())
                    .senderPhone(viewModel.senderPhone.get().trim())
                    .consigneeName(viewModel.consigneeName.get().trim())
                    .consigneePhone(viewModel.consigneePhone.get().trim())
                    .customerNote(viewModel.customerNote.get().trim())
                    .isCod(viewModel.selectCOD.get())
                    .codPrice(!viewModel.codPrice.get().isEmpty() && viewModel.selectCOD.get() ? Integer.parseInt(viewModel.codPrice.get()) : 0)
                    .build();
            if(isValidRegisterRequest(shippingInfo)){
                EventBus.getDefault().postSticky(shippingInfo);
                onBackPressed();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
