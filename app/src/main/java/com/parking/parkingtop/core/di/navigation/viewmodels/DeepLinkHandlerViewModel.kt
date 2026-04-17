package com.parking.parkingtop.core.di.navigation.viewmodels

import androidx.lifecycle.ViewModel
import com.parking.parkingtop.core.di.navigation.DeepLinkHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel delgado cuyo único propósito es exponer [DeepLinkHandler]
 * al árbol de composables sin acoplarlo directamente al Activity.
 *
 * Al ser un HiltViewModel su ciclo de vida está atado al NavBackStackEntry
 * raíz, pero el handler en sí es @Singleton y sobrevive recomposiciones.
 */
@HiltViewModel
class DeepLinkHandlerViewModel @Inject constructor(
    val handler: DeepLinkHandler
) : ViewModel()
