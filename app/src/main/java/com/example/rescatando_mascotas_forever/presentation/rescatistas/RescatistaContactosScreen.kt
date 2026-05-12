package com.example.rescatando_mascotas_forever.presentation.rescatistas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.data.network.models.Rescatista
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescatistaContactosScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val statusAvail = stringResource(R.string.rescuer_status_avail)
    val statusBusy = stringResource(R.string.rescuer_status_busy)

    val rescatistasPrueba = listOf(
        Rescatista(
            id = 1,
            nombre = "Carlos A. Muñoz",
            fotoUrl = "https://randomuser.me/api/portraits/men/1.jpg",
            disponibilidad = stringResource(R.string.mock_rescuer_avail_1),
            municipio = "Popayán",
            barrio = stringResource(R.string.mock_location_4),
            especialidad = stringResource(R.string.mock_rescuer_spec_1),
            organizacion = "Paramédicos GER",
            estado = statusAvail,
            whatsapp = "123456789",
            telefono = "987654321"
        ),
        Rescatista(
            id = 2,
            nombre = "Diana P. Torres",
            fotoUrl = "https://randomuser.me/api/portraits/women/2.jpg",
            disponibilidad = stringResource(R.string.mock_rescuer_avail_2),
            municipio = "Popayán",
            barrio = "La Esmeralda",
            especialidad = stringResource(R.string.mock_rescuer_spec_2),
            organizacion = "Fundación Naciona...",
            estado = statusAvail,
            whatsapp = "123456789",
            telefono = "987654321"
        ),
        Rescatista(
            id = 3,
            nombre = "Julián R. López",
            fotoUrl = "https://randomuser.me/api/portraits/men/3.jpg",
            disponibilidad = stringResource(R.string.mock_rescuer_avail_3),
            municipio = "Popayán",
            barrio = "San Eduardo",
            especialidad = stringResource(R.string.mock_rescuer_spec_3),
            organizacion = "Independiente - Red...",
            estado = statusBusy,
            whatsapp = "123456789",
            telefono = "987654321"
        ),
        Rescatista(
            id = 4,
            nombre = "María Fernanda Ruiz",
            fotoUrl = "https://randomuser.me/api/portraits/women/4.jpg",
            disponibilidad = stringResource(R.string.mock_rescuer_avail_1),
            municipio = "Popayán",
            barrio = stringResource(R.string.mock_location_4),
            especialidad = stringResource(R.string.mock_rescuer_spec_4),
            organizacion = "Fundación ComeDog...",
            estado = statusBusy,
            whatsapp = "123456789",
            telefono = "987654321"
        )
    )

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { AppBottomBar(navController = navController) },
            containerColor = Color(0xFFFDF7F2)
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                item {
                    RescatistaHeader()
                }

                item {
                    RescatistaFilters()
                }

                items(rescatistasPrueba) { rescatista ->
                    RescatistaCard(rescatista)
                }
                
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun RescatistaHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF7E57C2), Color(0xFF673AB7))
                )
            )
            .padding(vertical = 32.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.rescuer_title),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.rescuer_header_desc),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun RescatistaFilters() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterDropdown(stringResource(R.string.rescuer_filter_city), Modifier.weight(1f))
            FilterDropdown(stringResource(R.string.rescuer_filter_spec), Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterDropdown(stringResource(R.string.rescuer_filter_hood), Modifier.weight(1f))
            FilterDropdown(stringResource(R.string.rescuer_filter_available), Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FilterDropdown(label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF7E57C2),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.rescuer_filter_all), color = Color.White, fontSize = 13.sp)
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun RescatistaCard(rescatista: Rescatista) {
    val statusAvail = stringResource(R.string.rescuer_status_avail)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = rescatista.nombre,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(rescatista.fotoUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(20.dp))

                Column(modifier = Modifier.weight(1.3f)) {
                    RescatistaDetail(stringResource(R.string.rescuer_detail_avail), rescatista.disponibilidad, Color(0xFF9575CD))
                    Spacer(modifier = Modifier.height(4.dp))
                    RescatistaDetail(stringResource(R.string.rescuer_detail_city), rescatista.municipio, Color(0xFF9575CD))
                    Spacer(modifier = Modifier.height(4.dp))
                    RescatistaDetail(stringResource(R.string.rescuer_detail_hood), rescatista.barrio, Color(0xFF9575CD))
                    Spacer(modifier = Modifier.height(4.dp))
                    RescatistaDetail(stringResource(R.string.rescuer_detail_spec), rescatista.especialidad, Color(0xFF9575CD))
                    Spacer(modifier = Modifier.height(4.dp))
                    RescatistaDetail(stringResource(R.string.rescuer_detail_org), rescatista.organizacion, Color(0xFF9575CD))
                }

                Column(
                    modifier = Modifier.weight(0.7f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(stringResource(R.string.rescuer_label_status), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (rescatista.estado == statusAvail) Color(0xFF7E57C2) else Color(0xFFF06292)
                    ) {
                        Text(
                            text = rescatista.estado,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))

                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { },
                        shape = CircleShape,
                        color = Color(0xFF25D366),
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Call, 
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { },
                        shape = CircleShape,
                        color = Color(0xFF7E57C2),
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Phone,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RescatistaDetail(label: String, value: String, valueColor: Color) {
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(
            text = value, 
            fontSize = 12.sp, 
            color = valueColor, 
            maxLines = 1,
            fontWeight = FontWeight.Medium
        )
    }
}
