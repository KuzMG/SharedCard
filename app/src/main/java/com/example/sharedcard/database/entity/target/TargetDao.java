package com.example.sharedcard.database.entity.target;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.project.shared_card.database.dao.target.TargetEntity;

import java.util.List;

@Dao
public interface TargetDao {
    @Insert
    void add(TargetEntity target);
    @Query("select * from target where id_group = :id and status = 2 order by status")
    LiveData<List<TargetEntity>> getAllForHistory(long id);
    @Query("select * from target where id_group = :id and status != 2 order by status,date_first desc")
    LiveData<List<TargetEntity>> getAllForCheck(long id);
    @Query("select * from target where id_group = :id and status != 2 and name like :query order by status,name asc")
    LiveData<List<TargetEntity>> getAllForCheckQuery(long id, String query);
    @Update
    void update(TargetEntity entity);
    @Delete
    void delete(TargetEntity entity);
}
