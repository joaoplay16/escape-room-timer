package com.example.testcompose

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
    val flingBehavior = rememberSnapFlingBehavior(listState)
    val isScrollInProgress = listState.isScrollInProgress

    LaunchedEffect(key1 = startIndex){
        listState.scrollToItem(index = startIndex)
    }

    LaunchedEffect(isScrollInProgress) {
        if(!isScrollInProgress) {
            onScrollFinished(  calculateSnappedItemIndex(listState))
        }
    }

    LazyColumn(
        modifier = modifier
            .height(size.height)
            .width(size.width / 2),
        state = listState,
        contentPadding = PaddingValues(vertical = size.height / 3),
        flingBehavior = flingBehavior
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
