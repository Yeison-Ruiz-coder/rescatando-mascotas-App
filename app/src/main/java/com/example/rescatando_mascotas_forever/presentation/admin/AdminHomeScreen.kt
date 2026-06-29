package com.example.rescatando_mascotas_forever.presentation.admin

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.data.network.api.MonthlyData
import com.example.rescatando_mascotas_forever.data.network.api.SpeciesDistribution
import com.example.rescatando_mascotas_forever.presentation.common.components.*
import com.example.rescatando_mascotas_forever.utils.ReportGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    navController: NavHostController,
    viewModel: AdminHomeViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val adminGradient = Brush.verticalGradient(
        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.width(300.dp)
            ) {
                AdminDrawerContent(navController, drawerState, scope)
            }
        }
    ) {
        Scaffold(
            topBar = {
                MainTopBar(drawerState = drawerState, scope = scope)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Sección
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(adminGradient)
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.admin_home_title),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.Default.VerifiedUser, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                        }
                        Text(
                            text = stringResource(R.string.admin_home_subtitle),
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    // Resumen de Estadísticas
                    Text(
                        text = stringResource(R.string.admin_home_stat_title),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(stringResource(R.string.admin_home_stat_pets), "24", Icons.Default.Pets, Color(0xFF4CAF50), Modifier.weight(1f))
                        StatCard(stringResource(R.string.admin_home_stat_rescues), "12", Icons.Default.Warning, Color(0xFFF44336), Modifier.weight(1f))
                        StatCard(stringResource(R.string.admin_home_stat_adoptions), "8", Icons.Default.CheckCircle, Color(0xFF2196F3), Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Acciones Rápidas (Panel de Control)
                    Text(
                        text = stringResource(R.string.admin_home_actions_title),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                            // --- 3. GRÁFICA DE TENDENCIA (RESCATES) ---
                            item {
                                AnalyticsSectionCard("Impacto Semanal (Rescates)", Icons.AutoMirrored.Filled.ShowChart) {
                                    SeniorAreaChart(state.stats.rescueHistory)
                                }
                            }

                            // --- 4. DISTRIBUCIÓN Y REPORTES ---
                            item {
                                Row(
                                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Card(
                                        modifier = Modifier.weight(1.2f).height(200.dp),
                                        shape = RoundedCornerShape(28.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(2.dp)
                                    ) {
                                        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("Especies", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                                            Spacer(Modifier.weight(1f))
                                            SeniorDonutChart(state.stats.speciesDistribution)
                                            Spacer(Modifier.weight(1f))
                                        }
                                    }
                                    
                                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        ActionCardPro("Mascotas", Icons.Default.Edit, Color(0xFF673AB7)) { navController.navigate("admin_mascotas") }
                                        ActionCardPro("Usuarios", Icons.Default.Group, Color(0xFF3B82F6)) { navController.navigate("admin_usuarios") }
                                        ActionCardPro("Reporte", Icons.Default.PictureAsPdf, Color(0xFF10B981)) { 
                                            ReportGenerator.generateStatsPdf(context, state.stats.totalMascotas, state.stats.totalRescates, state.stats.totalAdopciones) 
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Botón de Modo Usuario para Vista Previa
                    OutlinedButton(
                        onClick = { navController.navigate("home") },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.RemoveRedEye, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.admin_home_btn_preview), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusLiveBadge() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "alpha"
    )
    Surface(color = Color.White.copy(0.15f), shape = RoundedCornerShape(100.dp)) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).alpha(alpha).background(Color(0xFF10B981), CircleShape))
            Spacer(Modifier.width(8.dp))
            Text("LIVE RAILWAY DATABASE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MetricCardSenior(title: String, value: String, trend: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.size(45.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = action.title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = action.description ?: "",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun AnalyticsSectionCard(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = Color(0xFF673AB7), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(12.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
            }
            Spacer(Modifier.height(20.dp))
            content()
        }
    }
}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // HEADER ADMIN MODERNO
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))))
                .padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            Column {
                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.25f),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color.White.copy(alpha = 0.5f))
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings, 
                            contentDescription = null, 
                            tint = Color.White, 
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.admin_drawer_mode), 
                    color = Color.White, 
                    fontWeight = FontWeight.ExtraBold, 
                    fontSize = 20.sp
                )
                Text(
                    text = "admin@rescatando.com", 
                    color = Color.White.copy(alpha = 0.85f), 
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        drawPath(path, color = Color(0xFF673AB7), style = Stroke(width = 6f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp), 
            color = MaterialTheme.colorScheme.outlineVariant
        )

@Composable
fun SeniorDonutChart(data: List<SpeciesDistribution>) {
    Canvas(modifier = Modifier.size(90.dp)) {
        var startAngle = -90f
        val total = data.sumOf { it.count }.coerceAtLeast(1)
        data.forEach { species ->
            val sweepAngle = (species.count.toFloat() / total) * 360f
            drawArc(
                color = try { Color(android.graphics.Color.parseColor(species.color)) } catch(e:Exception) { Color.Gray },
                startAngle = startAngle, sweepAngle = sweepAngle, useCenter = false,
                style = Stroke(width = 25f, cap = StrokeCap.Round)
            )
            startAngle += sweepAngle
        }
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
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ErrorLayout(msg: String, onRetry: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.CloudOff, null, Modifier.size(48.dp), tint = Color.Gray)
        Text("Fallo en sincronización Railway", fontWeight = FontWeight.Bold)
        Button(onClick = onRetry, modifier = Modifier.padding(top = 16.dp)) { Text("Reintentar") }
    }
}
