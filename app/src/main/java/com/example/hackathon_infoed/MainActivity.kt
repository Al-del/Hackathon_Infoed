package com.example.hackathon_infoed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hackathon_infoed.ui.theme.Hackathon_InfoedTheme
import kotlinx.coroutines.delay

data class Element(val name: String, val color: Color)
val elements = listOf(
    Element("Water", Color.Blue),
    Element("Fire", Color.Red),
    Element("Earth", Color.Gray),
    Element("Air", Color.Gray)
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hackathon_InfoedTheme {
                var selectedColor by remember { mutableStateOf(Color.Transparent) }
                var elementPositions by remember { mutableStateOf(mutableListOf<Pair<Float, Float>>()) }

                Box(modifier = Modifier.fillMaxSize()) {
                    DrawingCanvas(elementPositions, selectedColor) { newPositions ->
                        elementPositions = newPositions.toMutableList()
                    }
                    ElementList(elements = elements) { color ->
                        selectedColor = color
                    }
                }
            }
        }
    }
}
@Composable
fun ElementList(elements: List<Element>, onElementSelected: (Color) -> Unit) {
    var selectedElement by remember { mutableStateOf<Element?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            items(elements) { element ->
                val isSelected = element == selectedElement
                val backgroundColor = if (isSelected) {
                    element.color.copy(alpha = 0.7f)
                } else {
                    element.color
                }

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(backgroundColor)
                        .padding(16.dp)
                        .clickable {
                            selectedElement = element
                            onElementSelected(element.color)
                        }
                ) {
                    Text(text = element.name, color = Color.White)
                }
            }
        }
    }
}
@Composable
fun DrawingCanvas(
    elementPositions: List<Pair<Float, Float>>,
    color: Color,
    onPathChanged: (List<Pair<Float, Float>>) -> Unit
) {
    val currentPositions = remember { mutableStateOf(elementPositions) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(16L) // Simulate 60 FPS
            val newPositions = currentPositions.value.map { (x, y) ->
                if (y < 1000f) { // Arbitrary screen bottom limit
                    x to y + 5f // Simulate gravity
                } else {
                    x to y
                }
            }
            currentPositions.value = newPositions
            onPathChanged(newPositions)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    val newPoint = change.position.x to change.position.y
                    currentPositions.value = currentPositions.value + newPoint
                    onPathChanged(currentPositions.value)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            currentPositions.value.forEach { (x, y) ->
                drawCircle(color, radius = 10f, center = Offset(x, y))
            }
        }
    }
}