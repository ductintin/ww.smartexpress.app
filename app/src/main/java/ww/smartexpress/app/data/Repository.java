package ww.smartexpress.app.data;


import ww.smartexpress.app.data.local.prefs.PreferencesService;
import ww.smartexpress.app.data.local.room.RoomService;
import ww.smartexpress.app.data.remote.ApiService;

public interface Repository {

    /**
     * ################################## Preference section ##################################
     */
    String getToken();

    void setToken(String token);

    PreferencesService getSharedPreferences();


    /**
     * ################################## Remote api ##################################
     */
    ApiService getApiService();

    RoomService getRoomService();

}
