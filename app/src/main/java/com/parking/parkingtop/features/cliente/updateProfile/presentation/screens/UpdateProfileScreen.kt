package com.parking.parkingtop.features.cliente.updateProfile.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.parking.parkingtop.core.utils.uriToFile
import com.parking.parkingtop.features.cliente.updateProfile.presentation.viewmodels.UpdateProfileViewModel
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreen(
    // Datos actuales del usuario para pre-poblar los campos
    currentName:  String,
    currentPhone: String?,
    currentImage: String?,           // URL de la foto actual (Cloudinary)
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: UpdateProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state   by viewModel.state.collectAsStateWithLifecycle()

    var fullName         by remember { mutableStateOf(currentName) }
    var phone            by remember { mutableStateOf(currentPhone ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> selectedImageUri = uri }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onSuccess()
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Editar Perfil",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBackIosNew, null, tint = TextPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // ── Foto de perfil ────────────────────────────────────────────────
            Box(
                modifier         = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F4FF))
                        .border(2.dp, BlueSecondary, CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        // Nueva imagen seleccionada
                        selectedImageUri != null -> {
                            Image(
                                painter            = rememberAsyncImagePainter(selectedImageUri),
                                contentDescription = "Nueva foto",
                                modifier           = Modifier.fillMaxSize(),
                                contentScale       = ContentScale.Crop
                            )
                        }
                        // Imagen actual de Cloudinary
                        currentImage != null -> {
                            AsyncImage(
                                model              = currentImage,
                                contentDescription = "Foto actual",
                                modifier           = Modifier.fillMaxSize(),
                                contentScale       = ContentScale.Crop
                            )
                        }
                        // Sin imagen
                        else -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.AddAPhoto,
                                    contentDescription = null,
                                    tint     = BlueSecondary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Foto", style = MaterialTheme.typography.labelSmall, color = BlueSecondary)
                            }
                        }
                    }
                }

                // Badge de edición
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(BlueSecondary)
                        .align(Alignment.BottomCenter)
                        .offset(x = 36.dp, y = (-4).dp)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AddAPhoto,
                        contentDescription = "Cambiar foto",
                        tint     = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text     = if (selectedImageUri == null) "Toca para cambiar tu foto" else "Nueva foto seleccionada",
                style    = MaterialTheme.typography.bodySmall,
                color    = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Error ─────────────────────────────────────────────────────────
            state.error?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text     = error,
                        color    = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(12.dp),
                        style    = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // ── Nombre ────────────────────────────────────────────────────────
            ProfileField(
                label       = "Nombre Completo",
                value       = fullName,
                onChange    = { fullName = it },
                placeholder = "Tu nombre",
                leadingIcon = { Icon(Icons.Outlined.Person, null, tint = Color.Gray) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Teléfono ──────────────────────────────────────────────────────
            ProfileField(
                label       = "Teléfono (Opcional)",
                value       = phone,
                onChange    = { phone = it },
                placeholder = "+52 999 123 4567",
                leadingIcon = { Icon(Icons.Outlined.Phone, null, tint = Color.Gray) }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ── Botón guardar ─────────────────────────────────────────────────
            Button(
                onClick = {
                    val imageFile = selectedImageUri?.let { uriToFile(context, it) }
                    viewModel.updateProfile(
                        fullName     = fullName,
                        phone        = phone.ifBlank { null },
                        profileImage = imageFile
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape   = RoundedCornerShape(14.dp),
                colors  = ButtonDefaults.buttonColors(containerColor = BlueSecondary),
                enabled = fullName.isNotBlank() && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "Guardar Cambios",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ── Campo reutilizable ────────────────────────────────────────────────────────
@Composable
private fun ProfileField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable () -> Unit
) {
    Text(
        label,
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
        color = TextPrimary
    )
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
        value         = value,
        onValueChange = onChange,
        placeholder   = { Text(placeholder, color = Color.LightGray) },
        leadingIcon   = leadingIcon,
        modifier      = Modifier.fillMaxWidth(),
        shape         = RoundedCornerShape(12.dp),
        singleLine    = true,
        colors        = OutlinedTextFieldDefaults.colors(
            focusedTextColor     = TextPrimary,
            unfocusedTextColor   = TextPrimary,
            unfocusedBorderColor = Color(0xFFEEEEEE),
            focusedBorderColor   = BlueSecondary
        )
    )
}