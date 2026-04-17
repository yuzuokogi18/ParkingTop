package com.parking.parkingtop

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.parking.parkingtop.core.di.navigation.AppNavigation
import com.parking.parkingtop.core.di.navigation.DeepLinkHandler
import com.parking.parkingtop.ui.theme.ParkingTopTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject lateinit var deepLinkHandler: DeepLinkHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ParkingTopTheme {
                AppNavigation()
            }
        }

        // IMPORTANTE: llamar DESPUÉS de setContent para que el NavHost
        // ya esté en proceso de composición cuando el evento se emite.
        deepLinkHandler.handle(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        deepLinkHandler.handle(intent)
    }
}