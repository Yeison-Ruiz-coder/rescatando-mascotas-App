package com.example.rescatando_mascotas_forever.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(35.dp), shape = CircleShape, color = Color.White) {
                    Image(painter = painterResource(id = R.mipmap.logo_foreground), contentDescription = null)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text("Rescatando Mascotas", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF673AB7))
            }
        },
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(Icons.Default.Menu, "Menú", tint = Color(0xFF673AB7))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = currentRoute == "adopciones",
            onClick = { navController.navigate("adopciones") },
            icon = { Icon(Icons.Default.Favorite, null) },
            label = { Text("Adopta") }
        )
        NavigationBarItem(
            selected = currentRoute == "formulario_rescate",
            onClick = { navController.navigate("formulario_rescate") },
            icon = { Icon(Icons.Default.AddCircle, null, tint = Color.Red) },
            label = { Text("Rescatar", color = Color.Red) }
        )
        NavigationBarItem(
            selected = currentRoute == "perfil",
            onClick = { navController.navigate("perfil") },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Perfil") }
        )
    }
}

@Composable
fun AppDrawer(navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope, content: @Composable () -> Unit) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: ""
            val isAdminRoute = currentRoute.startsWith("admin_")

            ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                if (isAdminRoute) {
                    AdminDrawerContent(navController, drawerState, scope)
                } else {
                    DrawerContent(navController, drawerState, scope)
                }
            }
        }
    ) { content() }
}

@Composable
fun DrawerContent(navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope) {
    val navigateAndClose: (String) -> Unit = { route ->
        scope.launch {
            drawerState.close()
            navController.navigate(route)
        }
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.fillMaxWidth().background(AppMainGradient).padding(24.dp)) {
            Column {
                Icon(Icons.Default.AccountCircle, null, tint = Color.White, modifier = Modifier.size(64.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("Hola, Yeison", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }

        Spacer(Modifier.height(8.dp))
        DrawerMenuItem("Inicio", Icons.Default.Home) { navigateAndClose("home") }
        DrawerMenuItem("Mi Perfil", Icons.Default.Person) { navigateAndClose("perfil") }

        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        Text("GESTIÓN", modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp), fontSize = 11.sp, color = Color.Gray)
        DrawerMenuItem("Adoptar", Icons.Default.Favorite) { navigateAndClose("adopciones") }
        DrawerMenuItem("Reportar Rescate", Icons.Default.AddCircle) { navigateAndClose("formulario_rescate") }

        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        Text("COMUNIDAD", modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp), fontSize = 11.sp, color = Color.Gray)
        DrawerMenuItem("Eventos", Icons.Default.DateRange) { navigateAndClose("eventos") }
        DrawerMenuItem("Voluntarios", Icons.Default.Face) { navigateAndClose("rescatista_contactos") }
        DrawerMenuItem("Donaciones", Icons.Default.Star) { navigateAndClose("donaciones") }
        DrawerMenuItem("Veterinarias", Icons.Default.LocationOn) { navigateAndClose("veterinarias") }
        DrawerMenuItem("Fundaciones", Icons.Default.Business) { navigateAndClose("fundaciones") }

        Spacer(Modifier.weight(1f))
        DrawerMenuItem("Cerrar Sesión", Icons.AutoMirrored.Filled.ExitToApp, Color.Red) { navigateAndClose("login") }
    }
}

@Composable
fun AdminDrawerContent(navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope) {
    val navigateAndClose: (String) -> Unit = { route ->
        scope.launch {
            drawerState.close()
            navController.navigate(route)
        }
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF333333)).padding(24.dp)) {
            Text("Panel Admin", color = Color.White, fontWeight = FontWeight.Bold)
        }
        DrawerMenuItem("Inicio Admin", Icons.Default.Dashboard) { navigateAndClose("admin_home") }
        DrawerMenuItem("Veterinarias", Icons.Default.LocationOn) { navigateAndClose("veterinarias") }
        DrawerMenuItem("Fundaciones", Icons.Default.Business) { navigateAndClose("fundaciones") }
        HorizontalDivider()
        DrawerMenuItem("Salir", Icons.AutoMirrored.Filled.ExitToApp) { navigateAndClose("home") }
    }
}

@Composable
fun DrawerMenuItem(text: String, icon: ImageVector, color: Color = Color.DarkGray, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(text, color = color) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, null, tint = color) },
        modifier = Modifier.padding(horizontal = 12.dp),
        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
    )
}
