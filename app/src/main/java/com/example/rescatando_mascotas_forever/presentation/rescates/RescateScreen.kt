package com.example.rescatando_mascotas_forever.presentation.rescates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.GradientHeader
import com.example.rescatando_mascotas_forever.presentation.common.components.AppMainGradient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescateScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val rescatesPrueba = listOf(
        RescateData(
            stringResource(R.string.mock_rescue_date_1),
            stringResource(R.string.mock_rescue_loc_1),
            stringResource(R.string.rescue_status_treatment),
            "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?q=80&w=1000&auto=format&fit=crop"
        ),
        RescateData(
            stringResource(R.string.mock_rescue_date_2),
            stringResource(R.string.mock_rescue_loc_2),
            stringResource(R.string.pet_status_available),
            "https://images.unsplash.com/photo-1543466835-00a7907e9de1?q=80&w=1000&auto=format&fit=crop"
        ),
        RescateData(
            stringResource(R.string.mock_rescue_date_3),
            stringResource(R.string.mock_rescue_loc_3),
            stringResource(R.string.rescue_status_recovery),
            "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?q=80&w=1000&auto=format&fit=crop"
        )
    )

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            topBar = {
                MainTopBar(drawerState = drawerState, scope = scope)
            },
            bottomBar = {
                AppBottomBar(navController)
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ) {
                // 1. Header Moderno
                item {
                    GradientHeader(stringResource(R.string.rescue_title))
                }

                // 2. Banner Hero
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1444212477490-ca407925329e?q=80&w=2000&auto=format&fit=crop"), 
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f))
                        )
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                stringResource(R.string.rescue_hero_title),
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                stringResource(R.string.rescue_hero_desc),
                                color = Color.White.copy(0.8f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // 3. Título de sección
                item {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            stringResource(R.string.rescue_subtitle),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF2E1A7A)
                        )
                        Text(
                            stringResource(R.string.rescue_subtitle_desc),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }

                // 4. Lista de Tarjetas Modernas
                items(rescatesPrueba) { rescate ->
                    ModernRescateCard(rescate)
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // 5. Frase destacada (Estilo Nosotros)
                item {
                    Box(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(brush = AppMainGradient)
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Star, null, tint = Color.White, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                stringResource(R.string.rescue_quote),
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                stringResource(R.string.rescue_quote_desc),
                                color = Color.White.copy(0.9f),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                // 6. Sección de Acción
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(R.string.rescue_action_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E1A7A)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = { navController.navigate("formulario_rescate") },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E1A7A)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.Edit, null , tint = Color.White, modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.rescue_btn_report), fontWeight = FontWeight.Bold , color = Color.White)
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedButton(
                            onClick = { navController.navigate("registro_rescatista") },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, Color(0xFF2E1A7A))
                        ) {
                            Text(stringResource(R.string.rescue_btn_join), color = Color(0xFF2E1A7A), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModernRescateCard(rescate: RescateData) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(rescate.imageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(220.dp),
                    contentScale = ContentScale.Crop
                )
                // Badge de estado
                Surface(
                    modifier = Modifier.padding(16.dp).align(Alignment.TopStart),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.9f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF4CAF50)))
                        Spacer(Modifier.width(8.dp))
                        Text(rescate.estado, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }
            
            Column(modifier = Modifier.padding(20.dp)) {
                RescateDetailRow(Icons.Default.DateRange, rescate.fecha)
                Spacer(Modifier.height(8.dp))
                RescateDetailRow(Icons.Default.LocationOn, rescate.lugar)
                
                Spacer(Modifier.height(16.dp))
                
                TextButton(
                    onClick = { },
                    modifier = Modifier.align(Alignment.End),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(stringResource(R.string.rescue_card_history), color = Color(0xFF2E1A7A), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RescateDetailRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 14.sp, color = Color.DarkGray)
    }
}

data class RescateData(val fecha: String, val lugar: String, val estado: String, val imageUrl: String)
