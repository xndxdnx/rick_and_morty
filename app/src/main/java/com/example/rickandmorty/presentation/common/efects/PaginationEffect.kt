package com.example.rickandmorty.presentation.common.efects

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter


// Реализ. Бесконечную пагинацию когда пользователь будет останавливаться у конца списка 
@Composable
fun LazyListPaginationEffect(
    // Состояние списка Lazy елементов (создано заранее разрабами) 
    listState: LazyListState,
    itemCount: Int,
    hasNextPage: Boolean,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    prefetchOffset: Int = 5
) {

    LaunchedEffect(
        listState,
        itemCount,
        hasNextPage,
        isLoading,
        isLoadingMore
    ) {
        // создаёт flow  который излучает при изменении отслеж. свойств и внутри него вычисляются условия для запуска загрузки
        snapshotFlow {
            
            val layoutInfo = listState.layoutInfo
            
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            
            val isNearEnd = lastVisibleIndex >= (itemCount - prefetchOffset)
            
            val userHasScrolled = listState.firstVisibleItemIndex > 0 || listState.canScrollForward

            val scrollSettLet = !listState.isScrollInProgress
            
            isNearEnd && hasNextPage && !isLoading && !isLoadingMore && itemCount > 0 && userHasScrolled && scrollSettLet  
        }   
            .debounce(300)
            .distinctUntilChanged()
            // фильтровать повторения значений которые не изменились
            .filter { value -> value == true }
            .collect {  
                onLoadMore()
            }
        
        
    }


}