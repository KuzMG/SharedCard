package com.example.sharedcard.database.entity.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;


@Dao
public interface UserDao {
    @Insert
    void createUser(UserEntity user);
    @Insert
    void createUsers(List<UserEntity> users);
    @Query("select * from user")
   LiveData<List<UserEntity>> findAll();
    @Query("select * from user where id =-1")
    LiveData<UserEntity> getMe();
    @Update
    void update(UserEntity entity);
    @Delete
    void delete(UserEntity entity);

}
