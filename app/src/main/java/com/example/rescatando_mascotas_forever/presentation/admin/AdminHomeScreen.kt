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

    AppDrawer(navController, drawerState, scope) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            containerColor = Color(0xFFF8F9FE)
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                when (val state = uiState) {
                    is AdminHomeState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = Color(0xFF673AB7))
                    is AdminHomeState.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 32.dp)
                        ) {
                            // --- 1. HEADER PÚRPURA PREMIUM ---
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Brush.verticalGradient(listOf(Color(0xFF673AB7), Color(0xFF4C35A3))))
                                        .padding(horizontal = 24.dp, vertical = 32.dp)
                                ) {
                                    Column {
                                        StatusLiveBadge()
                                        Spacer(Modifier.height(8.dp))
                                        Text("Panel Administrativo", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White)
                                        Text("Analítica avanzada desde Railway Production", fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
                                    }
                                }
                            }

                            // --- 2. KPI GRID (MÉTRICAS) ---
                            item {
                                Row(
                                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    MetricCardSenior("Pets", state.stats.totalMascotas.toString(), state.stats.mascotasTrend ?: "0%", Icons.Default.Pets, Color(0xFF10B981), Modifier.weight(1f))
                                    MetricCardSenior("Rescues", state.stats.totalRescates.toString(), state.stats.rescatesTrend ?: "0%", Icons.Default.Warning, Color(0xFFF43F5E), Modifier.weight(1f))
                                    MetricCardSenior("Adoptions", state.stats.totalAdopciones.toString(), state.stats.adopcionesTrend ?: "0%", Icons.Default.CheckCircle, Color(0xFF3B82F6), Modifier.weight(1f))
                                }
                            }

                            // --- 3. GRÁFICA DE TENDENCIA (RESCATES) ---
                            item {
                                AnalyticsSectionCard("Impacto Semanal (Rescates)", Icons.AutoMirrored.Filled.ShowChart) {
                                    SeniorAreaChart(state.stats.rescueHistory ?: emptyList())
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
                                            SeniorDonutChart(state.stats.speciesDistribution ?: emptyList())
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
                    is AdminHomeState.Error -> ErrorLayout(state.message) { viewModel.fetchDashboardData() }
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
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Box(Modifier.size(36.dp).background(color.copy(0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.weight(1f))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontSize = 11.sp, color = Color(0xFF64748B), modifier = Modifier.weight(1f))
                Text(trend, fontSize = 10.sp, color = if(trend.startsWith("+")) Color(0xFF10B981) else Color.Red, fontWeight = FontWeight.Bold)
            }
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

@Composable
fun SeniorAreaChart(data: List<MonthlyData>) {
    val points = if(data.isEmpty()) listOf(MonthlyData("L", 10f), MonthlyData("M", 25f), MonthlyData("X", 15f), MonthlyData("J", 35f), MonthlyData("V", 28f)) else data
    val maxValue = points.maxByOrNull { it.value }?.value ?: 1f
    
    Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
        val path = Path()
        val space = size.width / (points.size - 1).coerceAtLeast(1)
        
        points.forEachIndexed { i, item ->
            val x = i * space
            val y = size.height - (item.value / maxValue) * size.height
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        
        drawPath(path, color = Color(0xFF673AB7), style = Stroke(width = 6f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        
        // Fill Area
        val fillPath = Path().apply {
            addPath(path)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(fillPath, brush = Brush.verticalGradient(listOf(Color(0xFF673AB7).copy(0.2f), Color.Transparent)))
    }
}

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
fun ActionCardPro(title: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(52.dp).clickable { onClick() },
        color = Color.White, shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Row(Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(32.dp).background(color.copy(0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
            }
            Spacer(Modifier.width(10.dp))
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
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
