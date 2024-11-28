package com.mbahrami.todolist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbahrami.todolist.data.models.ToDoTask
import com.mbahrami.todolist.data.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: ToDoRepository) : ViewModel() {
    private val _allTask: MutableStateFlow<List<ToDoTask>> = MutableStateFlow(emptyList())
    val allTask: StateFlow<List<ToDoTask>> = _allTask

    fun getAllTask() {
        viewModelScope.launch {
            repository.getAllTask.collectLatest {
                _allTask.emit(it)
            }
        }
    }
}