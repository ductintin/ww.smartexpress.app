package ww.smartexpress.app.ui.search;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.response.LocationSaved;
import ww.smartexpress.app.databinding.ActivitySearchBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.search.adapter.LocationSavedAdapter;

public class SearchActivity extends BaseActivity<ActivitySearchBinding, SearchViewModel>{

    LocationSavedAdapter locationSavedAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
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

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            viewModel.categoryId.set(bundle.getLong(Constants.KEY_CATEGORY_ID));
        }
        loadLocationSaved();
    }

    private void loadLocationSaved(){
        //
        List<LocationSaved> locationSavedList = new ArrayList<>();
        locationSavedList.add(new LocationSaved(R.drawable.ic_icon_home_private,"Nhà riêng"));
        locationSavedList.add(new LocationSaved(R.drawable.ic_icon_company,"Công ty"));


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this
                ,LinearLayoutManager.HORIZONTAL, false);
        viewBinding.rcLocationSaved.setLayoutManager(layoutManager);
        locationSavedAdapter = new LocationSavedAdapter(locationSavedList);
        viewBinding.rcLocationSaved.setAdapter(locationSavedAdapter);

        locationSavedAdapter.setOnItemClickListener(cartItem -> {
        });
    }
}
