package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import android.content.Context
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import junit.framework.TestCase

import org.junit.Before;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import org.junit.After

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest: TestCase() {

//    TODO: Add testing implementation to the RemindersDao.kt
    private lateinit var database: RemindersDatabase
    private lateinit var dao: RemindersDao

    @Before
    public override fun setUp() {
        // get context -- since this is an instrumental test it requires
        // context from the running application
        val context = ApplicationProvider.getApplicationContext<Context>()
        // initialize the db and dao variable
        database = Room.inMemoryDatabaseBuilder(context, RemindersDatabase::class.java).build()
        dao = database.reminderDao()
    }

    @After
    fun closeDb() {
        database.close()
    }


}