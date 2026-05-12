package com.example.rescatando_mascotas_forever.presentation.eventos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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

    // Estado para la barra de búsqueda
    var searchText by remember { mutableStateOf("") }

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
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
            ) {
                // Encabezado
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        stringResource(R.string.event_title),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2E1A7A)
                    )
                    Text(
                        stringResource(R.string.event_subtitle),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Campo de búsqueda
                item {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar evento o lugar") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF673AB7),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Filtros de categoría
                item {
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
                                    val icon = when (categoria) {
                                        catAll -> Icons.Default.DateRange
                                        catFree -> Icons.Default.Favorite
                                        catContests -> Icons.Default.EmojiEvents
                                        else -> Icons.Default.Pets
                                    }
                                    Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
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
                }

                // Contenido según el estado del ViewModel
                when (val currentState = state) {
                    is EventoState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color(0xFF673AB7))
                            }
                        }
                    }

                    is EventoState.Success -> {
                        val eventosFiltrados = currentState.eventos.filter { evento ->
                            val matchesCat = when (categoriaSeleccionada) {
                                catAll -> true
                                catFree -> evento.tipo?.contains("Gratis", ignoreCase = true) == true
                                else -> evento.tipo?.contains(categoriaSeleccionada, ignoreCase = true) == true
                            }
                            val matchesSearch = searchText.isEmpty() ||
                                    evento.nombre.contains(searchText, ignoreCase = true) ||
                                    (evento.lugar?.contains(searchText, ignoreCase = true) == true)
                            matchesCat && matchesSearch
                        }

                        if (eventosFiltrados.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No se encontraron eventos", color = Color.Gray)
                                }
                            }
                        } else {
                            items(eventosFiltrados) { evento ->
                                EventCard(
                                    evento = evento,
                                    onDetailsClick = {
                                        navController.navigate("eventos/${evento.id}")
                                    },
                                    onActionClick = { msg ->
                                        scope.launch {
                                            snackbarHostState.showSnackbar(msg)
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }

                    is EventoState.Error -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = currentState.message, color = Color.Red)
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(30.dp)) }
            }
        }
    }
}

@Composable
fun EventCard(
    evento: Evento,
    onDetailsClick: () -> Unit,
    onActionClick: (String) -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }
    var isConfirmed by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box {
                val painter = if (!evento.imagenUrl.isNullOrEmpty()) {
                    rememberAsyncImagePainter(evento.imagenUrl)
                } else {
                    rememberAsyncImagePainter("https://via.placeholder.com/400x200?text=Sin+Imagen")
                }

                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )

                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF673AB7)
                ) {
                    Text(
                        evento.tipo ?: "EVENTO",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    evento.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Event,
                        contentDescription = null,
                        tint = Color(0xFF673AB7),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(evento.fecha, fontSize = 13.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Place,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(evento.lugar, fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        evento.tipo ?: "Gratis",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E1A7A)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    evento.descripcion ?: "Sin descripción",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDetailsClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.event_btn_details), fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isFavorite) Color.Red else Color.Gray
                        ),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            stringResource(if (isFavorite) R.string.event_btn_liked else R.string.event_btn_like),
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = {
                            isConfirmed = !isConfirmed
                            val msg = if (isConfirmed) "¡Asistencia confirmada!" else "Asistencia cancelada"
                            onActionClick(msg)
                        },
                        modifier = Modifier
                            .weight(1.2f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isConfirmed) Color(0xFF4CAF50) else Color(0xFF2E1A7A)
                        ),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Icon(
                            Icons.Outlined.EventAvailable,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            stringResource(if (isConfirmed) R.string.event_btn_confirmed else R.string.event_btn_attend),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}