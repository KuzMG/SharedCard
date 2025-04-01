package com.example.sharedcard.repository

import com.example.sharedcard.database.entity.gpoup_person.GroupPersonsDao
import com.example.sharedcard.database.entity.group.GroupDao
import com.example.sharedcard.database.entity.person.PersonDao
import javax.inject.Inject

class PersonRepository @Inject constructor(
    private val groupDao: GroupDao,
    private val personDao: PersonDao,
    private val groupPersonsDao: GroupPersonsDao,
) {
}