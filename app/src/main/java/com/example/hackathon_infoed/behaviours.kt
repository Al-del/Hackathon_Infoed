package com.example.hackathon_infoed

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import kotlin.math.truncate

data class ElementStatus(val color: Color, var isActive: Boolean)

val elements = listOf(
    Element("Water", Color.Blue),
    Element("Fire", Color.Red),
    Element("Ice", Color.Cyan),
    Element("Lava", Color(0xffFF4500)),
    Element("Earth", Color(0xff61340b)),
    Element("Rock", Color(0xff808080)),
    Element("Wood", Color(0xff8B4513)),
    Element("Metal", Color(0xffB0C4DE)),
    Element("Smoke", Color(0xff696969)),
    Element("Oxygen", Color(0xffADD8E6)),
    Element("Hydrogen", Color(0xffF0FFFF)),
    Element("Ash", Color(0xffA9A9A9)),
    Element("Gunpowder", Color(0xff2F4F4F)),
    Element("Coal", Color(0xff36454F)),

)

const val CEILING_HEIGHT = 200f
const val TRANSFORM_DELAY = 9000L // 20 seconds in milliseconds

val behaviors = mapOf<Color, (ColoredPoint) -> ColoredPoint?>(
    Color.Blue to { point -> if (point.collided) null else if (point.y <= 2000f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Water
    Color.Red to { point -> // Fire
        if (point.collided) null else {
            val currentTime = System.currentTimeMillis()
            val creationTime = point.creationTime ?: currentTime
            if (currentTime - creationTime > 500 || point.y <= CEILING_HEIGHT) null else point.copy(y = point.y + 5f, creationTime = creationTime)
        }
    },
    Color.Cyan to { point -> if (point.collided) null else if (point.y <= 2000f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 0f) else point }, // Ice
    Color(0xffFF4500) to { point -> if (point.collided) null else if (point.y <= 2000f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Lava
    Color(0xff61340b) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Earth
    Color(0xffC2B280) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Sand
    Color(0xff808080) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 0f) else point }, // Rock
    Color(0xff8B4513) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 0f) else point }, // Wood
    Color(0xffB0C4DE) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 0f) else point }, // Metal
    Color(0xff696969) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y - 5f) else point }, // Smoke
    Color(0xffADD8E6) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Oxygen
    Color(0xffF0FFFF) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Hydrogen
    Color(0xffA9A9A9) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Ash
    Color(0xff2F4F4F) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Gunpowder
    Color(0xff36454F) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 5f) else point }, // Coal,
    Color(0xff6E4B3A) to { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 3f) else point }, // Mud behavior
    Color(0xffA0522D) to { point -> if (point.collided) null else point.copy(y = point.y + 0f) }, // Heated Rock behavior
    Color(0xff8B0000) to { point -> // Burning Wood behavior
        if (point.collided) null else {
            val currentTime = System.currentTimeMillis()
            val creationTime = point.creationTime ?: currentTime
            val elapsedTime = currentTime - creationTime
            if (elapsedTime > 5000) {
                null // Disappear after 5 seconds
            } else {
                val newColor = Color(
                    red = (point.color.red + (255 - point.color.red) * (elapsedTime / 1000.0)).toInt(),
                    green = (point.color.green * (1 - elapsedTime / 1000.0)).toInt(),
                    blue = (point.color.blue * (1 - elapsedTime / 1000.0)).toInt()
                )
                point.copy(color = newColor, creationTime = creationTime)
            }
        }
    },
    Color(0xffB22222) to { point -> // Melted Metal behavior
        if (point.collided) null else {
            val currentTime = System.currentTimeMillis()
            val creationTime = point.creationTime ?: currentTime
            val elapsedTime = currentTime - creationTime
            if (elapsedTime < 3000) {
                val newColor = Color(
                    red = (point.color.red + (255 - point.color.red) * (elapsedTime / 1000.0)).toInt(),
                    green = (point.color.green * (1 - elapsedTime / 1000.0)).toInt(),
                    blue = (point.color.blue * (1 - elapsedTime / 1000.0)).toInt()
                )
                point.copy(color = newColor, creationTime = creationTime,y = point.y + 3f)
            } else {
                ColoredPoint(point.x, point.y, Color(0xffB0C4DE), { point -> if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 0f) else point }) // Resolidify after 3 seconds
            }
        }
    },
    Color(0xffFFA500) to { point -> // Explosion behavior
        if (point.collided) null else {
            val currentTime = System.currentTimeMillis()
            val creationTime = point.creationTime ?: currentTime
            val elapsedTime = currentTime - creationTime
            if (elapsedTime < 2000) {
                point.copy(y = point.y + 4f, creationTime = creationTime)
            } else {
                null // Disappear after 2 seconds
            }
        }
    },
    Color(0xff8B4513) to { point -> // Special Mud behavior
        if (point.collided) null else if (point.y <= 2001f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 3f) else point
    },
    Color(0xff00FFFF) to { point -> // Frozen Water behavior
        if (point.collided) null else if (point.y <= 2000f && point.y > CEILING_HEIGHT) point.copy(y = point.y + 0f) else point
    }

)
val specificColors = listOf(
    Color(0xff81a5ba),
    Color(0xffA9A9A9),
    Color(0xffe3b8e2),
    Color(0xff81a5bb),
    Color(0xff6E4B3A),
    Color(0xffA0522D),
    Color(0xff8B0000),
    Color(0xffB22222),
    Color(0xffADD8E6),
    Color(0xffFFA500),
    Color(0xff8B4513),
    Color(0xff81a5bc),
    Color(0xff00FFFF),
    Color(0xffADD8E6),
    Color(0xffF0FFFE),
Color(0xffF0FFFD)

    )
val reactions_achieved: MutableList<String> = mutableListOf()
val colorMap: MutableMap<Color, Boolean> = specificColors.associateWith { false }.toMutableMap()

fun getBooleanForColor(color: Color): Boolean? {
    return colorMap[color]
}
val collisionFunctions = mapOf<Pair<Color, Color>, (ColoredPoint, ColoredPoint, TemperatureViewModel) -> Pair<ColoredPoint?, Boolean>>(
    Pair(Color.Blue, Color.Red) to { point1, point2, temperatureViewModel -> // Water + Fire = Steam
        point1.collided = true
        point2.collided = true
        val currentTemperature = temperatureViewModel.getTemperature()
        Log.d("Collision", "Fire and Water collided. Current temperature: $currentTemperature°C")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff81a5ba), behaviors[Color(0xff81a5ba)] ?: {
            point -> // Gas
            if (point.collided) null else {
                if(colorMap[Color(0xff81a5ba)] == false) {
                    colorMap[Color(0xff81a5ba)] = true
                    //Add Steam to the list reactions_achieved
                    reactions_achieved.add("Steam")
                    Log.d("kilo", reactions_achieved.toString())
                  Log.d("kilo","slay")
                }
                val currentTime = System.currentTimeMillis()
                val creationTime = point.creationTime ?: currentTime
                if (currentTemperature < 100 && (currentTime - creationTime > TRANSFORM_DELAY || point.y <= CEILING_HEIGHT + 100f)) {
                    ColoredPoint(point.x, point.y, Color.Blue, behaviors[Color.Blue] ?: { it })
                } else {
                    point.copy(y = point.y - 5f, creationTime = creationTime)
                }

            }
        }), true)
    },
    Pair(Color.Red, Color.Blue) to { point1, point2, temperatureViewModel -> // Fire + Water = Steam
        point1.collided = true
        point2.collided = true
        val currentTemperature = temperatureViewModel.getTemperature()
        Log.d("Collision", "Fire and Water collided. Current temperature: $currentTemperature°C")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff81a5ba), behaviors[Color(0xff81a5ba)] ?: { point -> // Gas
            if (point.collided) null else {
                if(colorMap[Color(0xff81a5ba)] == false) {
                    colorMap[Color(0xff81a5ba)] = true
                    reactions_achieved.add("Steam")
                    Log.d("kilo","slay")
                }
                val currentTime = System.currentTimeMillis()
                val creationTime = point.creationTime ?: currentTime
                if (currentTemperature < 100 && (currentTime - creationTime > TRANSFORM_DELAY || point.y <= CEILING_HEIGHT + 100f)) {
                    ColoredPoint(point.x, point.y, Color.Blue, behaviors[Color.Blue] ?: { it })
                } else {
                    point.copy(y = point.y - 5f, creationTime = creationTime)
                }
            }
        }), true)
    },
    Pair(Color(0xff81a5ba), Color.Cyan) to { point1, point2, temperatureViewModel -> // Steam + Ice = Water
        point1.collided = true
        point2.collided = true
        val currentTemperature = temperatureViewModel.getTemperature()
        Log.d("Collision", "Steam and Ice collided. Current temperature: $currentTemperature°C")
        if(colorMap[Color(0xff81a5ba)] == false) {
            colorMap[Color(0xff81a5ba)] = true
            reactions_achieved.add("Freezing")
            Log.d("kilo","slay")
        }
        Pair(ColoredPoint(point1.x, point1.y, Color.Cyan, behaviors[Color.Cyan] ?: { it }), true)
    },
    Pair(Color(0xff81a5ba), Color(0xffFF4500)) to { point1, point2, temperatureViewModel -> // Steam + Lava = Water
        point1.collided = true
        point2.collided = true
        val currentTemperature = temperatureViewModel.getTemperature()
        Log.d("Collision", "Steam and Lava collided. Current temperature: $currentTemperature°C")
        Pair(ColoredPoint(point1.x, point1.y, Color.Blue, behaviors[Color.Blue] ?: { it }), true)
    },
    Pair(Color(0xff81a5ba), Color(0xffA9A9A9)) to { point1, point2, temperatureViewModel -> // Steam + Ash = calcium hydroxide

        point1.collided = true
        point2.collided = true
        val currentTemperature = temperatureViewModel.getTemperature()
        Log.d("Collision", "Steam and Ash collided. Current temperature: $currentTemperature°C")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xffe3b8e2), behaviors[Color(0xffe3b8e2)] ?: { point -> if (point.collided) null else if (point.y <= 2000f && point.y > CEILING_HEIGHT) {
            if(colorMap[Color(0xff81a5ba)] == false) {
                colorMap[Color(0xff81a5ba)] = true
                reactions_achieved.add("Calcium oxidation")
                Log.d("kilo","slay")
            }
            point.copy(y = point.y + 5f)

        } else point }), true) //calcium hydroxide
    },
    Pair(Color.Cyan, Color(0xffFF4500)) to { point1, point2, temperatureViewModel -> // Steam + Ash = calcium hydroxide

        point1.collided = true
        point2.collided = true
        val currentTemperature = temperatureViewModel.getTemperature()
        Log.d("Collision", "Fire and Water collided. Current temperature: $currentTemperature°C")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff81a5bb), behaviors[Color(0xff81a5ba)] ?: {
                point -> // Gas
            if (point.collided) null else {
                if(colorMap[Color(0xff81a5bb)] == false) {
                    colorMap[Color(0xff81a5bb)] = true
                    //Add Steam to the list reactions_achieved
                    reactions_achieved.add("Melting")
                    Log.d("kilo", reactions_achieved.toString())
                    Log.d("kilo","slay")
                }
                val currentTime = System.currentTimeMillis()
                val creationTime = point.creationTime ?: currentTime
                if (currentTemperature < 100 && (currentTime - creationTime > TRANSFORM_DELAY || point.y <= CEILING_HEIGHT + 100f)) {
                    ColoredPoint(point.x, point.y, Color.Blue, behaviors[Color.Blue] ?: { it })
                } else {
                    point.copy(y = point.y - 5f, creationTime = creationTime)
                }

            }
        }), true)
    },
    Pair(Color.Blue, Color(0xff61340b)) to { point1, point2, temperatureViewModel -> // Water + Earth = Mud
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xff6E4B3A)] == false) {
            colorMap[Color(0xff6E4B3A)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Mud")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Water and Earth collided to create Mud.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff6E4B3A), behaviors[Color(0xff6E4B3A)] ?: { it }), true)
    },
    Pair(Color(0xff61340b), Color.Blue) to { point1, point2, temperatureViewModel -> // Earth + Water = Mud
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xff6E4B3A)] == false) {
            colorMap[Color(0xff6E4B3A)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Mud")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Earth and Water collided to create Mud.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff6E4B3A), behaviors[Color(0xff6E4B3A)] ?: { it }), true)
    },
    Pair(Color(0xff808080), Color.Red) to { point1, point2, temperatureViewModel -> // Rock + Fire = Heated Rock
        point1.collided = true
        point2.collided = true
        temperatureViewModel.incrementTemperature() // Increase temperature

        if(colorMap[Color(0xffA0522D)] == false) {
            colorMap[Color(0xffA0522D)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Thermal Decomposition")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Rock and Fire collided to create Heated Rock.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xffA0522D), behaviors[Color(0xffA0522D)] ?: { it }), true)
    },
    Pair(Color.Red, Color(0xff808080)) to { point1, point2, temperatureViewModel -> // Fire + Rock = Heated Rock
        point1.collided = true
        point2.collided = true
        temperatureViewModel.incrementTemperature() // Increase temperature

        if(colorMap[Color(0xffA0522D)] == false) {
            colorMap[Color(0xffA0522D)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Thermal Decomposition")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Fire and Rock collided to create Heated Rock.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xffA0522D), behaviors[Color(0xffA0522D)] ?: { it }), true)
    },
    Pair(Color(0xff8B4513), Color.Red) to { point1, point2, temperatureViewModel -> // Wood + Fire = Burning Wood
        point1.collided = true
        point2.collided = true
        temperatureViewModel.incrementTemperature() // Increase temperature

        if(colorMap[Color(0xff8B0000)] == false) {
            colorMap[Color(0xff8B0000)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Burning wood")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Wood and Fire collided to create Burning Wood.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff8B0000), behaviors[Color(0xff8B0000)] ?: { it }), true)
    },
    Pair(Color.Red, Color(0xff8B4513)) to { point1, point2, temperatureViewModel -> // Fire + Wood = Burning Wood
        point1.collided = true
        point2.collided = true
        temperatureViewModel.incrementTemperature() // Increase temperature

        if(colorMap[Color(0xff8B0000)] == false) {
            colorMap[Color(0xff8B0000)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Burning wood")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Fire and Wood collided to create Burning Wood.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff8B0000), behaviors[Color(0xff8B0000)] ?: { it }), true)
    },
    Pair(Color(0xffB0C4DE), Color.Red) to { point1, point2, temperatureViewModel -> // Metal + Fire = Melted Metal
        point1.collided = true
        point2.collided = true
        temperatureViewModel.incrementTemperature() // Increase temperature

        if(colorMap[Color(0xffB22222)] == false) {
            colorMap[Color(0xffB22222)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Metal melting")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Metal and Fire collided to create Melted Metal.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xffB22222), behaviors[Color(0xffB22222)] ?: { it }), true)
    },
    Pair(Color.Red, Color(0xffB0C4DE)) to { point1, point2, temperatureViewModel -> // Fire + Metal = Melted Metal
        point1.collided = true
        point2.collided = true
        temperatureViewModel.incrementTemperature() // Increase temperature

        if(colorMap[Color(0xffB22222)] == false) {
            colorMap[Color(0xffB22222)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Metal melting")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Fire and Metal collided to create Melted Metal.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xffB22222), behaviors[Color(0xffB22222)] ?: { it }), true)
    },
    Pair(Color(0xffADD8E6), Color.Red) to { point1, point2, temperatureViewModel -> // Oxygen + Fire = Fire + Fire
        point1.collided = true
        temperatureViewModel.incrementTemperature() // Increase temperature

        point2.collided = true
        if(colorMap[Color(0xffADD8E6)] == false) {
            colorMap[Color(0xffADD8E6)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Fire expansion")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Oxygen and Fire collided to create more Fire.")
        val newFire1 = ColoredPoint(point1.x, point1.y, Color.Red, behaviors[Color.Red] ?: { it })
        val newFire2 = ColoredPoint(point2.x, point2.y, Color.Red, behaviors[Color.Red] ?: { it })
        Pair(newFire1, true)
        Pair(newFire2, true)
    },
    Pair(Color.Red, Color(0xffADD8E6)) to { point1, point2, temperatureViewModel -> // Fire + Oxygen = Fire + Fire
        point1.collided = true
        point2.collided = true
        temperatureViewModel.incrementTemperature() // Increase temperature

        if(colorMap[Color(0xffADD8E6)] == false) {
            colorMap[Color(0xffADD8E6)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Fire expansion")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Fire and Oxygen collided to create more Fire.")
        val newFire1 = ColoredPoint(point1.x, point1.y, Color.Red, behaviors[Color.Red] ?: { it })
        val newFire2 = ColoredPoint(point2.x, point2.y, Color.Red, behaviors[Color.Red] ?: { it })
        Pair(newFire1, true)
        Pair(newFire2, true)
    },
    Pair(Color(0xff2F4F4F), Color.Red) to { point1, point2, temperatureViewModel -> // Gunpowder + Fire = Explosion
        point1.collided = true
        point2.collided = true
        temperatureViewModel.incrementTemperature() // Increase temperature

        if(colorMap[Color(0xffFFA500)] == false) {
            colorMap[Color(0xffFFA500)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Explosion")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Gunpowder and Fire collided to create an Explosion.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xffFFA500), behaviors[Color(0xffFFA500)] ?: { it }), true)
    },
    Pair(Color.Red, Color(0xff2F4F4F)) to { point1, point2, temperatureViewModel -> // Fire + Gunpowder = Explosion
        point1.collided = true
        point2.collided = true
        temperatureViewModel.incrementTemperature() // Increase temperature

        if(colorMap[Color(0xffFFA500)] == false) {
            colorMap[Color(0xffFFA500)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Explosion")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Fire and Gunpowder collided to create an Explosion.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xffFFA500), behaviors[Color(0xffFFA500)] ?: { it }), true)
    },
    Pair(Color(0xffA9A9A9), Color.Blue) to { point1, point2, temperatureViewModel -> // Ash + Water = Special Mud
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xff8B4513)] == false) {
            colorMap[Color(0xff8B4513)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Mudification with Ash")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Ash and Water collided to create Special Mud.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff8B4513), behaviors[Color(0xff8B4513)] ?: { it }), true)
    },
    Pair(Color.Blue, Color(0xffA9A9A9)) to { point1, point2, temperatureViewModel -> // Water + Ash = Special Mud
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xff8B4513)] == false) {
            colorMap[Color(0xff8B4513)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Mudification with Ash")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Water and Ash collided to create Special Mud.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff8B4513), behaviors[Color(0xff8B4513)] ?: { it }), true)
    },
    Pair(Color(0xffFF4500), Color.Blue) to { point1, point2, temperatureViewModel -> // Lava + Water = Steam
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xff81a5bc)] == false) {
            colorMap[Color(0xff81a5bc)] = true
            reactions_achieved.add("Rapid cooling of lava")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Lava and Water collided to create Steam.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff81a5bc), behaviors[Color(0xff81a5ba)] ?: {
                point -> // Gas
            if (point.collided) null else {
                if(colorMap[Color(0xff81a5ba)] == false) {
                    colorMap[Color(0xff81a5ba)] = true
                    //Add Steam to the list reactions_achieved
                    reactions_achieved.add("Steam")
                    Log.d("kilo", reactions_achieved.toString())
                    Log.d("kilo","slay")
                }
                val currentTime = System.currentTimeMillis()
                val currentTemperature = temperatureViewModel.getTemperature()
                val creationTime = point.creationTime ?: currentTime
                if (currentTemperature < 100 && (currentTime - creationTime > TRANSFORM_DELAY || point.y <= CEILING_HEIGHT + 100f)) {
                    ColoredPoint(point.x, point.y, Color.Blue, behaviors[Color.Blue] ?: { it })
                } else {
                    point.copy(y = point.y - 5f, creationTime = creationTime)
                }

            }
        }), true)
    },
    Pair(Color.Blue, Color(0xffFF4500)) to { point1, point2, temperatureViewModel -> // Water + Lava = Steam
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xff81a5bc)] == false) {
            colorMap[Color(0xff81a5bc)] = true
            reactions_achieved.add("Rapid cooling of lava")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Water and Lava collided to create Steam.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff81a5bc), behaviors[Color(0xff81a5ba)] ?: {
                point -> // Gas
            if (point.collided) null else {
                if(colorMap[Color(0xff81a5ba)] == false) {
                    colorMap[Color(0xff81a5ba)] = true
                    //Add Steam to the list reactions_achieved
                    reactions_achieved.add("Steam")
                    Log.d("kilo", reactions_achieved.toString())
                    Log.d("kilo","slay")
                }
                val currentTime = System.currentTimeMillis()
                val currentTemperature = temperatureViewModel.getTemperature()
                val creationTime = point.creationTime ?: currentTime
                if (currentTemperature < 100 && (currentTime - creationTime > TRANSFORM_DELAY || point.y <= CEILING_HEIGHT + 100f)) {
                    ColoredPoint(point.x, point.y, Color.Blue, behaviors[Color.Blue] ?: { it })
                } else {
                    point.copy(y = point.y - 5f, creationTime = creationTime)
                }

            }
        }), true)
    },
    Pair(Color.Blue, Color.Cyan) to { point1, point2, temperatureViewModel -> // Water + Ice = Frozen Water
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xff00FFFF)] == false) {
            colorMap[Color(0xff00FFFF)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Freezing")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        temperatureViewModel.decrementTemperature() // Decrease temperature
        Log.d("Collision", "Water and Ice collided to create Frozen Water.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff00FFFF), behaviors[Color(0xff00FFFF)] ?: { it }), true)
    },
    Pair(Color.Cyan, Color.Blue) to { point1, point2, temperatureViewModel -> // Ice + Water = Frozen Water
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xff00FFFF)] == false) {
            colorMap[Color(0xff00FFFF)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Freezing")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        temperatureViewModel.decrementTemperature() // Decrease temperature
        Log.d("Collision", "Ice and Water collided to create Frozen Water.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff00FFFF), behaviors[Color(0xff00FFFF)] ?: { it }), true)
    },
    Pair(Color(0xffF0FFFF), Color(0xffADD8E6)) to { point1, point2, temperatureViewModel -> // Hydrogen + Oxygen = Water
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xffF0FFFE)] == false) {
            colorMap[Color(0xffF0FFFE)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Making water")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Pair(ColoredPoint(point1.x, point1.y, Color.Blue, behaviors[Color.Blue] ?: { it }), true)
    },
    Pair(Color(0xffADD8E6), Color(0xffF0FFFF)) to { point1, point2, temperatureViewModel -> // Oxygen + Hydrogen = Water
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xffF0FFFE)] == false) {
            colorMap[Color(0xffF0FFFE)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Making water")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Oxygen and Hydrogen collided to create Water.")
        Pair(ColoredPoint(point1.x, point1.y, Color.Blue, behaviors[Color.Blue] ?: { it }), true)
    },
    Pair(Color(0xffF0FFFF), Color.Red) to { point1, point2, temperatureViewModel -> // Hydrogen + Fire = Water
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xffF0FFFD)] == false) {
            colorMap[Color(0xffF0FFFD)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("combustion")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        temperatureViewModel.decrementTemperature() // Decrease temperature
        Log.d("Collision", "Hydrogen and Fire collided to create Water.")
        Pair(ColoredPoint(point1.x, point1.y, Color.Blue, behaviors[Color.Blue] ?: { it }), true)
    },
    Pair(Color.Red, Color(0xffF0FFFF)) to { point1, point2, temperatureViewModel -> // Fire + Hydrogen = Water
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xffF0FFFD)] == false) {
            colorMap[Color(0xffF0FFFD)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("combustion")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        temperatureViewModel.decrementTemperature() // Decrease temperature
        Log.d("Collision", "Fire and Hydrogen collided to create Water.")
        Pair(ColoredPoint(point1.x, point1.y, Color.Blue, behaviors[Color.Blue] ?: { it }), true)
    }

)