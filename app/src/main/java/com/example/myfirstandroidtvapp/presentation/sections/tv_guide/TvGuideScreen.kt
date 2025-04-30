package com.example.myfirstandroidtvapp.presentation.sections.tv_guide

import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myfirstandroidtvapp.R
import com.example.myfirstandroidtvapp.data.remote.dto.PlaylistResponse
import com.example.myfirstandroidtvapp.data.remote.dto.PlaylistVideoResponse
import com.example.myfirstandroidtvapp.presentation.shared_viewmodel.ChannelViewModel
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Created by John Ralph Dela Rosa on 4/20/2025.
 */

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TvGuideScreen(
    channelViewModel: ChannelViewModel,
    navController: NavController
) {
    val channelsState by channelViewModel.channels.collectAsState()
    val channelList = channelsState?.results ?: emptyList()

    val thirtyMinuteWidth = 300.dp

    Box(modifier = Modifier.fillMaxSize()) {

        // Overlay your actual UI
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .align(Alignment.BottomStart)
            ) {
                items(channelList.size) { index ->
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        // Channel Name
                        Box(
                            modifier = Modifier
                                .width(180.dp)
                                .height(100.dp)
                                .background(Color.White.copy(alpha = 0.8f))
                                .border(1.dp, Color.Black),
                            contentAlignment = Alignment.Center
                        ) {

                            val imagePainter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(channelList[index].thumbnail)
                                    .crossfade(true)
                                    .placeholder(R.drawable.logo)
                                    .error(R.drawable.logo)
                                    .build()
                            )

                            Image(
                                painter = imagePainter,
                                contentDescription = channelList[index].title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        // Program Row
                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                                .height(100.dp)
                        ) {

                            channelList[index].playlists?.forEach { playlist ->

                                playlist.videosOrder?.forEach{ video ->
                                    val durationMinutes = parseDurationToMinutes(video.video?.duration)
                                    val width = (durationMinutes / 30f) * thirtyMinuteWidth

                                    ProgramItem(playlistVideoResponse = video.video!!, width = width)
                                }


                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgramItem(playlistVideoResponse: PlaylistVideoResponse, width: Dp) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .background(
                Color.Red.copy(alpha = 0.20f)
            )
            .border(
                width = 1.dp,
                color = if (isFocused) Color.Red else Color.White
            )
            .focusable()
            .onFocusChanged { focusState ->
                isFocused = focusState.hasFocus
            }
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            BasicText(
                text = playlistVideoResponse.title.toString(),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            BasicText(
                text = playlistVideoResponse.duration.toString(),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            )
        }

    }
}

fun parseDurationToMinutes(duration: String?): Float {
    if (duration == null) return 0f

    return try {
        val parts = duration.split(":")
        if (parts.size != 3) return 0f

        val hours = parts[0].toFloatOrNull() ?: 0f
        val minutes = parts[1].toFloatOrNull() ?: 0f
        val seconds = parts[2].toFloatOrNull() ?: 0f

        hours * 60f + minutes + seconds / 60f
    } catch (e: Exception) {
        0f
    }
}
