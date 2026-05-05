package com.example.rescatando_mascotas_forever.presentation.eventos

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoScreen(
    navController: NavHostController,
    viewModel: EventoViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.state.collectAsState()

    val catAll = stringResource(R.string.event_cat_all)
    val catFree = "Gratis"
    val catContests = "Concursos"
    val catAdoptions = "Adopciones"

    val categorias = listOf(catAll, catFree, catContests, catAdoptions)
    var categoriaSeleccionada by remember { mutableStateOf(catAll) }

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { AppBottomBar(navController = navController) },
            containerColor = Color(0xFFFDF7F2)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(stringResource(R.string.event_title), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2E1A7A))
                Text(stringResource(R.string.event_subtitle), fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(categorias) { categoria ->
                        FilterChip(
                            selected = categoria == categoriaSeleccionada,
                            onClick = { categoriaSeleccionada = categoria },
                            label = { Text(categoria) },
                            leadingIcon = {
                                val icon = when(categoria) {
                                    catAll -> Icons.Default.DateRange
                                    catFree -> Icons.Default.Favorite
                                    catContests -> Icons.Default.EmojiEvents
                                    else -> Icons.Default.Pets
                                }
                                Icon(icon, null, modifier = Modifier.size(18.dp))
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF673AB7),
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (val currentState = state) {
                    is EventoState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF673AB7))
                        }
                    }
                    is EventoState.Success -> {
                        val eventosFiltrados = remember(categoriaSeleccionada, currentState.eventos) {
                            when (categoriaSeleccionada) {
                                catAll -> currentState.eventos
                                catFree -> currentState.eventos.filter { it.tipo?.contains("Gratis", ignoreCase = true) == true }
                                else -> currentState.eventos.filter { it.tipo?.contains(categoriaSeleccionada, ignoreCase = true) == true }
                            }
                        }

                        if (eventosFiltrados.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No hay eventos disponibles", color = Color.Gray)
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(20.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(eventosFiltrados) { evento ->
                                    val snackMsg = "Te has unido a ${evento.nombre}"
                                    EventCard(
                                        evento = evento,
                                        isFeatured = evento.tipo == "DESTACADO",
                                        onActionClick = {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(snackMsg)
                                            }
                                        }
                                    )
                                }
                                item { Spacer(modifier = Modifier.height(30.dp)) }
                            }
                        }
                    }
                    is EventoState.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Error al cargar eventos", color = Color.Red)
                                Button(onClick = { viewModel.getEventos() }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventCard(evento: Evento, isFeatured: Boolean = false, onActionClick: () -> Unit) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .clickable { /* Detalles */ },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(evento.imagenUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(if (isFeatured) 200.dp else 160.dp),
                    contentScale = ContentScale.Crop
                )

                // Etiqueta
                Surface(
                    modifier = Modifier.padding(12.dp).align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF673AB7)
                ) {
                    Text(evento.tipo ?: "EVENTO", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                // Botón de Favorito
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .size(36.dp)
                        .clickable { isFavorite = !isFavorite },
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.8f)
                ) {
                    Icon(
                        if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        null,
                        modifier = Modifier.padding(8.dp),
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(evento.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Event, null, tint = Color(0xFF673AB7), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(evento.fecha, fontSize = 13.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, null, tint = Color.Red, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(evento.lugar, fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(evento.tipo ?: "Gratis", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E1A7A))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(evento.descripcion ?: "Sin descripción", fontSize = 13.sp, color = Color.Gray, lineHeight = 18.sp, maxLines = 2)

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Likes: ${evento.likes ?: 0}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF673AB7))
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = onActionClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E1A7A)),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text(stringResource(R.string.event_btn_join), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
