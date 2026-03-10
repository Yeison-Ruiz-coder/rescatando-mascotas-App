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
                    modifier = Modifier.size(55.dp),
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
                Column {
                    Text(
                        "Rescatando Mascotas Forever",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFFB388FF)
                    )
                    Text(
                        "Sanando su historia.",
                        fontSize = 13.sp,
                        color = Color(0xFF2E1A7A)
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = { scope.launch { drawerState.open() } },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun GradientHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(brush = AppMainGradient),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = AppMainGradient)
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            val itemColors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.White.copy(alpha = 0.4f),
                unselectedTextColor = Color.White.copy(alpha = 0.4f),
                indicatorColor = Color.Transparent // Estilo limpio sin el óvalo de fondo
            )

            NavigationBarItem(
                selected = currentRoute == "home",
                onClick = { if (currentRoute != "home") navController.navigate("home") },
                icon = { Icon(Icons.Default.Home, null) },
                label = { Text("Inicio") },
                colors = itemColors,
                alwaysShowLabel = true
            )
            NavigationBarItem(
                selected = currentRoute == "adopciones" || currentRoute == "formulario_adopcion",
                onClick = { if (currentRoute != "adopciones") navController.navigate("adopciones") },
                icon = { Icon(Icons.Default.Search, null) },
                label = { Text("Buscar") },
                colors = itemColors,
                alwaysShowLabel = true
            )
            NavigationBarItem(
                selected = currentRoute == "formulario_rescate",
                onClick = { if (currentRoute != "formulario_rescate") navController.navigate("formulario_rescate") },
                icon = { 
                    Icon(
                        Icons.Default.Warning, 
                        null, 
                        tint = if (currentRoute == "formulario_rescate") 
                            Color.Red else Color.Red.copy(alpha = 0.5f),
                        modifier = Modifier.size(28.dp)
                    ) 
                },
                label = { Text("Reportar") },
                colors = itemColors,
                alwaysShowLabel = true
            )
            NavigationBarItem(
                selected = currentRoute == "perfil",
                onClick = { },
                icon = { Icon(Icons.Default.Person, null) },
                label = { Text("Perfil") },
                colors = itemColors,
                alwaysShowLabel = true
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
                drawerContainerColor = Color(0xFF121232),
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
    
    var expandedMascotas by remember { mutableStateOf(false) }
    var expandedRescates by remember { mutableStateOf(false) }
    var expandedFormularios by remember { mutableStateOf(false) }
    var expandedRescatistas by remember { mutableStateOf(false) }
    var expandedDonacion by remember { mutableStateOf(false) }

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
            .background(Color(0xFF121232))
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {
        // Cabecera del Menú
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DrawerCircleIcon(Icons.Default.Person)
            Column {
                Text("Bienvenido", color = Color.White.copy(0.6f), fontSize = 12.sp)
                Text("Usuario Pet Lover", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // SECCIÓN 1: ACCIONES PRIORITARIAS
        MenuSectionTitle("ACCIONES")
        DrawerItem("ADOPTA", Icons.Default.Favorite) { navigateAndClose("adopciones") }
        DrawerItem("ULTIMOS RESCATES", Icons.Default.Warning) { navigateAndClose("ultimos_rescates") }
        DrawerItem("RESCATA", Icons.Default.Add) { }
        
        HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color.White.copy(0.1f))

        // SECCIÓN 2: COMUNIDAD Y EVENTOS
        MenuSectionTitle("COMUNIDAD")
        DrawerItem("EVENTOS", Icons.Default.DateRange) { navigateAndClose("eventos") }
        ExpandableDrawerItem("RESCATISTAS", Icons.Default.Face, expandedRescatistas, { expandedRescatistas = !expandedRescatistas }) {
            SubDrawerItem("- REGISTRARSE") { navigateAndClose("registro_rescatista") }
            SubDrawerItem("- CONTACTOS") { navigateAndClose("rescatista_contactos") }
            SubDrawerItem("- FORMULARIO") { navigateAndClose("registro_rescatista") }
        }

        HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color.White.copy(0.1f))

        // SECCIÓN 3: GESTIÓN Y FORMULARIOS
        MenuSectionTitle("GESTIÓN")
        ExpandableDrawerItem("FORMULARIOS", Icons.Default.Edit, expandedFormularios, { expandedFormularios = !expandedFormularios }) {
            SubDrawerItem("- ADOPCION") { navigateAndClose("formulario_adopcion") }
            SubDrawerItem("- RESCATES") { navigateAndClose("encuesta_rescate") }
            SubDrawerItem("- RESCATISTAS") { navigateAndClose("registro_rescatista") }
        }
        ExpandableDrawerItem("MASCOTAS", Icons.Default.Search, expandedMascotas, { expandedMascotas = !expandedMascotas }) {
            SubDrawerItem("- REPORTAR")
            SubDrawerItem("- PROCESO") { navigateAndClose("proceso_adopcion") }
            SubDrawerItem("- ACTUALIZAR")
        }
        ExpandableDrawerItem("RESCATES", Icons.Default.Info, expandedRescates, { expandedRescates = !expandedRescates }) {
            SubDrawerItem("- ACERCA DE")
            SubDrawerItem("- ULTIMOS RESCATES") { navigateAndClose("ultimos_rescates") }
        }

        HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color.White.copy(0.1f))

        // SECCIÓN 4: OTROS
        MenuSectionTitle("OTROS")
        ExpandableDrawerItem("DONACIÓN", Icons.Default.ShoppingCart, expandedDonacion, { expandedDonacion = !expandedDonacion }) {
            SubDrawerItem("- UNICA")
            SubDrawerItem("- SUMINISTROS")
        }
        DrawerItem("SUSCRÍBETE", Icons.Default.Email) { }
        DrawerItem("NOSOTROS", Icons.Default.Info) { navigateAndClose("nosotros") }

        Spacer(modifier = Modifier.height(40.dp))
        
        // Footer con Logo
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Surface(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                color = Color.White
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.logo_foreground),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Text("Sanando su historia", color = Color.White.copy(0.5f), fontSize = 10.sp, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun MenuSectionTitle(text: String) {
    Text(
        text = text,
        color = Color(0xFFB388FF),
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun DrawerCircleIcon(icon: ImageVector) {
    Surface(
        modifier = Modifier.size(45.dp),
        shape = CircleShape,
        color = Color.White.copy(0.1f)
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.padding(10.dp))
    }
}

@Composable
fun DrawerItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.White.copy(0.7f), modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(16.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ExpandableDrawerItem(text: String, icon: ImageVector, isExpanded: Boolean, onToggle: () -> Unit, content: @Composable () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color.White.copy(0.7f), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(16.dp))
                Text(text = text, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Icon(
                if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFFB388FF),
                modifier = Modifier.size(20.dp)
            )
        }
        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.fillMaxWidth().padding(start = 36.dp, bottom = 8.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SubDrawerItem(text: String, onClick: () -> Unit = {}) {
    Text(
        text = text,
        color = Color.White.copy(0.6f),
        fontSize = 13.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    )
}
