package com.example.typesafenavigation.dragAndDrop

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactM3TextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default
) {
    val interactionSource = remember { MutableInteractionSource() }

    val colors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        singleLine = true,
        textStyle = textStyle,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 0.dp),
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = { Text(placeholderText) },
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                colors = colors
            )
        }
    )
}

@Composable
fun Swap2And3_NoOverlap() {
    var t1 by rememberSaveable { mutableStateOf("") }
    var t2 by rememberSaveable { mutableStateOf("") }
    var t3 by rememberSaveable { mutableStateOf("") }

    var rowHeightPx by remember { mutableStateOf(0) }

    // one shared drag state
    var active by remember { mutableStateOf<Int?>(null) } // 2 or 3 (which one is being dragged)
    var dragDy by remember { mutableStateOf(0f) }         // current drag offset (px)

    fun swapText2And3() {
        val tmp = t2
        t2 = t3
        t3 = tmp
    }

    // Apply offsets so rows move together (no visual overlap)
    val offset2 = when (active) {
        2 -> dragDy.coerceIn(0f, rowHeightPx.toFloat())            // dragging 2 down
        3 -> dragDy.coerceIn(-rowHeightPx.toFloat(), 0f)           // dragging 3 up moves 2 up
        else -> 0f
    }

    val offset3 = when (active) {
        2 -> dragDy.coerceIn(0f, rowHeightPx.toFloat()) - rowHeightPx // push 3 down as 2 comes down (keeps spacing)
        3 -> dragDy.coerceIn(-rowHeightPx.toFloat(), 0f) + rowHeightPx // push 2 up as 3 goes up (keeps spacing)
        else -> 0f
    }

    Column(Modifier.background(Color.White).padding(16.dp)) {

        CompactM3TextField(
            value = t1,
            onValueChange = { t1 = it },
            placeholderText = "Text Field 1"
        )

        Spacer(Modifier.height(22.dp))
        HorizontalDivider()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { rowHeightPx = it.height }
                .offset { IntOffset(0, offset2.roundToInt()) }
                .pointerInput(rowHeightPx) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { active = 2 },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragDy += dragAmount.y
                            // allow only towards each other
                            dragDy = dragDy.coerceIn(0f, rowHeightPx.toFloat())
                        },
                        onDragEnd = {
                            if (rowHeightPx > 0 && dragDy > rowHeightPx * 0.5f) swapText2And3()
                            dragDy = 0f
                            active = null
                        },
                        onDragCancel = {
                            dragDy = 0f
                            active = null
                        }
                    )
                }
        ) {
            CompactM3TextField(
                value = t2,
                onValueChange = { t2 = it },
                placeholderText = "Text Field 2",
                readOnly = active == 2
            )
        }

        Spacer(Modifier.height(22.dp))
        HorizontalDivider()

        // Row 3
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(0, offset3.roundToInt()) }
                .pointerInput(rowHeightPx) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { active = 3 },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragDy += dragAmount.y
                            // allow only towards each other (up)
                            dragDy = dragDy.coerceIn(-rowHeightPx.toFloat(), 0f)
                        },
                        onDragEnd = {
                            if (rowHeightPx > 0 && abs(dragDy) > rowHeightPx * 0.5f) swapText2And3()
                            dragDy = 0f
                            active = null
                        },
                        onDragCancel = {
                            dragDy = 0f
                            active = null
                        }
                    )
                }
        ) {
            CompactM3TextField(
                value = t3,
                onValueChange = { t3 = it },
                placeholderText = "Text Field 3",
                readOnly = active == 3
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Swap2And3_NoOverlap_Preview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Swap2And3_NoOverlap()
    }
}





