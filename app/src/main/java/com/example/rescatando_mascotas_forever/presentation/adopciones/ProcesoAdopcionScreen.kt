package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcesoAdopcionScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var cedula by remember { mutableStateOf("") }

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { AppBottomBar(navController = navController) },
            containerColor = Color(0xFFFDF7F2)
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header con diseño moderno
                item {
                    HeaderSeccion()
                }

                // Buscador de Cédula actualizado
                item {
                    BuscadorCedula(cedula) { cedula = it }
                }

                // Contenido principal con Cards actualizadas
                item {
                    ContenidoProceso()
                }

                // Footer de Estado ondulado
                item {
                    FooterEstadoProceso()
                }
                
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun HeaderSeccion() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF9C27B0), Color(0xFF673AB7))
                )
            )
            .padding(vertical = 40.dp, horizontal = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Bienvenido a tu espacio sobre el estado de proceso de adopción",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )
    }
}

@Composable
fun BuscadorCedula(value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Ingrese su número de cédula para consultar",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .shadow(8.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, null, tint = Color(0xFF673AB7), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (value.isEmpty()) {
                            Text("Ej: 1061789...", color = Color.LightGray)
                        }
                        innerTextField()
                    }
                )
            }
        }
    }
}

@Composable
fun ContenidoProceso() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Columna Izquierda
        Column(modifier = Modifier.weight(1f)) {
            InfoCardActualizada(
                numero = "1",
                titulo = "Requisitos Generales",
                items = listOf(
                    "Edad mínima del adoptante",
                    "Estado civil permitido",
                    "Situación económica",
                    "Documentación necesaria"
                ),
                color = Color(0xFFF3E5F5)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Ilustración plato (Placeholder)
            IconoCircular(Icons.Default.Favorite, Color(0xFFFFEB3B))

            Spacer(modifier = Modifier.height(24.dp))

            InfoCardActualizada(
                numero = "3",
                titulo = "Recursos y Apoyo",
                items = listOf(
                    "Plantillas descargables",
                    "Lista de chequeo",
                    "Calendario del proceso"
                ),
                color = Color(0xFFE8EAF6)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Ilustración perro (Placeholder)
            IconoCircular(Icons.Default.Face, Color(0xFFFFCC80))
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Columna Derecha
        Column(modifier = Modifier.weight(1f)) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Ilustración gato (Placeholder)
            IconoCircular(Icons.Default.Star, Color(0xFFCE93D8))

            Spacer(modifier = Modifier.height(24.dp))

            InfoCardActualizada(
                numero = "2",
                titulo = "Proceso de Adopción",
                items = listOf(
                    "Etapas del proceso",
                    "Evaluación y entrevistas",
                    "Asignación de mascota",
                    "Duración aproximada"
                ),
                color = Color(0xFFE1F5FE)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Ilustración huella (Placeholder)
            IconoCircular(Icons.Default.ThumbUp, Color(0xFFC8E6C9))

            Spacer(modifier = Modifier.height(24.dp))

            InfoCardActualizada(
                numero = "4",
                titulo = "Comunidad",
                items = listOf(
                    "Grupos de apoyo local",
                    "Compartir experiencias",
                    "Testimonios reales"
                ),
                color = Color(0xFFFCE4EC)
            )
        }
    }
}

@Composable
fun InfoCardActualizada(numero: String, titulo: String, items: List<String>, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(24.dp),
                    shape = CircleShape,
                    color = Color(0xFF673AB7)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(numero, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF311B92))
            }
            Spacer(modifier = Modifier.height(12.dp))
            items.forEach { item ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("•", color = Color(0xFF673AB7), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = item, fontSize = 11.sp, color = Color.DarkGray, lineHeight = 15.sp)
                }
            }
        }
    }
}

@Composable
fun IconoCircular(icon: ImageVector, bgColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = bgColor,
            shadowElevation = 4.dp
        ) {
            Icon(icon, null, modifier = Modifier.padding(20.dp), tint = Color.White)
        }
    }
}

@Composable
fun FooterEstadoProceso() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        color = Color(0xFF673AB7)
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF9C27B0), Color(0xFF673AB7))
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Estado de proceso",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EstadoItemActualizado(Icons.Default.CheckCircle, "Enviada", true)
                EstadoItemActualizado(Icons.Default.Search, "Revisión", false)
                EstadoItemActualizado(Icons.Default.DateRange, "Visita", false)
                EstadoItemActualizado(Icons.Default.Home, "Finalizado", false)
            }
        }
    }
}

@Composable
fun EstadoItemActualizado(icon: ImageVector, label: String, isCompleted: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(45.dp),
            shape = CircleShape,
            color = if (isCompleted) Color.White else Color.White.copy(alpha = 0.2f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon, 
                    null, 
                    tint = if (isCompleted) Color(0xFF673AB7) else Color.White, 
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Normal
        )
    }
}
