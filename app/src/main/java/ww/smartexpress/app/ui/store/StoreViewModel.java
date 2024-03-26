package ww.smartexpress.app.ui.store;

import android.content.Intent;

import androidx.databinding.ObservableField;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.cart.CartActivity;
import ww.smartexpress.app.ui.purchase.PurchaseActivity;

public class StoreViewModel extends BaseViewModel {
    public ObservableField<Integer> count = new ObservableField<Integer>(0);
    public ObservableField<Integer> price = new ObservableField<Integer>(0);
    public StoreViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void selectDrinks(){

    }
    public void selectFastFood(){

    }
    public void addToCart(){
        Intent intent =new Intent(getApplication().getCurrentActivity(), CartActivity.class);
        getApplication().getCurrentActivity().startActivity(intent);
    }
    public void payment(){
        Intent intent =new Intent(getApplication().getCurrentActivity(), PurchaseActivity.class);
        getApplication().getCurrentActivity().startActivity(intent);
    }

    public void back(){
        getApplication().getCurrentActivity().onBackPressed();
    }
    public void favoriteStore(){

    }
}
