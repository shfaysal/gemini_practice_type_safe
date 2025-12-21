package com.example.typesafenavigation.dragAndDrop

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun LocationSelectionCard() {
    var showStopField by remember { mutableStateOf(false) }
    var originText by remember { mutableStateOf("Dhanmondi, Dhaka") }
    var stopText by remember { mutableStateOf("") }
    var destinationText by remember { mutableStateOf("") }

    var rowHeightPx by remember { mutableStateOf(0) }
    var active by remember { mutableStateOf<Int?>(null) } // 2 for stop, 3 for destination
    var dragDy by remember { mutableStateOf(0f) }

    var originFocused by remember { mutableStateOf(false) }
    var stopFocused by remember { mutableStateOf(false) }
    var destinationFocused by remember { mutableStateOf(false) }


    fun swapLocations() {
        val temp = stopText
        stopText = destinationText
        destinationText = temp
    }

    val offsetStop = if (active == 2) dragDy else 0f
    val offsetDestination = if (active == 3) dragDy else 0f

    LaunchedEffect(dragDy, rowHeightPx, offsetStop, offsetDestination) {
        Log.d(
            "DRAGGABLE",
            "dragDy: $dragDy rowHeightPx: $rowHeightPx, offsetStop: $offsetStop, offsetDestination: $offsetDestination"
        )
    }


    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                // Origin
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = Color.White,
                        )
                        .padding(horizontal = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "A", color = Color.White)
                    }
                    LocationTextField(
                        value = originText,
                        onValueChange = { originText = it },
                        placeholder = "Pickup",
                        isFocused = originFocused,
                        onFocusChange = { originFocused = it },
                        modifier = Modifier.weight(1f)
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(start = 40.dp))

                // Stop
                AnimatedVisibility(visible = showStopField) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onSizeChanged { rowHeightPx = it.height }
                            .offset { IntOffset(0, offsetStop.roundToInt()) }
                            .then(
                                if (showStopField) {
                                    Modifier.pointerInput(Unit) {
                                        detectDragGesturesAfterLongPress(
                                            onDragStart = {
                                                active = 2
                                            },
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                dragDy += dragAmount.y
                                            },
                                            onDragEnd = {
                                                if (dragDy > rowHeightPx / 2) {
                                                    swapLocations()
                                                }
                                                dragDy = 0f
                                                active = null
                                            },
                                            onDragCancel = {
                                                dragDy = 0f
                                                active = null
                                            }
                                        )
                                    }
                                } else Modifier
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .then(if (active == 2) Modifier.shadow((2).dp, RoundedCornerShape(8.dp)) else Modifier)
                                .background(
                                    color = Color.White,
                                )
                                .padding(horizontal = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "B", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            LocationTextField(
                                value = stopText,
                                onValueChange = { stopText = it },
                                placeholder = "Stop",
                                isFocused = stopFocused,
                                onFocusChange = { stopFocused = it },
                                modifier = Modifier.weight(1f),
                                enabled = active == null
                            )
                        }
                    }
                }

                if (showStopField) {
                    HorizontalDivider(modifier = Modifier.padding(start = 40.dp))
                }

                // Destination
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset { IntOffset(0, offsetDestination.roundToInt()) }
                        .then(
                            if (showStopField) {
                                Modifier.pointerInput(Unit) {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = {
                                            active = 3
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            dragDy += dragAmount.y
                                        },
                                        onDragEnd = {
                                            if (dragDy < -rowHeightPx / 2) {
                                                swapLocations()
                                            }
                                            dragDy = 0f
                                            active = null
                                        },
                                        onDragCancel = {
                                            dragDy = 0f
                                            active = null
                                        }
                                    )
                                }
                            } else Modifier
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .then(if (active == 3) Modifier.shadow((2).dp, RoundedCornerShape(8.dp)) else Modifier)
                            .background(
                                color = Color.White,
                            )
                            .padding(horizontal = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = if (showStopField) "C" else "B", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        LocationTextField(
                            value = destinationText,
                            onValueChange = { destinationText = it },
                            placeholder = "Type your destination...",
                            isFocused = destinationFocused,
                            onFocusChange = { destinationFocused = it },
                            modifier = Modifier.weight(1f),
                            enabled = active == null
                        )
                    }
                }
            } // Close the Column

            IconButton(onClick = { showStopField = !showStopField }) {
                Icon(
                    imageVector = if (showStopField) Icons.Default.Clear else Icons.Default.Add,
                    contentDescription = if (showStopField) "Remove stop" else "Add stop"
                )
            }
        } // Close the Row
    } // Close the Card
} // Close the LocationSelectionCard function

@Composable
private fun LocationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isFocused: Boolean,
    onFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        maxLines = 1,
        singleLine = true,
        modifier = modifier.onFocusChanged { onFocusChange(it.isFocused) },
        enabled = enabled,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            if (value.isNotEmpty() && isFocused) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LocationSelectionCardPreview() {
    LocationSelectionCard()
}
