package ww.smartexpress.app.data.local.room;

public interface RoomService {
    DbUserDao userDao();
//    DbOrderDao orderDao();
    DbAddressDao addressDao();
}
