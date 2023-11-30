package com.example.sharedcard.database.entity.gpoup_users;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.project.shared_card.database.dao.group_users.GroupUsersEntity;


import java.util.List;

@Dao
public interface GroupUsersDao {
    @Insert
    void createGroup(GroupUsersEntity entity);
    @Insert
    void createGroups(List<GroupUsersEntity> entity);

    @Query("SELECT * FROM group_users")
    LiveData<List<GroupUsersEntity>>  getAllGroup();
}
