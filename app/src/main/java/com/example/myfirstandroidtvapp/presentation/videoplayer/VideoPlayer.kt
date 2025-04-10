package com.example.myfirstandroidtvapp.presentation.videoplayer

import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.myfirstandroidtvapp.presentation.login.CircularLogoWithLoadingRing
import com.example.myfirstandroidtvapp.presentation.sections.dashboard.VodViewModel
import timber.log.Timber


@Composable
fun VideoPlayer(viewModel: VodViewModel) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val video = viewModel.selectedMovie
    var isBuffering by remember { mutableStateOf(false) }

    // Initialize ExoPlayer
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(video?.url ?: "")
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_IDLE -> Timber.tag("PlaybackState").d("STATE_IDLE")
                        Player.STATE_BUFFERING -> {
                            isBuffering = true
                            Timber.tag("PlaybackState").d("STATE_BUFFERING")
                        }
                        Player.STATE_READY -> {
                            isBuffering = false
                        }
                        Player.STATE_ENDED -> Timber.tag("PlaybackState").d("STATE_ENDED")
                    }
                }
            })
        }
    }

    // Initialize PlayerView
    val playerView = remember {
        PlayerView(context).apply {
            player = exoPlayer
            isFocusable = true
        }
    }

    // Handle key events
    DisposableEffect(context) {
        val keyEventListener = View.OnKeyListener { v, keyCode, event ->
            when (keyCode) {
                KEYCODE_DPAD_UP -> {
                    // Handle D-Pad Up
                    true
                }
                KEYCODE_DPAD_DOWN -> {
                    // Handle D-Pad Down
                    true
                }
                KEYCODE_DPAD_LEFT -> {
                    // Handle D-Pad Left
                    true
                }
                KEYCODE_DPAD_RIGHT -> {
                    // Handle D-Pad Right
                    true
                }
                KEYCODE_DPAD_CENTER -> {
                    // Handle D-Pad Center (OK)
                    true
                }
                else -> false
            }
        }
        playerView.setOnKeyListener(keyEventListener)
        onDispose {
            playerView.setOnKeyListener(null) // Clean up the listener
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.playWhenReady = false
                }
                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.playWhenReady = true
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isBuffering) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .zIndex(2f), // Keeps background consistent
                contentAlignment = Alignment.Center // Centers the loader
            ) {
                CircularLogoWithLoadingRing()
            }
        }
        AndroidView(
            factory = { playerView },
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
        )

    }
}