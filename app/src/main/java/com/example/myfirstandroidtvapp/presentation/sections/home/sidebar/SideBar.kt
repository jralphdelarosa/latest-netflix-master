package com.example.myfirstandroidtvapp.presentation.sections.home.sidebar

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.myfirstandroidtvapp.R
import com.example.myfirstandroidtvapp.presentation.NavItem

@Composable
fun Sidebar(
    navController: NavController,
    lastSelectedItem: NavItem,
    onItemSelected: (NavItem) -> Unit
) {
    var isSidebarExpanded by remember { mutableStateOf(true) }

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    var boxSize by remember { mutableStateOf(IntSize.Zero) }


    // Smoothly animate the sidebar width
    val sidebarWidth by animateDpAsState(
        targetValue = if (isSidebarExpanded) screenWidthDp else 40.dp,
        animationSpec = tween(durationMillis = 200, easing = LinearOutSlowInEasing),
        label = "Sidebar Width Animation"
    )

    val menuItems = remember {
        listOf(
            NavItem.Search to Icons.Filled.Search,
            NavItem.Dashboard to Icons.Filled.Home,
            NavItem.Movies to Icons.Filled.Movie,
            NavItem.Series to Icons.Filled.Tv,
            NavItem.TvGuide to Icons.AutoMirrored.Filled.ViewList,
            NavItem.Settings to Icons.Filled.Settings
        )
    }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(sidebarWidth)
            .onFocusChanged { focusState ->
                isSidebarExpanded = focusState.hasFocus
            }
            .onGloballyPositioned { coordinates ->
                boxSize = coordinates.size
            }
    ) {
        // Background gradient overlay — behind everything else
        if (isSidebarExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.9f),
                                Color.Transparent
                            ),
                            startX = boxSize.width * 0.3f,
                            endX = boxSize.width * 0.6f
                        )
                    )
                    .zIndex(0f)
            )
        }

        // Main content over the gradient
        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(top = 40.dp, start = 5.dp)
                    .align(Alignment.Start) // Aligns the logo to the start (left) of the column
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight()
            ) {
                menuItems.forEach { (item, icon) ->
                    key(item) {
                        SidebarItem(
                            item = item,
                            icon = icon,
                            isExpanded = isSidebarExpanded,
                            onFocus = {},
                            onClick = { onItemSelected(item) },
                            lastSelectedItem = lastSelectedItem,
                            context = LocalContext.current
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SidebarItem(
    item: NavItem,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isExpanded: Boolean,
    onFocus: () -> Unit,
    onClick: () -> Unit,
    lastSelectedItem: NavItem,
    context: Context
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    var textWidth by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    LaunchedEffect(isExpanded) {
        if(isExpanded) {
            if (item == lastSelectedItem) {
                focusRequester.requestFocus()
            }
        }
    }

    val textSize by animateFloatAsState(
        targetValue = if (isFocused) 18f else 16f, // 🔹 Animate size change
        animationSpec = tween(durationMillis = 200),
        label = "Text Size Animation"
    )

    val textIconColor by animateColorAsState(
        targetValue = if (isFocused) Color.White else Color.Gray, // 🔹 Animate color change
        animationSpec = tween(durationMillis = 200),
        label = "Text Color Animation"
    )

    val iconDp by animateDpAsState(
        targetValue = if (isFocused) 36.dp else 32.dp, // 🔹 Animate color change
        animationSpec = tween(durationMillis = 200),
        label = "Text Color Animation"
    )

    Row(modifier = Modifier
        .fillMaxWidth()
        .focusRequester(focusRequester)
        .onFocusChanged { focusState ->
            isFocused = focusState.hasFocus
            if (focusState.hasFocus) { // Save last focused item
                onFocus()
            }
        }
        .focusable()
        .height(50.dp)
        .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = if (isExpanded) 50.dp else 5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = item.title,
                    tint = textIconColor,
                    modifier = Modifier
                        .size(iconDp)
                        .padding(
                            start = if (isExpanded) 0.dp else 7.dp,
                            end = if (isExpanded) 12.dp else 7.dp
                        )
                )

                AnimatedVisibility(visible = isExpanded) {
                    Column {
                        Text(
                            text = item.title,
                            fontSize = textSize.sp,
                            color = textIconColor,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .onGloballyPositioned { coordinates ->
                                    textWidth = with(localDensity) {
                                        coordinates.size.width.toDp()
                                    }
                                }
                        )

                        val underlineWidth by animateDpAsState(
                            targetValue = if (isFocused) textWidth else 0.dp,
                            animationSpec = tween(durationMillis = 200),
                            label = "Underline Width"
                        )

                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp, start = 8.dp)
                                .height(2.dp)
                                .width(underlineWidth)
                                .background(Color.Red)
                        )
                    }

                }


            }

        }
    }
}