package com.example.hackathon_infoed

import androidx.compose.ui.graphics.Color

val elements = listOf(
    Element("Water", Color.Blue),
    Element("Fire", Color.Red),
    Element("Ice", Color.Cyan),
    Element("Lava", Color(0xffFF4500)),
    Element("Earth", Color(0xff61340b)),
    Element("Sand", Color(0xffC2B280)),
    Element("Rock", Color(0xff808080)),
    Element("Wood", Color(0xff8B4513)),
    Element("Metal", Color(0xffB0C4DE)),
    Element("Smoke", Color(0xff696969)),
    Element("Oxygen", Color(0xffADD8E6)),
    Element("Hydrogen", Color(0xffF0FFFF)),
    Element("Ash", Color(0xffA9A9A9)),
    Element("Gunpowder", Color(0xff2F4F4F)),
    Element("Coal", Color(0xff36454F))
)

const val CEILING_HEIGHT = 200f

val behaviors = mapOf<Color, (ColoredPoint) -> ColoredPoint?>(
    Color.Blue to { point -> if (point.collided) null else if (point.y <= 2000f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Water
    Color.Red to { point -> // Fire
        if (point.collided) null else {
            val currentTime = System.currentTimeMillis()
            val creationTime = point.creationTime ?: currentTime
            if (currentTime - creationTime > 500 || point.y <= CEILING_HEIGHT) null else point.copy(y = point.y + 5f, creationTime = creationTime)
        }
    },
    Color.Cyan to { point -> if (point.collided) null else if (point.y <= 2000f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 2f) else point }, // Ice
    Color(0xffFF4500) to { point -> if (point.collided) null else if (point.y <= 2000f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Lava
    Color(0xff61340b) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Earth
    Color(0xffC2B280) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Sand
    Color(0xff808080) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Rock
    Color(0xff8B4513) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Wood
    Color(0xffB0C4DE) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Metal
    Color(0xff696969) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y - 5f) else point }, // Smoke
    Color(0xffADD8E6) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Oxygen
    Color(0xffF0FFFF) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Hydrogen
    Color(0xffA9A9A9) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Ash
    Color(0xff2F4F4F) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Gunpowder
    Color(0xff36454F) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Coal,
)
val collisionFunctions = mapOf<Pair<Color, Color>, (ColoredPoint, ColoredPoint) -> Pair<ColoredPoint?, Boolean>>(
    Pair(Color.Blue, Color.Red) to { point1, point2 -> // Water + Fire = Steam
        point1.collided = true
        point2.collided = true
    Pair(ColoredPoint(point1.x, point1.y, Color(0xff81a5ba), behaviors[Color(0xff81a5ba)] ?: { point -> if (point.y > CEILING_HEIGHT) point.copy(y = point.y - 5f) else point }), true)
    },
    Pair(Color.Red, Color.Blue) to { point1, point2 -> // Fire + Water = Steam
        point1.collided = true
        point2.collided = true
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff81a5ba), behaviors[Color(0xff81a5ba)] ?: { point -> if (point.y > CEILING_HEIGHT) point.copy(y = point.y - 5f) else point }), true)
    },

)