package com.example.rescatando_mascotas_forever.presentation.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
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
import com.example.rescatando_mascotas_forever.ui.theme.*
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    drawerState: DrawerState,
    scope: CoroutineScope,
    onTitleClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val isDark = ThemeController.isDarkOverride.value ?: isSystemInDarkTheme()
    val containerColor = if (isDark) StaticWhite else StaticBlack
    val brandPurple = WebPrimary

    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onTitleClick() }
            ) {
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 0.dp
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.logo_foreground),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
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
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = containerColor
        ),
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun AppBottomBar(navController: NavHostController, onReselect: (() -> Unit)? = null) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val brandPurple = WebPrimary

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 12.dp,
        modifier = Modifier.height(80.dp)
    ) {
        val items = listOf(
            Triple("home", Icons.Default.Home, stringResource(R.string.nav_home)),
            Triple("adopciones", Icons.Default.Pets, stringResource(R.string.nav_adopt)),
            Triple("formulario_rescate", Icons.Default.AddCircle, "Reportar"),
            Triple("perfil", Icons.Default.Person, stringResource(R.string.nav_profile))
        )

        items.forEach { (route, icon, label) ->
            val isSelected = currentRoute == route
            NavigationBarItem(
                selected = isSelected,
                alwaysShowLabel = true,
                onClick = {
                    if (currentRoute == route) {
                        onReselect?.invoke()
                    } else {
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (route == "formulario_rescate") WebDanger else if (isSelected) brandPurple else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = brandPurple,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = brandPurple.copy(alpha = 0.1f)
                )
            )
        }
    }
}

@Composable
fun AppDrawer(navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope, content: @Composable () -> Unit) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(310.dp), drawerContainerColor = MaterialTheme.colorScheme.surface) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: ""
                
                when {
                    currentRoute.startsWith("admin_") -> AdminDrawerContent(navController, drawerState, scope)
                    currentRoute.startsWith("foundation_") -> FoundationDrawerContent(navController, drawerState, scope)
                    else -> DrawerContent(navController, drawerState, scope)
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

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val user by SessionManager.userFlow.collectAsState()
    val userName = user?.nombre ?: "Usuario"

    val avatarUrl = user?.avatar?.let {
        if (it.startsWith("http") || it.startsWith("content://") || it.startsWith("file://") || it.startsWith("/")) it
        else "${com.example.rescatando_mascotas_forever.utils.Constants.BASE_URL}storage/$it"
    }

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

    Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState).background(MaterialTheme.colorScheme.surface)) {
        Box(modifier = Modifier.fillMaxWidth().background(AppMainGradient).padding(24.dp).padding(top = 24.dp)) {
            Column {
                if (avatarUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUrl),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.AccountCircle,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    stringResource(R.string.drawer_hello, userName),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(user?.email ?: "Bienvenido a Rescatando Mascotas", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }
        Spacer(Modifier.height(16.dp))

        DrawerMenuItem(stringResource(R.string.nav_home), Icons.Default.Home, currentRoute == "home") { navigateAndClose("home") }
        DrawerMenuItem(stringResource(R.string.nav_profile), Icons.Default.Person, currentRoute == "perfil") { navigateAndClose("perfil") }

        HorizontalDivider(Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
        DrawerSectionHeader("COMUNIDAD Y APOYO")

        DrawerMenuItem("Solicitud Adopción", Icons.Default.Pets, currentRoute == "adopciones") { navigateAndClose("adopciones") }
        DrawerMenuItem("Suscripciones", Icons.Default.CardMembership, currentRoute == "suscripciones") { navigateAndClose("suscripciones") }
        DrawerMenuItem("Donaciones", Icons.Default.VolunteerActivism, currentRoute == "donaciones") { navigateAndClose("donaciones") }

        HorizontalDivider(Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
        DrawerSectionHeader("SERVICIOS")

        DrawerMenuItem("Eventos", Icons.Default.Event, currentRoute == "eventos") { navigateAndClose("eventos") }
        DrawerMenuItem("Veterinarias", Icons.Default.LocalHospital, currentRoute == "veterinarias") { navigateAndClose("veterinarias") }

        HorizontalDivider(Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
        DrawerSectionHeader("INFORMACIÓN")
        DrawerMenuItem("Nosotros", Icons.Default.Info, currentRoute == "nosotros") { navigateAndClose("nosotros") }
        DrawerMenuItem("Configuración", Icons.Default.Settings, currentRoute == "configuracion") { navigateAndClose("configuracion") }

        Spacer(modifier = Modifier.height(24.dp))
        if (user == null) {
            DrawerMenuItem("Iniciar Sesión", Icons.AutoMirrored.Filled.Login, false, WebPrimary) { navigateAndClose("login") }
        } else {
            DrawerMenuItem(stringResource(R.string.drawer_logout), Icons.AutoMirrored.Filled.ExitToApp, false, Color.Red) {
                scope.launch {
                    sessionManager.logout()
                    drawerState.close()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FoundationDrawerContent(navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope) {
    val scrollState = rememberScrollState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val user by SessionManager.userFlow.collectAsState()
    val userName = user?.nombre ?: "Fundación"
    
    val avatarUrl = user?.avatar?.let {
        if (it.startsWith("http") || it.startsWith("content://") || it.startsWith("file://") || it.startsWith("/")) it
        else "${com.example.rescatando_mascotas_forever.utils.Constants.BASE_URL}storage/$it"
    }

    val navigateAndClose: (String) -> Unit = { route ->
        scope.launch {
            drawerState.close()
            navController.navigate(route) {
                launchSingleTop = true
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState).background(MaterialTheme.colorScheme.surface)) {
        // Header estilo Imagen Enviada (Cambiado a Gradiente Púrpura/Azul)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppMainGradient)
                .padding(24.dp)
                .padding(top = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (avatarUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUrl),
                        contentDescription = "Logo",
                        modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White.copy(0.2f)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Surface(modifier = Modifier.size(56.dp), shape = CircleShape, color = Color.White.copy(0.2f)) {
                        Icon(Icons.Default.Business, null, tint = Color.White, modifier = Modifier.padding(12.dp))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(userName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("fundacion", color = Color.White.copy(0.7f), fontSize = 13.sp)
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { scope.launch { drawerState.close() } }) {
                    Icon(Icons.Default.Close, null, tint = Color.White.copy(0.6f))
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Dashboard Directo
        DrawerMenuItem("Dashboard", Icons.Default.Speed, currentRoute == "foundation_home") { navigateAndClose("foundation_home") }

        // Grupos Expandibles usando WebPrimary
        ExpandableDrawerGroup(
            title = "Rescates",
            icon = Icons.Default.MedicalServices,
            items = listOf(
                "Rescates cerca de mí" to "foundation_rescates_near",
                "Mis rescates asignados" to "foundation_my_rescues"
            ),
            currentRoute = currentRoute ?: "",
            accentColor = WebPrimary,
            onItemClick = navigateAndClose
        )

        ExpandableDrawerGroup(
            title = "Mascotas",
            icon = Icons.Default.Pets,
            items = listOf(
                "Mis mascotas" to "foundation_pets",
                "Registrar mascota" to "foundation_mascota_form"
            ),
            currentRoute = currentRoute ?: "",
            accentColor = WebPrimary,
            onItemClick = navigateAndClose
        )

        ExpandableDrawerGroup(
            title = "Adopciones",
            icon = Icons.Default.Favorite,
            items = listOf(
                "Solicitudes recibidas" to "foundation_adoptions",
                "Adopciones aprobadas" to "foundation_adoptions_approved",
                "Seguimiento post-adopción" to "foundation_adoptions_followup"
            ),
            currentRoute = currentRoute ?: "",
            accentColor = WebPrimary,
            onItemClick = navigateAndClose
        )

        ExpandableDrawerGroup(
            title = "Eventos",
            icon = Icons.Default.Event,
            items = listOf(
                "Mis eventos" to "foundation_events",
                "Crear evento" to "foundation_event_form"
            ),
            currentRoute = currentRoute ?: "",
            accentColor = WebPrimary,
            onItemClick = navigateAndClose
        )

        ExpandableDrawerGroup(
            title = "Suscripciones",
            icon = Icons.Default.VolunteerActivism,
            items = listOf(
                "Mis Suscripciones" to "foundation_subscriptions"
            ),
            currentRoute = currentRoute ?: "",
            accentColor = WebPrimary,
            onItemClick = navigateAndClose
        )

        Spacer(modifier = Modifier.weight(1f))
        
        HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
        
        DrawerMenuItem("Cerrar Sesión", Icons.AutoMirrored.Filled.ExitToApp, false, Color.Red) {
            scope.launch {
                drawerState.close()
                com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient.setToken(null)
                navController.navigate("login") { popUpTo(0) { inclusive = true } }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ExpandableDrawerGroup(
    title: String,
    icon: ImageVector,
    items: List<Pair<String, String>>,
    currentRoute: String,
    accentColor: Color = WebPrimary,
    onItemClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isAnyChildSelected = items.any { it.second == currentRoute }
    
    LaunchedEffect(currentRoute) {
        if (isAnyChildSelected) expanded = true
    }

    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 2.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { expanded = !expanded },
            color = if (isAnyChildSelected && !expanded) accentColor.copy(0.08f) else Color.Transparent
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = if (isAnyChildSelected) accentColor else MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(16.dp))
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    fontWeight = if (isAnyChildSelected) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = if (isAnyChildSelected) accentColor else MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(start = 24.dp)) {
                items.forEach { (label, route) ->
                    val isSelected = currentRoute == route
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 1.dp)
                            .height(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onItemClick(route) },
                        color = if (isSelected) accentColor.copy(0.12f) else Color.Transparent
                    ) {
                        Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(6.dp).background(if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(0.4f), CircleShape))
                            Spacer(Modifier.width(16.dp))
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDrawerContent(navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigateAndClose: (String) -> Unit = { route ->
        scope.launch {
            drawerState.close()
            if (currentRoute != route) {
                navController.navigate(route) {
                    launchSingleTop = true
                }
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(MaterialTheme.colorScheme.surface)) {
        Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF333333)).padding(24.dp).padding(top = 24.dp)) {
            Text(stringResource(R.string.admin_drawer_mode), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        DrawerMenuItem("Dashboard", Icons.Default.Dashboard, isSelected = currentRoute == "admin_home") { navigateAndClose("admin_home") }
        DrawerMenuItem("Usuarios", Icons.Default.Group, isSelected = currentRoute == "admin_usuarios") { navigateAndClose("admin_usuarios") }
        DrawerMenuItem("Mascotas", Icons.Default.Pets, isSelected = currentRoute == "admin_mascotas") { navigateAndClose("admin_mascotas") }
        DrawerMenuItem("Suscripciones", Icons.Default.CardMembership, isSelected = currentRoute == "admin_suscripciones") { navigateAndClose("admin_suscripciones") }
        DrawerMenuItem("Reportes Rescate", Icons.Default.Warning, isSelected = currentRoute == "admin_reportes_rescate") { navigateAndClose("admin_reportes_rescate") }
        DrawerMenuItem("Eventos", Icons.Default.Event, isSelected = currentRoute == "admin_eventos") { navigateAndClose("admin_eventos") }

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        DrawerMenuItem("Cerrar Sesión", Icons.AutoMirrored.Filled.ExitToApp, isSelected = false, color = Color.Red) {
            scope.launch {
                drawerState.close()
                com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient.setToken(null)
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DrawerMenuItem(text: String, icon: ImageVector, isSelected: Boolean, color: Color = MaterialTheme.colorScheme.onSurface, onClick: () -> Unit) {
    val brandPurple = WebPrimary
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
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
