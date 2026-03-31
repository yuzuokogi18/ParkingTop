package com.example.parkingtop.features.cliente.BusquedaClient.presentation.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.parkingtop.core.utils.MapsValues
import com.example.parkingtop.features.cliente.BusquedaClient.domain.entities.ParkingMarker
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary
import org.maplibre.android.camera.CameraPosition
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.runtime.DisposableEffect
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.PropertyFactory.*
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point

@Composable
fun ParkingMap(
    markers: List<ParkingMarker>,
    selectedMarker: ParkingMarker?,
    onMarkerClick: (ParkingMarker) -> Unit,
    onCardClick: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val localMarkers = remember(markers) {
        markers.filter { MapsValues.isInsideSanCristobal(it.latitude, it.longitude) }
    }

    // ✅ Guarda referencia al MapView (no solo al map) para manejar lifecycle
    var mapViewRef by remember { mutableStateOf<MapView?>(null) }
    // ✅ Callback para actualizar markers desde fuera del estilo
    var updateMarkersCallback by remember { mutableStateOf<((List<ParkingMarker>) -> Unit)?>(null) }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    // ✅ Conecta el lifecycle del MapView al lifecycle de Compose
    DisposableEffect(mapViewRef, lifecycle) {
        val mapView = mapViewRef ?: return@DisposableEffect onDispose {}
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START   -> mapView.onStart()
                Lifecycle.Event.ON_RESUME  -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE   -> mapView.onPause()
                Lifecycle.Event.ON_STOP    -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    // ✅ Actualiza markers cuando cambian — callback ya garantiza que el estilo está listo
    LaunchedEffect(localMarkers) {
        updateMarkersCallback?.invoke(localMarkers)
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                MapView(ctx).also { mapView ->
                    mapViewRef = mapView
                    mapView.onCreate(null) // ✅ Necesario antes de getMapAsync

                    mapView.getMapAsync { map ->
                        map.setStyle(MapsValues.MAP_STYLE) { style ->

                            map.cameraPosition = CameraPosition.Builder()
                                .target(MapsValues.SAN_CRISTOBAL)
                                .zoom(14.5)
                                .build()

                            map.setLatLngBoundsForCameraTarget(MapsValues.SAN_CRISTOBAL_BOUNDS)
                            map.setMinZoomPreference(12.0)
                            map.setMaxZoomPreference(18.0)

                            style.addImage(
                                MapsValues.MARKER_IMAGE_ID,
                                createParkingMarkerBitmap()
                            )

                            // ✅ Agrega fuente y capa vacías primero
                            val emptyCollection = FeatureCollection.fromFeatures(emptyList())
                            style.addSource(GeoJsonSource(MapsValues.SOURCE_ID, emptyCollection))
                            style.addLayer(buildSymbolLayer())

                            // ✅ Expone el callback para actualizar desde LaunchedEffect
                            updateMarkersCallback = { currentMarkers ->
                                style.getSourceAs<GeoJsonSource>(MapsValues.SOURCE_ID)
                                    ?.setGeoJson(buildFeatureCollection(currentMarkers))
                            }

                            // ✅ Carga los markers que ya estuvieran disponibles
                            updateMarkersCallback?.invoke(localMarkers)

                            map.addOnMapClickListener { latLng ->
                                val point = map.projection.toScreenLocation(latLng)
                                val hits = map.queryRenderedFeatures(point, MapsValues.LAYER_ID)
                                if (hits.isNotEmpty()) {
                                    val clickedId = hits[0].getStringProperty(MapsValues.PROP_ID)
                                    localMarkers.find { it.id == clickedId }
                                        ?.let { onMarkerClick(it) }
                                    true
                                } else false
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        selectedMarker?.let { parking ->
            ParkingPreviewCard(
                parking     = parking,
                onCardClick = { onCardClick(parking.id) },
                onDismiss   = onDismiss,
                modifier    = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun buildFeatureCollection(markers: List<ParkingMarker>): FeatureCollection {
    val features = markers.map { parking ->
        Feature.fromGeometry(
            Point.fromLngLat(parking.longitude, parking.latitude)
        ).also { f ->
            f.addStringProperty(MapsValues.PROP_ID,    parking.id)
            f.addStringProperty(MapsValues.PROP_NAME,  parking.name)
            f.addNumberProperty(MapsValues.PROP_PRICE, parking.pricePerHour) // ✅ nuevo
        }
    }
    return FeatureCollection.fromFeatures(features)
}

private fun buildSymbolLayer() = SymbolLayer(MapsValues.LAYER_ID, MapsValues.SOURCE_ID).apply {
    setProperties(
        iconImage(MapsValues.MARKER_IMAGE_ID),
        iconSize(1.2f),
        iconAllowOverlap(true),
        iconAnchor("bottom"),          // ✅ ancla el pin en la punta
        textField("{${MapsValues.PROP_NAME}}"),
        textSize(11f),
        textOffset(arrayOf(0f, 0.5f)),
        textFont(arrayOf("Noto Sans Regular")), // ✅ fuente que SÍ tiene OpenFreeMap
        textColor("#1A1A2E"),
        textHaloColor("#FFFFFF"),       // ✅ halo blanco para legibilidad
        textHaloWidth(1.5f),
        textAllowOverlap(false),
        textOptional(true)
    )
}
private fun createParkingMarkerBitmap(): Bitmap {
    val size   = 96
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = android.graphics.Color.parseColor("#2563EB")
        canvas.drawCircle(size / 2f, size / 2f, size / 2f - 4, it)
    }
    Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color       = android.graphics.Color.WHITE
        it.style       = Paint.Style.STROKE
        it.strokeWidth = 4f
        canvas.drawCircle(size / 2f, size / 2f, size / 2f - 4, it)
    }
    Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color          = android.graphics.Color.WHITE
        it.textSize       = 44f
        it.isFakeBoldText = true
        it.textAlign      = Paint.Align.CENTER
        val y = size / 2f - (it.descent() + it.ascent()) / 2
        canvas.drawText("P", size / 2f, y, it)
    }

    return bitmap
}

// ── Card preview ──────────────────────────────────────────────────────────────
@Composable
private fun ParkingPreviewCard(
    parking: ParkingMarker,
    onCardClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier.fillMaxWidth().clickable { onCardClick() },
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F4FF)),
                contentAlignment = Alignment.Center
            ) {
                if (parking.imageUrl != null) {
                    AsyncImage(
                        model              = parking.imageUrl,
                        contentDescription = parking.name,
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector        = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint               = BlueSecondary,
                        modifier           = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text     = parking.name,
                    style    = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color    = TextPrimary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text     = parking.address,
                    style    = MaterialTheme.typography.bodySmall,
                    color    = Color.Gray,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint     = Color(0xFFFFC107),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text  = "%.1f".format(parking.rating),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text  = "${parking.availableSpots} espacios",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (parking.availableSpots == 0) Color.Red else Color(0xFF22C55E)
                    )
                    Text(
                        text  = "${"%.0f".format(parking.pricePerHour)}/h",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = BlueSecondary
                    )
                }
            }

            IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                Text("✕", color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}