package com.example.rescatando_mascotas_forever.presentation.rescates.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.data.local.SessionManager
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    // Observamos el flow global para que cualquier cambio se vea en todas partes
    val user by SessionManager.userFlow.collectAsState()
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            // Copiar la imagen al almacenamiento interno para que sea permanente
            val internalPath = saveImageToInternalStorage(context, uri)
            if (internalPath != null) {
                val updatedUser = user?.copy(avatar = internalPath)
                if (updatedUser != null) {
                    sessionManager.updateUser(updatedUser)
                    Toast.makeText(context, "Foto de perfil guardada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { AppBottomBar(navController = navController) },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    ProfileHeader(
                        name = user?.nombre ?: "Usuario",
                        email = user?.email ?: "Sin correo",
                        avatarUrl = user?.avatar,
                        onEditClick = { launcher.launch("image/*") }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    ProfileMenuSection(navController, context)
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    LogoutButton(navController, sessionManager)
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "profile_pic_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        
        // Eliminar fotos de perfil anteriores para ahorrar espacio
        context.filesDir.listFiles()?.forEach { 
            if (it.name.startsWith("profile_pic_")) it.delete() 
        }
        
        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun ProfileHeader(name: String, email: String, avatarUrl: String?, onEditClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF7B5EE1), Color(0xFF4C35A3))
                ),
                shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
            )
            .padding(vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    modifier = Modifier
                        .size(120.dp)
                        .clickable { onEditClick() },
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (!avatarUrl.isNullOrEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(avatarUrl),
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = Color(0xFF673AB7)
                            )
                        }
                    }
                }
                
                Surface(
                    modifier = Modifier
                        .size(36.dp)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .clickable { onEditClick() },
                    shape = CircleShape,
                    color = Color(0xFF673AB7),
                    shadowElevation = 4.dp,
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color.White)
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Cambiar foto",
                        modifier = Modifier.padding(8.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = email,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ProfileMenuSection(navController: NavHostController, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
            .padding(vertical = 8.dp)
    ) {
        ProfileMenuItem(
            icon = Icons.Default.Favorite,
            title = stringResource(R.string.profile_my_adoptions),
            subtitle = stringResource(R.string.profile_adoptions_desc),
            onClick = { navController.navigate("adopciones") }
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
        ProfileMenuItem(
            icon = Icons.Default.Settings,
            title = stringResource(R.string.profile_settings),
            subtitle = stringResource(R.string.profile_settings_desc),
            onClick = { navController.navigate("settings") }
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
        ProfileMenuItem(
            icon = Icons.Default.SupportAgent,
            title = stringResource(R.string.profile_support),
            subtitle = stringResource(R.string.profile_support_desc),
            onClick = { 
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://wa.me/573123456789")
                }
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Composable
fun LogoutButton(navController: NavHostController, sessionManager: SessionManager) {
    Button(
        onClick = { 
            sessionManager.logout()
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(Icons.AutoMirrored.Filled.ExitToApp, null, tint = Color.White)
        Spacer(modifier = Modifier.width(12.dp))
        Text(stringResource(R.string.profile_logout), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
    }
}
