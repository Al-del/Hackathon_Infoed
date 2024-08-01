package com.example.hackathon_infoed

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { finish() }) {
            Text("Back")
        }
        Text(
            text = "Description: $title",
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Toast.makeText(this@show_data, title, Toast.LENGTH_SHORT).show()
        loAD_video(reactionVideos[title]!!)
    }
}
    }
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

