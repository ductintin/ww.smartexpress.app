package ww.smartexpress.app.di.component;


import ww.smartexpress.app.di.module.FragmentModule;
import ww.smartexpress.app.di.scope.FragmentScope;

import dagger.Component;
import ww.smartexpress.app.ui.fragment.activity.ActivityFragment;
import ww.smartexpress.app.ui.fragment.home.HomeFragment;
import ww.smartexpress.app.ui.fragment.notification.NotificationFragment;
import ww.smartexpress.app.ui.fragment.search.SearchFragment;
import ww.smartexpress.app.ui.profile.ProfileFragment;

@FragmentScope
@Component(modules = {FragmentModule.class}, dependencies = AppComponent.class)
public interface FragmentComponent {
    void inject(HomeFragment homeFragment);

    void inject(ActivityFragment activityFragment);
    void inject(ProfileFragment profileFragment);
    void inject(SearchFragment searchFragment);
    void inject(NotificationFragment notificationFragment);
}
