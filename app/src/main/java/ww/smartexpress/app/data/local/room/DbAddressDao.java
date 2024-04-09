package ww.smartexpress.app.data.local.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import ww.smartexpress.app.data.model.room.AddressEntity;
import ww.smartexpress.app.data.model.room.UserWithAddresses;

@Dao
public interface DbAddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(AddressEntity addressEntity);

    @Query("SELECT COUNT(*) FROM `address` WHERE place_id =:place_id")
    int getAddressCount(String place_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<AddressEntity> addressEntities);

    @Query("SELECT * FROM `address`")
    Single<List<AddressEntity>> loadAll();

    @Query("SELECT * FROM `address`")
    Single<List<AddressEntity>> loadAllAddress();

    @Query("SELECT * FROM `address` WHERE userId=:userId")
    Single<List<AddressEntity>> loadAllAddressByUserId(Long userId);


    @Query("DELETE FROM `address` WHERE address_id=:id")
    Completable deleteAddressById(Long id);


    @Query("DELETE FROM `address` WHERE userId=:id")
    Completable deleteAddressByUserId(Long id);
}
