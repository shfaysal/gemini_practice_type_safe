package com.example.typesafenavigation.dragAndDrop

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.typesafenavigation.R


@Preview
@Composable
fun OnSizedChanged(){

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Box {
            var imageHeightPx by remember { mutableIntStateOf(0) }

            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "I'm above the text",
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { size ->
                        // Don't do this
                        imageHeightPx = size.height
                    }
            )

            Text(
                text = "I'm below the image",
                modifier = Modifier.padding(
                    top = with(LocalDensity.current) { imageHeightPx.toDp() }
                )
            )
        }
    }


}