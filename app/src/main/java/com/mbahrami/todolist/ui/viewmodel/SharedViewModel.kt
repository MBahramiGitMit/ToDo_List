package com.mbahrami.todolist.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbahrami.todolist.data.models.Priority
import com.mbahrami.todolist.data.models.ToDoTask
import com.mbahrami.todolist.data.repository.ToDoRepository
import com.mbahrami.todolist.util.Action
import com.mbahrami.todolist.util.Constants
import com.mbahrami.todolist.util.RequestState
import com.mbahrami.todolist.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: ToDoRepository) : ViewModel() {

    val id: MutableState<Int> = mutableIntStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _searchedTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks

    private val _searchQuery: MutableState<String> = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    fun onSearchFieldValueChanged(newValue: String) {
        _searchQuery.value = newValue
    }

    fun onSearchAppBarStateChange(newState: SearchAppBarState) {
        searchAppBarState.value = newState
    }

    fun onTitleChanged(newValue: String) {
        if (newValue.length <= Constants.MAX_TITLE_LENGTH) {
            title.value = newValue
        }
    }

    fun handleAction(action: Action) {
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

            }

            Action.UNDO -> {
                undoTask()
            }

            else -> {
            }
        }
    }

    fun getAllTask() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTask.collectLatest {
                    _allTasks.value = RequestState.Success(data = it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(error = e)
        }

    }

    fun getSearchedTask() {
        _searchedTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = searchQuery.value).collectLatest {
                    _searchedTasks.value = RequestState.Success(data = it)
                }
            }
        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(error = e)
        }
        onSearchAppBarStateChange(newState = SearchAppBarState.TRIGGERED)
    }

    fun getSelectedTask(taskId: Int) {
        _selectedTask.value =
            if (allTasks.value is RequestState.Success) {
                (allTasks.value as RequestState.Success<List<ToDoTask>>).data.find {
                    it.id == taskId
                }?.copy()
            } else {
                null
            }
        updateTaskFields()
    }

    private fun updateTaskFields() {
        if (selectedTask.value != null) {
            selectedTask.value!!.let {
                id.value = it.id
                title.value = it.title
                description.value = it.description
                priority.value = it.priority
            }
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(
                toDoTask = ToDoTask(
                    id = id.value,
                    title = title.value,
                    description = description.value,
                    priority = priority.value
                )
            )
        }
        onSearchAppBarStateChange(newState = SearchAppBarState.CLOSED)
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(
                toDoTask = ToDoTask(
                    id = id.value,
                    title = title.value,
                    description = description.value,
                    priority = priority.value
                )
            )
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(
                toDoTask = ToDoTask(
                    id = id.value,
                    title = title.value,
                    description = description.value,
                    priority = priority.value
                )
            )
        }
        onSearchAppBarStateChange(newState = SearchAppBarState.CLOSED)
    }

    private fun undoTask() {
        viewModelScope.launch(Dispatchers.IO) {
            selectedTask.value?.let {
                repository.addTask(
                    it
                )
            }
        }
    }


    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }
}