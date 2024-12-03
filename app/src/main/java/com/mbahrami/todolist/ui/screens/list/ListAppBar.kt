package com.mbahrami.todolist.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.mbahrami.todolist.R
import com.mbahrami.todolist.components.PriorityItem
import com.mbahrami.todolist.data.models.Priority
import com.mbahrami.todolist.ui.theme.MEDIUM_PADDING
import com.mbahrami.todolist.ui.theme.TOP_APP_BAR_HEIGHT
import com.mbahrami.todolist.ui.theme.Typography
import com.mbahrami.todolist.ui.theme.topAppBarBackgroundColor
import com.mbahrami.todolist.ui.theme.topAppBarContentColor
import com.mbahrami.todolist.util.SearchAppBarState

@Composable
fun ListAppBar(
    searchFieldValue: String,
    onSearchFieldValueChanged: (String) -> Unit,
    searchAppBarState: SearchAppBarState,
    onSearchAppBarStateChanged: (SearchAppBarState) -> Unit,
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    if (searchAppBarState == SearchAppBarState.CLOSED) {
        DefaultListAppBar(
            onSearchClicked = { onSearchAppBarStateChanged(SearchAppBarState.OPENED) },
            onSortClicked = onSortClicked,
            onDeleteAllClicked = onDeleteAllClicked
        )
    } else {
        LaunchedEffect(key1 = searchAppBarState) {
            if (searchAppBarState == SearchAppBarState.OPENED) {
                focusRequester.requestFocus()
            }
        }
        SearchAppBar(
            text = searchFieldValue,
            onTextChange = onSearchFieldValueChanged,
            onCloseClicked = { onSearchAppBarStateChanged(SearchAppBarState.CLOSED) },
            onSearchClicked = onSearchClicked,
            focusRequester = focusRequester

        )
    }

}

@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.list_screen_title),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteAllClicked = onDeleteAllClicked
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteAllClicked = onDeleteAllClicked)
}

@Composable
fun SearchAction(onSearchClicked: () -> Unit) {
    IconButton(onClick = { onSearchClicked() }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.search_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun SortAction(onSortClicked: (Priority) -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.FilterList,
            contentDescription = stringResource(R.string.sort_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }) {
            Priority.entries.toTypedArray().slice(setOf(0,2,3)).forEach{priority: Priority ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onSortClicked(priority)
                }) {
                    PriorityItem(priority)
                }
            }
        }
    }
}

@Composable
fun DeleteAllAction(onDeleteAllClicked: () -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(id = R.string.delete_all_actions),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                onDeleteAllClicked()
            }) {
                Text(
                    modifier = Modifier.padding(start = MEDIUM_PADDING),
                    text = stringResource(id = R.string.delete_all_actions),
                    style = Typography.subtitle2
                )
            }
        }
    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    focusRequester: FocusRequester
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor
    ) {
        val customTextSelectionColors = TextSelectionColors(
            handleColor = Color.Transparent,
            backgroundColor = Color.White.copy(alpha = ContentAlpha.disabled)
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = text,
                onValueChange = onTextChange,
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(ContentAlpha.medium),
                        text = stringResource(id = R.string.search_placeholder),
                        color = Color.White
                    )
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colors.topAppBarContentColor,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize
                ),
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        modifier = Modifier.alpha(ContentAlpha.disabled),
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.search_icon),
                            tint = MaterialTheme.colors.topAppBarContentColor
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (text.isEmpty()) {
                                onCloseClicked()
                            } else {
                                onTextChange("")
                            }

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.close_icon),
                            tint = MaterialTheme.colors.topAppBarContentColor
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = MaterialTheme.colors.topAppBarContentColor,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent
                )
            )
        }
    }
}

//@Composable
//@Preview
//fun DefaultListAppBarPreview() {
//    DefaultListAppBar(
//        onSearchClicked = {},
//        onSortClicked = {},
//        onDeleteAllClicked = {}
//    )
//}
//
//@Composable
//@Preview
//fun SearchAppBarPreview() {
//    SearchAppBar(text = "", onTextChange = {}, onCloseClicked = {}, onSearchClicked = {})
//}