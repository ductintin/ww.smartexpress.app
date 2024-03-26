package ww.smartexpress.app.di.module;

import android.content.Context;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.ViewModelProviderFactory;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.di.scope.FragmentScope;
import ww.smartexpress.app.ui.base.fragment.BaseFragment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ww.smartexpress.app.ui.fragment.activity.ActivityFragmentViewModel;
import ww.smartexpress.app.ui.fragment.home.adapter.CategoryAdapter;
import ww.smartexpress.app.ui.fragment.home.HomeFragmentViewModel;
import ww.smartexpress.app.ui.fragment.search.SearchFragmentViewModel;
import ww.smartexpress.app.ui.profile.ProfileFragmentViewModel;

@Module
public class FragmentModule {

    private final BaseFragment<?, ?> fragment;

    public FragmentModule(BaseFragment<?, ?> fragment) {
        this.fragment = fragment;
    }

    @Named("access_token")
    @Provides
    @FragmentScope
    String provideToken(Repository repository) {
        return repository.getToken();
    }

    @Provides
    @FragmentScope
    HomeFragmentViewModel provideHomeFragmentViewModel(Repository repository, Context application){
        Supplier<HomeFragmentViewModel> supplier = () -> new HomeFragmentViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<HomeFragmentViewModel> factory = new ViewModelProviderFactory<>(HomeFragmentViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(HomeFragmentViewModel.class);
    }

    @Provides
    @FragmentScope
    ActivityFragmentViewModel provideActivityFragmentViewModel(Repository repository, Context application){
        Supplier<ActivityFragmentViewModel> supplier = () -> new ActivityFragmentViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<ActivityFragmentViewModel> factory = new ViewModelProviderFactory<>(ActivityFragmentViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(ActivityFragmentViewModel.class);
    }
    @Provides
    @FragmentScope
    ProfileFragmentViewModel provideProfileFragmentViewModel(Repository repository, Context application){
        Supplier<ProfileFragmentViewModel> supplier = () -> new ProfileFragmentViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<ProfileFragmentViewModel> factory = new ViewModelProviderFactory<>(ProfileFragmentViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(ProfileFragmentViewModel.class);
    }

    @Provides
    @FragmentScope
    SearchFragmentViewModel provideSearchFragmentViewModel(Repository repository, Context application){
        Supplier<SearchFragmentViewModel> supplier = () -> new SearchFragmentViewModel(repository, (MVVMApplication) application);
        ViewModelProviderFactory<SearchFragmentViewModel> factory = new ViewModelProviderFactory<>(SearchFragmentViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(SearchFragmentViewModel.class);
    }
}
