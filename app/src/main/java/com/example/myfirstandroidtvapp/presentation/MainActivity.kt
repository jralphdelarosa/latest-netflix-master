package com.example.myfirstandroidtvapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myfirstandroidtvapp.presentation.AppNavigation
import com.example.myfirstandroidtvapp.ui.theme.MyFirstAndroidTVAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyFirstAndroidTVAppTheme {
                AppNavigation()
            }
        }
    }
}