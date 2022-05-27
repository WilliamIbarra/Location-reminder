package com.udacity.project4.locationreminders.data.local


import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import android.content.Context
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.*
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    private val reminder1 = ReminderDTO("Title1", "Description1", "Location1", 40.00, 40.00, true, "1")
    private val reminder2 = ReminderDTO("Title2", "Description2", "Location2", 60.00, 60.00)
    private val reminder3 = ReminderDTO("Title3", "Description3", "Location3", 80.00, 80.00)


    private lateinit var remindersDatabase: RemindersDatabase
    // Class under test
    private lateinit var remindersRepository: RemindersLocalRepository
    private lateinit var remindersDAO: RemindersDao

    @Before
    fun createRepository() {
        remindersDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        remindersDAO = remindersDatabase.reminderDao()
        remindersRepository =
            RemindersLocalRepository(
                remindersDAO,
                Dispatchers.Main
            )
    }

    @After
    fun close() {
        remindersDatabase.close()
    }

//    TODO: Add testing implementation to the RemindersLocalRepository.kt

    @Test
    fun getReminders_requestsAllRemindersFromLocalDataSource() = runBlocking {
        remindersDAO.saveReminder(reminder1)
        remindersDAO.saveReminder(reminder2)
        remindersDAO.saveReminder(reminder3)
        // When tasks are requested from the tasks repository
        val reminders = remindersRepository.getReminders() as Result.Success

        // Then reminders are loaded from the local data source
        assertThat(reminders.data.size, `is`(3))
        assertThat(reminders.data, notNullValue())
    }

    @Test
    fun saveReminder_saveAReminderInLocalDataSource() = runBlocking {
        remindersRepository.saveReminder(reminder1)

        val reminder = remindersRepository.getReminder("1") as Result.Success

        assertThat(reminder.data.title, `is`("Title1"))

    }

    @Test
    fun deleteReminders_deleteAllRemindersInLocalDataSource() = runBlocking {
        remindersDAO.saveReminder(reminder1)
        remindersDAO.saveReminder(reminder2)
        remindersDAO.saveReminder(reminder3)

        val resultAfterSaved = remindersRepository.getReminders() as Result.Success

        assertThat(resultAfterSaved.data.size, `is`(3))

        remindersRepository.deleteAllReminders()

        val resultAfterDeleteAll = remindersRepository.getReminders() as Result.Success

        assertThat(resultAfterDeleteAll.data.size, `is`(0))
    }




}

