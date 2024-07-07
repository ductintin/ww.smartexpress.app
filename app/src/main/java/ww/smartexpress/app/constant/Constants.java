package ww.smartexpress.app.constant;

public class Constants {
    public static final String DB_NAME = "room";
    public static final String PREF_NAME = "mvvm.prefs";

    public static final String VALUE_BEARER_TOKEN_DEFAULT = "NULL";
    public static final String APP_SERVER = "BACKEND_SOCKET_APP";

    public static String dateFormat = "dd/MM/yyyy HH:mm";
    //Local Action manager
    public static final String ACTION_EXPIRED_TOKEN = "ACTION_EXPIRED_TOKEN";
    public static final String INSTAGRAM_LOGIN_URL = "https://www.instagram.com/accounts/login/";
    public static final String INSTAGRAM_URL = "https://www.instagram.com/";
    public static final int LOCATION_PERMISSION_CODE = 1;
    public static final char SYMBOLS = '.';
    public static final String LOGIN_PHONE_NUMBER = "LOGIN_PHONE_NUMBER";
    public static final String KEY_CATEGORY_ID = "KEY_CATEGORY_ID";
    public static final String KEY_SERVICE_ID = "KEY_SERVICE_ID";
    public static final String KEY_USER_ID = "KEY_USER_ID";
    public static final String FILE_TYPE_AVATAR = "AVATAR";
    public static final String GEO_API_KEY = "AIzaSyDQFJ-AGut2GL97rVRvf2q1SJLwABJjWOU";
    public static final String GEO_API_URL = "https://maps.googleapis.com/maps/api";
    public static final String KEY_ORIGIN_ID = "KEY_ORIGIN_ID";
    public static final String KEY_DESTINATION_ID = "KEY_DESTINATION_ID";
    public static final String KEY_DESTINATION_NAME = "KEY_DESTINATION_NAME";
    public static final String KEY_ORIGIN_NAME = "KEY_ORIGIN_NAME";
    public static final String KEY_BOOK_LOCATION_INFO = "KEY_BOOK_LOCATION_INFO";
    public static final String MINUTE = "phút";
    public static final String HOUR = "giờ";
    public static final String DISTANCE_MEASURE = "km";
    public static final String APP_CUSTOMER = "CUSTOMER_APP";
    public static final String CUSTOMER_BOOKING_NOTE = "CUSTOMER_BOOKING_NOTE";
    public static final String CUSTOMER_BOOKING_PROMOTION = "CUSTOMER_BOOKING_PROMOTION";
    public static final String CUSTOMER_BOOKING_OBJECT = "CUSTOMER_BOOKING_OBJECT";
    public static final String CUSTOMER_BOOKING_DETAIL_OBJECT = "CUSTOMER_BOOKING_DETAIL_OBJECT";
    public static final String CUSTOMER_BOOKING_DETAIL_ID = "CUSTOMER_BOOKING_DETAIL_ID";
    public static final String CUSTOMER_BOOKING_ID = "CUSTOMER_BOOKING_ID";

    public static final Integer BOOKING_STATE_BOOKING_CANCELED = -100;
    public static final Integer BOOKING_STATE_BOOKING = 0;
    public static final Integer BOOKING_STATE_DRIVER_ACCEPT = 100;
    public static final Integer BOOKING_STATE_PICKUP_SUCCESS = 200;
    public static final Integer BOOKING_STATE_NOT_FOUND_DRIVER = 400;

    public static final String BOOKING_CANCEL_STATE= "BOOKING_CANCEL_STATE";
    public static final String BOOKING_COMPLETE_STATE= "BOOKING_COMPLETE_STATE";
    public static final String BOOKING_STATE= "BOOKING_STATE";
    public static final String ROOM_ID= "ROOM_ID";
    public static final String DRIVER_POSITION= "DRIVER_POSITION";
    public static final String SHIPPING_INFO= "SHIPPING_INFO";

    public static final Integer REQUEST_OTP_KIND_PHONE = 1;
    public static final Integer REQUEST_OTP_KIND_EMAIL = 2;

    public static final String VERIFY_OPTION = "VERIFY_OPTION";
    public static final String OTP = "OTP";
    public static final String PHONE_NUMBER_REGEX = "^(?!0987654321)(0[3|5|7|8|9])([0-9]{8})$";

    public static final int NOTIFICATION_KIND_SYSTEM = 4;
    public static final int NOTIFICATION_KIND_PROMOTION = 5;
    public static final int NOTIFICATION_KIND_WARNING = 6;

    public static final int NOTIFICATION_KIND_DEPOSIT_SUCCESSFULLY = 1;
    public static final int NOTIFICATION_KIND_APPROVE_PAYOUT = 2;
    public static final int NOTIFICATION_KIND_REJECT_PAYOUT = 3;
    public static final String CUSTOMER_ERROR_STATUS_PENDING = "ERROR-CUSTOMER-ERROR-00011";
    public static final String CUSTOMER_ERROR_LOGIN_FAILED = "ERROR-CUSTOMER-ERROR-0003";

    private Constants() {

    }
}
