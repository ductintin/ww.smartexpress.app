package ww.smartexpress.app.data.websocket;

public class Command {
    public static final String COMMAND_PING = "CLIENT_PING";
    public static final String COMMAND_CLIENT_INFO = "CLIENT_INFO";
    public static final String COMMAND_VERIFY_TOKEN = "VERIFY_TOKEN_CLIENT";
    public static final String CM_CONTACT_DRIVER = "CONTACT_DRIVER";
    public static final String CMD_DRIVER_ACCEPTED = "DRIVER_ACCEPTED";
    public static final String CMD_DRIVER_PICKUP_SUCCESS = "BOOKING_STATE_PICKUP_SUCCESS";
    public static final String CMD_DRIVER_DONE_BOOKING = "DRIVER_DONE_BOOKING";
    public static final String CM_SEND_MESSAGE = "SEND_MSG";
    public static final String CMD_DRIVER_CANCEL_BOOKING = "DRIVER_CANCEL_BOOKING";
    public static final String CMD_NOT_FOUND_DRIVER= "NOT_FOUND_DRIVER";
    public static final String CMD_DRIVER_UPDATE_POSITION= "DRIVER_UPDATE_POSITION";
}
