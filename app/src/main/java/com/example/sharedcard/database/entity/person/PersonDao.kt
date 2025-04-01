package com.example.sharedcard.database.entity.person

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.sharedcard.database.entity.gpoup_person.UserEntityWithStatus
import java.util.UUID

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(users: List<PersonEntity>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: PersonEntity): Long

    @Update
    fun update(user: PersonEntity): Int

    @Transaction
    fun insertOrUpdate(user: PersonEntity) {
        val id = insert(user)
        if (id ==-1L)
            update(user)
    }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createPersonAccount(user: PersonAccountEntity)
    @Query("select * from account where email =:email")
    fun findPersonAccount(email: String): PersonAccountEntity
    @Insert
    fun addPersons(users: List<PersonEntity>)
    @Query("select * from person where id = :id")
    fun getPersonLiveData(id: UUID): LiveData<PersonEntity>
    @Query("select * from person where id = :id")
    fun get(id: UUID): PersonEntity
    @Query("select * from account where id = :id")
    fun getAccountLiveData(id: UUID): LiveData<PersonAccountEntity>

    @Query("select * from account where id = :id")
    fun getAccount(id: UUID): PersonAccountEntity
    @Query("update person set name = :name where  id = :id")
    fun setName(id: UUID,name: String)
    @Query("update person set weight = :weight where  id = :id")
    fun setWeight(id: UUID,weight: Double)
    @Query("update person set height = :height where  id = :id")
    fun setHeight(id: UUID,height: Int)

    @Query("delete from person where id=:idUser")
    fun delete(idUser: UUID)
    @Query("select * from person where id= (select id_person from group_persons where id_group= (select id_group from group_persons where id_person=:personId)and id_person != :personId)")
    fun getAllWithoutYou(personId: UUID): LiveData<List<PersonEntity>>

    @Query("select * from group_persons where id_group= :groupId")
    fun getByGroup(groupId:UUID): LiveData<List<UserEntityWithStatus>>
    @Query("select * from person where id= (select id_person from group_persons where id_group= (select id_group from group_persons where id_person=:personId) and id_person not in (:excludePersonsSet)) " +
            "and (select count(*) from purchase where purchase.id_person=person.id) >0")
    fun getAllWithoutYou(personId: UUID, excludePersonsSet: Set<UUID>): LiveData<List<PersonEntity>>

}