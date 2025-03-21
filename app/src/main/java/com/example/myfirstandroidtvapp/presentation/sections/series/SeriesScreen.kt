package com.example.myfirstandroidtvapp.presentation.sections.series

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.myfirstandroidtvapp.R
import com.example.myfirstandroidtvapp.presentation.sections.home.HomeViewModel

@Composable
fun SeriesScreen(homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusable()
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .focusable(),
            contentAlignment = Alignment.Center // Ensures the text is centered
        ) {
            Text(
                text = stringResource(R.string.series),
                color = Color.White,
                fontSize = 80.sp
            )
        }
    }
}