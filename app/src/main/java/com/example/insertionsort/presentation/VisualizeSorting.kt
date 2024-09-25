package com.example.insertionsort.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.insertionsort.domain.model.SortInfo
import com.example.insertionsort.domain.model.SortState
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.math.pow
import kotlin.random.Random



@Composable
fun VisualizeSorting(sortFlow: Flow<SortInfo>) {
    val sortState by sortFlow.collectAsState(initial = SortInfo(
        id = UUID.randomUUID().toString(),
        index = 0,
        key = -1,
        list = listOf(),
        sortState = SortState.IN_PROGRESS,
        j = -1,
        j1 = -1,
        currentLine = 1 // Initialize the currentLine parameter
    ))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            sortState.list.forEachIndexed { index, number ->
                val boxColor = when (index) {
                    sortState.j -> Color.Red
                    sortState.j1 -> Color(0xFF1F51FF)
                    else -> Color.Gray
                }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(4.dp)
                        .background(boxColor, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number.toString(),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (sortState.key != -1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (sortState.j >= 0) {
                    ArrowPointer("j", sortState.j, sortState.list, Color.Red)
                }
                if (sortState.j1 < sortState.list.size) {
                    ArrowPointer("j+1", sortState.j1, sortState.list, Color(0xFF1F51FF))
                }
                KeyPointer("key", sortState.index, sortState.key)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = "Code",
            color = Color.Magenta,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        PseudoCode(currentLine = sortState.currentLine)
    }
}

@Composable
fun PseudoCode(currentLine: Int) {
    val pseudocode = listOf(
        " void insertionSort(int arr[], int n) {",
        "     int i, key, j;",
        "     for (i = 1; i < n; i++) {",
        "         key = arr[i];",
        "         j = i - 1;",
        "         while (j >= 0 && arr[j] > key) {",
        "             arr[j + 1] = arr[j];",
        "             j = j - 1;",
        "         }",
        "        arr[j + 1] = key;",
        "    }",
        " }"
    )

    Column {
        pseudocode.forEachIndexed { index, line ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (index + 1 == currentLine) Color(0x80FFFFFF) else Color.Transparent // Light white highlight
                    )
                    .padding(4.dp) // Padding around the text
            ) {
                Text(
                    text = line,
                    color = if (index + 1 == currentLine) Color.Yellow else Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}



@Composable
fun ArrowPointer(pointerLabel: String, index: Int, list: List<Int>, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(pointerLabel, color = color, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text("↓", color = color, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(list[index].toString(), color = color, fontSize = 18.sp)
    }
    Spacer(modifier = Modifier.width(16.dp))
}

@Composable
fun KeyPointer(pointerLabel: String, index: Int, key: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(pointerLabel, color = Color.Green, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text("↓", color = Color.Green, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(key.toString(), color = Color.Green, fontSize = 18.sp)
    }
    Spacer(modifier = Modifier.width(16.dp))
}

@Composable
fun TimeComplexityGraph(inputSize: Int) {
    val randomData = generateRandomData(inputSize)
    val theoreticalData = generateTheoreticalData(inputSize)

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .padding(16.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw axes
            val padding = 20.dp.toPx() // Add padding for axes
            val canvasWidth = size.width - padding * 2
            val canvasHeight = size.height - padding * 2

            // Draw horizontal axis
            drawLine(
                color = Color.White,
                start = Offset(padding, canvasHeight + padding),
                end = Offset(size.width - padding, canvasHeight + padding),
                strokeWidth = 4f
            )

            // Draw vertical axis
            drawLine(
                color = Color.White,
                start = Offset(padding, padding),
                end = Offset(padding, canvasHeight + padding),
                strokeWidth = 4f
            )

            // Draw random data points
            val maxY = randomData.maxOf { it.second }
            randomData.forEach { (x, y) ->
                drawCircle(
                    color = Color.Blue,
                    center = Offset(
                        x * (canvasWidth / inputSize) + padding,
                        canvasHeight - (y * (canvasHeight / maxY)) + padding
                    ),
                    radius = 5f
                )
            }

            // Draw theoretical data line
            theoreticalData.zipWithNext { (x1, y1), (x2, y2) ->
                drawLine(
                    color = Color.Red,
                    start = Offset(
                        x1 * (canvasWidth / inputSize) + padding,
                        canvasHeight - (y1 * (canvasHeight / maxY)) + padding
                    ),
                    end = Offset(
                        x2 * (canvasWidth / inputSize) + padding,
                        canvasHeight - (y2 * (canvasHeight / maxY)) + padding
                    ),
                    strokeWidth = 4f
                )
            }
        }
    }
}
// Function to generate random quadratic data (for random points)
fun generateRandomData(size: Int): List<Pair<Float, Float>> {
    return List(size) {
        val x = (it + 1).toFloat() // x value
        val y = (x.pow(2) + Random.nextFloat() * 0.2f * x.pow(2)) // y value with some randomness
        x to y // Pair of x and y
    }
}

// Function to generate theoretical quadratic data (for O(n^2) curve)
fun generateTheoreticalData(size: Int): List<Pair<Float, Float>> {
    return List(size) {
        val x = (it + 1).toFloat() // x value
        val y = x.pow(2) // y value for the quadratic curve
        x to y // Pair of x and y
    }
}
