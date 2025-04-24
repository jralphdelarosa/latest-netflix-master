package com.example.myfirstandroidtvapp.presentation.sections.settings

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfirstandroidtvapp.R
import kotlinx.coroutines.delay

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var isAutoplayEnabled by remember { mutableStateOf(true) }
    val autoplayFocusRequester = remember { FocusRequester() }
    val requestFocusNow = remember { mutableStateOf(false) }

    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(requestFocusNow.value) {
        if (requestFocusNow.value) {
            delay(300) // Let Compose finish layout
            autoplayFocusRequester.requestFocus()
            requestFocusNow.value = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                boxSize = coordinates.size
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Transparent
                        ),
                        startX = boxSize.width * 0.45f,
                        endX = boxSize.width * 0.55f
                    )
                )
        )
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .onFocusChanged { focusState ->
                if (focusState.hasFocus) {
                    requestFocusNow.value = true
                }
            }
    ) {
        // LEFT LOGO
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(180.dp)
            )
        }

        // RIGHT SETTINGS ITEMS
        Box(
            Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {

            Column(
                modifier = Modifier
                    .padding(start = 10.dp, top = 20.dp, end = 10.dp)
                    .fillMaxSize()
                    .focusGroup()
            ) {
                // AUTOPLAY TOGGLE - First Item
                SettingToggleItem(
                    title = "Autoplay",
                    isChecked = isAutoplayEnabled,
                    onToggle = { isAutoplayEnabled = it },
                    modifier = Modifier
                        .focusRequester(autoplayFocusRequester)
                )

                Spacer(Modifier.height(20.dp))
                SettingsNavItem("FAQ") { navController.navigate("faq_screen") }
                Spacer(Modifier.height(20.dp))
                SettingsNavItem("Contact Us") { navController.navigate("contact_screen") }
                Spacer(Modifier.height(20.dp))
                SettingsNavItem("About Us") { navController.navigate("about_screen") }
                Spacer(Modifier.height(20.dp))
                SettingsNavItem("Privacy Policy") { navController.navigate("policy_screen") }
                Spacer(Modifier.height(20.dp))
                SettingsNavItem("Clear Cache") { viewModel.clearAppCache() }
                Spacer(Modifier.height(20.dp))
                SettingsNavItem("Subscriptions") { navController.navigate("subscriptions_screen") }
            }
        }

    }

}

@Composable
fun SettingToggleItem(
    title: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(if (isFocused) 1.01f else 1f)
    val backgroundColor =
        if (isFocused) Color.White else Color.Gray.copy(alpha = 0.8f)
    val textColor = if (isFocused) Color.Black else Color.White

    Row(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .onFocusChanged { isFocused = it.hasFocus }
            .clickable { onToggle(!isChecked) }
            .background(backgroundColor, shape = RoundedCornerShape(14.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = if (isChecked) "On" else "Off",
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun SettingsNavItem(
    title: String,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(if (isFocused) 1.05f else 1f)
    val backgroundColor =
        if (isFocused) Color.White else Color.Gray.copy(alpha = 0.8f)
    val textColor = if (isFocused) Color.Black else Color.White

    Row(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .onFocusChanged { isFocused = it.hasFocus }
            .clickable(onClick = onClick)
            .background(backgroundColor, shape = RoundedCornerShape(14.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}