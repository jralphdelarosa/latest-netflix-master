package com.example.myfirstandroidtvapp.presentation.sections.tv_guide

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.myfirstandroidtvapp.R
import com.example.myfirstandroidtvapp.presentation.shared_viewmodel.ChannelViewModel
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Created by John Ralph Dela Rosa on 4/20/2025.
 */
// Dummy models
data class Program(val title: String, val startTime: String, val endTime: String)
data class Channel(val name: String, val programs: List<Program>)

// Dummy data
val channels = listOf(
    Channel(
        "SHO", listOf(
            Program("Silicon Valley", "18:00", "18:30"),
            Program("Silicon Valley", "18:30", "19:00"),
            Program("Silicon Valley", "19:00", "19:30"),
            Program("Silicon Valley", "19:30", "20:30"),
            Program("Silicon Valley", "20:30", "21:30"),
            Program("Silicon Valley", "21:30", "23:30"),
            Program("Silicon Valley", "23:30", "24:00")
        )
    ), Channel(
        "HBO", listOf(
            Program("Game of Thrones", "18:00", "19:00"),
            Program("Silicon Valley", "19:00", "19:30")
        )
    ), Channel(
        "NGC", listOf(
            Program("Captain America", "17:00", "18:30"),
            Program("Running Wild", "18:30", "19:00"),
            Program("Blood Creek", "19:00", "20:00")
        )
    )
)

val timeSlots = listOf("17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00")
val thirtyMinuteWidth = 120.dp

@Composable
fun TvGuideScreen(
    channelViewModel: ChannelViewModel,
    navController: NavController
) {
    val background = painterResource(id = R.drawable.main_bg)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = background,
            contentDescription = "TV Guide Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay your actual UI
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Time row
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 88.dp, top = 8.dp, bottom = 8.dp) // Offset start to match channel names
//                    .horizontalScroll(rememberScrollState()),
//                horizontalArrangement = Arrangement.Start
//            ) {
//                timeSlots.forEach { time ->
//                    Box(
//                        modifier = Modifier
//                            .width(thirtyMinuteWidth)
//                            .height(40.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        BasicText(
//                            text = time,
//                            style = MaterialTheme.typography.bodyLarge.copy(
//                                color = Color.White,
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 16.sp
//                            )
//                        )
//                    }
//                }
//            }
            val focusRequester = remember { FocusRequester() }
            var focusedIndex by remember { mutableStateOf(-1) }

            LazyColumn(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                items(channels.size) { index ->
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        // Channel Name
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(80.dp)
                                .background(Color.White.copy(alpha = 0.8f))
                                .border(1.dp, Color.Black)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BasicText(
                                text = channels[index].name,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.Black, fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        // Program Row
                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                                .height(80.dp)
                        ) {
                            channels[index].programs.forEach { program ->

                                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                                val start = LocalTime.parse(program.startTime, formatter)
                                val end = LocalTime.parse(program.endTime, formatter)
                                val durationMinutes = Duration.between(start, end).toMinutes()

                                val width = (durationMinutes / 30f) * thirtyMinuteWidth

                                ProgramItem(program = program, width = width)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgramItem(program: Program, width: Dp) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .background(
                Color.Red.copy(alpha = 0.20f)
            )
            .border(
                width = 2.dp,
                color = if (isFocused) Color.Red else Color.White
            )
            .focusable()
            .onFocusChanged { focusState ->
                isFocused = focusState.hasFocus
            }
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicText(
            text = program.title,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
