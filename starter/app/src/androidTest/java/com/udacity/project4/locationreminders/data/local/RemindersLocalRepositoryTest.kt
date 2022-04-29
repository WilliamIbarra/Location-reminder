package com.udacity.project4.locationreminders.data.local


import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    private val reminder1 = ReminderDTO("Title1", "Description1", "Location1", 40.00, 40.00)
    private val reminder2 = ReminderDTO("Title2", "Description2", "Location2", 60.00, 60.00)
    private val reminder3 = ReminderDTO("Title3", "Description3", "Location3", 80.00, 80.00)
    private val localReminders = listOf(reminder1, reminder2).sortedBy { it.id }
    private val newReminders = listOf(reminder3).sortedBy { it.id }


    private lateinit var remindersDatabase: RemindersDatabase
    // Class under test
    private lateinit var remindersRepository: RemindersLocalRepository
    private lateinit var remindersDAO: RemindersDao

    @Before
    fun createRepository() {
        remindersDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            RemindersDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        remindersDAO = remindersDatabase.reminderDao()
        remindersRepository =
            RemindersLocalRepository(
                remindersDAO
            )
    }

//    TODO: Add testing implementation to the RemindersLocalRepository.kt

    @Test
    fun getReminders_requestsAllRemindersFromLocalDataSource() = runBlockingTest {
        // When tasks are requested from the tasks repository
        val reminders = remindersRepository.getReminders() as Result.Success

        // Then reminders are loaded from the local data source
        assertThat(reminders.data, IsEqual(localReminders))
    }

}

