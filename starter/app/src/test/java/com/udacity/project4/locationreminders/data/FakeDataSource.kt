package com.udacity.project4.locationreminders.data


import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminders: MutableList<ReminderDTO>? = mutableListOf()) : ReminderDataSource {

    private var remindersServiceData: LinkedHashMap<ReminderDTO, ReminderDTO> = LinkedHashMap()


//    TODO: Create a fake data source to act as a double to the real data source

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        reminders?.let { return Result.Success(ArrayList(it)) }
        return Result.Error(
            "Tasks not found"
        )
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {

        reminders?.add(reminder)

    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        reminders?.let { return Result.Success(it.first { it.id == id }) }

        return Result.Error(
            "Task not found"
        )
    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }


}