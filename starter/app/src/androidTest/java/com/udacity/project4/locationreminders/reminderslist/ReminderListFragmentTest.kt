package com.udacity.project4.locationreminders.reminderslist


import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {

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


//    TODO: add testing for the error messages.


}