package com.example.myfirstandroidtvapp.presentation.videoplayer

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView


//@Composable
//fun VideoPlayer(videoUrl: String) {
//  val player = remember {
//        ExoPlayer.Builder().build().apply {
//            setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
//            prepare()
//            playWhenReady = true
//        }
//    }
//
//    AndroidView(factory = { context ->
//        PlayerView(context).apply {
//          this.player = player
//       }
//    })
//}