package ww.smartexpress.app.utils;

import android.content.Context;
import android.location.LocationManager;

public class LocationUtils {
    public static boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            // Kiểm tra xem GPS hoặc mạng định vị có được bật không
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return false;
    }
}
