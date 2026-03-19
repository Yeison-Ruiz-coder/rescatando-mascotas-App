package com.example.rescatando_mascotas_forever.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.presentation.common.components.*

data class UsuarioAdmin(
    val id: Int,
    val nombre: String,
    val email: String,
    val rol: String,
    val fechaRegistro: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsuariosScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val usuarios = remember {
        mutableStateListOf(
            UsuarioAdmin(1, "Yeison", "admin@rescatando.com", "Administrador", "01/01/2024"),
            UsuarioAdmin(2, "Juan Pérez", "juan@gmail.com", "Voluntario", "15/02/2024"),
            UsuarioAdmin(3, "María García", "maria@hotmail.com", "Adoptante", "20/02/2024"),
            UsuarioAdmin(4, "Carlos Ruiz", "carlos@outlook.com", "Usuario", "01/03/2024")
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                AdminDrawerContent(navController, drawerState, scope)
            }
        }
    ) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F9FA))
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF673AB7))
                        .padding(24.dp)
                ) {
                    Column {
                        Text("Gestión de Usuarios", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Administra los accesos y roles", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }

                // Barra de búsqueda (Simulada)
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Buscar usuario...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF673AB7),
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(usuarios) { usuario ->
                        UsuarioCard(usuario)
                    }
                }
            }
        }
    }
}

@Composable
fun UsuarioCard(usuario: UsuarioAdmin) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF673AB7).copy(alpha = 0.1f),
                modifier = Modifier.size(45.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = usuario.nombre.take(1).uppercase(),
                        color = Color(0xFF673AB7),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
            
            Spacer(Modifier.width(16.dp))
            
            Column(Modifier.weight(1f)) {
                Text(usuario.nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(usuario.email, color = Color.Gray, fontSize = 12.sp)
                
                Spacer(Modifier.height(4.dp))
                
                Surface(
                    color = when(usuario.rol) {
                        "Administrador" -> Color(0xFFFF9800).copy(alpha = 0.1f)
                        "Voluntario" -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                        else -> Color(0xFF673AB7).copy(alpha = 0.1f)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = usuario.rol,
                        color = when(usuario.rol) {
                            "Administrador" -> Color(0xFFFF9800)
                            "Voluntario" -> Color(0xFF4CAF50)
                            else -> Color(0xFF673AB7)
                        },
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, null, tint = Color.Gray)
            }
        }
    }
}
