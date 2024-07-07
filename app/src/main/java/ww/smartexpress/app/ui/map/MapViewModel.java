package ww.smartexpress.app.ui.map;

import androidx.databinding.ObservableField;

import com.google.gson.JsonObject;

import io.reactivex.rxjava3.core.Observable;
import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class MapViewModel extends BaseViewModel {
    public ObservableField<String> name = new ObservableField<>("");
    public ObservableField<String> latlng = new ObservableField<>("");
    public ObservableField<String> lat = new ObservableField<>("");
    public ObservableField<String> lng = new ObservableField<>("");
    public ObservableField<String> description = new ObservableField<>("");
    public ObservableField<String> mainText = new ObservableField<>("");
    public ObservableField<String> placeId = new ObservableField<>("");
    public ObservableField<String> country = new ObservableField<>("");
    public ObservableField<Boolean> isValidLocation = new ObservableField<>(false);
    public ObservableField<Integer> kind = new ObservableField<>(0);
    public ObservableField<String> locationId = new ObservableField<>("");
    public MapViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    Observable<JsonObject> getLocationInfo(String id) {
        return repository.getApiService().getLocationInfo(id, Constants.GEO_API_KEY)
                .doOnNext(response -> {

                });
    }

    Observable<JsonObject> getLocationInfo() {
        description.set("");
        placeId.set("");
        mainText.set("");
        return repository.getApiService().getLocationInfoByLatLng(latlng.get(), Constants.GEO_API_KEY)
                .doOnNext(response -> {

                });
    }
}
