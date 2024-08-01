package com.example.hackathon_infoed

import News
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.analytics.AnalyticsListener
import com.example.hackathon_infoed.ui.theme.Hackathon_InfoedTheme
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.controller.VideoPlayerControllerConfig
import io.sanghun.compose.video.uri.VideoPlayerMediaItem

class show_data : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val title = intent.getStringExtra("title")
        setContent {
            val context = LocalContext.current
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                Button(onClick = { finish() }) {
                    Text("Back")
                }
                Text(
                    text = "Description: $title",
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Toast.makeText(this@show_data, title, Toast.LENGTH_SHORT).show()
                loAD_video(reactionVideos[title]!!)
                Text(
                    text = "Safety Instructions: ${Safety_instructions[title]}",
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                ReactionsAchievedRow(reactions_achieved, reactionDetails)

                /* Button(onClick ={
                     val intent = Intent(context, Noutati::class.java)
                     context.startActivity(intent)
                 }) {
                     Text("View Safety Instructions")
                 }*/
            }
        }
    }
    val reactionDetails: Map<String, Pair<Int, String>> = mapOf(
        "Melting" to Pair(R.drawable.metal_melting, "Heat Protection: Wear heat-resistant gloves and eye protection.\nProper Ventilation: Melting metals can release fumes; ensure good ventilation."),
        "Fire expansion" to Pair(R.drawable.fire_exp, "Containment: Use fire-resistant barriers to prevent the spread of fire.\nAvoid Flammable Materials: Keep away from flammable substances."),
        "Explosion" to Pair(R.drawable.expl, "Distance and Shielding: Maintain a safe distance and use protective shielding.\nHearing Protection: Wear ear protection to prevent hearing damage."),
        "Mud" to Pair(R.drawable.mudification_with_ash, "Respiratory Protection: Wear masks to avoid inhaling ash particles.\nSkin Protection: Use gloves to prevent skin irritation."),
        "Rapid cooling of lava" to Pair(R.drawable.rpd_cool_lava, "Heat Protection: Use heat-resistant gloves and face shields.\nAvoid Contact: Do not touch the lava directly; it remains extremely hot."),
        "Freezing" to Pair(R.drawable.frz, "Cold Protection: Wear gloves and appropriate clothing to avoid frostbite.\nSafe Handling: Use tongs or other tools to handle extremely cold objects."),
        "Making water" to Pair(R.drawable.mkg_water, "Explosion Risk: Handle hydrogen with care as it is highly flammable.\nProper Ventilation: Ensure adequate ventilation to avoid gas buildup."),
        "combustion" to Pair(R.drawable.combustion, "Fire Safety: Keep flammable materials away and have a fire extinguisher nearby.\nAvoid Inhalation: Combustion can produce harmful gases; use masks."),
        "Calcium oxidation" to Pair(R.drawable.ca_oxd, "Eye Protection: Use goggles to prevent dust from entering eyes.\nGloves: Wear gloves to avoid skin contact with reactive materials."),
        "Obsidian" to Pair(R.drawable.obsidian, "Sharp Edges: Obsidian can be sharp; handle with care to avoid cuts.\nProper Disposal: Dispose of any broken pieces safely to avoid injury."),
        "Lava cooling" to Pair(R.drawable.lava_cooling, "Heat Protection: Use appropriate protective gear to handle hot materials.\nAvoid Direct Contact: Lava remains hot for a long time; avoid touching."),
        "Fire melting ice" to Pair(R.drawable.brn_wood, "Water Management: Be mindful of the water produced from melting ice to prevent slips.\nFire Safety: Control the fire to prevent it from spreading."),
        "Thermal Decomposition" to Pair(R.drawable.fire_exp, "Ventilation: Ensure proper ventilation to prevent gas buildup.\nProtective Gear: Wear appropriate safety gear to avoid exposure."),
        "Steam" to Pair(R.drawable.steam, "Burn Risk: Steam can cause burns; avoid direct contact.\nVentilation: Ensure proper ventilation to prevent steam buildup."),
    )

}
@Composable
fun loAD_video(res_id: Int){
    VideoPlayer(
        mediaItems = listOf(
            VideoPlayerMediaItem.RawResourceMediaItem(
                resourceId = res_id,
            ),


            ),
        handleLifecycle = true,
        autoPlay = true,
        usePlayerController = true,
        enablePip = true,
        handleAudioFocus = true,
        controllerConfig = VideoPlayerControllerConfig(
            showSpeedAndPitchOverlay = false,
            showSubtitleButton = false,
            showCurrentTimeAndTotalTime = true,
            showBufferingProgress = false,
            showForwardIncrementButton = true,
            showBackwardIncrementButton = true,
            showBackTrackButton = true,
            showNextTrackButton = true,
            showRepeatModeButton = true,
            controllerShowTimeMilliSeconds = 5_000,
            controllerAutoShow = true,
            showFullScreenButton = true,
        ),
        volume = 0.5f,  // volume 0.0f to 1.0f
        repeatMode = RepeatMode.NONE,       // or RepeatMode.ALL, RepeatMode.ONE
        onCurrentTimeChanged = { // long type, current player time (millisec)
            Log.e("CurrentTime", it.toString())
        },
        playerInstance = { // ExoPlayer instance (Experimental)
            addAnalyticsListener(
                object : AnalyticsListener {
                    // player logger
                }
            )
        },
        modifier = Modifier
            .requiredHeight(300.dp)
            .fillMaxWidth()
        ,
    )
}
@Composable
fun ReactionsAchievedRow(reactions: List<String>, reactionDetails: Map<String, Pair<Int, String>>) {
    val context = LocalContext.current
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(reactions) { reaction ->
            Log.d("kilo", reaction)
            val details = reactionDetails[reaction]
            if (details != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = details.first),
                        contentDescription = reaction,
                        modifier = Modifier.size(100.dp)
                            .clickable {
                                val intent = Intent(context, Noutati::class.java)
                                intent.putExtra("title", reaction)
                                intent.putExtra("description", details.second)
                                intent.putExtra("photo_path", details.first)
                                context.startActivity(intent)
                            }
                    )
                    Text(
                        text = reaction,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
