package com.udacity.project4.locationreminders.reminderslist


import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.FakeDataSource
import com.udacity.project4.locationreminders.data.local.RemindersDatabase
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest: KoinTest {

    private lateinit var database: RemindersDatabase
    private lateinit var reminderDataSource: ReminderDataSource
    private val fakeDataSource: FakeDataSource by inject()

    private val reminder1 = ReminderDTO("Title1", "Description1", "Location1", 40.00, 40.00, true, "1")
    private val reminder2 = ReminderDTO("Title2", "Description2", "Location2", 60.00, 60.00)
    private val reminder3 = ReminderDTO("Title3", "Description3", "Location3", 80.00, 80.00)


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = ActivityTestRule(RemindersActivity::class.java)

    @get:Rule
    val permission: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)


    private val testModules = module {
        viewModel {
            RemindersListViewModel(
                get(),
                get() as FakeDataSource
            )
        }

        single {
            database = Room.inMemoryDatabaseBuilder(
                getApplicationContext(),
                RemindersDatabase::class.java).allowMainThreadQueries().build()

        }

        single {
            RemindersLocalRepository(get()) as RemindersLocalRepository
        }

        single {
            FakeDataSource()
        }
    }

    @Before
    fun setup() {
        stopKoin()
        startKoin {
            androidContext(getApplicationContext())
            modules(listOf(testModules))
        }
    }

//    TODO: test the navigation of the fragments.
@Test
fun clickRemindersList_navigateToAddReminder() = runBlockingTest {

    // GIVEN - On the home screen
    val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

    val navController = mock(NavController::class.java)
    scenario.onFragment {
        Navigation.setViewNavController(it.view!!, navController)
    }

    // WHEN - Click on the add reminder
    onView(withId(R.id.addReminderFAB)).perform(click())


    // THEN - Verify that we navigate to the add reminder
    verify(navController).navigate(
        ReminderListFragmentDirections.toSaveReminder()
    )
}

//    TODO: test the displayed data on the UI.

    @Test
    fun checkDataDisplayed() = runBlockingTest {
        fakeDataSource.deleteAllReminders()

        // Given reminders
        fakeDataSource.saveReminder(reminder1)
        fakeDataSource.saveReminder(reminder2)
        fakeDataSource.saveReminder(reminder3)

        // When launch to display data
        launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)


        //Then data is displayed on the screen
        onView(withText("Title1")).check(matches(isDisplayed()))
        onView(withText("Title2")).check(matches(isDisplayed()))
    }

    @Test
    fun checkNoDataDisplayed() = runBlockingTest {
        // delete data
        fakeDataSource.deleteAllReminders()

        // launch to display reminders
        launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

        // no data displayed on the screen
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
    }

//    TODO: add testing for the error messages.

    @Test
    fun displayErrorMessages() {

        //error message
        val error = "You need to grant location permission in order to select the location."

        // reminders screen
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        scenario.onFragment{
            it.showSnackBar()
        }

        //check snackbar displayed
        onView(withText(error))
            .inRoot(withDecorView(not(rule.activity.window.decorView)))
            .check(matches(isDisplayed()))

    }

}