package com.playlab.escaperoomtimer.ui.components

import android.util.Log
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.snapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WheelTimePicker(
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    count: Int,
    size: DpSize = DpSize(128.dp, 128.dp),
    onScrollFinished: (index: Int) -> Unit,
    content: @Composable (index: Int) -> Unit
) {
    val listState = rememberLazyListState(0)
    val isScrollInProgress = listState.isScrollInProgress

    val snapFlingBehavior = snapFlingBehavior(
        snapLayoutInfoProvider = SnapLayoutInfoProvider(listState)
            .also {
                it.calculateSnapOffset(0f)
            },
        decayAnimationSpec = rememberSplineBasedDecay(),
        snapAnimationSpec = tween(
            300,
            easing = EaseInOutQuad
        )
    )

    LaunchedEffect(key1 = startIndex){
        listState.scrollToItem(index = startIndex)
    }

    LaunchedEffect(isScrollInProgress) {
//        if(!isScrollInProgress) {
            onScrollFinished(  calculateSnappedItemIndex(listState))
//        }
    }

    LazyColumn(
        modifier = modifier
            .height(size.height)
            .width(size.width / 2),
        state = listState,
        contentPadding = PaddingValues(vertical = size.height / 3),
        flingBehavior = snapFlingBehavior
    ) {
        items(count) { index ->
            Box(
                modifier = Modifier
                    .size(size.width, size.height / 3),
                contentAlignment = Alignment.Center
            ) {

                content(index)
            }
        }
    }
}

private fun calculateSnappedItemIndex(lazyListState: LazyListState): Int {

    var currentIndex = lazyListState.firstVisibleItemIndex
    val firstVisibleItemScrollOffset = lazyListState.firstVisibleItemScrollOffset

    if(firstVisibleItemScrollOffset != 0){
        currentIndex ++
    }

    return currentIndex
}

@Composable
@Preview(showBackground = true)
fun PreviewCustomTimePicker(){
    EscapeRoomTimerTheme() {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            WheelTimePicker(
                count = 60,
                startIndex = 59,
                onScrollFinished = {
                    Log.d("FLING", "selected index $it")
                }
            ) { index ->
                Text(text = "$index")
            }
        }
    }
}
