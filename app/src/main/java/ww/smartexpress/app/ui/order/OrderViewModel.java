package ww.smartexpress.app.ui.order;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableField;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.databinding.ItemToastFavRestaurantBinding;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.cart.CartActivity;

public class OrderViewModel extends BaseViewModel {
    public ObservableField<Integer> amount = new ObservableField<>(1);
    public ObservableField<String> total = new ObservableField<>("30.000");
    public ObservableField<String> cost = new ObservableField<>("30.000đ");
    public ObservableField<String> name = new ObservableField<>("Trà sữa truyền thống");
    public ObservableField<Integer> sold = new ObservableField<>(15);
    public ObservableField<Integer> comment = new ObservableField<>(10);
    public ObservableField<Boolean> isFav = new ObservableField<>(false);
    public ObservableField<Integer> price = new ObservableField<>(80000);
    public ObservableField<Boolean> isExpand = new ObservableField<>(false);
    public OrderViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void back(){
        application.getCurrentActivity().onBackPressed();
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

    public void doFav(){
        isFav.set(!isFav.get());
        ItemToastFavRestaurantBinding binding = ItemToastFavRestaurantBinding.inflate(getApplication().getCurrentActivity().getLayoutInflater());
        View layout = binding.getRoot();
        binding.setIsFav(isFav.get());
        Toast toast = new Toast(application.getCurrentActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        //View view = application.getCurrentActivity().getLayoutInflater().inflate(R.layout.item_toast_fav_restaurant, (ViewGroup) application.getCurrentActivity().findViewById(R.id.customToast));
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 150);
        toast.setView(layout);
        toast.show();
    }

    public void addToCart(){
        Intent intent = new Intent(application.getCurrentActivity(), CartActivity.class);
        application.getCurrentActivity().startActivity(intent);
        application.getCurrentActivity().finish();
    }

    public String parse(int x){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        return decimalFormat.format(x);
    }
}
