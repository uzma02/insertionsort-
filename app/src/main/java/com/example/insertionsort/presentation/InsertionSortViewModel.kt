package com.example.insertionsort.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertionsort.domain.InsertionSortUseCase
import kotlinx.coroutines.launch

class InsertionSortViewModel : ViewModel() {

    var listToSort = mutableStateListOf<Int>()
        set(value) { field = value } // Making the setter public

    private val insertionSortUseCase = InsertionSortUseCase()

    fun startSorting(delayMillis: Long = 500L) {
        viewModelScope.launch {
            insertionSortUseCase(listToSort.toList(), delayMillis)
        }
    }

    fun getSortFlow() = insertionSortUseCase.sortFlow
}
