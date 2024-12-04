package com.mbahrami.todolist.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.mbahrami.todolist.R
import com.mbahrami.todolist.data.models.Priority
import com.mbahrami.todolist.ui.theme.PRIORITY_DROP_DOWN_HEIGHT
import com.mbahrami.todolist.ui.theme.PRIORITY_INDICATOR_SIZE

@Composable
fun PriorityDropDown(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = stringResource(id = R.string.drop_down_arrow_anim)
    )

    var parentSize by remember { mutableStateOf(IntSize.Zero) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .onGloballyPositioned {
            parentSize = it.size
        }
        .height(PRIORITY_DROP_DOWN_HEIGHT)
        .background(MaterialTheme.colorScheme.background)
        .clickable { expanded = true }
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            shape = MaterialTheme.shapes.small
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(PRIORITY_INDICATOR_SIZE)
                .weight(1f)
        ) {
            drawCircle(color = selectedPriority.color)
        }
        Text(
            modifier = Modifier.weight(8f),
            text = selectedPriority.name,
            style = MaterialTheme.typography.bodyMedium
        )
        Icon(
            modifier = Modifier
                .alpha(0.38f)
                .rotate(angle)
                .weight(1.5f),
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = stringResource(id = R.string.drop_down_arrow)
        )
    }
    Box {
        DropdownMenu(
            modifier = Modifier.width(with(LocalDensity.current) { parentSize.width.toDp() }),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Priority.entries.toTypedArray().slice(0..2).forEach { prior ->
                DropdownMenuItem(
                    text = { PriorityItem(prior) },
                    onClick = {
                        expanded = false
                        onPrioritySelected(prior)
                    })
            }
        }
    }
}

@Composable
@Preview
fun PreviewPriorityDropDown() {
    PriorityDropDown(Priority.HIGH) { }
}