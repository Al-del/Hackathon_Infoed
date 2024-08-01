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
    Element("\uD83D\uDCA7", Color.Blue),
    Element("\uD83D\uDD25", Color.Red),
    Element("\uD83E\uDDCA", Color.Cyan),
    Element("\uD83C\uDF0B", Color(0xffFF4500)),
    Element("\uD83C\uDF31", Color(0xff61340b)),
    Element("\uD83E\uDEA8", Color(0xff808080)),
    Element("\uD83E\uDEB5", Color(0xff8B4513)),
    Element("⛓\uFE0F", Color(0xffB0C4DE)),
    Element("\uD83D\uDCA8", Color(0xff696969)),
    Element("\uD83C\uDD7E", Color(0xffADD8E6)),
    Element("\uD83C\uDDED", Color(0xffF0FFFF)),
    Element("☁", Color(0xffA9A9A9)),
    Element("⛰", Color(0xff2F4F4F)),
    Element("\uD83C\uDF11", Color(0xff36454F)),

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
    },
    Color(0xff4B0082) to { point -> if (point.collided) null else point }, // Obsidian behavior
    Color(0xff808080) to { point -> if (point.collided) null else point } // Rock behavior


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
    Color(0xffF0FFFD),
    Color(0xff4B0082),
    Color(0xff808080),
    Color(0xff80808f)

    )
val reactions_achieved: MutableList<String> = mutableListOf()
val colorMap: MutableMap<Color, Boolean> = specificColors.associateWith { false }.toMutableMap()
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
    },
    Pair(Color(0xffFF4500), Color(0xff61340b)) to { point1, point2, temperatureViewModel -> // Lava + Earth = Obsidian
        point1.collided = true
        point2.collided = true
        //Increase temperature
        temperatureViewModel.incrementTemperature()
        if(colorMap[Color(0xff4B0082)] == false) {
            colorMap[Color(0xff4B0082)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Obsidian")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Lava and Earth collided to create Obsidian.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff4B0082), behaviors[Color(0xff4B0082)] ?: { it }), true)
    },
    Pair(Color(0xff61340b), Color(0xffFF4500)) to { point1, point2, temperatureViewModel -> // Earth + Lava = Obsidian
        point1.collided = true
        point2.collided = true
        temperatureViewModel.incrementTemperature()
        if(colorMap[Color(0xff4B0082)] == false) {
            colorMap[Color(0xff4B0082)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Obsidian")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Earth and Lava collided to create Obsidian.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff4B0082), behaviors[Color(0xff4B0082)] ?: { it }), true)
    },
    Pair(Color.Red, Color(0xff61340b)) to { point1, point2, temperatureViewModel -> // Fire + Earth = Rock
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xff808080)] == false) {
            colorMap[Color(0xff808080)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Lava cooling")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Fire and Earth collided to create Rock.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff808080), behaviors[Color(0xff808080)] ?: { it }), true)
    },
    Pair(Color(0xff61340b), Color.Red) to { point1, point2, temperatureViewModel -> // Earth + Fire = Rock
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xff808080)] == false) {
            colorMap[Color(0xff808080)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Lava cooling")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Earth and Fire collided to create Rock.")
        Pair(ColoredPoint(point1.x, point1.y, Color(0xff808080), behaviors[Color(0xff808080)] ?: { it }), true)
    },
    Pair(Color.Red, Color.Cyan) to { point1, point2, temperatureViewModel -> // Fire + Ice = Water
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xff80808f)] == false) {
            colorMap[Color(0xff80808f)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Fire melting ice")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Fire and Ice collided to create Water.")
        Pair(ColoredPoint(point1.x, point1.y, Color.Blue, behaviors[Color.Blue] ?: { it }), true)
    },
    Pair(Color.Cyan, Color.Red) to { point1, point2, temperatureViewModel -> // Ice + Fire = Water
        point1.collided = true
        point2.collided = true
        if(colorMap[Color(0xff80808f)] == false) {
            colorMap[Color(0xff80808f)] = true
            //Add Steam to the list reactions_achieved
            reactions_achieved.add("Fire melting ice")
            Log.d("kilo", reactions_achieved.toString())
            Log.d("kilo","slay")
        }
        Log.d("Collision", "Ice and Fire collided to create Water.")
        Pair(ColoredPoint(point1.x, point1.y, Color.Blue, behaviors[Color.Blue] ?: { it }), true)
    }

)
val reactionVideos = mapOf(
    "Steam" to R.raw.apafoc,
    "Mud" to R.raw.earthwater,
    "Thermal Decomposition" to R.raw.piatrafoc,
    "Burning wood" to R.raw.lemnfoc,
    "Metal melting" to R.raw.focmetal,
    "Fire expansion" to R.raw.piatrafoc, //TODO PUNE-L PE CEZAR SA FACA VIDEO OXIGEN FOC
    "Explosion" to R.raw.gunpowderfire,
    "Mudification with Ash" to R.raw.ashsteam,
    "Rapid cooling of lava" to R.raw.icelava,
    "Freezing" to R.raw.earthwater, //TODO PUNE-L PE CEZAR SA FACA VIDEO FREEZING-ul
    "Making water" to R.raw.hydrogenfire, //TODO PUNE-l PE CEZAR SA IA VIDEOCLIP DESPRE OXYGEN + APA
    "combustion" to R.raw.hydrogenfire,
    "Calcium oxidation" to R.raw.ashsteam, //TODO Pune-l pe Cezar sa faca video cu calcium oxidation
    "Obsidian" to R.raw.coalfire, //TODO pune-l pe Cezar sa puna video de cum se obtine obsidian
    "Lava cooling" to R.raw.piatrafoc,//TODO Pune-l pe Cezar sa puna video de piatra + fox
    "Fire melting ice" to R.raw.fireice
)
val Safety_instructions = mapOf(
    "Steam" to "Avoid Burns: Steam can cause severe burns. Keep a safe distance and avoid direct contact.\n" +
            "Use Protective Gear: Wear gloves and eye protection when dealing with steam.",
    "Mud" to "Avoid Slippery Surfaces: Wet mud can be slippery; be cautious of footing to prevent falls.\n" +
            "Hygiene: Wash hands thoroughly after handling mud to prevent the spread of bacteria.\n",
    "Thermal Decomposition" to "Ventilation: Ensure adequate ventilation as decomposition can release gases.\n" +
            "Avoid Inhalation: Use masks to avoid inhaling potentially harmful fumes.\n",
    "Burning wood" to "Fire Safety: Keep a fire extinguisher nearby and maintain a safe distance.\n" +
            "Smoke Inhalation: Avoid inhaling smoke; use a mask if necessary.",
    "Metal melting" to "Heat Protection: Wear heat-resistant gloves and eye protection.\n" +
            "Proper Ventilation: Melting metals can release fumes; ensure good ventilation.",
    "Fire expansion" to "Containment: Use fire-resistant barriers to prevent the spread of fire.\n" +
            "Avoid Flammable Materials: Keep away from flammable substances.", //TODO PUNE-L PE CEZAR SA FACA VIDEO OXIGEN FOC
    "Explosion" to "Distance and Shielding: Maintain a safe distance and use protective shielding.\n" +
            "Hearing Protection: Wear ear protection to prevent hearing damage.",
    "Mudification with Ash" to "Respiratory Protection: Wear masks to avoid inhaling ash particles.\n" +
            "Skin Protection: Use gloves to prevent skin irritation.",
    "Rapid cooling of lava" to "Heat Protection: Use heat-resistant gloves and face shields.\n" +
            "Avoid Contact: Do not touch the lava directly; it remains extremely hot.",
    "Freezing" to "Cold Protection: Wear gloves and appropriate clothing to avoid frostbite.\n" +
            "Safe Handling: Use tongs or other tools to handle extremely cold objects.",
    "Making water" to "Explosion Risk: Handle hydrogen with care as it is highly flammable.\n" +
            "Proper Ventilation: Ensure adequate ventilation to avoid gas buildup.",
    "combustion" to "Fire Safety: Keep flammable materials away and have a fire extinguisher nearby.\n" +
            "Avoid Inhalation: Combustion can produce harmful gases; use masks.",
    "Calcium oxidation" to "Eye Protection: Use goggles to prevent dust from entering eyes.\n" +
            "Gloves: Wear gloves to avoid skin contact with reactive materials.",
    "Obsidian" to " Sharp Edges: Obsidian can be sharp; handle with care to avoid cuts.\n" +
            "Proper Disposal: Dispose of any broken pieces safely to avoid injury.",
    "Lava cooling" to "Heat Protection: Use appropriate protective gear to handle hot materials.\n" +
            "Avoid Direct Contact: Lava remains hot for a long time; avoid touching.",
    "Fire melting ice" to "Water Management: Be mindful of the water produced from melting ice to prevent slips.\n" +
            "Fire Safety: Control the fire to prevent it from spreading."
)
val colorToEmoji = mapOf(
    Color(0xff00FFFF) to "❄️", // Frozen Water
    Color(0xff4B0082) to "🪨", // Obsidian
    Color(0xff808080) to "🪨", // Rock
    Color.Blue to "💧", // Water
    Color.Red to "🔥", // Fire
    Color(0xff81a5ba) to "💨", // Steam
    Color.Cyan to "❄️", // Ice
    Color(0xffFF4500) to "🌋", // Lava
    Color(0xffA9A9A9) to "🌫️", // Ash
    Color(0xff61340b) to "🌍", // Earth
    Color(0xff8B4513) to "🪵", // Wood
    Color(0xffB0C4DE) to "🔩", // Metal
    Color(0xff2F4F4F) to "💣", // Gunpowder
    Color(0xffF0FFFF) to "💧", // Hydrogen
    Color(0xffADD8E6) to "💨", // Oxygen
    Color(0xffFFA500) to "💥", // Explosion
    Color(0xff8B0000) to "🔥", // Burning Wood
    Color(0xffB22222) to "🔥", // Melted Metal
    Color(0xff6E4B3A) to "🌍", // Mud
    Color(0xffe3b8e2) to "🧪", // Calcium Hydroxide
    Color(0xff81a5bb) to "💨", // Gas
    Color(0xff81a5bc) to "💨", // Steam
    Color(0xffF0FFFE) to "💧", // Water
    Color(0xffF0FFFD) to "💧", // Water
    Color(0xff80808f) to "💧" // Water
)