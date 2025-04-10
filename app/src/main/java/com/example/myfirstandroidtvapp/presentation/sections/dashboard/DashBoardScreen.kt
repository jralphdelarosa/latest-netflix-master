package com.example.myfirstandroidtvapp.presentation.sections.dashboard

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myfirstandroidtvapp.R
import com.example.myfirstandroidtvapp.data.remote.dto.VodCategory
import com.example.myfirstandroidtvapp.data.remote.dto.VodVideo
import com.example.myfirstandroidtvapp.presentation.login.CircularLogoWithLoadingRing

@Composable
fun DashBoardScreen(viewModel: VodViewModel, navController: NavController) {

    VideoDashboard(viewModel, navController)
}

@Composable
fun VideoDashboard(viewModel: VodViewModel = hiltViewModel(), navController: NavController) {
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
                .padding(start = 20.dp, top = 40.dp)
                .zIndex(3f)
        ) {
            Column(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp / 2) // Limit width to half the screen
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
                            .height(240.dp) // Restrict height to one row visibility
                            .zIndex(4f)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(categories.results.size) { index ->

                                val category = categories.results[index]
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 20.dp,
                                            end = 16.dp,
                                            bottom = 10.dp
                                        ),
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
                                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                                    ) {
                                        if (category.children.isNotEmpty()) {
                                            item {
                                                VideoThumbnail(
                                                    type = category.type,
                                                    childrenThumbnail = category.thumbnail,
                                                    video = null,
                                                    vodCategory = category,
                                                    viewModel = viewModel,
                                                    onFocusChanged = { video, category ->
                                                        focusedThumbnail =
                                                            category?.thumbnail.toString()
                                                        focusedTitle =
                                                            category?.title.toString()
                                                        focusedDescription =
                                                            category?.longDescription.toString()
                                                    },
                                                    navController = navController
                                                )
                                            }

                                        } else {
                                            items(category.videos ?: emptyList()) { video ->
                                                VideoThumbnail(
                                                    type = category.type,
                                                    video = video,
                                                    vodCategory = null,
                                                    viewModel = viewModel,
                                                    onFocusChanged = { vod, vodCategory ->
                                                        focusedTrailerUrl =
                                                            vod?.trailer?.url
                                                                ?: "" // Update focused trailer
                                                        focusedThumbnail =
                                                            vod?.thumbnail.toString()
                                                        focusedTitle = vod?.title.toString()
                                                        focusedDescription =
                                                            vod?.description.toString()

                                                    },
                                                    navController = navController
                                                )
                                            }
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
        ) ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(4f),
            contentAlignment = Alignment.Center
        ) {
            CircularLogoWithLoadingRing()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
        ) {
            NetflixStyleOverlay()
        }

        // VIDEO PREVIEW with BLACK GRADIENT COVER
        Box(
            modifier = Modifier
                .fillMaxWidth(0.77f)
                .fillMaxHeight(0.737f)
                .align(Alignment.TopEnd)
                .zIndex(1f)
        ) {
            VideoPlayer(url = focusedTrailerUrl, thumbnail = focusedThumbnail)
        }
    }
}


@Composable
fun VideoThumbnail(
    type: String = "",
    childrenThumbnail: String = "",
    video: VodVideo?,
    vodCategory: VodCategory?,
    viewModel: VodViewModel,
    onFocusChanged: (VodVideo?, VodCategory?) -> Unit,
    navController: NavController
) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .size(if (isFocused) 140.dp else 130.dp, if (isFocused) 210.dp else 200.dp)
            .onFocusChanged { focusState ->
                if (focusState.hasFocus) {
                    onFocusChanged(video, vodCategory)
                }
                isFocused = focusState.hasFocus
            }
            .clickable {
                if(video!=null) {
                    viewModel.selectedMovie = video
                    navController.navigate("video_details")
                }
            }
            .zIndex(1f),
        shape = RoundedCornerShape(6.dp)
    ) {
        // Use a Box to stack the image and the Series tag
        Box(modifier = Modifier.fillMaxSize()) {

            // Thumbnail image
            val imagePainter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(childrenThumbnail.ifEmpty { video?.thumbnail })
                    .crossfade(true)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .build()
            )

            Image(
                painter = imagePainter,
                contentDescription = video?.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // SERIES TAG - Only shown if type == "series"
            if (type.lowercase() == "series") {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(
                            Color.Red.copy(alpha = 0.85f),
                            shape = RoundedCornerShape(bottomEnd = 6.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Series",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
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
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Red)
    ) {
        // Show Video when playing
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    useController = false
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White
                )
        )

        // Show Thumbnail while loading
        if (!isVideoPlaying || url.isNullOrEmpty()) {
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
fun NetflixStyleOverlay() {
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                boxSize = coordinates.size // captures width & height in pixels
            }
    ) {
        // Background (e.g. image or video)

        // Horizontal Gradient (left → right)
        if (boxSize.width > 0) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black,
                                Color.Transparent
                            ),
                            startX = boxSize.width * 0.3f,
                            endX = boxSize.width * 0.6f
                        )
                    )
            )
        }

        // Vertical Gradient (top → bottom)
        if (boxSize.height > 0) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = boxSize.height * 0.5f,
                            endY = boxSize.height * 0.6f
                        )
                    )
            )
        }
    }
}


