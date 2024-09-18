package com.example.insertionsort.domain.model


data class SortInfo(
    val id: String,
    val index: Int,
    val key: Int,
    val list: List<Int>,
    val sortState: SortState,
    val j: Int,
    val j1: Int,
    val currentLine: Int // Add this line
)
