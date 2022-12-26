package com.playlab.escaperoomtimer.ui.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme
import kotlinx.coroutines.delay

@Composable
fun RatingAnimation(
    modifier: Modifier = Modifier,
    travelDistance: Dp = 20.dp
) {
    val items = listOf(
        remember { Animatable(initialValue = 0f)},
        remember { Animatable(initialValue = 0f)},
        remember { Animatable(initialValue = 0f)},
        remember { Animatable(initialValue = 0f)},
        remember { Animatable(initialValue = 0f)}
    )

    items.forEachIndexed{ index, animatable ->
        LaunchedEffect(key1 = animatable){
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing

                        0.0f at 10000 with LinearOutSlowInEasing
                        //tempo em repouso
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at durationMillis with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    val itemsValues = items.map{ it.value }
    //para poder usar a funcao de extensao toPx()
    val distance = with(LocalDensity.current){ travelDistance.toPx() }
    val lastCircle = itemsValues.size -1

    Row(modifier = modifier){
        itemsValues.forEachIndexed { index, value ->
            Box(
               modifier = Modifier
                   .size(32.dp)
                   .graphicsLayer {
                       translationY = -value * distance
                   },
            ){
                Text(
                    text = "\uD83D\uDCA3",
                )
            }
            if(index != lastCircle){
                Spacer(modifier = Modifier.width(0.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoadingAnimation() {
    EscapeRoomTimerTheme() {
        RatingAnimation(
            modifier = Modifier.padding(18.dp),
        )
    }
}