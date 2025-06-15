package com.example.familytree

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

object Widgets {

    @Composable
    fun SetImage(
        imageId: Int
    ) {
        Image(
            painter = painterResource(id = imageId),
            modifier = Modifier
                .size(128.dp)
                .padding(
                    bottom = 16.dp
                )
                .fillMaxWidth(),
            contentDescription = "logo"
        )
    }

    @Composable
    fun ErrorSnackbar(
        snackbarHostState: SnackbarHostState,
        modifier: Modifier = Modifier
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = modifier
        ) { data ->
            Snackbar(
                containerColor = Color(0xFFAD0202),
                contentColor = Color.White,
                content = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Text(
                            text = data.visuals.message,
                            fontSize = stringResource(R.string.snackbar_text_size).toInt().sp,

                            )
                    }

                }
            )
        }
    }

}