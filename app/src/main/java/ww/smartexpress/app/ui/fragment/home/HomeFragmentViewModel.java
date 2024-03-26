package ww.smartexpress.app.ui.fragment.home;

import android.content.Intent;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.ResponseListObj;
import ww.smartexpress.app.data.model.api.ResponseWrapper;
import ww.smartexpress.app.data.model.api.response.Category;
import ww.smartexpress.app.data.model.api.response.CategoryResponse;
import ww.smartexpress.app.data.model.api.response.ProfileResponse;
import ww.smartexpress.app.data.model.api.response.ServiceResponse;
import ww.smartexpress.app.data.model.room.UserEntity;
import ww.smartexpress.app.ui.base.fragment.BaseFragmentViewModel;
import ww.smartexpress.app.ui.search.food.SearchFoodActivity;

public class HomeFragmentViewModel extends BaseFragmentViewModel {

    public MutableLiveData<List<CategoryResponse>> categoryListLiveData;
    public ObservableField<List<CategoryResponse>> categories = new ObservableField<>(new ArrayList<>());
    public ObservableField<List<ServiceResponse>> services = new ObservableField<>(new ArrayList<>());
    public ObservableField<ProfileResponse> profile = new ObservableField<>(new ProfileResponse());
    public HomeFragmentViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
        categoryListLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<CategoryResponse>> getCategoryListLiveData() {
        return categoryListLiveData;
    }
    public void openNotification(){

    }
    public void showAllCategory(){

    }
    public void showAllPropose(){

    }
    public void showAllPopularFood(){

    }

    public void searchFood(){
        Intent intent = new Intent(application.getCurrentActivity(), SearchFoodActivity.class);
        application.getCurrentActivity().startActivity(intent);
    }

    Observable<ResponseWrapper<ResponseListObj<CategoryResponse>>> getCategory() {
        return repository.getApiService().getCategoryAutoComplete(2, null, null, null)
                .doOnNext(response -> {
                    //categories.set(response.getData().getContent());
                    for (CategoryResponse cr : response.getData().getContent()){
                        categories.get().add(cr);
                    }
                });
    }

    Observable<ResponseWrapper<ResponseListObj<ServiceResponse>>> getService() {
        return repository.getApiService().getServiceAutoComplete(null, null, null, null, null)
                .doOnNext(response -> {
                    //categories.set(response.getData().getContent());
                    for (ServiceResponse sr : response.getData().getContent()){
                        services.get().add(sr);
                    }
                });
    }

    Observable<ResponseWrapper<ProfileResponse>> setProfile() {
        return repository.getApiService().getProfile()
                .doOnNext(response -> {
                    profile.set(response.getData());

                    UserEntity userEntity = new UserEntity();
                    userEntity.setId(response.getData().getId());
                    userEntity.setName(response.getData().getName());
                    userEntity.setAvatar(response.getData().getAvatar());
                    userEntity.setPhone(response.getData().getPhone());
                    userEntity.setEmail(response.getData().getEmail());
                    userEntity.setStatus(response.getData().getStatus());
                    repository.getRoomService().userDao().insert(userEntity)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {

                            }, throwable -> {

                            });

                });
    }

//    Observable<JsonElement> searchLocation() {
//        return repository.getApiService().searchLocation("Phu Yen", "AIzaSyAP9ViAFSCQHr4i_DjkbKcj0Lj2BarZNIk")
//                .doOnNext(response -> {
//                    Log.d("TAG", "searchLocation: " + response.toString());
//                });
//    }
}
