package com.sample.ripedotnet.core.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sample.ripedotnet.core.designsystem.component.setEdgeEvents

@Composable
fun <Item> ListContentWidget(
    items: List<Item>,
    onKey: (Item) -> String,
    onItemClick: (Item) -> Unit,
    onBottomEvent: () -> Unit,
    isBottomProgress: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (Item, (Item) -> Unit) -> Unit
) {
    println("ItemsOrganizationsContent()")

    val listState = rememberLazyListState()

    listState.setEdgeEvents(
        debounce = 1000,
        prefetch = 3,
        onTopList = { index ->
            println("ItemsOrganizationsContent() - onTopList: $index")
        },
        onBottomList = { index ->
            println("ItemsOrganizationsContent() - onBottomList: $index")
            onBottomEvent()
        }
    )

    Column(modifier = modifier.fillMaxWidth().fillMaxHeight()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = items,
                key = onKey
            ) { item ->
                content(item, onItemClick)
            }
        }

        if (isBottomProgress) {
            LinearProgressIndicator(modifier = Modifier
                .fillMaxWidth()
                .height(4.dp))
        }
    }
}