package com.example.hackathon_infoed

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hackathon_infoed.ui.theme.Hackathon_InfoedTheme

class Noutati : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val photo_path = intent.getIntExtra("photo_path",0)
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        setContent {
            Hackathon_InfoedTheme {
                if (title != null) {
                    if (description != null) {
                        NewsScreen(onBackClick = { finish() },photo_path,title,description)
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(onBackClick: () -> Unit,photo_path:Int,title:String,description:String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details", modifier = Modifier.offset(x = 20.dp)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text(text ="<")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Photo at the top
            Image(
                painter = painterResource(id = photo_path),
                contentDescription = "News Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {

                    }
            )

            // Title
            Text(
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Creators Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "CuzzApp Team",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(id = R.drawable.iconitza),
                    contentDescription = "Creators Photo",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(start = 8.dp)
                )
            }

            // Scrollable Content
            Text(
                text = description,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun NewsScreenPreview(photo_path:Int,title:String,description:String) {
    Hackathon_InfoedTheme {
        NewsScreen(onBackClick = {},photo_path,title,description)
    }
}