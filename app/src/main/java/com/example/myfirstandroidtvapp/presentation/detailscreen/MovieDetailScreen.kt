package com.example.myfirstandroidtvapp.presentation.detailscreen

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.tv.material3.Icon
import coil.compose.AsyncImage
import com.example.myfirstandroidtvapp.presentation.sections.dashboard.VodViewModel
import timber.log.Timber

@Composable
fun MovieDetailScreen(
    viewModel: VodViewModel,
    navController: NavController
) {
    val video = viewModel.selectedMovie
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    var isPlayBtnFocused by remember { mutableStateOf(false) }
    var isAddToWatchListFocused by remember { mutableStateOf(false) }


    Column (
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                boxSize = coordinates.size // captures width & height in pixels
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Poster / Backdrop Image
            AsyncImage(
                model = video?.thumbnail,
                contentDescription = video?.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
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

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
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
                    Button(
                        onClick = {
                            // Handle button click
                            navController.navigate("video_player")
                        },
                        modifier = Modifier
                            .onFocusChanged { focusState ->
                                isPlayBtnFocused = focusState.isFocused
                            },// Attach the FocusRequester here
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPlayBtnFocused) Color.White else Color.White.copy(alpha = 0.1f),
                            contentColor = if (isPlayBtnFocused) Color.Black else Color.White
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = if (isPlayBtnFocused) Color.Black else Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            androidx.tv.material3.Text(
                                text = "Play",
                                color = if (isPlayBtnFocused) Color.Black else Color.White
                            )
                        }
                    }

                    Button(
                        onClick = {
                            // Handle button click
                            Timber.tag("MovieDetailScreen").d("Play button clicked")
                        },
                        modifier = Modifier // Attach the FocusRequester here
                            .onFocusChanged { focusState ->
                                isAddToWatchListFocused = focusState.isFocused
                            },// Attach the FocusRequester here
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAddToWatchListFocused) Color.White else Color.White.copy(alpha = 0.1f),
                            contentColor = if (isAddToWatchListFocused) Color.Black else Color.White
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = if (isAddToWatchListFocused) Color.Black else Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            androidx.tv.material3.Text(
                                text = "Add to Watchlist",
                                color = if (isAddToWatchListFocused) Color.Black else Color.White
                            )
                        }
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

}

