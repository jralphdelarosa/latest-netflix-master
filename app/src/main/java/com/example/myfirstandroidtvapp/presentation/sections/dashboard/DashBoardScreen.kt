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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
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
    var focusedTrailerUrl by remember { mutableStateOf("") } // Store focused trailer URL
    var focusedThumbnail by remember { mutableStateOf("") }
    var focusedTitle by remember { mutableStateOf("") }
    var focusedDescription by remember { mutableStateOf("") }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, start = 10.dp)
                .zIndex(2f)
        ) {
            Column(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp / 2) // Limit width to half the screen
                    .padding(16.dp) // Padding for better spacing
            ) {
                // Video Title
                Text(
                    text = focusedTitle,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )

                // Video Description
                Text(
                    text = focusedDescription,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        vodCategories?.fold(
            onSuccess = { categories ->
                if (categories.results.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter) // Push to the bottom
                            .height(300.dp) // Restrict height to one row visibility
                            .padding(bottom = 16.dp)
                            .zIndex(1f)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(categories.results.size) { index ->

                                val category = categories.results[index]
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 10.dp, end = 16.dp, bottom = 16.dp, top = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // Category Title
                                    Text(
                                        text = category.title,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 10.dp)
                                    )

                                    // Video Thumbnails in Row
                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(category.videos ?: emptyList()) { video ->
                                            VideoThumbnail(
                                                video = video,
                                                viewModel = viewModel,
                                                onFocusChanged = { vod ->
                                                    focusedTrailerUrl =
                                                        vod?.trailer?.url
                                                            ?: "" // Update focused trailer
                                                    focusedThumbnail = vod?.thumbnail.toString()
                                                    focusedTitle = vod?.title.toString()
                                                    focusedDescription = vod?.description.toString()

                                                }
                                            )
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

        // VIDEO PREVIEW with BLACK GRADIENT COVER
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.4f)
                .align(Alignment.TopEnd)
                .zIndex(4f)
        ) {
            VideoPlayer(url = focusedTrailerUrl, thumbnail = focusedThumbnail)

            // Black fading overlay IN FRONT of video

        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Transparent
                        )
                    )
                )
                .zIndex(3f)
        )
    }
}


@Composable
fun VideoThumbnail(video: VodVideo?, viewModel: VodViewModel, onFocusChanged: (VodVideo?) -> Unit) {
    var isFocused by remember { mutableStateOf(false) }
    Timber.tag("video").d(video?.thumbnail)

    Card(
        modifier = Modifier
            .size(if(isFocused) 160.dp else 145.dp, if(isFocused) 260.dp else 235.dp)
            .onFocusChanged { focusState ->
                if (focusState.hasFocus) {
                    onFocusChanged(video) // Send trailer URL to parent
                }
                isFocused = focusState.hasFocus // Update focus state
            }
            .clickable {
                // Handle click here
            }
            .zIndex(1f),
        shape = RoundedCornerShape(12.dp)
    ) {

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
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun VideoPlayer(url: String?, thumbnail: String?) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE // Loop video
            volume = 0f // Mute video
            playWhenReady = true // Auto-play
        }
    }

    var isVideoPlaying by remember { mutableStateOf(false) } // Track video playback state

    if (!url.isNullOrEmpty()) {
        LaunchedEffect(url) {
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }

        DisposableEffect(Unit) {
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    isVideoPlaying =
                        state == Player.STATE_READY // Hide thumbnail when video is ready
                }
            })

            onDispose {
                exoPlayer.release()
            }
        }
    } else {
        // Show Logo if URL is Empty
        Image(
            painter = rememberAsyncImagePainter(
                model = thumbnail ?: R.drawable.logo,
                error = painterResource(R.drawable.logo)
            ), // Replace with your logo resource
            contentDescription = "App Logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit // Fill the screen with the logo
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Show Video when playing
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Show Thumbnail while loading
        if (!isVideoPlaying) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = thumbnail ?: R.drawable.logo, // Fallback to logo
                    error = painterResource(R.drawable.logo)
                ),
                contentDescription = "Thumbnail",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
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
