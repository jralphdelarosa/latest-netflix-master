package com.example.myfirstandroidtvapp.presentation.login

import android.widget.Toast
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myfirstandroidtvapp.R

@Composable
fun LoginRegistrationScreen(navController: NavController) {

    var isSignup by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(isSignup) {
        Toast.makeText(context, isSignup.toString(), Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Logo at the top-right corner
            Row(
                modifier = Modifier
                    .padding(end = 20.dp)
                    .align(Alignment.TopEnd)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(150.dp)
                )
            }


            // Centered Row for Toggle Switch
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
                .weight(1f) // Pushes this Box to take up remaining space
        ) {
            if(isSignup){
                RegistrationScreen(navController)
            }else{
                LoginScreen(navController)
            }

        }

        // Guest Mode
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    navController.navigate("home")
                },
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text("Watch as Guest", color = Color.White)
            }
        }


    }


}

@Composable
fun LoginScreen(navController: NavController) {
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
                    TextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Email", color = Color.Gray) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.DarkGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.Red
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Password", color = Color.Gray) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.DarkGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.Red
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.fillMaxWidth()
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
                    .height(220.dp)
                ,
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

@Composable
fun RegistrationScreen(navController: NavController) {
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
                    TextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Email", color = Color.Gray) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.DarkGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.Red
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Password", color = Color.Gray) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.DarkGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.Red
                        ),
                        modifier = Modifier.fillMaxWidth()
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
