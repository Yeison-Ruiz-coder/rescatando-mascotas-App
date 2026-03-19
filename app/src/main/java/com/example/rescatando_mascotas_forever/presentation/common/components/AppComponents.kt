package com.example.rescatando_mascotas_forever.presentation.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rescatando_mascotas_forever.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Degradado global para la aplicación
val AppMainGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF9C27B0), Color(0xFF3F51B5))
)

@Composable
fun GradientHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = AppMainGradient)
            .padding(vertical = 40.dp, horizontal = 24.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(drawerState: DrawerState, scope: CoroutineScope) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color.White
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.logo_foreground),
                        contentDescription = null,
                        modifier = Modifier.padding(2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Rescatando Mascotas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF673AB7)
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = { 
                    scope.launch { 
                        if (drawerState.isClosed) drawerState.open() else drawerState.close()
                    } 
                }
            ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = Color(0xFF673AB7)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { if (currentRoute != "home") navController.navigate("home") },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = currentRoute == "adopciones",
            onClick = { if (currentRoute != "adopciones") navController.navigate("adopciones") },
            icon = { Icon(Icons.Default.Favorite, null) },
            label = { Text("Adopta") }
        )
        // BOTÓN DE RESCATAR EN ROJO
        NavigationBarItem(
            selected = currentRoute == "formulario_rescate",
            onClick = { if (currentRoute != "formulario_rescate") navController.navigate("formulario_rescate") },
            icon = { 
                Icon(
                    Icons.Default.AddCircle, 
                    contentDescription = null,
                    tint = if (currentRoute == "formulario_rescate") Color.Red else Color.Red.copy(alpha = 0.7f)
                ) 
            },
            label = { Text("Rescatar", color = Color.Red, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Red,
                unselectedIconColor = Color.Red.copy(alpha = 0.7f),
                selectedTextColor = Color.Red,
                unselectedTextColor = Color.Red.copy(alpha = 0.7f),
                indicatorColor = Color.Red.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            selected = currentRoute == "perfil",
            onClick = { if (currentRoute != "perfil") navController.navigate("perfil") },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Perfil") }
        )
    }
}

@Composable
fun AppDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                DrawerContent(navController, drawerState, scope)
            }
        }
    ) {
        content()
    }
}

@Composable
fun DrawerContent(navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope) {
    val scrollState = rememberScrollState()
    
    val navigateAndClose: (String) -> Unit = { route ->
        scope.launch {
            drawerState.close()
            navController.navigate(route)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        // Cabecera del Menú
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppMainGradient)
                .padding(24.dp)
                .padding(top = 24.dp)
        ) {
            Column {
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Icon(Icons.Default.AccountCircle, null, tint = Color.White, modifier = Modifier.size(48.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Hola, Yeison", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("usuario@ejemplo.com", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Items del Menú Principal
        DrawerMenuItem("Inicio", Icons.Default.Home) { navigateAndClose("home") }
        DrawerMenuItem("Mi Perfil", Icons.Default.Person) { navigateAndClose("perfil") }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))
        
        Text("GESTIÓN DE MASCOTAS", modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        
        DrawerMenuItem("Adoptar Mascota", Icons.Default.Favorite) { navigateAndClose("adopciones") }
        DrawerMenuItem("Reportar Rescate", Icons.Default.AddCircle, Color.Red) { navigateAndClose("formulario_rescate") }
        DrawerMenuItem("Solicitud de Adopción", Icons.Default.Assignment) { navigateAndClose("formulario_adopcion") }
        DrawerMenuItem("Últimos Rescates", Icons.AutoMirrored.Filled.List) { navigateAndClose("ultimos_rescates") }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))

        Text("COMUNIDAD", modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        
        DrawerMenuItem("Eventos", Icons.Default.DateRange) { navigateAndClose("eventos") }
        DrawerMenuItem("Voluntarios", Icons.Default.Face) { navigateAndClose("rescatista_contactos") }
        DrawerMenuItem("Donaciones", Icons.Default.Star) { navigateAndClose("donaciones") }
        
        Spacer(modifier = Modifier.weight(1f))
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))

        DrawerMenuItem("Cerrar Sesión", Icons.AutoMirrored.Filled.ExitToApp, Color.Red) {
            navigateAndClose("login")
        }
    }
}

@Composable
fun DrawerMenuItem(text: String, icon: ImageVector, color: Color = Color.DarkGray, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(text, color = color, fontWeight = FontWeight.Medium) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = null, tint = color) },
        modifier = Modifier.padding(horizontal = 12.dp),
        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
    )
}
