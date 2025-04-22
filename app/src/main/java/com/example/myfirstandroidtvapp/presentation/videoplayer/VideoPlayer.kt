package com.example.myfirstandroidtvapp.presentation.videoplayer

import android.view.KeyEvent
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Forward10
import androidx.compose.material.icons.outlined.Replay10
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusable()
            .onKeyEvent {
                if (it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    when (it.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER,
                        KeyEvent.KEYCODE_DPAD_UP,
                        KeyEvent.KEYCODE_DPAD_DOWN,
                        KeyEvent.KEYCODE_DPAD_LEFT,
                        KeyEvent.KEYCODE_DPAD_RIGHT -> {
                            controlsVisible = true
                            true
                        }

                        else -> false
                    }
                } else false
            }
    ) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    useController = false
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
                onHideControls = { controlsVisible = false }
            )
        }

        if (isBuffering) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
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
    onHideControls: () -> Unit,
) {
    var visible by remember { mutableStateOf(true) }

    val focusRequesterRewind = remember { FocusRequester() }
    val focusRequesterPlay = remember { FocusRequester() }
    val focusRequesterForward = remember { FocusRequester() }

    var isFocusedRewind by remember { mutableStateOf(false) }
    var isFocusedPlay by remember { mutableStateOf(false) }
    var isFocusedForward by remember { mutableStateOf(false) }

    LaunchedEffect(visible, isPlaying) {
        if (visible && isPlaying) {
            delay(3000)
            visible = false
            onHideControls()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            // Central Playback Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.Center)
            ) {
                IconButton(
                    onClick = onSeekBackward,
                    modifier = Modifier
                        .focusable()
                        .focusRequester(focusRequesterRewind)
                        .onFocusChanged { isFocusedRewind = it.isFocused }
                        .netflixFocus(isFocusedRewind)
                        .onKeyEvent {
                            if (it.type == KeyEventType.KeyDown && it.key == Key.DirectionRight) {
                                focusRequesterPlay.requestFocus()
                                true
                            } else false
                        }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Replay10,
                        contentDescription = "Rewind 10s",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                IconButton(
                    onClick = onTogglePlay,
                    modifier = Modifier
                        .focusRequester(focusRequesterPlay)
                        .onFocusChanged { isFocusedPlay = it.isFocused }
                        .netflixFocus(isFocusedPlay)
                        .onKeyEvent {
                            if (it.type == KeyEventType.KeyDown) {
                                when (it.key) {
                                    Key.DirectionLeft -> {
                                        focusRequesterRewind.requestFocus()
                                        true
                                    }

                                    Key.DirectionRight -> {
                                        focusRequesterForward.requestFocus()
                                        true
                                    }

                                    else -> false
                                }
                            } else false
                        }
                        .focusable()
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }

                IconButton(
                    onClick = onSeekForward,
                    modifier = Modifier
                        .focusable()
                        .focusRequester(focusRequesterForward)
                        .onFocusChanged { isFocusedForward = it.isFocused }
                        .netflixFocus(isFocusedForward)
                        .onKeyEvent {
                            if (it.type == KeyEventType.KeyDown && it.key == Key.DirectionLeft) {
                                focusRequesterPlay.requestFocus()
                                true
                            } else false
                        }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Forward10,
                        contentDescription = "Forward 10s",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // Progress bar and time indicators at bottom
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                VideoProgressBar(
                    progress = if (totalDuration > 0) currentPosition / totalDuration.toFloat() else 0f,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(currentPosition),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    Text(
                        text = formatTime(totalDuration),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            // Focus play button after visibility
            LaunchedEffect(Unit) {
                delay(100)
                focusRequesterPlay.requestFocus()
            }
        }
    }
}

fun Modifier.netflixFocus(isFocused: Boolean): Modifier = this
    .graphicsLayer {
        scaleX = if (isFocused) 1.05f else 1f
        scaleY = if (isFocused) 1.05f else 1f
    }
    .drawBehind {
        if (isFocused) {
            val radius = size.minDimension / 1.8f
            drawCircle(
                color = Color.White.copy(alpha = 0.10f),
                radius = radius,
                center = center
            )
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
