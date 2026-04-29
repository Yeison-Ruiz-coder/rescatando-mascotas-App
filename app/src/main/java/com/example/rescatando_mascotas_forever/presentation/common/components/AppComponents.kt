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
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
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
        actions = {
            // Lupa eliminada
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val brandPurple = Color(0xFF673AB7)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        // Usamos etiquetas cortas para evitar deformaciones en la barra
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
                alwaysShowLabel = true, // Esto mantiene el texto siempre visible y evita que los iconos "salten"
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
                        color = if (isSelected) brandPurple else Color.Gray, 
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        fontSize = 11.sp, // Tamaño un poco más pequeño para mayor estabilidad
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = brandPurple,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color(0xFFD1C4E9).copy(alpha = 0.3f)
                )
            )
        }
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
    val darkGray = Color(0xFF333333)
    val scrollState = rememberScrollState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val navigateAndClose: (String) -> Unit = { route ->
        scope.launch {
            drawerState.close()
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
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
                Text(
                    stringResource(R.string.drawer_hello, "Yeison"), 
                    color = Color.White, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 20.sp
                )
                Text("usuario@ejemplo.com", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Items del Menú Principal - Usando gris oscuro para no seleccionados
        DrawerMenuItem(stringResource(R.string.nav_home), Icons.Default.Home, currentRoute == "home") { navigateAndClose("home") }
        DrawerMenuItem(stringResource(R.string.nav_profile), Icons.Default.Person, currentRoute == "perfil") { navigateAndClose("perfil") }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))
        
        Text(
            stringResource(R.string.drawer_section_management), 
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), 
            fontSize = 12.sp, 
            fontWeight = FontWeight.Bold, 
            color = darkGray
        )
        
        DrawerMenuItem(stringResource(R.string.nav_adopt), Icons.Default.Favorite, currentRoute == "adopciones") { navigateAndClose("adopciones") }
        DrawerMenuItem(stringResource(R.string.home_section_report), Icons.Default.AddCircle, currentRoute == "formulario_rescate", Color.Red) { navigateAndClose("formulario_rescate") }
        DrawerMenuItem(stringResource(R.string.adop_form_title), Icons.AutoMirrored.Filled.Assignment, currentRoute == "formulario_adopcion") { navigateAndClose("formulario_adopcion") }
        DrawerMenuItem(stringResource(R.string.home_section_rescues), Icons.AutoMirrored.Filled.List, currentRoute == "ultimos_rescates") { navigateAndClose("ultimos_rescates") }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))

        Text(
            stringResource(R.string.drawer_section_community), 
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), 
            fontSize = 12.sp, 
            fontWeight = FontWeight.Bold, 
            color = darkGray
        )
        
        DrawerMenuItem(stringResource(R.string.nav_events), Icons.Default.DateRange, currentRoute == "eventos") { navigateAndClose("eventos") }
        DrawerMenuItem(stringResource(R.string.drawer_item_volunteers), Icons.Default.Face, currentRoute == "rescatista_contactos") { navigateAndClose("rescatista_contactos") }
        DrawerMenuItem(stringResource(R.string.drawer_item_donations), Icons.Default.Star, currentRoute == "donaciones") { navigateAndClose("donaciones") }
        DrawerMenuItem(stringResource(R.string.drawer_item_about), Icons.Default.Info, currentRoute == "nosotros") { navigateAndClose("nosotros") }
        
        Spacer(modifier = Modifier.weight(1f))
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))

        DrawerMenuItem(stringResource(R.string.drawer_logout), Icons.AutoMirrored.Filled.ExitToApp, false, Color.Red) {
            navigateAndClose("login")
        }
    }
}

@Composable
fun DrawerMenuItem(text: String, icon: ImageVector, isSelected: Boolean, color: Color = Color(0xFF333333), onClick: () -> Unit) {
    val brandPurple = Color(0xFF673AB7)
    NavigationDrawerItem(
        label = { 
            Text(
                text = text, 
                color = if (isSelected) brandPurple else color, 
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold
            ) 
        },
        selected = isSelected,
        onClick = onClick,
        icon = { 
            Icon(
                imageVector = icon, 
                contentDescription = null, 
                tint = if (isSelected) brandPurple else color
            ) 
        },
        modifier = Modifier.padding(horizontal = 12.dp),
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            selectedContainerColor = brandPurple.copy(alpha = 0.1f)
        )
    )
}
