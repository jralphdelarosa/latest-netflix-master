package com.example.myfirstandroidtvapp.presentation.sections.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.myfirstandroidtvapp.R
import com.example.myfirstandroidtvapp.TvCoreApplication
import com.example.myfirstandroidtvapp.presentation.NavItem
import com.example.myfirstandroidtvapp.presentation.login.LoginState
import com.example.myfirstandroidtvapp.presentation.login.LoginViewModel
import com.example.myfirstandroidtvapp.presentation.sections.dashboard.DashBoardScreen
import com.example.myfirstandroidtvapp.presentation.sections.dashboard.VodViewModel
import com.example.myfirstandroidtvapp.presentation.sections.home.sidebar.Sidebar
import com.example.myfirstandroidtvapp.presentation.sections.movies.MovieScreen
import com.example.myfirstandroidtvapp.presentation.sections.search.SearchScreen
import com.example.myfirstandroidtvapp.presentation.sections.series.SeriesScreen
import com.example.myfirstandroidtvapp.presentation.sections.settings.SettingsScreen
import com.example.myfirstandroidtvapp.presentation.sections.tv_guide.TvGuideScreen
import com.example.myfirstandroidtvapp.presentation.shared_viewmodel.ChannelViewModel


@Composable
fun HomeScreen(
    vodViewModel: VodViewModel,
    loginViewModel: LoginViewModel,
    channelViewModel: ChannelViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    var lastSelectedItem by remember { mutableStateOf(NavItem.Dashboard) }

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showProceedToLoginDialog by remember { mutableStateOf(false) }
    var backPressTime by remember { mutableLongStateOf(0L) }

    // Observe logout state
    val loginState by loginViewModel.loginState.collectAsState()

    //set registerState to idle
    loginViewModel.registerStateSetIdle()

    val focusRequester = remember { FocusRequester() }

    // Navigate to login if logged out
    LaunchedEffect(loginState) {
        if (TvCoreApplication.isUserLoggedIn.value == true) {
            if (loginState == LoginState.LogoutSuccess) {
                TvCoreApplication.isUserLoggedIn.value = false
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }
    }

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressTime < 2000) {
            if (TvCoreApplication.isUserLoggedIn.value == true) {
                showLogoutDialog = true
            } else {
                showProceedToLoginDialog = true
            }
        } else {
            backPressTime = currentTime
            Toast.makeText(context, "Press back again", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.main_bg),
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Box(
            modifier = Modifier
                .zIndex(1f)
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.DirectionRight && keyEvent.type == KeyEventType.KeyDown) {
                        focusRequester.requestFocus()
                        true
                    } else {
                        false
                    }
                }
        ) {
            Sidebar(
                navController = navController,
                lastSelectedItem = lastSelectedItem,
                onItemSelected = { lastSelectedItem = it }
            )
        }



        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(0f)
                .focusable()
                .focusRequester(focusRequester)
                .padding(start = 40.dp)
        ) {
            when (lastSelectedItem) {
                NavItem.Search -> SearchScreen()
                NavItem.Dashboard -> DashBoardScreen(
                    viewModel = vodViewModel,
                    navController = navController
                )

                NavItem.Movies -> MovieScreen()
                NavItem.Series -> SeriesScreen()
                NavItem.TvGuide -> TvGuideScreen(
                    navController = navController,
                    channelViewModel = channelViewModel
                )

                NavItem.Settings -> SettingsScreen(navController)
            }
        }


        if (showLogoutDialog) {
            LogoutDialog(
                onConfirm = {
                    showLogoutDialog = false
                    loginViewModel.logoutUser()
                },
                onDismiss = { showLogoutDialog = false }
            )
        }

        if (showProceedToLoginDialog) {
            GoToLoginDialog(
                onConfirm = {
                    showProceedToLoginDialog = false
                    navController.navigate("login")
                },
                onDismiss = { showProceedToLoginDialog = false }
            )
        }
    }
}

@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var isCancelFocused by remember { mutableStateOf(false) }
    var isLogoutFocused by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(12.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Logout",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "Are you sure you want to logout?",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCancelFocused) Color.White else Color.Black,
                            contentColor = if (isCancelFocused) Color.Black else Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = if (isCancelFocused) Color.Red else Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .onFocusChanged { focusState ->
                                isCancelFocused = focusState.hasFocus
                            }
                    ) {
                        Text(
                            "Cancel",
                            fontSize = 16.sp,
                            color = if (isLogoutFocused) Color.Black else Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLogoutFocused) Color.White else Color.Black,
                            contentColor = if (isLogoutFocused) Color.Black else Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = if (isLogoutFocused) Color.Red else Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .onFocusChanged { focusState ->
                                isLogoutFocused = focusState.hasFocus
                            }
                    ) {
                        Text("Logout", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun GoToLoginDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var isCancelFocused by remember { mutableStateOf(false) }
    var isLoginNowFocused by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(12.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Proceed with Login",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "Do you want to login now?",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCancelFocused) Color.White else Color.Black,
                            contentColor = if (isCancelFocused) Color.Black else Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = if (isCancelFocused) Color.Red else Color.White,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .onFocusChanged { focusState ->
                                isCancelFocused = focusState.hasFocus
                            }
                    ) {
                        Text("Cancel", fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLoginNowFocused) Color.White else Color.Black,
                            contentColor = if (isLoginNowFocused) Color.Black else Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = if (isLoginNowFocused) Color.Red else Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .onFocusChanged { focusState ->
                                isLoginNowFocused = focusState.hasFocus
                            },
                        shape = ButtonDefaults.shape
                    ) {
                        Text("Login Now", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
