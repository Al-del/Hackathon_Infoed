package com.example.hackathon_infoed

import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.MutableState
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
var selectedColorr : MutableState<Color> = mutableStateOf(Color.Transparent)

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
                 selectedColorr = remember { mutableStateOf(Color.Transparent) }
                var elementPositions by remember { mutableStateOf(mutableListOf<ColoredPoint>()) }

                Box(modifier = Modifier.fillMaxSize()) {
                    DrawingCanvas(elementPositions, selectedColor = selectedColorr.value) { newPositions ->
                        elementPositions = newPositions.toMutableList()
                    }
                    ElementList(elements = elements) { color ->
                        selectedColorr.value = color
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
data class ColoredPoint(val x: Float, val y: Float, var color: Color)
fun detectCollision(point1: ColoredPoint, point2: ColoredPoint): Boolean {
    val distance = Math.sqrt(Math.pow((point1.x - point2.x).toDouble(), 2.0) + Math.pow((point1.y - point2.y).toDouble(), 2.0))
    return distance < 20 // Assuming radius of 10 for each circle
}

fun handleCollision(point1: ColoredPoint, point2: ColoredPoint): ColoredPoint {
    return if ((point1.color == Color.Blue && point2.color == Color.Red) || (point1.color == Color.Red && point2.color == Color.Blue)) {
        ColoredPoint(point1.x, point1.y, Color.DarkGray)
    } else {
        point1
    }
}

@Composable
fun DrawingCanvas(
    elementPositions: List<ColoredPoint>,
    selectedColor: Color,
    onPathChanged: (List<ColoredPoint>) -> Unit
) {
    val currentPositions = remember { mutableStateOf(elementPositions) }
    val culi = selectedColorr

    LaunchedEffect(Unit) {
        while (true) {
            delay(16L) // Simulate 60 FPS

            // Detect and handle collisions
            val newPositions = currentPositions.value.map { point ->
                var newPoint = point
                currentPositions.value.forEach { otherPoint ->
                    if (point != otherPoint && detectCollision(point, otherPoint)) {
                        newPoint = handleCollision(point, otherPoint)
                    }
                }
                newPoint
            }.map { point ->
                // Update positions with gravity or flying effect
                if (point.color == Color.DarkGray) {
                    point.copy(y = point.y - 5f) // Fly upwards
                } else if (point.y < 1000f) { // Arbitrary screen bottom limit
                    point.copy(y = point.y + 5f) // Simulate gravity
                } else {
                    point
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
                    val newPoint = ColoredPoint(change.position.x, change.position.y, selectedColor)
                    Log.d("kilo", "selectedColor: $culi")
                    newPoint.color = culi.value
                    currentPositions.value = currentPositions.value + newPoint
                    onPathChanged(currentPositions.value)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            currentPositions.value.forEach { point ->
                Log.d("DrawingCanvas", "Drawing point at (${point.x}, ${point.y})")
                Log.d("DrawingCanvas", "Color: ${point.color}")
                drawCircle(color = point.color, radius = 10f, center = Offset(point.x, point.y))
            }
        }
    }
}