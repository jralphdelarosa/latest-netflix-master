package com.example.myfirstandroidtvapp.presentation.detailscreen

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.myfirstandroidtvapp.R
import com.example.myfirstandroidtvapp.presentation.sections.dashboard.MovieViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DetailScreen(
    movieId: Int,
    viewModel: MovieViewModel = hiltViewModel()
) {
    val trailerKey by viewModel.movieTrailer.collectAsState()
    val rawResourceId = R.raw.sample_video

    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri =  Uri.parse("android.resource://${context.packageName}/$rawResourceId")
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                useController = true // Show playback controls
                this.player = player
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

