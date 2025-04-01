package com.example.myfirstandroidtvapp.presentation.sections.dashboard

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.myfirstandroidtvapp.R
import com.example.myfirstandroidtvapp.data.remote.dto.VodVideo
import timber.log.Timber

@Composable
fun DashBoardScreen(viewModel: VodViewModel = hiltViewModel(), navController: NavController) {
    LaunchedEffect(Unit) {
        viewModel.fetchVodCategories()
    }
    VideoDashboard(viewModel)
}

@Composable
fun VideoDashboard(viewModel: VodViewModel = hiltViewModel()) {
    val vodCategories by viewModel.vodCategories.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        vodCategories?.fold(
            onSuccess = { categories ->
                if (categories.results.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter) // Push to the bottom
                            .height(200.dp) // Restrict height to one row visibility
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(categories.results.size) { index ->

                                val category = categories.results[index]
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // Category Title
                                    Text(
                                        text = category.title,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    )

                                    // Video Thumbnails in Row
                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(category.videos ?: emptyList()) { video ->
                                            VideoThumbnail(video, viewModel)
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "No categories available",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            onFailure = { error ->
                Text(
                    text = "Error: ${error.message}",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        ) ?: CircularLogoWithLoadingRing()
    }
}



@Composable
fun VideoThumbnail(video: VodVideo?, viewModel: VodViewModel) {
    var isFocused by remember { mutableStateOf(false) }
    Timber.tag("video").d(video?.thumbnail)

    Box(
        modifier = Modifier
            .size(200.dp, 100.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.hasFocus // Update focus state
            }
            .border(
                width = 2.dp,  // Increase the border width for better visibility
                color = if (isFocused) Color.Red else Color.Transparent // Red border when focused, transparent otherwise
            )
            .background(Color.Black) // Use a background to contrast with the border
            .clickable {
                // Handle click here
            }
            .zIndex(1f) // Ensure it's on top
    ) {
        // Check if video.trailer is null and fallback to a logo

        /*if (isFocused && video?.trailer != null) {
            // If focused and trailer URL is available, show VideoPlayer
            val trailer = video.trailer.url
            if (trailer.isNotEmpty()) {
                VideoPlayer(video.trailer.url)
            }
        }*/

        // Use rememberAsyncImagePainter to load the thumbnail image
        val imagePainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current) // Create the ImageRequest
                .data(video?.thumbnail) // Assign the URL for the image
                .crossfade(true) // Optional: Enable crossfade transition when loading the image
                .placeholder(R.drawable.logo) // Optional: Show a placeholder while loading
                .error(R.drawable.logo) // Optional: Show an error image if loading fails
                .build()
        )

        // Check if image is loaded successfully or not
        Image(
            painter = imagePainter,
            contentDescription = video?.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}


@Composable
fun VideoPlayer(url: String) {
    AndroidView(factory = { context ->
        PlayerView(context).apply {
            player = ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(url))
                prepare()
                play()
            }
        }
    })
}

@Composable
fun CircularLogoWithLoadingRing() {
    val infiniteTransition = rememberInfiniteTransition()

    // Animate the rotation of the outer ring
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(100.dp)
    ) {
        // Circular Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Loading",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape) // Makes the image circular
        )

        // Loading Ring
        Canvas(
            modifier = Modifier
                .size(80.dp) // Slightly larger than the logo
                .rotate(rotation) // Rotates the progress ring
        ) {
            drawArc(
                color = Color.Red, // Netflix theme color
                startAngle = 0f,
                sweepAngle = 270f, // Creates an incomplete circle effect
                useCenter = false,
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}
