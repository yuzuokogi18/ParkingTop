package com.parking.parkingtop

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.parking.parkingtop.core.di.navigation.AppNavigation
import com.parking.parkingtop.ui.theme.ParkingTopTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject
    @ApplicationContext
    lateinit var appContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapLibre.getInstance(
            appContext,
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