package com.id.mindtodo.ui.viewmodel

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.id.mindtodo.data.model.Priority
import com.id.mindtodo.data.model.ToDoTask
import com.id.mindtodo.data.repository.DataStoreRepository
import com.id.mindtodo.data.repository.ToDoRepository
import com.id.mindtodo.receiver.AlarmReceiver
import com.id.mindtodo.ui.util.Action
import com.id.mindtodo.ui.util.Constants.MAX_TITLE_LENGTH
import com.id.mindtodo.ui.util.RequestState
import com.id.mindtodo.ui.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)
    val isReminder: MutableState<Boolean> = mutableStateOf(false)
    val reminderAt: MutableState<String> = mutableStateOf("")
    val alarmID: MutableState<Int> = mutableStateOf(0)
    private val date: MutableState<String> = mutableStateOf("")
    val times: MutableState<String> = mutableStateOf("")

    // To compare time on alarm when time not change
    private val oldTimes: MutableState<String> = mutableStateOf("")
    private val newTimes: MutableState<String> = mutableStateOf("")
    private val alarmReceiver = AlarmReceiver()

    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(
            SearchAppBarState.CLOSED
        )
    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _searchedTask = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTask: StateFlow<RequestState<List<ToDoTask>>> = _searchedTask

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    val lowerPriorityTask: StateFlow<List<ToDoTask>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val higherPriorityTask: StateFlow<List<ToDoTask>> =
        repository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    init {
        getAllTasks()
        readSortState()
    }

    private fun getAllTasks() = viewModelScope.launch {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTasks.collect { tasks ->
                    _allTasks.value = RequestState.Success(tasks)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    fun searchDatabase(searchQuery: String) {
        _searchedTask.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = "%$searchQuery%").collect { searchedTask ->
                    _searchedTask.value = RequestState.Success(searchedTask)
                }
            }
        } catch (e: Exception) {
            _searchedTask.value = RequestState.Error(e)
        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId).collect { task ->
                _selectedTask.value = task
            }
        }
    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
            isReminder.value = selectedTask.isReminder
            reminderAt.value = selectedTask.reminderAt
            alarmID.value = selectedTask.alarmId
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
            isReminder.value = false
            reminderAt.value = ""
            alarmID.value = 0
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    fun selectDateTime(context: Context) {
        var time: String
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        val uniqueID = Random.nextInt(100000)
        val alarms = alarmID.value
        val ids = if (alarms != 0) {
            alarmID.value
        } else {
            uniqueID
        }
        alarmID.value = ids

        val datePickerDialog = DatePickerDialog(context, { _, year, month, day ->
            val timePickerDialog = TimePickerDialog(context, { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                val monthStr: String = if ((month + 1).toString().length == 1) {
                    "0${month + 1}"
                } else {
                    month.toString()
                }
                val dateIndStr = "$day-$monthStr-$year"
                val dateUsStr = "$year-$monthStr-$day"
                val timeStr = "$hour:$minute"
                time = "$dateIndStr $timeStr"

                date.value = dateUsStr
                times.value = timeStr
                isReminder.value = true

                when {
                    reminderAt.value == "" -> {
                        reminderAt.value = time

                        alarmReceiver.setOneTimeAlarm(
                            context = context,
                            type = AlarmReceiver.TYPE_ONE_TIME,
                            date = date.value,
                            time = times.value,
                            message = "Don't forget! You have task to do now",
                            title = title.value,
                            alarmID = alarmID.value
                        )
                    }
                    reminderAt.value.isNotEmpty() -> {
                        oldTimes.value = reminderAt.value
                        newTimes.value = time
                        Log.e("OldTimes|NewTimes", "$oldTimes || $newTimes")

                        if (oldTimes != newTimes) {
                            reminderAt.value = newTimes.value
                            Log.e("NewTimes", newTimes.value)

                            alarmReceiver.setOneTimeAlarm(
                                context = context,
                                type = AlarmReceiver.TYPE_ONE_TIME,
                                date = date.value,
                                time = times.value,
                                message = "Don't forget! You have task to do now",
                                title = title.value,
                                alarmID = alarmID.value
                            )
                        }
                    }
                }
            }, startHour, startMinute, false)
            timePickerDialog.show()
        }, startYear, startMonth, startDay)
        datePickerDialog.setCanceledOnTouchOutside(false)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title.value,
                description = description.value,
                priority = priority.value,
                isReminder = isReminder.value,
                reminderAt = reminderAt.value,
                alarmId = alarmID.value
            )
            repository.addTask(toDoTask)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value,
                isReminder = isReminder.value,
                reminderAt = reminderAt.value,
                alarmId = alarmID.value
            )
            repository.updateTask(toDoTask)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value,
                isReminder = isReminder.value,
                reminderAt = reminderAt.value,
                alarmId = alarmID.value
            )
            repository.deleteTask(toDoTask)
        }
    }

    private fun deleteAllTask() {
        viewModelScope.launch {
            repository.deleteAllTask()
        }
    }

    private fun readSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                    .map { Priority.valueOf(it) }
                    .collect {
                        _sortState.value = RequestState.Success(it)
                    }
            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(e)
        }
    }

    fun persistSortingState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority)
        }
    }

    fun handleDatabaseAction(action: Action) {
        when (action) {
            Action.ADD -> {
                addTask()
            }
            Action.UPDATE -> {
                updateTask()
            }
            Action.DELETE -> {
                deleteTask()
            }
            Action.DELETE_ALL -> {
                deleteAllTask()
            }
            Action.UNDO -> {
                addTask()
            }
            else -> {

            }
        }

    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title.value = newTitle
        }
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }
}