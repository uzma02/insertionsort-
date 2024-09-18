package com.example.insertionsort

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.insertionsort.presentation.InsertionSortViewModel
import com.example.insertionsort.presentation.TimeComplexityGraph
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
    var showTimeComplexity by remember { mutableStateOf(false) }
    var showGraph by remember { mutableStateOf(false) }  // State for showing graph
    var inputSize by remember { mutableStateOf(10) }     // State for controlling input size

    // Use a scroll state to handle scrolling
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),  // Apply vertical scrolling
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Visualize Sorting
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            VisualizeSorting(sortFlow = viewModel.getSortFlow())
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Field
        Text(
            "Enter numbers separated by space",
            color = Color.Magenta,
            fontSize = 18.sp
        )

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            singleLine = true
        )

        // Delay Input
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
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

        // Buttons for Starting Sort and Showing Time Complexity
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    val numbers = input.text.split(" ").mapNotNull { it.toIntOrNull() }
                    viewModel.listToSort.clear()
                    viewModel.listToSort.addAll(numbers)
                    viewModel.startSorting(delayMillis)
                    showTimeComplexity = false
                    showGraph = false  // Hide the graph when sorting starts
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Start Sort")
            }

            Spacer(modifier = Modifier.width(16.dp))


            Button(
                onClick = { showGraph = !showGraph },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text(if (showGraph) "Hide Time complexity" else "Show Time Complexity")
            }

        }

        Spacer(modifier = Modifier.height(16.dp))
        if (showGraph) {
            Text(
                text = "Time Complexity: O(n^2)",
                color = Color.Green,
                fontSize = 18.sp
            )
        }


        // Display Time Complexity Graph
        Spacer(modifier = Modifier.height(16.dp))

        if (showGraph) {
            TimeComplexityGraph(inputSize = inputSize)

            Spacer(modifier = Modifier.height(16.dp))

            Slider(
                value = inputSize.toFloat(),
                onValueChange = { newValue -> inputSize = newValue.toInt() },
                valueRange = 1f..100f,
                steps = 99,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}