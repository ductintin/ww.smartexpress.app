package ww.smartexpress.app.ui.search.food;

import androidx.databinding.ObservableField;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;

public class SearchFoodViewModel extends BaseViewModel {

    public ObservableField<String> textSearch = new ObservableField<>();
    public SearchFoodViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void back(){
        application.getCurrentActivity().onBackPressed();
    }

    public void deleteTextSearch(){
        textSearch.set("");
    }
}
