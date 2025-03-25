package com.example.myfirstandroidtvapp.presentation.login

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfirstandroidtvapp.R
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by John Ralph Dela Rosa on 3/23/2025.
 */

//sliding animation
@Composable
fun LoginRegistrationScreen(navController: NavController) {
    var isSignup by remember { mutableStateOf(false) }
    val viewModel: LoginViewModel = hiltViewModel()
    val context = LocalContext.current
    val offsetX by animateDpAsState(
        targetValue = if (isSignup) (-300).dp else 300.dp,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "OffsetX Animation"
    )
    val alpha by animateFloatAsState(
        targetValue = if (isSignup) 1f else 1f,
        animationSpec = tween(durationMillis = 500), label = "Alpha Animation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .onKeyEvent {
                if (it.type == KeyEventType.KeyDown) {
                    when (it.key) {
                        Key.DirectionLeft -> {
                            isSignup = false
                            true
                        }

                        Key.DirectionRight -> {
                            isSignup = true
                            true
                        }

                        else -> false
                    }
                } else false
            }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(end = 20.dp)
                    .align(Alignment.TopEnd)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(150.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                LoginToggleSwitch(selectedOption = isSignup, onOptionSelected = { isSignup = it })
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            this@Column.AnimatedVisibility(
                visible = !isSignup,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            ) {
                LoginScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            this@Column.AnimatedVisibility(
                visible = isSignup,
                enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            ) {
                RegistrationScreen(navController)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text("Watch as Guest", color = Color.White)
            }
        }
    }
}


@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                isLoading = false
                navController.navigate("home")
            }

            is LoginState.Error -> {
                isLoading = false
                val error = loginState as LoginState.Error
                errorMessage = when (error.errorType) {
                    ErrorType.EMPTY_FIELDS -> "Please fill in all fields."
                    ErrorType.INVALID_CREDENTIALS -> "Incorrect email or password. Try again."
                    ErrorType.NETWORK_ERROR -> "Network error. Check your internet connection."
                    ErrorType.UNKNOWN_ERROR -> "Something went wrong. Please try again."
                }
            }

            else -> {}
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black), // Keeps background consistent
            contentAlignment = Alignment.Center // Centers the loader
        ) {
            CircularLogoWithLoadingRing()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Login Form
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        NetflixTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "Enter your email"
                        )

                        NetflixTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = "Enter your password"
                        )

                        // Show error message below text fields
                        errorMessage?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = it,
                                color = Color(0xFFFF3D00),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = {
                                isLoading = true
                                coroutineScope.launch {
                                    viewModel.login(email, password)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text("Sign In", color = Color.White)
                        }
                    }
                }

                // Vertical Divider with OR
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    VerticalDivider(
                        modifier = Modifier
                            .height(100.dp)
                            .width(1.dp),
                        color = Color.White
                    )
                    Text(
                        "OR",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(100.dp)
                            .width(1.dp),
                        color = Color.White
                    )
                }

                // QR Code Login
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .height(220.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sample_qr),
                            contentDescription = "QR Code",
                            modifier = Modifier.size(150.dp)
                        )
                        Text(
                            "Scan the QR Code with your smartphone's camera to login automatically.",
                            color = Color.White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }


}

@Composable
fun RegistrationScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Login Form
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NetflixTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Enter your email"
                    )

                    NetflixTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Enter your password"
                    )
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Register", color = Color.White)
                    }
                }
            }

            // Vertical Divider with OR
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                VerticalDivider(
                    modifier = Modifier
                        .height(100.dp)
                        .width(1.dp),
                    color = Color.White
                )
                Text("OR", color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(8.dp))
                VerticalDivider(
                    modifier = Modifier
                        .height(100.dp)
                        .width(1.dp),
                    color = Color.White
                )
            }

            // QR Code Login
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .height(220.dp),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sample_qr),
                        contentDescription = "QR Code",
                        modifier = Modifier.size(150.dp)
                    )
                    Text(
                        "Scan the QR Code with your smartphone's camera to register.",
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NetflixTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.Black,
            unfocusedContainerColor = Color.Black,
            focusedIndicatorColor = Color.Red,
            unfocusedIndicatorColor = Color.Gray,
            cursorColor = Color.Red
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(horizontal = 8.dp)
    )
}

@Composable
fun LoginToggleSwitch(
    selectedOption: Boolean,
    onOptionSelected: (Boolean) -> Unit
) {
    val transition = updateTransition(targetState = selectedOption, label = "Toggle Animation")

    val offset by transition.animateDp(label = "Offset Animation") {
        if (it) 90.dp else 0.dp
    }

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.DarkGray)
            .clickable { onOptionSelected(!selectedOption) }
    ) {
        Box(
            modifier = Modifier
                .offset(x = offset)
                .width(110.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (selectedOption) Icons.Filled.PersonAdd else Icons.Filled.Person,
                    contentDescription = if (selectedOption) "Sign Up" else "Sign In",
                    tint = Color.Black,
                    modifier = Modifier
                )

                Text(
                    text = if (selectedOption) "Sign Up" else "Sign In",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.offset(5.dp)
                )
            }

        }
    }
}

@Composable
fun CircularLogoWithLoadingRing() {
    val infiniteTransition = rememberInfiniteTransition()

    // Animate the rotation of the outer ring
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(100.dp)
    ) {
        // Circular Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Loading",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape) // Makes the image circular
        )

        // Loading Ring
        Canvas(
            modifier = Modifier
                .size(80.dp) // Slightly larger than the logo
                .rotate(rotation) // Rotates the progress ring
        ) {
            drawArc(
                color = Color.Red, // Netflix theme color
                startAngle = 0f,
                sweepAngle = 270f, // Creates an incomplete circle effect
                useCenter = false,
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}
