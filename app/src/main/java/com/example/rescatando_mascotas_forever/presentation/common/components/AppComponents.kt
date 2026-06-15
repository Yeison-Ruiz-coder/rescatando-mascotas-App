package com.example.rescatando_mascotas_forever.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val AppMainGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF673AB7), Color(0xFF3F51B5))
)

@Composable
fun GradientHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(brush = AppMainGradient),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
            Text(text = title, fontSize = 11.sp, color = Color(0xFF555555), fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(drawerState: DrawerState, scope: CoroutineScope) {
    val brandPurple = Color(0xFF673AB7)
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.mipmap.logo_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = brandPurple
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.nav_menu),
                    tint = brandPurple
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val brandPurple = Color(0xFF673AB7)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        val items = listOf(
            Triple("home", Icons.Default.Home, stringResource(R.string.nav_home)),
            Triple("adopciones", Icons.Default.Pets, stringResource(R.string.nav_adopt)),
            Triple("formulario_rescate", Icons.Default.AddCircle, stringResource(R.string.home_section_report)),
            Triple("perfil", Icons.Default.Person, stringResource(R.string.nav_profile))
        )

        items.forEach { (route, icon, label) ->
            val isSelected = currentRoute == route
            NavigationBarItem(
                selected = isSelected,
                alwaysShowLabel = true,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(icon, null, tint = if (route == "formulario_rescate") Color.Red else if (isSelected) brandPurple else Color.Gray) },
                label = { Text(label, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                colors = NavigationBarItemDefaults.colors(selectedTextColor = brandPurple, indicatorColor = Color(0xFFD1C4E9).copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
fun AppDrawer(navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope, content: @Composable () -> Unit) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: ""
                if (currentRoute.startsWith("admin_")) {
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
    val scrollState = rememberScrollState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigateAndClose: (String) -> Unit = { route ->
        scope.launch {
            drawerState.close()
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState).background(Color.White)) {
        Box(modifier = Modifier.fillMaxWidth().background(AppMainGradient).padding(24.dp).padding(top = 24.dp)) {
            Column {
                Icon(Icons.Default.AccountCircle, null, tint = Color.White, modifier = Modifier.size(64.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text(stringResource(R.string.drawer_hello, "Yeison"), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("usuario@ejemplo.com", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }
        Spacer(Modifier.height(16.dp))
        // TEXTO EN GRIS OSCURO (#333333)
        DrawerMenuItem(stringResource(R.string.nav_home), Icons.Default.Home, currentRoute == "home") { navigateAndClose("home") }
        DrawerMenuItem(stringResource(R.string.nav_profile), Icons.Default.Person, currentRoute == "perfil") { navigateAndClose("perfil") }
        
        HorizontalDivider(Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
        DrawerSectionHeader(stringResource(R.string.drawer_section_management))
        
        DrawerMenuItem(stringResource(R.string.nav_adopt), Icons.Default.Favorite, currentRoute == "adopciones") { navigateAndClose("adopciones") }
        DrawerMenuItem(stringResource(R.string.home_section_report), Icons.Default.AddCircle, currentRoute == "formulario_rescate", Color.Red) { navigateAndClose("formulario_rescate") }
        DrawerMenuItem("Veterinarias", Icons.Default.LocalHospital, currentRoute == "veterinarias") { navigateAndClose("veterinarias") }

        Spacer(modifier = Modifier.weight(1f))
        DrawerMenuItem(stringResource(R.string.drawer_logout), Icons.AutoMirrored.Filled.ExitToApp, false, Color.Red) { navigateAndClose("login") }
    }
}

@Composable
fun AdminDrawerContent(navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val navigateAndClose: (String) -> Unit = { route ->
        scope.launch {
            drawerState.close()
            navController.navigate(route) { launchSingleTop = true }
        }
    }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF333333)).padding(24.dp).padding(top = 24.dp)) {
            Text(stringResource(R.string.admin_drawer_mode), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        Spacer(Modifier.height(16.dp))
        DrawerMenuItem(stringResource(R.string.admin_drawer_dashboard), Icons.Default.Dashboard, currentRoute == "admin_home") { navigateAndClose("admin_home") }
        DrawerMenuItem(stringResource(R.string.admin_drawer_pets), Icons.Default.Pets, currentRoute == "admin_mascotas") { navigateAndClose("admin_mascotas") }
        DrawerMenuItem(stringResource(R.string.admin_drawer_events), Icons.Default.Event, currentRoute == "admin_eventos") { navigateAndClose("admin_eventos") }
        DrawerMenuItem(stringResource(R.string.admin_drawer_donations), Icons.Default.Payments, currentRoute == "admin_donaciones") { navigateAndClose("admin_donaciones") }
        Spacer(modifier = Modifier.weight(1f))
        DrawerMenuItem("Salir Modo Admin", Icons.AutoMirrored.Filled.ExitToApp, false, Color.Red) { navigateAndClose("home") }
    }
}

@Composable
fun DrawerMenuItem(text: String, icon: ImageVector, isSelected: Boolean, color: Color = Color(0xFF333333), onClick: () -> Unit) {
    val brandPurple = Color(0xFF673AB7)
    val contentColor = if (isSelected) brandPurple else color
    val backgroundColor = if (isSelected) brandPurple.copy(alpha = 0.08f) else Color.Transparent

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 2.dp).height(48.dp).clip(RoundedCornerShape(12.dp)).clickable { onClick() },
        color = backgroundColor
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text, color = contentColor, fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold, fontSize = 14.sp)
        }
    }
}

@Composable
fun DrawerSectionHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray
    )
}
