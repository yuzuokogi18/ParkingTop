package com.example.parkingtop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.parkingtop.core.di.navigation.AppNavigation
import com.example.parkingtop.ui.theme.ParkingTopTheme
import dagger.hilt.android.AndroidEntryPoint
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapLibre.getInstance(
            this,
            "",
            WellKnownTileServer.MapLibre
        )
        enableEdgeToEdge()

        setContent {
            ParkingTopTheme(darkTheme = false) {
                AppNavigation()
            }
        }
    }
}