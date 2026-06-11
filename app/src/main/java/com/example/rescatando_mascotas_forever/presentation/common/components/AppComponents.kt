package com.example.rescatando_mascotas_forever.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import com.example.rescatando_mascotas_forever.data.local.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

<<<<<<< HEAD
val AppMainGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF9C27B0), Color(0xFF3F51B5))
=======
val AppMainGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF673AB7), Color(0xFF3F51B5))
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc
)

@Composable
fun GradientHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(AppMainGradient),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(drawerState: DrawerState, scope: CoroutineScope) {
    val brandPurple = Color(0xFF673AB7)
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
<<<<<<< HEAD
                Surface(modifier = Modifier.size(35.dp), shape = CircleShape, color = Color.White) {
                    Image(painter = painterResource(id = R.mipmap.logo_foreground), contentDescription = null)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text("Rescatando Mascotas", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF673AB7))
=======
                Image(
                    painter = painterResource(id = R.mipmap.logo_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = brandPurple
                )
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc
            }
        },
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
<<<<<<< HEAD
                Icon(Icons.Default.Menu, "Menú", tint = Color(0xFF673AB7))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
=======
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.nav_menu),
                    tint = brandPurple
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White
        )
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc
    )
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val brandPurple = Color(0xFF673AB7)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

<<<<<<< HEAD
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
=======
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple("home", Icons.Default.Home, "Inicio"),
            Triple("adopciones", Icons.Default.Pets, "Adoptar"),
            Triple("formulario_rescate", Icons.Default.AddCircle, "Reportar"),
            Triple("perfil", Icons.Default.Person, "Perfil")
        )

        items.forEach { (route, icon, label) ->
            val isSelected = currentRoute == route
            NavigationBarItem(
                selected = isSelected,
                alwaysShowLabel = true,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { 
                    Icon(
                        imageVector = icon, 
                        contentDescription = null,
                        tint = if (route == "formulario_rescate") Color.Red else if (isSelected) brandPurple else Color.Gray
                    ) 
                },
                label = { 
                    Text(
                        text = label, 
                        color = if (route == "formulario_rescate") Color.Red else if (isSelected) brandPurple else Color.Gray,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = if (route == "formulario_rescate") Color.Red else brandPurple,
                    selectedTextColor = if (route == "formulario_rescate") Color.Red else brandPurple,
                    unselectedIconColor = if (route == "formulario_rescate") Color.Red.copy(alpha = 0.6f) else Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = brandPurple.copy(alpha = 0.1f)
                )
            )
        }
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc
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
<<<<<<< HEAD
=======
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val user = remember { sessionManager.getUser() }
    
    val userName = user?.nombre ?: "Usuario"
    val userEmail = user?.email ?: "usuario@ejemplo.com"

    val scrollState = rememberScrollState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc
    val navigateAndClose: (String) -> Unit = { route ->
        scope.launch {
            drawerState.close()
            if (currentRoute != route) {
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }

<<<<<<< HEAD
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
=======
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        // HEADER MODERNO
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppMainGradient)
                .padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            Column {
                // AVATAR PREMIUM CON EFECTO DE LUZ
                Box(contentAlignment = Alignment.Center) {
                    // Círculo de fondo (brillo)
                    Surface(
                        modifier = Modifier.size(84.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.15f)
                    ) {}
                    
                    Surface(
                        modifier = Modifier.size(72.dp),
                        shape = CircleShape,
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(2.dp, Color.White),
                        shadowElevation = 8.dp
                    ) {
                        if (!user?.avatar.isNullOrEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(user?.avatar),
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                contentAlignment = Alignment.Center, 
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Brush.linearGradient(listOf(Color(0xFFBBDEFB), Color(0xFFE1BEE7))))
                            ) {
                                Text(
                                    text = userName.take(1).uppercase(),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF673AB7)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Hola, $userName", 
                    color = Color.White, 
                    fontWeight = FontWeight.ExtraBold, 
                    fontSize = 22.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = userEmail, 
                    color = Color.White.copy(alpha = 0.85f), 
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // SECCIONES DE NAVEGACIÓN
        DrawerSectionHeader("PRINCIPAL")
        DrawerMenuItem("Inicio", Icons.Default.Home, currentRoute == "home") { navigateAndClose("home") }
        DrawerMenuItem("Perfil", Icons.Default.Person, currentRoute == "perfil") { navigateAndClose("perfil") }
        
        DrawerSectionHeader("GESTIÓN")
        DrawerMenuItem("Adoptar", Icons.Default.Pets, currentRoute == "adopciones") { navigateAndClose("adopciones") }
        DrawerMenuItem("Reportar Rescate", Icons.Default.AddCircle, currentRoute == "formulario_rescate", Color.Red) { navigateAndClose("formulario_rescate") }
        DrawerMenuItem("Solicitud Adopción", Icons.AutoMirrored.Filled.Assignment, currentRoute == "formulario_adopcion") { navigateAndClose("formulario_adopcion") }
        DrawerMenuItem("Suscripciones", Icons.Default.Star, currentRoute == "suscripciones") { navigateAndClose("suscripciones") }
        DrawerMenuItem("Últimos Rescates", Icons.AutoMirrored.Filled.List, currentRoute == "ultimos_rescates") { navigateAndClose("ultimos_rescates") }

        DrawerSectionHeader("COMUNIDAD")
        DrawerMenuItem("Eventos", Icons.Default.DateRange, currentRoute == "eventos") { navigateAndClose("eventos") }
        DrawerMenuItem("Veterinarias", Icons.Default.LocalHospital, currentRoute == "veterinarias") { navigateAndClose("veterinarias") }
        DrawerMenuItem("Voluntarios", Icons.Default.Face, currentRoute == "rescatista_contactos") { navigateAndClose("rescatista_contactos") }
        DrawerMenuItem("Donaciones", Icons.Default.Favorite, currentRoute == "donaciones") { navigateAndClose("donaciones") }
        DrawerMenuItem("Nosotros", Icons.Default.Info, currentRoute == "nosotros") { navigateAndClose("nosotros") }
        
        Spacer(modifier = Modifier.weight(1f))
        
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp), 
            color = Color.LightGray.copy(alpha = 0.4f)
        )

        DrawerMenuItem("Cerrar Sesión", Icons.AutoMirrored.Filled.ExitToApp, false, Color(0xFFD32F2F)) {
            scope.launch {
                drawerState.close()
                sessionManager.logout()
                com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient.setToken(null)
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc
        }

        Spacer(modifier = Modifier.weight(1f))

        // FOOTER: REDES SOCIALES Y VERSIÓN
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Abrir Instagram */ }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Instagram", tint = Color(0xFFE91E63))
                }
                IconButton(onClick = { /* Abrir Facebook */ }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Facebook, contentDescription = "Facebook", tint = Color(0xFF1877F2))
                }
                IconButton(onClick = { /* Abrir Web */ }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Language, contentDescription = "Web", tint = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Versión 1.0.4 - Premium",
                fontSize = 11.sp,
                color = Color.LightGray,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
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
<<<<<<< HEAD
fun DrawerMenuItem(text: String, icon: ImageVector, color: Color = Color.DarkGray, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(text, color = color) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, null, tint = color) },
        modifier = Modifier.padding(horizontal = 12.dp),
        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
=======
fun DrawerSectionHeader(title: String) {
    Text(
        text = title, 
        modifier = Modifier
            .padding(horizontal = 28.dp, vertical = 12.dp), 
        fontSize = 11.sp, 
        fontWeight = FontWeight.ExtraBold, 
        color = Color.Gray,
        letterSpacing = 1.2.sp
>>>>>>> 5bd816f6f897ad38f7e94b1cad096ff5e47b8ffc
    )
}

@Composable
fun DrawerMenuItem(text: String, icon: ImageVector, isSelected: Boolean, color: Color = Color(0xFF444444), onClick: () -> Unit) {
    val brandPurple = Color(0xFF673AB7)
    val contentColor = if (isSelected) brandPurple else color
    val backgroundColor = if (isSelected) brandPurple.copy(alpha = 0.08f) else Color.Transparent

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon, 
                contentDescription = null, 
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text, 
                color = contentColor, 
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}
