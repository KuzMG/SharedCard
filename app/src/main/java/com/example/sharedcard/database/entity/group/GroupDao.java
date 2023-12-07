package com.example.sharedcard.database.entity.group;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GroupDao {
    @Insert
    void createGroup(GroupEntity group);
    @Insert
    void createGroups(List<GroupEntity> groups);
    @Query("SELECT * FROM `group` where id_group>0")
    LiveData<List<GroupEntity>> getALLGroup();

    @Query("select * from `group` where id_group=:id")
    LiveData<GroupEntity> getGroupById(Long id);
    @Update
    void update(GroupEntity entity);
}
