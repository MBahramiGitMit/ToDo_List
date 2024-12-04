package com.mbahrami.todolist.ui.screens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEach
import com.mbahrami.todolist.R
import com.mbahrami.todolist.data.models.Priority
import com.mbahrami.todolist.data.models.ToDoTask
import com.mbahrami.todolist.ui.theme.HighPriorityColor
import com.mbahrami.todolist.ui.theme.LARGEST_PADDING
import com.mbahrami.todolist.ui.theme.LARGE_PADDING
import com.mbahrami.todolist.ui.theme.PRIORITY_INDICATOR_SIZE
import com.mbahrami.todolist.ui.theme.SMALL_PADDING
import com.mbahrami.todolist.ui.theme.TASK_ITEM_ELEVATION
import com.mbahrami.todolist.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListContent(
    modifier: Modifier,
    tasks: RequestState<List<ToDoTask>>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    swipeToDelete: (taskId: Int) -> Unit
) {
    if (tasks is RequestState.Success) {
        if (tasks.data.isNotEmpty()) {
            DisplayContent(
                modifier = modifier,
                tasks = tasks.data,
                navigateToTaskScreen = navigateToTaskScreen,
                swipeToDelete = swipeToDelete
            )
        } else {
            EmptyContent(modifier = modifier)
        }
    }
}


@Composable
fun DisplayContent(
    modifier: Modifier,
    tasks: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    swipeToDelete: (taskId: Int) -> Unit
) {
    LazyColumn(modifier = modifier) {
        tasks.fastForEach { task ->
            item(key = task.id) {
                val scope = rememberCoroutineScope()
                val density = LocalDensity.current

                var itemAppeared by remember { mutableStateOf(false) }
                LaunchedEffect(true) {
                    itemAppeared = true
                }

                val dismissState = remember(task) {
                    SwipeToDismissBoxState(
                        initialValue = SwipeToDismissBoxValue.Settled,
                        density = density,
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.EndToStart) {
                                scope.launch {
                                    itemAppeared = false
                                    delay(300)
                                    swipeToDelete(task.id)
                                }
                                true
                            } else {
                                false
                            }
                        },
                        positionalThreshold = { totalDistance -> totalDistance / 3 }
                    )
                }

                val degree by animateFloatAsState(
                    if (dismissState.progress in 0f..0.33f) 0f else -45f,
                    label = "rotationDegree"
                )

                AnimatedVisibility(
                    visible = itemAppeared,
                    enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
                ) {
                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = { RedBackground(rotationDegree = degree) },
                        enableDismissFromStartToEnd = false
                    ) {
                        TaskItem(
                            toDoTask = task,
                            onItemClicked = navigateToTaskScreen
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    onItemClicked: (taskId: Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        shadowElevation = TASK_ITEM_ELEVATION,
        onClick = { onItemClicked(toDoTask.id) }
    ) {

        Column(
            modifier = Modifier
                .padding(LARGE_PADDING)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = toDoTask.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)) {
                    drawCircle(color = toDoTask.priority.color)
                }
            }
            Spacer(modifier = Modifier.height(SMALL_PADDING))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = toDoTask.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}

@Composable
fun RedBackground(rotationDegree: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HighPriorityColor)
            .padding(horizontal = LARGEST_PADDING),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.rotate(degrees = rotationDegree),
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_icon),
            tint = Color.White
        )
    }
}

@Composable
@Preview()
fun PreviewTaskItem() {
    TaskItem(
        ToDoTask(
            id = 10,
            title = "title",
            description = "hello my name is mehdi bahrami.hello my name is mehdi bahrami.hello my name is mehdi bahrami.hello my name is mehdi bahrami.",
            priority = Priority.HIGH
        )
    ) { }
}