package com.mbahrami.todolist.ui.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mbahrami.todolist.R

@Composable
fun EmptyContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(150.dp),
            imageVector = Icons.Filled.SentimentVeryDissatisfied,
            contentDescription = stringResource(id = R.string.sad_face_icon),
            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )

        Text(
            text = stringResource(id = R.string.empty_content),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            fontWeight = FontWeight.W900,
            fontSize = MaterialTheme.typography.titleSmall.fontSize
        )
    }
}