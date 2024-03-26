package ww.smartexpress.app.ui.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.ObservableField;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.response.SearchLocation;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.search.location.SearchLocationActivity;

public class SearchViewModel extends BaseViewModel {
    public ObservableField<Long> categoryId = new ObservableField<>();
    public SearchViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void searchLocation(){
        Intent intent = new Intent(application.getCurrentActivity(), SearchLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.KEY_CATEGORY_ID, categoryId.get());
        intent.putExtras(bundle);
        application.getCurrentActivity().startActivity(intent);
    }

    public void back(){
        getApplication().getCurrentActivity().onBackPressed();
    }

    public void getNotifications(){

    }
}
