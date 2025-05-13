package com.example.myfirstandroidtvapp.presentation.sections.tv_guide

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myfirstandroidtvapp.R
import com.example.myfirstandroidtvapp.presentation.sections.dashboard.VideoPlayer
import com.example.myfirstandroidtvapp.presentation.shared_viewmodel.ChannelViewModel
import com.example.myfirstandroidtvapp.presentation.shared_viewmodel.ProgramUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Created by John Ralph Dela Rosa on 4/20/2025.
 */

@Composable
fun TvGuideScreen(
    channelViewModel: ChannelViewModel,
    navController: NavController
) {
    val tryUrl = "https://vod-lb-cdn.tvstartupengine.com/tvs-vod/prod/media/af4303c3-3b68-482a-bbc6-7aa87f2e99f4/41ea2c28-418e-4424-aa4f-37ea150b662b/videos/4577dc43-29bc-4179-b3df-b02ea8fd1e51/output/4577dc43-29bc-4179-b3df-b02ea8fd1e51_1080p.mp4"
    val tryThumbnail = "https://vod-lb-cdn.tvstartupengine.com/tvs-asset/prod/af4303c3-3b68-482a-bbc6-7aa87f2e99f4/41ea2c28-418e-4424-aa4f-37ea150b662b/images/thumbnail/a2185107fe5c2645a0a9c54504994c75.jpeg"
    val channels by channelViewModel.uiChannels.collectAsState()

    val density = LocalDensity.current
    val now = remember { LocalTime.now().withMinute(0).withSecond(0).withNano(0) }
    val timeBlocks = remember {
        (0..48).map { index -> // Show 6 hours (12 blocks of 30 mins)
            now.plusMinutes(index * 30L)
        }
    }

    val currentTime = remember { mutableStateOf(LocalTime.now()) }

    val blockWidthPx = with(density) { 100.dp.toPx() }

    val alignedStart = currentTime.value
        .truncatedTo(ChronoUnit.HOURS)
        .plusMinutes((currentTime.value.minute / 30) * 30L)

    val minutesFromStart = Duration.between(alignedStart, currentTime.value).toMinutes()
    val offsetPx = (minutesFromStart / 30f) * blockWidthPx

    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = LocalTime.now()
            delay(1000) // every 1 minute
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val today = remember { LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM d")) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, top = 300.dp)
                .zIndex(3f)
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(30.dp)
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = today,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(start = 115.dp, top = 300.dp ) // same as LazyRow padding
                .fillMaxSize()
                .height(30.dp)
                .zIndex(3f)
        ) {
            // Red Time Indicator
            Box(
                modifier = Modifier
                    .padding(start = offsetPx.dp)
                    .fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(Color.Red)
                )
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .zIndex(3f), // Offset to align with channel thumbnails
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {

                items(timeBlocks.size) { index ->
                    Box(
                        modifier = Modifier
                            .width(100.dp) // Match average program block width
                            .fillMaxHeight()
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = timeBlocks[index].format(DateTimeFormatter.ofPattern("HH:mm")),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopEnd)
                .zIndex(0f)
                .padding(start = 40.dp)
        ) {
            VideoPlayer(url = tryUrl, thumbnail = tryThumbnail)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
        ) {

            LazyColumn(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(3 * (60.dp + 4.dp)) // 100dp row height + 8dp top & bottom padding
                    .padding(start = 8.dp)
            ) {

                items(channels.size) { index ->

                    val reorderedVideos =
                        remember(channels[index].videos, channels[index].position) {
                            val currentPos = channels[index].position
                            val upcoming = channels[index].videos
                                .filter { it.position >= currentPos }
                                .sortedBy { it.position }
                            val finished = channels[index].videos
                                .filter { it.position < currentPos }
                                .sortedBy { it.position }
                            upcoming + finished
                        }

                    Row(modifier = Modifier.padding(vertical = 2.dp)) {


                        // Channel Thumbnail
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(60.dp)
                                .background(Color.White.copy(alpha = 0.8f))
                                .border(1.dp, Color.Red),
                            contentAlignment = Alignment.Center
                        ) {
                            val imagePainter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(channels[index].thumbnail)
                                    .crossfade(true)
                                    .placeholder(R.drawable.logo)
                                    .error(R.drawable.logo)
                                    .build()
                            )

                            Image(
                                painter = imagePainter,
                                contentDescription = channels[index].title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            MusicBarsAnimation(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp))

                        LazyRow(
                            modifier = Modifier
                                .height(60.dp)
                                .focusGroup()
                        ) {

                            items(reorderedVideos.size) { videoIndex ->
                                ProgramItem(program = reorderedVideos[videoIndex])
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgramItem(program: ProgramUiModel) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(program.width)
            .fillMaxHeight()
            .onFocusChanged { isFocused = it.hasFocus }
            .focusable()
            .padding(1.dp), // Needed to show border inside parent layout
        shape = RectangleShape,
        border = BorderStroke(
            width = 1.dp,
            color = if (isFocused) Color.White else Color.Red
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isFocused) Color.Gray.copy(alpha = 0.50f) else Color.Red.copy(alpha = 0.50f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(program.title, color = Color.White, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(5.dp))
            Text(program.duration, color = Color.White, fontSize = 12.sp)
        }
    }
}

@Composable
fun MusicBarsAnimation(
    modifier: Modifier = Modifier,
    barCount: Int = 4,
    barWidth: Dp = 4.dp,
    barMaxHeight: Dp = 20.dp,
    barColor: Color = Color.Red,
    barSpacing: Dp = 2.dp
) {
    val bars = remember { List(barCount) { androidx.compose.animation.core.Animatable(Random.nextFloat()) } }
    val randomDelays = remember { List(barCount) { Random.nextLong(0L, 300L) } }

    LaunchedEffect(Unit) {
        bars.forEachIndexed { index, bar ->
            launch {
                while (true) {
                    bar.animateTo(
                        targetValue = Random.nextFloat().coerceIn(0.2f, 1f),
                        animationSpec = tween(
                            durationMillis = Random.nextInt(200, 500),
                            easing = LinearEasing
                        )
                    )
                    delay(randomDelays[index])
                }
            }
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(barSpacing),
        verticalAlignment = Alignment.Bottom
    ) {
        bars.forEach { bar ->
            Box(
                modifier = Modifier
                    .width(barWidth)
                    .height(barMaxHeight * bar.value)
                    .background(barColor, shape = RoundedCornerShape(2.dp))
            )
        }
    }
}

