package com.example.sharedcard.database.entity.group;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

@Dao
public interface GroupDao {
    @Insert
    void createGroup(GroupEntity group);
    @Insert
    void createGroups(List<GroupEntity> groups);


    @Query("select * from `group` where id_group=:id")
    LiveData<GroupEntity> getGroup(UUID id);
    @Update
    void update(GroupEntity entity);
}
