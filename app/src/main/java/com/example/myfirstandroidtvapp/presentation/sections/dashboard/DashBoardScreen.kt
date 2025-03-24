package com.example.myfirstandroidtvapp.presentation.sections.dashboard

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myfirstandroidtvapp.data.remote.TmdbApiConstants
import com.example.myfirstandroidtvapp.domain.model.Movie
import com.example.myfirstandroidtvapp.presentation.NavItem
import com.example.myfirstandroidtvapp.presentation.sections.home.FocusUtils

val categorySection = listOf(
    "Popular Movies",
    "Drama Series",
    "Comedy Series",
    "Horror Series"
)

@Composable
fun DashBoardScreen(
    navController: NavController,
//    viewModel: MovieViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
//        viewModel.fetchPopularMovies(TmdbApiConstants.API_KEY)
    }


    LazyColumn {
        items(categorySection) { categorySection ->
//            MovieCategory(navController, viewModel, categorySection)

        }
    }
}

@Composable
fun MovieCategory(
    navController: NavController,
//    viewModel: MovieViewModel,
    category: String
) {
//    val movies by viewModel.movies.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = category,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
//        LazyRow {
//            items(movies.size) { index ->
//                MovieItem(movies[index]) {
//                    navController.navigate("details/${movies[index].id}")
//                }
//            }
//        }
    }
}

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
            .onFocusChanged { focusState -> isFocused = focusState.hasFocus }
            .focusable()
            .clickable { onClick() }
            .border(
                width = if (isFocused) 4.dp else 0.dp,
                color = if (isFocused) Color.White else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = movie.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}