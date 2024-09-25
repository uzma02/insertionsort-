package com.example.insertionsort

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.insertionsort.presentation.InsertionSortViewModel
import com.example.insertionsort.presentation.VisualizeSorting
import com.example.insertionsort.ui.theme.InsertionSortTheme

class InsertionSortActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InsertionSortTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = InsertionSortViewModel()
                    InsertionSortScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun InsertionSortScreen(viewModel: InsertionSortViewModel) {
    var input by remember { mutableStateOf(TextFieldValue("")) }
    var delayMillis by remember { mutableStateOf(500L) }
    var showGraph by remember { mutableStateOf(false) }
    var showSpaceComplexity by remember { mutableStateOf(false) }
    var showDescription by remember { mutableStateOf(false) } // State for showing description
    var worstCaseInputSize by remember { mutableStateOf(10) }
    var bestCaseInputSize by remember { mutableStateOf(10) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            VisualizeSorting(sortFlow = viewModel.getSortFlow())
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Enter numbers separated by space",
            color = Color.Magenta,
            fontSize = 18.sp
        )

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            singleLine = true
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Delay (ms):")
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = delayMillis.toString(),
                onValueChange = { delayMillis = it.toLongOrNull() ?: 500L },
                modifier = Modifier.width(100.dp),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Start Sort Button
        Button(
            onClick = {
                val numbers = input.text.split(" ").mapNotNull { it.toIntOrNull() }
                viewModel.listToSort.clear()
                viewModel.listToSort.addAll(numbers)
                viewModel.startSorting(delayMillis)
                showGraph = false
                showSpaceComplexity = false
                showDescription = false // Hide description when sorting starts
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Sort")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Description Button
        Button(
            onClick = { showDescription = !showDescription },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF50d8ec)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showDescription) "Hide Description" else "Show Description")
        }

        // Description Text
        if (showDescription) {
            Text(
                text = "Insertion sort  is a simple sorting algorithm that works by iteratively inserting each element of an unsorted list into its correct position in a sorted portion of the list. It is a  stable sorting  algorithm, meaning that elements with equal values maintain their relative order in the sorted output.",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Time Complexity Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { showGraph = !showGraph },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFc0f4b8)),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text(if (showGraph) "Hide Time Complexity" else "Show Time Complexity")
            }

            Button(
                onClick = { showSpaceComplexity = !showSpaceComplexity },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFfeffbe)),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text(if (showSpaceComplexity) "Hide Space Complexity" else "Show Space Complexity")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Time Complexity Graphs
        if (showGraph) {
            Text(
                text = "Worst-Case Time Complexity: O(n^2)",
                color = Color.Red,
                fontSize = 18.sp
            )

            TimeComplexityGraph(inputSize = worstCaseInputSize, complexityType = "Worst")
            Slider(
                value = worstCaseInputSize.toFloat(),
                onValueChange = { newValue -> worstCaseInputSize = newValue.toInt() },
                valueRange = 1f..100f,
                steps = 99,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Best-Case Time Complexity: O(n)",
                color = Color.Green,
                fontSize = 18.sp
            )

            TimeComplexityGraph(inputSize = bestCaseInputSize, complexityType = "Best")
            Slider(
                value = bestCaseInputSize.toFloat(),
                onValueChange = { newValue -> bestCaseInputSize = newValue.toInt() },
                valueRange = 1f..100f,
                steps = 99,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Display Space Complexity Graphs
        if (showSpaceComplexity) {
            Text(
                text = "Worst-Case Space Complexity: O(n)",
                color = Color.Red,
                fontSize = 18.sp
            )

            SpaceComplexityGraph(inputSize = worstCaseInputSize, complexityType = "Worst")
            Slider(
                value = worstCaseInputSize.toFloat(),
                onValueChange = { newValue -> worstCaseInputSize = newValue.toInt() },
                valueRange = 1f..100f,
                steps = 99,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Best-Case Space Complexity: O(1)",
                color = Color.Green,
                fontSize = 18.sp
            )

            SpaceComplexityGraph(inputSize = bestCaseInputSize, complexityType = "Best")
            Slider(
                value = bestCaseInputSize.toFloat(),
                onValueChange = { newValue -> bestCaseInputSize = newValue.toInt() },
                valueRange = 1f..100f,
                steps = 99,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



@Composable
fun TimeComplexityGraph(inputSize: Int, complexityType: String) {
    val data = when (complexityType) {
        "Worst" -> generateWorstCaseData(inputSize)
        "Best" -> generateBestCaseData(inputSize)
        else -> emptyList()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val padding = 50.dp.toPx()
            val canvasWidth = size.width - padding * 2
            val canvasHeight = size.height - padding * 2

            // Draw axes
            drawLine(
                color = Color.White,
                start = Offset(padding, canvasHeight + padding),
                end = Offset(size.width - padding, canvasHeight + padding),
                strokeWidth = 4f
            )

            drawLine(
                color = Color.White,
                start = Offset(padding, padding),
                end = Offset(padding, canvasHeight + padding),
                strokeWidth = 4f
            )

            // Draw data points
            val maxY = data.maxOf { it.second }
            data.forEach { (x, y) ->
                drawCircle(
                    color = if (complexityType == "Worst") Color.Red else Color.Green,
                    center = Offset(
                        x * (canvasWidth / inputSize) + padding,
                        canvasHeight - (y * (canvasHeight / maxY)) + padding
                    ),
                    radius = 5f
                )
            }
        }
    }
}

fun generateWorstCaseData(size: Int): List<Pair<Float, Float>> {
    return List(size) {
        val x = (it + 1).toFloat()
        val y = x * x
        x to y
    }
}

fun generateBestCaseData(size: Int): List<Pair<Float, Float>> {
    return List(size) {
        val x = (it + 1).toFloat()
        val y = x
        x to y
    }
}

@Composable
fun SpaceComplexityGraph(inputSize: Int, complexityType: String) {
    val data = when (complexityType) {
        "Worst" -> generateWorstSpaceData(inputSize)
        "Best" -> generateBestSpaceData(inputSize)
        else -> emptyList()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val padding = 50.dp.toPx()
            val canvasWidth = size.width - padding * 2
            val canvasHeight = size.height - padding * 2

            // Draw axes
            drawLine(
                color = Color.White,
                start = Offset(padding, canvasHeight + padding),
                end = Offset(size.width - padding, canvasHeight + padding),
                strokeWidth = 4f
            )

            drawLine(
                color = Color.White,
                start = Offset(padding, padding),
                end = Offset(padding, canvasHeight + padding),
                strokeWidth = 4f
            )

            // Draw data points
            val maxY = data.maxOf { it.second }
            data.forEach { (x, y) ->
                drawCircle(
                    color = if (complexityType == "Worst") Color.Red else Color.Green,
                    center = Offset(
                        x * (canvasWidth / inputSize) + padding,
                        canvasHeight - (y * (canvasHeight / maxY)) + padding
                    ),
                    radius = 5f
                )
            }
        }
    }
}

fun generateWorstSpaceData(size: Int): List<Pair<Float, Float>> {
    return List(size) {
        val x = (it + 1).toFloat()
        val y = x
        x to y
    }
}

fun generateBestSpaceData(size: Int): List<Pair<Float, Float>> {
    return listOf(Pair(0f, 1f), Pair(size.toFloat(), 1f))
}
