package ww.smartexpress.app.ui.purchase;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.cart.CartActivity;
import ww.smartexpress.app.ui.note.NoteActivity;
import ww.smartexpress.app.ui.order.details.OrderDetailsActivity;
import ww.smartexpress.app.ui.shipping.address.ShippingAddressActivity;

public class PurchaseViewModel extends BaseViewModel {

    public MutableLiveData<Integer> paymentMethod = new MutableLiveData<>(0);

    public ObservableField<Integer> amount = new ObservableField<>(1);
    public ObservableField<String> total = new ObservableField<>("30.000");
    public ObservableField<Integer> price = new ObservableField<>(80000);

    public PurchaseViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void doBack(){
        getApplication().getCurrentActivity().onBackPressed();
    }

    public void payCredit(){
        paymentMethod.setValue(1);
    }

    public void payCash(){
        paymentMethod.setValue(0);
    }

    public void increaseAmount(){
        amount.set(amount.get() + 1);
        total.set(parse(amount.get() * price.get()));
    }

    public void decreaseAmount(){
        if(amount.get() < 2)
            return;
        amount.set(amount.get() - 1);
        total.set(parse(amount.get() * price.get()));
    }

    public void doPurchase(){
        Intent intent = new Intent(application.getCurrentActivity(), OrderDetailsActivity.class);
        intent.putExtra("payment", true);
        application.getCurrentActivity().startActivity(intent);
        application.getCurrentActivity().finish();
    }

    public String parse(int x){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        return decimalFormat.format(x);
    }

    public void searchDestination(){
        Intent intent = new Intent(application.getCurrentActivity(), ShippingAddressActivity.class);
        application.getCurrentActivity().startActivity(intent);
    }

    public void doNote(){
        Intent intent = new Intent(application.getCurrentActivity(), NoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("noteHint", 1);
        intent.putExtras(bundle);
        application.getCurrentActivity().startActivity(intent);
    }
}
