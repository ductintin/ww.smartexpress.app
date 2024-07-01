package ww.smartexpress.app.data.local.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import ww.smartexpress.app.data.model.room.UserEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import ww.smartexpress.app.data.model.room.UserWithAddresses;

@Dao
public interface DbUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(UserEntity UserEntity);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<UserEntity> userEntities);

    @Query("SELECT * FROM `user`")
    Single<List<UserEntity>> loadAll();

    @Query("SELECT * FROM `user` ORDER BY user_id")
    LiveData<List<UserEntity>> loadAllToLiveData();

    @Query("SELECT * FROM `user` WHERE user_id=:id")
    Single<UserEntity> findById(long id);

    @Delete
    Completable delete(UserEntity UserEntity);

    @Query("DELETE FROM user WHERE user_id=:userId")
    Completable deleteById(Long userId);

    @Transaction
    @Query("SELECT * FROM `user` WHERE user_id=:userId")
    Single<List<UserWithAddresses>> loadAllAddressByUserId(Long userId);

    @Query("UPDATE  `user` SET avatar=:avatar, name=:name WHERE user_id=:id")
    Completable update(Long id, String avatar, String name);

    @Query("UPDATE  `user` SET avatar=:avatar, name=:name, phone=:phone, email=:email, bankCard=:bankCard WHERE user_id=:id")
    Completable updateFull(Long id, String avatar, String name, String phone, String email, String bankCard);

    @Query("UPDATE  `user` SET encryptedPassword=:encryptedPassword WHERE user_id=:userId")
    Completable updateEncryptedPassword(Long userId, String encryptedPassword);

    @Query("SELECT encryptedPassword FROM `user` WHERE user_id=:userId")
    Single<String> getEncryptedByUserId(Long userId);

    @Query("UPDATE  `user` SET bankCard=:bankCard WHERE user_id=:userId")
    Completable updateBankCard(Long userId, String bankCard);

    @Query("DELETE FROM user WHERE user_id !=:userId")
    Completable deleteAllExceptId(Long userId);

    @Query("UPDATE  `user` SET isBiometric=:isBiometric WHERE user_id=:userId")
    Completable updateBiometric(Long userId, Boolean isBiometric);

    @Query("UPDATE  `user` SET avatar=:avatar, name=:name, phone=:phone, email=:email, bankCard=:bankCard, encryptedPassword=:encryptedPassword WHERE user_id=:id")
    Completable updateExceptBiometric(Long id, String avatar, String name, String phone, String email, String bankCard, String encryptedPassword);
}
