package ww.smartexpress.app.ui.profile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.Executor;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.request.LoginRequest;
import ww.smartexpress.app.data.model.api.request.UpdateProfileRequest;
import ww.smartexpress.app.data.model.api.response.ProfileResponse;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.databinding.ActivityEditProfileBinding;
import ww.smartexpress.app.databinding.BaseDialogBinding;
import ww.smartexpress.app.databinding.DialogUploadAvtBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.utils.AES;
import ww.smartexpress.app.utils.BindingUtils;

public class EditProfileActivity extends BaseActivity<ActivityEditProfileBinding, EditProfileViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_profile;
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
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void dialogUpLoadAvatar() {

        Dialog dialog = new Dialog(EditProfileActivity.this);
        DialogUploadAvtBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(EditProfileActivity.this),R.layout.dialog_upload_avt,null, true);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(dialogBinding.getRoot());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetService;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
        dialog.setCanceledOnTouchOutside(true);


        dialogBinding.btnTakePt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        takeFromCamera();
                        dialog.dismiss();
                    }
            }
        });

        dialogBinding.btnChoosePt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        takeFromGallery();
                        dialog.dismiss();
                    }
            }
        });

        dialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private Bitmap updatedAvatar;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    String cameraPermission[];
    String storagePermission[];

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //setTheme(R.style.WhiteAppTheme);
        super.onCreate(savedInstanceState);


        viewBinding.setA(this);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        getProfileLocal();
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @lombok.NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);


            }

            @Override
            public void onAuthenticationSucceeded(
                    @lombok.NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                try {

                    Log.d("TAG", "onAuthenticationSucceeded: " + viewModel.encryptedPassword.get());
                    viewModel.password.set(viewModel.getApplication().getAes().decrypt(viewModel.encryptedPassword.get()));
                    viewBinding.edtPw.clearFocus();
                    Log.d("TAG", "onAuthenticationSucceeded: de " + viewModel.password.get());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Xác thực vân tay cho đăng nhập ứng dụng")
                .setNegativeButtonText("Sử dụng mật khẩu")
                .build();

        viewModel.isVisibility.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(androidx.databinding.Observable sender, int propertyId) {
                if(!viewModel.isVisibility.get()){
                    viewBinding.edtPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    viewBinding.edtPw.setTransformationMethod(null);;
                }

                viewBinding.edtPw.setSelection(viewBinding.edtPw.length());
            }
        });

    }

    public void getProfileLocal(){
        viewModel.getProfileLocal().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserEntity>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull UserEntity userEntity) {
                        viewModel.userEntityObservableField.set(userEntity);
                        viewModel.avatar.set(userEntity.getAvatar());
                        if(viewModel.avatar.get() == null){
                            viewBinding.civAvatar.setImageResource(R.drawable.avatar_holder);
                        }else{
                            BindingUtils.setImageUrl(viewBinding.civAvatar,viewModel.avatar.get());
                        }
                        viewModel.fullName.set(userEntity.getName());
                        viewModel.email.set(userEntity.getEmail());
                        viewModel.bankCard.set(userEntity.getBankCard());

                        Log.d("TAG", "onSuccess: " + userEntity.getEncryptedPassword());
                        if(userEntity.getIsBiometric()){
                            viewModel.encryptedPassword.set(userEntity.getEncryptedPassword());
                            viewBinding.edtPw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean b) {
                                    Log.d("TAG", "onFocusChange: " + b);
                                    if(b){
                                        biometricPrompt.authenticate(promptInfo);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
    }
    public void updateAvatar(){
        if(updatedAvatar != null){
            // Upload avatar then update profile

            // Convert the Bitmap to a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            updatedAvatar.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageByteArray = byteArrayOutputStream.toByteArray();

            // Create a request body for the image
            RequestBody requestFile = RequestBody.create( imageByteArray, MediaType.parse("image/jpeg"));

            // Create a multipart request builder
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            requestBodyBuilder.addFormDataPart("file", "image.jpg", requestFile);
            requestBodyBuilder.addFormDataPart("type", Constants.FILE_TYPE_AVATAR);

            UpdateProfileRequest request = prepareRequest();

            viewModel.showLoading();
            viewModel.compositeDisposable.add(viewModel.uploadAvatar(requestBodyBuilder.build())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(uploadResponse -> {
                        if (uploadResponse.isResult() && uploadResponse.getData().getFilePath() != null) {
                            viewModel.avatar.set(uploadResponse.getData().getFilePath());
                            updatedAvatar =  null;
                            handleUpdateProfile();
                        }else{
                            viewModel.showErrorMessage(uploadResponse.getMessage());
                        }
                    }, error ->{
                        error.printStackTrace();
                    })
            );
        }else{
            handleUpdateProfile();
        }
    }

    public void loadProfile(){
        viewModel.showLoading();
        viewModel.compositeDisposable.add(viewModel.getProfileObserve()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    viewModel.hideLoading();
                    if(response.isResult()){
                        viewModel.avatar.set(response.getData().getAvatar());
                        viewModel.fullName.set(response.getData().getName());
                        viewModel.email.set(response.getData().getEmail());
                    }else {
                        viewModel.getApplication().getErrorUtils().handelError(response.getCode());
                    }
                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }
    public UpdateProfileRequest prepareRequest() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setAvatar(viewModel.avatar.get());
        request.setName(viewModel.fullName.get());
        request.setOldPassword(viewModel.password.get());
        request.setNewPassword(viewModel.password.get());
        request.setBankCard(viewModel.bankCard.get());
        return request;
    }


    public void handleUpdateProfile() {
        UpdateProfileRequest request = prepareRequest();
        viewModel.showLoading();
        viewModel.compositeDisposable.add(viewModel.updateProfile(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    viewModel.hideLoading();
                    // render the avatar
                    if (response.isResult()) {
                        viewModel.showSuccessMessage(application.getString(R.string.update_profile_success));
                        viewModel.password.set("");
                        viewBinding.edtPw.clearFocus();
                        viewModel.storeProfile();
                    } else {
                        viewModel.avatar.set(viewModel.userEntityObservableField.get().getAvatar());
                        viewModel.password.set("");
                        viewModel.getApplication().getErrorUtils().handelError(response.getCode());
                    }

                }, err -> {
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(application.getString(R.string.network_error));
                }));
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
        ImagePicker.with(EditProfileActivity.this)
                .cameraOnly()
                .cropSquare()
                .start();
    }

    private void takeFromGallery() {
        ImagePicker.with(EditProfileActivity.this)
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
                viewBinding.civAvatar.setImageBitmap(photo);
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

}
