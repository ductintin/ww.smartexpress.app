package ww.smartexpress.app.data.local.room;

import javax.inject.Inject;

public class AppDbService implements RoomService {

    private final AppDatabase mAppDatabase;

    @Inject
    public AppDbService(AppDatabase appDatabase) {
        this.mAppDatabase = appDatabase;
    }


    @Override
    public DbUserDao userDao() {
        return mAppDatabase.getUserDao();
    }

    @Override
    public DbAddressDao addressDao() {
        return mAppDatabase.getAddressDao();
    }

//    @Override
//    public DbOrderDao orderDao() {
//        return mAppDatabase.getOrderDao();
//    }
}
