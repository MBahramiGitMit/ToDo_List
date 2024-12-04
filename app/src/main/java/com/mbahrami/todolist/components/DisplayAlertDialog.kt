package com.mbahrami.todolist.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mbahrami.todolist.R

@Composable
fun DisplayAlertDialog(
    isOpen: Boolean,
    title: String,
    message: String,
    onConfirmClicked: () -> Unit,
    onCloseClicked: () -> Unit
) {
    if (isOpen) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Normal
                )
            },
            confirmButton = {
                Button(onClick = {
                    onConfirmClicked()
                    onCloseClicked()
                }
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    onCloseClicked()
                }
                ) {
                    Text(text = stringResource(R.string.no))
                }
            },
            onDismissRequest = {
                onCloseClicked()
            },
        )
    }
}