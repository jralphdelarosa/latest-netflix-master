package com.example.myfirstandroidtvapp.presentation.detailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myfirstandroidtvapp.presentation.reusable.NetflixActionButton
import com.example.myfirstandroidtvapp.presentation.sections.dashboard.VodViewModel

@Composable
fun MovieDetailScreen(
    viewModel: VodViewModel
) {
    val video = viewModel.selectedMovie
    val playButtonFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        // Ensure the Play button gets focus when the screen loads
        playButtonFocusRequester.requestFocus()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusable(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Poster / Backdrop Image
            AsyncImage(
                model = video?.thumbnail,
                contentDescription = video?.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = video?.title ?: "",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${video?.yearRelease} • ${video?.duration} • must watch!",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = video?.description.toString(),
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    NetflixActionButton("Play", Icons.Default.PlayArrow, focusRequester = playButtonFocusRequester)
                    NetflixActionButton("Add to Watchlist", Icons.Default.Add)
                }
            }
        }

//            item {
//                // "More Like This" Row
//                Text(
//                    text = "More Like This",
//                    color = Color.White,
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    modifier = Modifier.padding(horizontal = 24.dp)
//                )
//            }
//
//            item {
//                LazyRow(
//                    contentPadding = PaddingValues(horizontal = 24.dp),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    items(state.similarMovies) { movie ->
//                        NetflixThumbnail(
//                            movieTitle = movie.title,
//                            posterUrl = movie.posterUrl,
//                            onClick = { /* Navigate to this movie's detail */ }
//                        )
//                    }
//                }
//            }
    }

}

