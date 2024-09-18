package com.example.insertionsort.presentation.state

import androidx.compose.ui.graphics.Color
import com.example.insertionsort.domain.model.SortState

data class InsertionSortInfoUiItem(
    val id: String,
    val index: Int,
    val key: Int,
    val list: List<Int>,
    val sortState: SortState,
    val color: Color,
    val currentI: Int,
    val currentJ: Int
)
