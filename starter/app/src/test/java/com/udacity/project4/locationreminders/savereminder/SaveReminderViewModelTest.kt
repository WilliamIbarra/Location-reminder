package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.util.getOrAwaitValue

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    //TODO: provide testing to the SaveReminderView and its live data objects

    private lateinit var dataSource: FakeDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupDataSource() = runBlockingTest {
        stopKoin()
        dataSource = FakeDataSource()
    }

    @Test
    fun saveReminder_savesReminderEvent() = runBlockingTest {

        // Given a fresh viewmodel
        val saveReminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), dataSource)

        // When saving a new reminder
        saveReminderViewModel.saveReminder(
            ReminderDataItem(
                id = "1",
                title = "Test1",
                description = "Test1 Description",
                latitude = 0.00,
                longitude = 0.00,
                location = "Test"
            )

        )
        val result = dataSource.getReminder("1") as Result.Success<ReminderDTO>

        // Then return the reminder saved
        assertThat(result.data.id, `is`("1"))


    }


    @Test
    fun cleanData_afterSaveReminder() {
        // Given a fresh viewmodel
        val saveReminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), dataSource)

        // When clean variables
        saveReminderViewModel.onClear()

        // Then return true if the data was properly clean
        assertThat(saveReminderViewModel.reminderTitle.getOrAwaitValue(), `is`(nullValue()))
        assertThat(saveReminderViewModel.reminderTitle.getOrAwaitValue(), `is`(nullValue()))
        assertThat(saveReminderViewModel.reminderTitle.getOrAwaitValue(), `is`(nullValue()))
    }

    @Test
    fun validateData_beforeSaveReminder() {
        // Given a fresh viewmodel
        val saveReminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), dataSource)

        // When clean variables
       val value = saveReminderViewModel.validateEnteredData(
            ReminderDataItem(
                id = "1",
                title = null,
                description = "Test1 Description",
                latitude = 0.00,
                longitude = 0.00,
                location = "Test"
            )
        )

        // Then return false if the title is null
        assertThat(value, `is`(false))


    }



}