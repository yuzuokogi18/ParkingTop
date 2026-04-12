package com.parking.parkingtop.features.cliente.ReservaClient.presentation.screens

import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.parking.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentWebViewScreen(
    paymentUrl: String,
    onPaymentSuccess: () -> Unit,
    onPaymentFailure: () -> Unit,
    onPaymentPending: () -> Unit,
    onBack: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var pageTitle by remember { mutableStateOf("Completar Pago") }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            pageTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                "Volver",
                                tint = TextPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color    = com.parking.parkingtop.ui.theme.BlueSecondary
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
            }
        }
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled  = true
                        settings.domStorageEnabled   = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort      = true

                        webViewClient = object : WebViewClient() {

                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoading  = false
                                pageTitle  = view?.title ?: "Completar Pago"
                            }

                            // ✅ Intercepta el deep link de retorno de MercadoPago
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val url = request?.url ?: return false

                                return when {
                                    // Deep link de éxito: parkingtop://payment/success
                                    url.scheme == "parkingtop" && url.host == "payment" -> {
                                        when (url.path) {
                                            "/success" -> onPaymentSuccess()
                                            "/failure" -> onPaymentFailure()
                                            "/pending" -> onPaymentPending()
                                        }
                                        true
                                    }
                                    // MercadoPago puede redirigir a HTTPS también
                                    url.toString().contains("payment/success") -> {
                                        onPaymentSuccess(); true
                                    }
                                    url.toString().contains("payment/failure") -> {
                                        onPaymentFailure(); true
                                    }
                                    url.toString().contains("payment/pending") -> {
                                        onPaymentPending(); true
                                    }
                                    else -> false
                                }
                            }
                        }

                        loadUrl(paymentUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color    = com.parking.parkingtop.ui.theme.BlueSecondary
                )
            }
        }
    }
}