package com.parking.parkingtop

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.parking.parkingtop.core.di.navigation.AppNavigation
import com.parking.parkingtop.ui.theme.ParkingTopTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkingTopTheme(darkTheme = false) {
                AppNavigation()
            }
        }
    }
}