package com.example.hackathon_infoed

import androidx.compose.ui.graphics.Color
val elements = listOf(
    Element("Water", Color.Blue),
    Element("Fire", Color.Red),
    Element("Earth", Color(0xff61340b)),
    Element("Air", Color.Gray)
)
val behaviors = mapOf<Color, (ColoredPoint) -> ColoredPoint>(
    Color.Blue to { point ->
        if (point.y <= 2000f) point.copy(y = point.y + 5f) else point
    }, // Move downwards
    Color.Red to { point ->
        if (point.y <= 2000f) point.copy(y = point.y + 5f) else point
    }, // Move right
    Color.Gray to { point ->
        if (point.y <= 2001f) point.copy(x = point.x - 2f) else point
    }, // Move left
    Color(0xff61340b) to { point ->
        if (point.y <= 2001f) point.copy(y = point.y + 5f) else point
    }, // Fly upwards
    Color.Black to { point ->
        if (point.y <= 2020f) point.copy(y = point.y - 5f) else point
    } // Fly upwards
)
val collisionFunctions = mapOf<Pair<Color, Color>, (ColoredPoint, ColoredPoint) -> ColoredPoint>(
    Pair(Color.Blue, Color.Red) to { point1, point2 ->
        ColoredPoint(point1.x, point1.y, Color.Black, behaviors[Color.Black] ?: { it.copy(y = it.y - 5f) })
    },
    Pair(Color.Red, Color.Blue) to { point1, point2 ->
        ColoredPoint(point1.x, point1.y, Color.Black, behaviors[Color.Black] ?: { it.copy(y = it.y - 5f) })
    }
    // Add more collision functions here
)