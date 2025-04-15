package com.example.myfirstandroidtvapp.presentation.videoplayer

import android.view.KeyEvent
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.myfirstandroidtvapp.presentation.login.CircularLogoWithLoadingRing
import com.example.myfirstandroidtvapp.presentation.sections.dashboard.VodViewModel
import kotlinx.coroutines.delay
import timber.log.Timber


@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(viewModel: VodViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val video = viewModel.selectedMovie

    var controlsVisible by remember { mutableStateOf(true) }
    var isBuffering by remember { mutableStateOf(false) }

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            video?.url?.let { url ->
                setMediaItem(MediaItem.fromUri(url))
                prepare()
                playWhenReady = true
            }

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    isBuffering = state == Player.STATE_BUFFERING
                }
            })
        }
    }

    var currentPosition by remember { mutableStateOf(0L) }
    var totalDuration by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            currentPosition = exoPlayer.currentPosition
            totalDuration = exoPlayer.duration.coerceAtLeast(0L)
            delay(500L)
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.playWhenReady = false
                Lifecycle.Event.ON_RESUME -> exoPlayer.playWhenReady = true
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .focusable()
        .onKeyEvent {
            if (it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN &&
                it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_CENTER
            ) {
                controlsVisible = !controlsVisible
                true
            } else false
        }
    ) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    useController = false // turn off default controller
                    player = exoPlayer
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (controlsVisible) {
            VideoControllerOverlay(
                isPlaying = exoPlayer.isPlaying,
                onTogglePlay = {
                    exoPlayer.playWhenReady = !exoPlayer.isPlaying
                },
                onSeekForward = {
                    exoPlayer.seekTo(exoPlayer.currentPosition + 10_000)
                },
                onSeekBackward = {
                    exoPlayer.seekTo((exoPlayer.currentPosition - 10_000).coerceAtLeast(0))
                },
                currentPosition = currentPosition,
                totalDuration = totalDuration,
                showControls = controlsVisible,
                onHideControls = { controlsVisible = false },
                onShowControls = { controlsVisible = true }
            )
        }

        if (isBuffering) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black), // Keeps background consistent
                contentAlignment = Alignment.Center // Centers the loader
            ) {
                CircularLogoWithLoadingRing()
            }
        }
    }
}

@Composable
fun VideoControllerOverlay(
    isPlaying: Boolean,
    currentPosition: Long,
    totalDuration: Long,
    onTogglePlay: () -> Unit,
    onSeekForward: () -> Unit,
    onSeekBackward: () -> Unit,
    showControls: Boolean,
    onHideControls: () -> Unit,
    onShowControls: () -> Unit,
) {
    var visible by remember { mutableStateOf(showControls) }
    val fadeInOut by rememberUpdatedState(newValue = visible)

    // Auto-hide after 3 seconds
    LaunchedEffect(visible, isPlaying) {
        if (visible && isPlaying) {
            delay(3000)
            visible = false
            onHideControls()
        }
    }

    AnimatedVisibility(
        visible = fadeInOut,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        onSeekBackward()
                        visible = true
                        onShowControls()
                    }) {
                        Icon(Icons.Default.FastRewind, contentDescription = "Rewind", tint = Color.White)
                    }
                    IconButton(onClick = {
                        onTogglePlay()
                        visible = true
                        onShowControls()
                    }) {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        onSeekForward()
                        visible = true
                        onShowControls()
                    }) {
                        Icon(Icons.Default.FastForward, contentDescription = "Forward", tint = Color.White)
                    }
                }

                // Your custom progress bar
                VideoProgressBar(
                    progress = if (totalDuration > 0) currentPosition / totalDuration.toFloat() else 0f,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 12.dp)
                )
            }
        }
    }
}

@Composable
fun VideoProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val clampedProgress = progress.coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .height(12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val barHeight = size.height
            val barWidth = size.width

            // Background (gray track)
            drawRoundRect(
                color = Color.DarkGray,
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barHeight / 2, barHeight / 2)
            )

            // Progress (red)
            drawRoundRect(
                color = Color.Red,
                size = Size(barWidth * clampedProgress, barHeight),
                cornerRadius = CornerRadius(barHeight / 2, barHeight / 2)
            )

            // Scrubber circle
            val knobRadius = barHeight * 1.2f
            val knobX = barWidth * clampedProgress

            drawCircle(
                color = Color.White,
                radius = knobRadius / 2f,
                center = Offset(knobX, barHeight / 2)
            )
        }
    }
}

fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
