package ww.smartexpress.app.ui.search.food;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.databinding.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.HotFood;
import ww.smartexpress.app.data.model.api.response.FoodSuggest;
import ww.smartexpress.app.databinding.ActivitySearchFoodBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.search.food.adapter.FoodSuggestAdapter;
import ww.smartexpress.app.ui.search.food.adapter.HotFoodAdapter;

public class SearchFoodActivity extends BaseActivity<ActivitySearchFoodBinding, SearchFoodViewModel> {
    
    FoodSuggestAdapter foodSuggestAdapter;
    HotFoodAdapter hotFoodAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_search_food;
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

        if(viewModel.textSearch.get()==null){
            loadHotFood();
        }else{
            loadFoodSuggest();
        }
        viewBinding.edtSearchFood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(viewModel.textSearch.get()==null){
                    loadHotFood();
                }else{
                    loadFoodSuggest();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void loadFoodSuggest(){
        //
        List<FoodSuggest> FoodSuggestList = new ArrayList<>();
        FoodSuggestList.add(new FoodSuggest("a"));
        FoodSuggestList.add(new FoodSuggest("a"));
        FoodSuggestList.add(new FoodSuggest("a"));
        FoodSuggestList.add(new FoodSuggest("a"));
        FoodSuggestList.add(new FoodSuggest("a"));
        FoodSuggestList.add(new FoodSuggest("a"));
        FoodSuggestList.add(new FoodSuggest("a"));
        FoodSuggestList.add(new FoodSuggest("a"));


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this
                ,LinearLayoutManager.VERTICAL, false);
        viewBinding.rcFoodSuggest.setLayoutManager(layoutManager);
        foodSuggestAdapter = new FoodSuggestAdapter(FoodSuggestList);
        viewBinding.rcFoodSuggest.setAdapter(foodSuggestAdapter);

        foodSuggestAdapter.setOnItemClickListener(foodSuggest -> {
        });
    }

    private void loadHotFood(){
        //
        List<HotFood> HotFoodList = new ArrayList<>();
        HotFoodList.add(new HotFood("Gà rán"));
        HotFoodList.add(new HotFood("Gỏi cuốn"));
        HotFoodList.add(new HotFood("Cơm tấm"));
        HotFoodList.add(new HotFood("Bún bò"));
        HotFoodList.add(new HotFood("Gongcha"));
        HotFoodList.add(new HotFood("Bún đậu mắn tôm"));


        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        viewBinding.rcHotFood.setLayoutManager(layoutManager);
        hotFoodAdapter = new HotFoodAdapter(HotFoodList);
        viewBinding.rcHotFood.setAdapter(hotFoodAdapter);

        hotFoodAdapter.setOnItemClickListener(hotFood -> {
            viewModel.textSearch.set(hotFood.getName());
        });
    }
    
}
