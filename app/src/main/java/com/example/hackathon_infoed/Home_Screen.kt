package com.example.hackathon_infoed

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hackathon_infoed.ui.theme.Hackathon_InfoedTheme

class Home_Screen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WelcomeScreen()
        }
    }
}

@Composable
fun WelcomeScreen() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9C71C3))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Welcome To\nLearning Lab!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.offset(x= -35.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Experiment. Discover. Grow.",
                fontSize = 22.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.norisor1),
                    contentDescription = "Girl reading",
                    modifier = Modifier
                        .weight(1f)
                        .size(200.dp)
                        .padding(end = 16.dp)
                        .offset(x= -16.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.norisor2),
                    contentDescription = "Girl reading",
                    modifier = Modifier
                        .weight(1f)
                        .size(200.dp)
                        .padding(start = 16.dp)
                        .offset(x= 26.dp, y=55.dp)

                )
            }
            Spacer(modifier = Modifier.height(55.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth().height(70.dp) .clip(RoundedCornerShape(16.dp))
                    .padding(horizontal = 32.dp)
                    .offset(y= 20.dp)
                    .clickable {

                        // Create an Intent to start MainActivity
                        val intent = Intent(context, MainActivity::class.java)
                        // Start MainActivity
                        context.startActivity(intent)
                    }
                    .background(color = Color.Black)


            ) {
                Text(
                    text = "Get started",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center) // Center the text
                )
            }
            Spacer(modifier = Modifier.weight(1f))

        }
        Box(
            modifier = Modifier.align(Alignment.BottomEnd),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = painterResource(id = R.drawable.femeieie),
                contentDescription = "Ahh Bih",
                modifier = Modifier
                    .size(350.dp)
                    .offset(y=60.dp)
            )
        }
    }
}