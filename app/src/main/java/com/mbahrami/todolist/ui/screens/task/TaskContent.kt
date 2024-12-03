package com.mbahrami.todolist.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mbahrami.todolist.R
import com.mbahrami.todolist.components.PriorityDropDown
import com.mbahrami.todolist.data.models.Priority
import com.mbahrami.todolist.ui.theme.LARGE_PADDING
import com.mbahrami.todolist.ui.theme.MEDIUM_PADDING


@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPriorityChange: (Priority) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(LARGE_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(id = R.string.title)) },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(MEDIUM_PADDING))

        PriorityDropDown(selectedPriority = priority, onPrioritySelected = onPriorityChange)

        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(text = stringResource(id = R.string.description)) },
            textStyle = MaterialTheme.typography.body1,
        )
    }
}

@Composable
@Preview()
fun PreviewTaskContent() {
    TaskContent(title = "Mehdi Bahrami", {}, "Hello i am programming", {}, Priority.MEDIUM, {})
}