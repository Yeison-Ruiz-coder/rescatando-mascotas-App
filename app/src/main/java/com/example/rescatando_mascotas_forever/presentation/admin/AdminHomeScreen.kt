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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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
    val lastUpdated = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()) }

    AppDrawer(navController, drawerState, scope) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dashboard Senior", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Default.Menu, "Menú", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.fetchDashboardData() }) {
                            Icon(Icons.Default.Refresh, "Refrescar", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF673AB7))
                )
            },
            containerColor = Color(0xFFF8F9FE)
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                when (val state = uiState) {
                    is AdminHomeState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = Color(0xFF673AB7))
                    is AdminHomeState.Success -> {
                        val stats = state.stats.stats // Nueva estructura de Railway
                        val graficos = state.stats.graficos

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
                                        StatusLiveBadge(lastUpdated)
                                        Spacer(Modifier.height(8.dp))
                                        Text("Panel Administrativo", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White)
                                        Text("Analítica avanzada desde Railway Production", fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
                                    }
                                }
                            }

                            // --- 2. KPI GRID (MÉTRICAS PRINCIPALES) ---
                            item {
                                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        MetricCardSenior("Mascotas", stats.mascotas.total.toString(), "${stats.mascotas.enAdopcion} libres", Icons.Default.Pets, Color(0xFF10B981), Modifier.weight(1f))
                                        MetricCardSenior("Rescates", stats.rescates.completados.toString(), null, Icons.Default.Warning, Color(0xFFF43F5E), Modifier.weight(1f))
                                        MetricCardSenior("Adopciones", stats.adopciones.totales.toString(), null, Icons.Default.CheckCircle, Color(0xFF3B82F6), Modifier.weight(1f))
                                    }

                                    Spacer(Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        MetricCardSenior("Usuarios", stats.usuarios.total.toString(), "${stats.usuarios.activos} activos", Icons.Default.Group, Color(0xFF6366F1), Modifier.weight(1f))
                                        MetricCardSenior("Entidades", (stats.usuarios.fundaciones + stats.usuarios.veterinarias).toString(), "Verificadas", Icons.Default.Verified, Color(0xFFF59E0B), Modifier.weight(1f))
                                    }
                                }
                            }

                            // --- 3. GRÁFICA DE TENDENCIA DINÁMICA ---
                            item {
                                AnalyticsSectionCard(
                                    title = "Tendencia de Adopciones",
                                    icon = Icons.AutoMirrored.Filled.ShowChart
                                ) {
                                    val graphData = graficos?.adoptionsHistory ?: emptyList()
                                    SeniorAreaChart(graphData)
                                }
                            }

                            // --- 4. DISTRIBUCIÓN Y ACCIONES ---
                            item {
                                Row(
                                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Card(
                                        modifier = Modifier.weight(1.2f).height(210.dp),
                                        shape = RoundedCornerShape(28.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(2.dp)
                                    ) {
                                        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("Distribución Especies", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                                            Spacer(Modifier.weight(1f))
                                            SeniorDonutChart(emptyList()) // API no envía distribución actualmente
                                            Spacer(Modifier.weight(1f))
                                        }
                                    }

                                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        ActionCardPro("Ver Mascotas", Icons.Default.Edit, Color(0xFF673AB7)) { navController.navigate("admin_mascotas") }
                                        ActionCardPro("Gestionar Usuarios", Icons.Default.Group, Color(0xFF3B82F6)) { navController.navigate("admin_usuarios") }
                                        ActionCardPro("Generar PDF", Icons.Default.PictureAsPdf, Color(0xFF10B981)) {
                                            ReportGenerator.generateStatsPdf(context, stats.mascotas.total, stats.rescates.completados, stats.adopciones.totales)
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
fun StatusLiveBadge(time: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "alpha"
    )
    Surface(color = Color.White.copy(0.15f), shape = RoundedCornerShape(100.dp)) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).alpha(alpha).background(Color(0xFF10B981), CircleShape))
            Spacer(Modifier.width(8.dp))
            Text("DB SYNC: $time", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MetricCardSenior(title: String, value: String, trend: String?, icon: ImageVector, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier.height(115.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Box(Modifier.size(32.dp).background(color.copy(0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
            }
            Spacer(Modifier.weight(1f))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontSize = 10.sp, color = Color(0xFF64748B), modifier = Modifier.weight(1f))
                if (trend != null) {
                    Text(trend, fontSize = 10.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AnalyticsSectionCard(title: String, icon: ImageVector, action: @Composable () -> Unit = {}, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = Color(0xFF673AB7), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(12.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B), modifier = Modifier.weight(1f))
                action()
            }
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun SeniorAreaChart(data: List<MonthlyData>) {
    val points = if(data.isEmpty()) listOf(MonthlyData("L", 0f), MonthlyData("M", 0f), MonthlyData("X", 0f), MonthlyData("J", 0f), MonthlyData("V", 0f)) else data
    val maxValue = points.maxByOrNull { it.value }?.value ?: 1f

    Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
        val path = Path()
        val space = size.width / (points.size - 1).coerceAtLeast(1)

        points.forEachIndexed { i, item ->
            val x = i * space
            val y = size.height - (item.value / (maxValue * 1.2f)) * size.height
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
    if (data.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Sin datos", fontSize = 10.sp, color = Color.Gray)
        }
        return
    }
    Canvas(modifier = Modifier.size(90.dp)) {
        var startAngle = -90f
        val total = data.sumOf { it.count }.coerceAtLeast(1)
        data.forEach { species ->
            val sweepAngle = (species.count.toFloat() / total) * 360f
            drawArc(
                color = try { Color(android.graphics.Color.parseColor(species.color)) } catch(e:Exception) { Color.LightGray },
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
        Text(msg, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(horizontal = 32.dp))
        Button(onClick = onRetry, modifier = Modifier.padding(top = 16.dp)) { Text("Reintentar") }
    }
}