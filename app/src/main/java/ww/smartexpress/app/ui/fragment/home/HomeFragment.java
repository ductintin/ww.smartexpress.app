package ww.smartexpress.app.ui.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.response.Banner;
import ww.smartexpress.app.data.model.api.response.CategoryResponse;
import ww.smartexpress.app.data.model.api.response.Food;
import ww.smartexpress.app.data.model.api.response.SearchLocation;
import ww.smartexpress.app.data.model.api.response.ServiceResponse;
import ww.smartexpress.app.databinding.FragmentHomeBinding;
import ww.smartexpress.app.di.component.FragmentComponent;
import ww.smartexpress.app.ui.base.fragment.BaseFragment;
import ww.smartexpress.app.ui.fragment.home.adapter.BannerAdapter;
import ww.smartexpress.app.ui.fragment.home.adapter.CategoryAdapter;
import ww.smartexpress.app.ui.fragment.home.adapter.FoodAdapter;
import ww.smartexpress.app.ui.fragment.home.adapter.ServiceAdapter;
import ww.smartexpress.app.ui.search.SearchActivity;
import ww.smartexpress.app.ui.search.location.SearchLocationActivity;
import ww.smartexpress.app.ui.shipping.address.search.SearchAddressActivity;
import ww.smartexpress.app.ui.store.StoreActivity;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeFragmentViewModel> {

    BannerAdapter bannerAdapter;
    CategoryAdapter categoryAdapter;
    ServiceAdapter serviceAdapter;
    FoodAdapter foodAdapter;

    List<CategoryResponse> categories = new ArrayList<>();
    List<ServiceResponse> services = new ArrayList<>();

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void performDataBinding() {
        loadProfile();
        //loadCategory();
        loadService();
        loadBanner();
        loadFood();
        //loadSearch();
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    private void loadBanner(){
        //
        List<Banner> banners = new ArrayList<>();
        banners.add(new Banner(R.drawable.banner));
        banners.add(new Banner(R.drawable.banner));
        banners.add(new Banner(R.drawable.banner));

        RecyclerView.LayoutManager layoutBanner = new LinearLayoutManager(getActivity().getApplicationContext()
                ,LinearLayoutManager.HORIZONTAL, false);
        binding.rcBanner.setLayoutManager(layoutBanner);
        binding.rcBanner.setItemAnimator(new DefaultItemAnimator());
        bannerAdapter = new BannerAdapter(banners);
        binding.rcBanner.setAdapter(bannerAdapter);
        bannerAdapter.setOnItemClickListener(banner -> {
            Log.d("Click", "performDataBinding: ");
        });
    }

    private void loadCategory(){
        viewModel.compositeDisposable.add(viewModel.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.isResult()){
                        viewModel.hideLoading();

                        categories = response.getData().getContent();

                        binding.setLifecycleOwner(this);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()
                                ,LinearLayoutManager.HORIZONTAL, false);
                        binding.rcCategory.setLayoutManager(layoutManager);
                        categoryAdapter = new CategoryAdapter(categories);
                        binding.rcCategory.setAdapter(categoryAdapter);

                        viewModel.categoryListLiveData.setValue(categories);

                        categoryAdapter.setOnItemClickListener(category -> {
                            Intent intent = new Intent();
                            if(category.getName().equals("Giao hàng")){
                                intent = new Intent(getActivity(), SearchAddressActivity.class);
                            }else{
                                intent = new Intent(getActivity(), SearchLocationActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong(Constants.KEY_CATEGORY_ID, category.getId());
                                intent.putExtras(bundle);
                            }
                            startActivity(intent);
                        });

                    }else {
                        viewModel.hideLoading();
                        viewModel.showErrorMessage(viewModel.getApplication().getErrorUtils().handelError(response.getCode()));
                    }
                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getActivity().getString(R.string.network_error));
                    error.printStackTrace();
                })
        );


    }

    private void loadService(){
        viewModel.compositeDisposable.add(viewModel.getService()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.isResult()){
                        viewModel.hideLoading();

                        services = response.getData().getContent();

                        binding.setLifecycleOwner(this);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()
                                ,LinearLayoutManager.HORIZONTAL, false);
                        binding.rcCategory.setLayoutManager(layoutManager);
                        serviceAdapter = new ServiceAdapter(services);
                        binding.rcCategory.setAdapter(serviceAdapter);

                        serviceAdapter.setOnItemClickListener(service -> {
                            Intent intent = new Intent(getActivity(), SearchLocationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putLong(Constants.KEY_SERVICE_ID, service.getId());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });

                    }else {
                        viewModel.hideLoading();
                        viewModel.showErrorMessage(viewModel.getApplication().getErrorUtils().handelError(response.getCode()));
                    }
                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getActivity().getString(R.string.network_error));
                    error.printStackTrace();
                })
        );


    }

    private void loadFood(){
        //
        List<Food> foods = new ArrayList<>();
        foods.add(new Food("Trà sữa Bubble - Ung Văn Khiêm", R.drawable.product));
        foods.add(new Food("Combo Burger - Ung Văn Khiêm", R.drawable.product));
        foods.add(new Food("Trà sữa Bubble - Ung Văn Khiêm", R.drawable.product));
        foods.add(new Food("Combo Burger - Ung Văn Khiêm", R.drawable.product));

        RecyclerView.LayoutManager layoutSg = new LinearLayoutManager(getActivity().getApplicationContext()
                ,LinearLayoutManager.HORIZONTAL, false);
        binding.rcSg.setLayoutManager(layoutSg);
        binding.rcSg.setItemAnimator(new DefaultItemAnimator());
        foodAdapter = new FoodAdapter(foods);
        binding.rcSg.setAdapter(foodAdapter);

        //
        RecyclerView.LayoutManager layoutPb = new LinearLayoutManager(getActivity().getApplicationContext()
                ,LinearLayoutManager.HORIZONTAL, false);
        binding.rcPb.setLayoutManager(layoutPb);
        binding.rcPb.setItemAnimator(new DefaultItemAnimator());
        binding.rcPb.setAdapter(foodAdapter);

        foodAdapter.setOnItemClickListener(food -> {
            startActivity(new Intent(getActivity(), StoreActivity.class));
        });
    }

    public void loadProfile(){
        viewModel.showLoading();
        viewModel.compositeDisposable.add(viewModel.setProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.isResult()){
                        viewModel.hideLoading();
                    }else {
                        viewModel.hideLoading();
                        viewModel.showErrorMessage(viewModel.getApplication().getErrorUtils().handelError(response.getCode()));
                    }
                },error->{
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getActivity().getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfile();
    }

//    public void loadSearch(){
//        viewModel.compositeDisposable.add(viewModel.searchLocation()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//
//                },error->{
//                    viewModel.hideLoading();
//                    viewModel.showErrorMessage(getActivity().getString(R.string.network_error));
//                    error.printStackTrace();
//                })
//        );
//    }
}
