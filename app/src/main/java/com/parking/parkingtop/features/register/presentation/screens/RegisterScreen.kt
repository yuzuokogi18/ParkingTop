package com.parking.parkingtop.features.register.presentation.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.parking.parkingtop.R
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.parking.parkingtop.core.utils.uriToFile
import com.parking.parkingtop.features.register.presentation.components.RegisterField
import com.parking.parkingtop.features.register.presentation.components.RoleCard
import com.parking.parkingtop.features.register.presentation.viewmodels.RegisterViewModel
import com.parking.parkingtop.ui.theme.BlueSecondary
import com.parking.parkingtop.ui.theme.TextPrimary
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onSubscription: () -> Unit,
    onHome: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    var name            by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var phone           by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole    by remember { mutableStateOf("Cliente") }

    val state   = viewModel.state.value
    val context = LocalContext.current

    // ── Foto de perfil — mismo patrón que ProfileClientScreen ────────────────
    var cameraImageUri       by remember { mutableStateOf<Uri?>(null) }
    var showPhotoSourceSheet by remember { mutableStateOf(false) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) cameraImageUri?.let { viewModel.onProfileImageSelected(it) }
    }

    val pickFromGalleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { viewModel.onProfileImageSelected(it) } }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val photoFile = File.createTempFile("profile_", ".jpg", context.cacheDir)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                photoFile
            )
            cameraImageUri = uri
            takePictureLauncher.launch(uri)
        } else {
            viewModel.onCameraPermissionDenied()
        }
    }

    // ── Bottom sheet: cámara o galería ────────────────────────────────────────
    if (showPhotoSourceSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPhotoSourceSheet = false },
            containerColor   = Color.White,
            shape            = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    "Foto de perfil",
                    style    = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color    = TextPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (state.cameraAvailable) {
                    ListItem(
                        headlineContent   = { Text("Tomar foto") },
                        supportingContent = { Text("Usa tu cámara", color = Color.Gray, fontSize = 12.sp) },
                        leadingContent    = { Icon(Icons.Default.CameraAlt, null, tint = BlueSecondary) },
                        modifier = Modifier.clickable {
                            showPhotoSourceSheet = false
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    )
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                }

                ListItem(
                    headlineContent   = { Text("Elegir de galería") },
                    supportingContent = { Text("Selecciona una imagen existente", color = Color.Gray, fontSize = 12.sp) },
                    leadingContent    = { Icon(Icons.Default.PhotoLibrary, null, tint = BlueSecondary) },
                    modifier = Modifier.clickable {
                        showPhotoSourceSheet = false
                        pickFromGalleryLauncher.launch("image/*")
                    }
                )
            }
        }
    }

    // ── Dialog rationale ──────────────────────────────────────────────────────
    if (state.showCameraPermissionRationale) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissCameraRationale() },
            title   = { Text("Permiso de cámara necesario") },
            text    = { Text("Para tomar tu foto de perfil necesitamos acceso a la cámara. Puedes concederlo desde Configuración > Aplicaciones.") },
            confirmButton   = {
                TextButton(onClick = { viewModel.dismissCameraRationale() }) {
                    Text("Entendido", color = BlueSecondary)
                }
            }
        )
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            when (state.role) {
                "owner"    -> onSubscription()
                "customer" -> onHome()
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Crear Cuenta",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold, fontSize = 18.sp
                            ),
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(TextPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter            = painterResource(R.drawable.logo_parking),
                                contentDescription = "Logo",
                                modifier           = Modifier.size(22.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
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

            Text(
                "Únete a Parking Top",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Completa tus datos para empezar a parquear o gestionar tus espacios.",
                style = MaterialTheme.typography.bodyLarge, color = Color.Gray, lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Avatar ────────────────────────────────────────────────────────
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F4FF))
                        .border(2.dp, BlueSecondary, CircleShape)
                        .clickable { showPhotoSourceSheet = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (state.profileImageUri != null) {
                        Image(
                            painter            = rememberAsyncImagePainter(state.profileImageUri),
                            contentDescription = "Foto de perfil",
                            modifier           = Modifier.fillMaxSize(),
                            contentScale       = ContentScale.Crop
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AddAPhoto, null, tint = BlueSecondary, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Foto", style = MaterialTheme.typography.labelSmall, color = BlueSecondary)
                        }
                    }
                }

                if (state.profileImageUri != null) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(BlueSecondary)
                            .align(Alignment.BottomCenter)
                            .offset(x = 32.dp, y = (-4).dp)
                            .clickable { showPhotoSourceSheet = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AddAPhoto, null, tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text     = if (state.profileImageUri == null) "Agregar foto de perfil (opcional)"
                else "Toca para cambiar la foto",
                style    = MaterialTheme.typography.bodySmall,
                color    = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (state.error != null) {
                Text(state.error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 16.dp))
            }

            RegisterField(
                "Nombre Completo",
                name,
                { name = it },
                "Ej. Juan Pérez",
                Icons.Outlined.Person
            )
            Spacer(modifier = Modifier.height(20.dp))
            RegisterField(
                "Correo Electrónico",
                email,
                { email = it },
                "juan@ejemplo.com",
                Icons.Outlined.Email
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text("Contraseña", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value         = password, onValueChange = { password = it },
                placeholder   = { Text("Mínimo 8 caracteres", color = Color.LightGray) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon   = { Icon(Icons.Outlined.Lock, null, tint = Color.Gray) },
                trailingIcon  = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, null, tint = Color.Gray)
                    }
                },
                modifier   = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true,
                colors     = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary,
                    unfocusedBorderColor = Color(0xFFEEEEEE), focusedBorderColor = BlueSecondary
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
            RegisterField(
                "Teléfono (Opcional)",
                phone,
                { phone = it },
                "+54 9 11 1234-5678",
                Icons.Outlined.Phone
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text("¿Cómo usarás la app?", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                RoleCard(
                    Modifier.weight(1f),
                    "Cliente",
                    Icons.Outlined.Person,
                    selectedRole == "Cliente"
                ) { selectedRole = "Cliente" }
                RoleCard(
                    Modifier.weight(1f),
                    "Propietario",
                    Icons.Filled.Business,
                    selectedRole == "Propietario"
                ) { selectedRole = "Propietario" }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    val role      = if (selectedRole == "Cliente") "customer" else "owner"
                    val imageFile = state.profileImageUri?.let { uriToFile(context, it) }
                    viewModel.register(
                        email        = email,
                        password     = password,
                        fullName     = name,
                        phone        = phone.ifEmpty { null },
                        role         = role,
                        profileImage = imageFile
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = BlueSecondary),
                enabled  = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Registrarme ahora", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = buildAnnotatedString {
                        append("¿Ya tienes cuenta? ")
                        withStyle(SpanStyle(color = BlueSecondary, fontWeight = FontWeight.Bold)) { append("Inicia sesión") }
                    },
                    style    = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}