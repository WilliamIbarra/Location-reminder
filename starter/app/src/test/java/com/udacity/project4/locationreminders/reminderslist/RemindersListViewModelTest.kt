package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.locationreminders.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.emptyArray
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    //TODO: provide testing to the RemindersListViewModel and its live data objects

    private lateinit var dataSource: FakeDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupDataSource() = runBlockingTest {
        stopKoin()
        dataSource = FakeDataSource(mutableListOf(
            ReminderDTO(
            id = "1",
            title = null,
            description = "Test1 Description",
            latitude = 0.00,
            longitude = 0.00,
            location = "Test"
        )
        ))
    }

    @Test
    fun loadReminders_loadsReminderEvent() = runBlockingTest {
        // Given a fresh viewmodel
        val reminderListViewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), dataSource)

        // When get the list of reminders
        reminderListViewModel.loadReminders()

        // Then return the reminders
        assertThat(reminderListViewModel.remindersList.getOrAwaitValue().isNullOrEmpty(), `is`(false))

    }

    @Test
    fun invalidateNoData_showNoDataAfterDeleteReminders() = runBlockingTest {
        //Given a fresh viewmodel
        val reminderListViewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), dataSource)
        // When delete the reminders and get the list of reminders
        dataSource.deleteAllReminders()
        reminderListViewModel.loadReminders()

        //Then return the reminders
        assertThat(reminderListViewModel.remindersList.getOrAwaitValue().isNullOrEmpty(), `is`(true))
    }
}