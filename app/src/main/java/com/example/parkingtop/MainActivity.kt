package com.example.parkingtop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.parkingtop.core.di.navigation.AppNavigation
import com.example.parkingtop.ui.theme.ParkingTopTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            // Forzamos darkTheme = false para que siempre se vea blanco independientemente del sistema
            ParkingTopTheme(darkTheme = false) {
                AppNavigation()
            }
        }
    }
}