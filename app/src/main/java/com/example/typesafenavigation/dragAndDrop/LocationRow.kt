package com.example.typesafenavigation.dragAndDrop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.typesafenavigation.R

@Composable
fun LocationRow(
    item: LocationField,
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    onHeightMeasured: (Int) -> Unit
) {
    Box(
        modifier = modifier
            .onGloballyPositioned {
                onHeightMeasured(it.size.height)
            }
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                painterResource(
                    when (item.type) {
                        LocationType.PICKUP -> R.drawable.ic_launcher_background
                        LocationType.STOPPAGE -> R.drawable.ic_launcher_background
                        LocationType.DROPOFF -> R.drawable.ic_launcher_background
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.width(10.dp))

            TextField(
                value = item.text,
                onValueChange = onTextChanged,
                modifier = Modifier.weight(1f)
            )

            when (item.type) {
                LocationType.DROPOFF -> IconButton(onClick = onAdd) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
                LocationType.STOPPAGE -> IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
                else -> {}
            }
        }
    }
}
