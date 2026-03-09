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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
                DrawerContent(navController)
            }
        }
    ) {
        content()
    }
}

@Composable
fun DrawerContent(navController: NavHostController) {
    val scrollState = rememberScrollState()
    
    var expandedMascotas by remember { mutableStateOf(false) }
    var expandedRescates by remember { mutableStateOf(false) }
    var expandedFormularios by remember { mutableStateOf(false) }
    var expandedRescatistas by remember { mutableStateOf(false) }
    var expandedDonacion by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DrawerCircleIcon(Icons.Default.Menu)
            DrawerCircleIcon(Icons.Default.Notifications)
            DrawerCircleIcon(Icons.Default.Person)
        }

        Spacer(modifier = Modifier.height(30.dp))

        DrawerItem("INICIO") { navController.navigate("home") }
        DrawerItem("ADOPTA") { navController.navigate("adopciones") }
        DrawerItem("REPORTA") { }
        DrawerItem("RESCATA") { }

        ExpandableDrawerItem("MASCOTAS", expandedMascotas, { expandedMascotas = !expandedMascotas }) {
            SubDrawerItem("- REPORTAR")
            SubDrawerItem("- BUSCAR")
            SubDrawerItem("- ACTUALIZAR")
        }

        ExpandableDrawerItem("RESCATES", expandedRescates, { expandedRescates = !expandedRescates }) {
            SubDrawerItem("- ACERCA DE")
            SubDrawerItem("- ULTIMOS RESCATES") { navController.navigate("ultimos_rescates") }
        }

        ExpandableDrawerItem("FORMULARIOS", expandedFormularios, { expandedFormularios = !expandedFormularios }) {
            SubDrawerItem("- ADOPCION")
            SubDrawerItem("- RESCATES")
            SubDrawerItem("- RESCATISTAS")
        }

        DrawerItem("SOLICITUDES") { }

        ExpandableDrawerItem("RESCATISTAS", expandedRescatistas, { expandedRescatistas = !expandedRescatistas }) {
            SubDrawerItem("- REGISTRARSE")
            SubDrawerItem("- CONTACTOS")
            SubDrawerItem("- FORMULARIO")
        }

        ExpandableDrawerItem("DONACION", expandedDonacion, { expandedDonacion = !expandedDonacion }) {
            SubDrawerItem("- UNICA")
            SubDrawerItem("- SUMINISTROS")
        }

        DrawerItem("SUSCRIBETE") { }
        DrawerItem("NOSOTROS") { navController.navigate("nosotros") }

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = Color.White
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.logo_foreground),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Rescatando Mascotas Forever", color = Color(0xFFE91E63), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Sanando su historia.", color = Color.Gray, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
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
fun DrawerItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).clickable { onClick() }
    )
}

@Composable
fun ExpandableDrawerItem(text: String, isExpanded: Boolean, onToggle: () -> Unit, content: @Composable () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).clickable { onToggle() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Icon(
                if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF7B5EE1)
            )
        }
        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, bottom = 8.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SubDrawerItem(text: String, onClick: () -> Unit = {}) {
    Text(
        text = text,
        color = Color.White.copy(0.7f),
        fontSize = 13.sp,
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onClick() }
    )
}
