package com.example.parkingtop.features.register.presentation.screens

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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.parkingtop.R
import com.example.parkingtop.core.utils.uriToFile
import com.example.parkingtop.features.register.presentation.components.RegisterField
import com.example.parkingtop.features.register.presentation.components.RoleCard
import com.example.parkingtop.features.register.presentation.viewmodels.RegisterViewModel
import com.example.parkingtop.ui.theme.BlueSecondary
import com.example.parkingtop.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onSubscription: () -> Unit,
    onHome: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("Cliente") }

    // Image state
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val state by viewModel.state

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            when (state.role) {
                "owner" -> onSubscription()
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
                            text = "Crear Cuenta",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
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
                                painter = painterResource(R.drawable.logo_parking),
                                contentDescription = "Logo",
                                modifier = Modifier.size(22.dp)
                            )
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

            Text(
                text = "Únete a Parking Top",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Completa tus datos para empezar a parquear o gestionar tus espacios.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── PROFILE IMAGE PICKER ──────────────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F4FF))
                        .border(2.dp, BlueSecondary, CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Filled.AddAPhoto,
                                contentDescription = null,
                                tint = BlueSecondary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Foto",
                                style = MaterialTheme.typography.labelSmall,
                                color = BlueSecondary
                            )
                        }
                    }
                }

                // Small edit badge when image is selected
                if (selectedImageUri != null) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(BlueSecondary)
                            .align(Alignment.BottomCenter)
                            .offset(x = 32.dp, y = (-4).dp)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddAPhoto,
                            contentDescription = "Cambiar foto",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (selectedImageUri == null) "Agregar foto de perfil (opcional)"
                else "Toca para cambiar la foto",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            // ─────────────────────────────────────────────────────────────

            Spacer(modifier = Modifier.height(32.dp))

            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            RegisterField(
                label = "Nombre Completo",
                value = name,
                onValueChange = { name = it },
                placeholder = "Ej. Juan Pérez",
                leadingIcon = Icons.Outlined.Person
            )

            Spacer(modifier = Modifier.height(20.dp))

            RegisterField(
                label = "Correo Electrónico",
                value = email,
                onValueChange = { email = it },
                placeholder = "juan@ejemplo.com",
                leadingIcon = Icons.Outlined.Email
            )

            Spacer(modifier = Modifier.height(20.dp))

            // CONTRASEÑA
            Text(
                "Contraseña",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Mínimo 8 caracteres", color = Color.LightGray) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color.Gray) },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null, tint = Color.Gray)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    unfocusedBorderColor = Color(0xFFEEEEEE),
                    focusedBorderColor = BlueSecondary
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            RegisterField(
                label = "Teléfono (Opcional)",
                value = phone,
                onValueChange = { phone = it },
                placeholder = "+54 9 11 1234-5678",
                leadingIcon = Icons.Outlined.Phone
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ROL SELECTION
            Text(
                "¿Cómo usarás la app?",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RoleCard(
                    modifier = Modifier.weight(1f),
                    title = "Cliente",
                    icon = Icons.Outlined.Person,
                    isSelected = selectedRole == "Cliente",
                    onClick = { selectedRole = "Cliente" }
                )
                RoleCard(
                    modifier = Modifier.weight(1f),
                    title = "Propietario",
                    icon = Icons.Filled.Business,
                    isSelected = selectedRole == "Propietario",
                    onClick = { selectedRole = "Propietario" }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // BOTÓN REGISTRO
            Button(
                onClick = {
                    val role = if (selectedRole == "Cliente") "customer" else "owner"
                    // Convert URI → File only when the user taps register
                    val imageFile = selectedImageUri?.let { uriToFile(context, it) }
                    viewModel.register(
                        email = email,
                        password = password,
                        fullName = name,
                        phone = phone.ifEmpty { null },
                        role = role,
                        profileImage = imageFile
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueSecondary),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "Registrarme ahora",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = buildAnnotatedString {
                        append("¿Ya tienes cuenta? ")
                        withStyle(style = SpanStyle(color = BlueSecondary, fontWeight = FontWeight.Bold)) {
                            append("Inicia sesión")
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}