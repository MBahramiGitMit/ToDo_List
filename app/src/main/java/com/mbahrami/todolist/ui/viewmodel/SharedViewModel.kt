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
import com.mbahrami.todolist.util.Constants
import com.mbahrami.todolist.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _searchQuery: MutableState<String> = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    fun onSearchFieldValueChanged(newValue: String) {
        _searchQuery.value = newValue
    }

    fun onTitleChanged(newValue: String) {
        if (newValue.length <= Constants.MAX_TITLE_LENGTH) {
            title.value = newValue
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

}