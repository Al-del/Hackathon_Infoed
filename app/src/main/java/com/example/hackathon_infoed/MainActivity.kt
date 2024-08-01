package com.example.hackathon_infoed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp

class TemperatureViewModel : ViewModel() {
    private val _temperature = mutableStateOf(20)
    val temperature: State<Int> = _temperature

    fun incrementTemperature() {
        _temperature.value++
    }

    fun decrementTemperature() {
        _temperature.value--
    }

    // Public function to get the current temperature
    fun getTemperature(): Int {
        return _temperature.value
    }
}
data class Element(val name: String, val color: Color)
var selectedColorr : MutableState<Color> = mutableStateOf(Color.Transparent)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val temperatureViewModel: TemperatureViewModel by viewModels()
        val sensorViewModel: SensorViewModel by viewModels()

        setContent {
            val showDialog = remember { mutableStateOf(false) }
            selectedColorr = remember { mutableStateOf(Color.Transparent) }
            var elementPositions by remember { mutableStateOf(mutableListOf<ColoredPoint>()) }
            Spacer(modifier = Modifier.height(25.dp))
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFFfaf9f5))) {
                DrawingCanvas(
                    elementPositions,
                    selectedColor = selectedColorr.value,
                    onPathChanged = { newPositions -> elementPositions = newPositions.toMutableList() },
                    temperatureViewModel = temperatureViewModel,
                    sensorViewModel = sensorViewModel
                )
                ElementList(elements = elements) { color ->
                    selectedColorr.value = color
                }
                Spacer(modifier = Modifier.size(8.dp))
                TemperatureControl(
                    temperature = temperatureViewModel.temperature.value,
                    onIncrement = { temperatureViewModel.incrementTemperature() },
                    onDecrement = { temperatureViewModel.decrementTemperature() }
                )
                Spacer(modifier = Modifier.size(8.dp))

                Button(
                    onClick = { showDialog.value = true },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .offset(y = 30.dp)
                        .requiredHeight(50.dp)
                        .requiredWidth(150.dp),
                ) {
                    Text("Show Reactions", fontSize = 14.sp, color = Color.White)
                }
                Spacer(modifier = Modifier.size(8.dp))

                ReactionsAchievedDialog(showDialog.value, reactions_achieved) {
                    showDialog.value = false
                }
                Spacer(modifier = Modifier.size(8.dp))

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
                    Text(text = element.name, color = Color.White, fontSize = 24.sp)
                }
            }
        }
    }
}
data class ColoredPoint(
    var x: Float,
    var y: Float,
    var color: Color,
    var behavior: (ColoredPoint) -> ColoredPoint? = { it },
    val creationTime: Long? = null,
    var collided: Boolean = false,
    var vx: Float = 0f, // Velocity in x direction
    var vy: Float = 0f,  // Velocity in y direction
    var emoji: String? = colorToEmoji[color]
)
fun detectCollision(points: List<ColoredPoint>): List<Pair<ColoredPoint, ColoredPoint>> {
    val collisions = mutableListOf<Pair<ColoredPoint, ColoredPoint>>()
    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            val point1 = points[i]
            val point2 = points[j]
            val distance = Math.sqrt(Math.pow((point1.x - point2.x).toDouble(), 2.0) + Math.pow((point1.y - point2.y).toDouble(), 2.0))
            if (distance < 20) { // Assuming radius of 10 for each circle
                collisions.add(Pair(point1, point2))

            }
        }
    }
    return collisions
}
fun handleCollision(
    collisions: List<Pair<ColoredPoint, ColoredPoint>>,
    temperatureViewModel: TemperatureViewModel
): List<ColoredPoint> {
    val updatedPoints = mutableListOf<ColoredPoint>()
    val pointsToErase = mutableSetOf<ColoredPoint>()

    collisions.forEach { (point1, point2) ->
        val collisionFunction = collisionFunctions[Pair(point1.color, point2.color)]
            ?: collisionFunctions[Pair(point2.color, point1.color)]
            ?: return@forEach

        val (newPoint, shouldErase) = collisionFunction(point1, point2, temperatureViewModel)
        if (newPoint != null) {
            updatedPoints.add(newPoint)
        }
        if (shouldErase) {
            point1.collided = true
            point2.collided = true
            pointsToErase.add(point1)
            pointsToErase.add(point2)
        }

    }

    return updatedPoints.filterNot { it in pointsToErase || it.collided }
}
@Composable
fun DrawingCanvas(
    elementPositions: List<ColoredPoint>,
    selectedColor: Color,
    onPathChanged: (List<ColoredPoint>) -> Unit,
    temperatureViewModel: TemperatureViewModel,
    sensorViewModel: SensorViewModel
) {
    val currentPositions = remember { mutableStateOf(elementPositions) }
    val culi = selectedColorr

    LaunchedEffect(Unit) {
        while (true) {
            delay(16L)

            // Detect and handle collisions
            val collisions = detectCollision(currentPositions.value)
            val collisionResults = handleCollision(collisions, temperatureViewModel)
            val newPositions = currentPositions.value.mapNotNull { point ->
                if (collisionResults.any { it.x == point.x && it.y == point.y }) {
                    collisionResults.first { it.x == point.x && it.y == point.y }
                } else {
                    point.behavior(point)
                }
            }.filterNotNull()

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
                    val newPoint = ColoredPoint(change.position.x, change.position.y, selectedColor, behaviors[selectedColor] ?: { it })
                    newPoint.color = culi.value
                    newPoint.emoji = colorToEmoji[culi.value]
                    newPoint.behavior = behaviors[culi.value] ?: { it }
                    currentPositions.value = currentPositions.value + newPoint
                    onPathChanged(currentPositions.value)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            currentPositions.value.forEach { point ->
                when (point.color) {
                    Color.Blue, Color.Red, Color(0xffFF4500), Color(0xff696969), Color(0xffADD8E6), Color(0xffF0FFFF), Color(0xffA9A9A9), Color(0xff2F4F4F), Color(0xff81a5ba),
                    Color(0xffA9A9A9), Color(0xffe3b8e2), Color(0xff81a5bb), Color(0xff6E4B3A), Color(0xffA0522D), Color(0xff8B0000), Color(0xffB22222), Color(0xffADD8E6),
                    Color(0xffFFA500), Color(0xff8B4513), Color(0xff81a5bc), Color(0xffADD8E6), Color(0xffF0FFFE), Color(0xffF0FFFD) -> {
                        drawCircle(color = point.color, radius = 20f, center = Offset(point.x, point.y))
                        drawContext.canvas.nativeCanvas.drawText(
                            point.emoji ?: "",
                            point.x,
                            point.y,
                            android.graphics.Paint().apply {
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = 40f
                            }
                        )
                    }
                    Color.Cyan, Color(0xff61340b), Color(0xff808080), Color(0xff8B4513), Color(0xffB0C4DE), Color(0xff36454F), Color(0xff4B0082), Color(0xff00FFFF) -> {
                        drawRect(color = point.color, topLeft = Offset(point.x - 20f, point.y - 20f), size = Size(40f, 40f))
                        //Draw the emoji in the center of the rectangle

                        drawContext.canvas.nativeCanvas.drawText(
                            point.emoji ?:"",
                            point.x,
                            point.y,
                            android.graphics.Paint().apply {
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = 40f
                            }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun TemperatureControl(temperature: Int, onIncrement: () -> Unit, onDecrement: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = 30.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onDecrement,
                modifier = Modifier.size(50.dp),
            ) {
                Text(text = "-", fontSize = 25.sp, color = Color.White, modifier = Modifier.offset(-5.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "$temperature°C",
                modifier = Modifier.padding(horizontal = 16.dp) .height(50.dp).width(100.dp).offset(y=10.dp),
                fontSize = 24.sp

            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onIncrement,
                modifier = Modifier.size(50.dp).offset(x= -65.dp),

            ) {
                Text(text = "+", color = Color.White,
                    modifier = Modifier.offset(x=-5.dp), fontSize = 25.sp)
            }
        }
    }
}

// Define the SteamCreationDialog composable function

@Composable
fun ReactionsAchievedDialog(showDialog: Boolean, reactions: List<String>, onDismiss: () -> Unit) {
    if (showDialog) {
        //Make the list to have unique elements
        reactions.distinct()
        val context  = LocalContext.current
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Reactions Achieved") },
            text = {
                Column {
                    reactions.forEach { reaction ->
                        Text(text = reaction, modifier = Modifier.padding(4.dp).clickable {
                            val intent = Intent(context, show_data::class.java)
                           intent.putExtra("title", reaction)
                            context.startActivity(intent)
                        })
                    }
                }
            },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
        )
    }
}