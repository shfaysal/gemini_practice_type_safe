package com.example.typesafenavigation.dragAndDrop

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import java.util.UUID
import kotlin.math.roundToInt

enum class LocationType { PICKUP, STOPPAGE, DROPOFF }

data class LocationField(
    val id: String = UUID.randomUUID().toString(),
    val type: LocationType,
    val text: String = ""
)


@Composable
fun LocationList() {

    var locations by remember {
        mutableStateOf(
            mutableListOf(
                LocationField(type = LocationType.PICKUP, text = "Pickup Location"),
                LocationField(type = LocationType.DROPOFF, text = "Dropoff Location")
            )
        )
    }

    fun addStoppage() {
        val dropIndex = locations.indexOfFirst { it.type == LocationType.DROPOFF }
        locations.add(dropIndex, LocationField(type = LocationType.STOPPAGE))
    }

    fun removeStoppage(id: String) {
        locations.removeAll { it.id == id }
    }

    var draggingIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffsetY by remember { mutableStateOf(0f) }
    var itemHeights = remember { mutableMapOf<Int, Int>() }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(locations, key = { _, item -> item.id }) { index, item ->

            val isDragging = draggingIndex == index
            val offsetY = if (isDragging) dragOffsetY.roundToInt() else 0

            LocationRow(
                item = item,
                modifier = Modifier
                    .offset { IntOffset(0, offsetY) }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {

                                if (item.type == LocationType.PICKUP) return@detectDragGestures
                                draggingIndex = index
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()

                                if (draggingIndex != index) return@detectDragGestures

                                dragOffsetY += dragAmount.y

                                val height = itemHeights[index] ?: return@detectDragGestures
                                val threshold = height / 2f

                                if (dragOffsetY > threshold && index < locations.lastIndex) {

                                    val next = index + 1
                                    val nextItem = locations[next]

                                    if (nextItem.type != LocationType.PICKUP) {

                                        // Swap types between stoppage & dropoff
                                        if (
                                            (item.type == LocationType.STOPPAGE && nextItem.type == LocationType.DROPOFF) ||
                                            (item.type == LocationType.DROPOFF && nextItem.type == LocationType.STOPPAGE)
                                        ) {
                                            val updated = locations.toMutableList()
                                            updated[index] = updated[index].copy(type = nextItem.type)
                                            updated[next] = updated[next].copy(type = item.type)
                                            locations = updated
                                        }

                                        // Normal reorder
                                        locations = locations.toMutableList().apply {
                                            add(next, removeAt(index))
                                        }

                                        draggingIndex = next
                                        dragOffsetY = 0f
                                    }
                                }

                                if (dragOffsetY < -threshold && index > 0) {

                                    val prev = index - 1
                                    val prevItem = locations[prev]

                                    if (prevItem.type != LocationType.PICKUP) {

                                        // Swap types
                                        if (
                                            (item.type == LocationType.STOPPAGE && prevItem.type == LocationType.DROPOFF) ||
                                            (item.type == LocationType.DROPOFF && prevItem.type == LocationType.STOPPAGE)
                                        ) {
                                            val updated = locations.toMutableList()
                                            updated[index] = updated[index].copy(type = prevItem.type)
                                            updated[prev] = updated[prev].copy(type = item.type)
                                            locations = updated
                                        }

                                        locations = locations.toMutableList().apply {
                                            add(prev, removeAt(index))
                                        }

                                        draggingIndex = prev
                                        dragOffsetY = 0f
                                    }
                                }
                            },
                            onDragEnd = {
                                draggingIndex = null
                                dragOffsetY = 0f
                            }
                        )
                    },
                onTextChanged = { newText ->
                    locations[index] = locations[index].copy(text = newText)
                },
                onAdd = { addStoppage() },
                onRemove = { removeStoppage(item.id) },
                onHeightMeasured = { height ->
                    itemHeights[index] = height
                }
            )
        }
    }
}

@Preview(showBackground = true, name = "Location List Preview")
@Composable
fun LocationListPreview() {
    MaterialTheme {
        Surface {
            LocationListPreviewData()
        }
    }
}

@Composable
fun LocationListPreviewData() {
    val previewLocations = listOf(
        LocationField(type = LocationType.PICKUP, text = "Shukrabad, Dhanmondi, Dhaka."),
        LocationField(type = LocationType.STOPPAGE, text = "Kawran Bazar"),
        LocationField(type = LocationType.DROPOFF, text = "Bashundhara R/A")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        itemsIndexed(previewLocations, key = { _, it -> it.id }) { index, item ->
            LocationRow(
                item = item,
                modifier = Modifier,
                onTextChanged = {},
                onAdd = {},
                onRemove = {},
                onHeightMeasured = {}
            )
        }
    }
}

