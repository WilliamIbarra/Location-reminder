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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest: TestCase() {

//    TODO: Add testing implementation to the RemindersDao.kt
    private lateinit var database: RemindersDatabase
    private lateinit var dao: RemindersDao

    // rule
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        // get context -- since this is an instrumental test it requires
        // context from the running application
        val context = ApplicationProvider.getApplicationContext<Context>()
        // initialize the db and dao variable
        database = Room.inMemoryDatabaseBuilder(context, RemindersDatabase::class.java).allowMainThreadQueries().build()
        dao = database.reminderDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun getReminders() = runBlocking {
        val reminder = ReminderDTO(
            id = "1",
            title = "Test1",
            description = "Test1 Description",
            latitude = 0.00,
            longitude = 0.00,
            location = "Test"
        )

        dao.saveReminder(reminder)

        val reminderList = dao.getReminders()

        assertNotNull(reminderList)
    }

    @Test
    fun saveReminder() = runBlockingTest {
        val reminder = ReminderDTO(
            id = "2",
            title = "Test2",
            description = "Test2 Description",
            latitude = 0.00,
            longitude = 0.00,
            location = "Test"
        )

        dao.saveReminder(reminder)

        //find by id
        assertNotNull(dao.getReminderById("2")?.title)

    }

    @Test
    fun getReminderById() = runBlockingTest {

        val reminder = ReminderDTO(
            id = "1",
            title = "Test1",
            description = "Test1 Description",
            latitude = 0.00,
            longitude = 0.00,
            location = "Test"
        )

        val reminder2 = ReminderDTO(
            id = "2",
            title = "Test2",
            description = "Test2 Description",
            latitude = 0.00,
            longitude = 0.00,
            location = "Test"
        )

        dao.saveReminder(reminder)
        dao.saveReminder(reminder2)

        assertThat(reminder.title, `is`("Test1"))
    }


    fun deleteAllReminders() = runBlockingTest {
        val reminder = ReminderDTO(
            id = "1",
            title = "Test1",
            description = "Test1 Description",
            latitude = 0.00,
            longitude = 0.00,
            location = "Test"
        )

        val reminder2 = ReminderDTO(
            id = "2",
            title = "Test2",
            description = "Test2 Description",
            latitude = 0.00,
            longitude = 0.00,
            location = "Test"
        )

        dao.saveReminder(reminder)
        dao.saveReminder(reminder2)

        dao.deleteAllReminders()

        val result = dao.getReminderById("2")

        assertNotNull(result)
    }

}