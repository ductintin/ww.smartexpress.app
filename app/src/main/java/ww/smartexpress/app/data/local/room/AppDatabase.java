package ww.smartexpress.app.data.local.room;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DeleteTable;
import androidx.room.RoomDatabase;
import androidx.room.migration.AutoMigrationSpec;
import androidx.sqlite.db.SupportSQLiteDatabase;

import ww.smartexpress.app.data.model.room.AddressEntity;
import ww.smartexpress.app.data.model.room.UserEntity;

@Database(entities = {
//        OrderEntity.class,
        UserEntity.class, AddressEntity.class},
        version = 12,
        autoMigrations = {
//                @AutoMigration(from = 1, to = 2),
//                @AutoMigration(from = 2, to = 3,spec = AppDatabase.TwoThree.class),

        }
//        exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {

    //    public abstract DbOrderDao getOrderDao();
    public abstract DbUserDao getUserDao();
    public abstract DbAddressDao getAddressDao();

    //    @RenameColumn(tableName = "db_restaurant",fromColumnName = "id",toColumnName = "id_id")
    public static class AppDatabaseMigration implements AutoMigrationSpec {
        @Override
        public void onPostMigrate(@NonNull SupportSQLiteDatabase db) {
            AutoMigrationSpec.super.onPostMigrate(db);
        }
    }

    @DeleteTable(tableName = "order")
    public static class TwoThree implements AutoMigrationSpec {
        @Override
        public void onPostMigrate(@NonNull SupportSQLiteDatabase db) {
            AutoMigrationSpec.super.onPostMigrate(db);
        }
    }
}
