@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.rescatando_mascotas_forever.presentation.suscripciones

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.data.network.models.Suscripcion


@Composable
fun SuscripcionScreen(
    navController: NavHostController,
    viewModel: SuscripcionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val paymentState by viewModel.paymentState.collectAsState()
    
    var showPaymentDialog by remember { mutableStateOf(false) }
    var selectedPlan by remember { mutableStateOf<Suscripcion?>(null) }

    val mainGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF673AB7), Color(0xFF9C27B0))
    )

    LaunchedEffect(paymentState) {
        if (paymentState is PaymentUiState.Success) {
            // Se mantiene el diálogo abierto un momento para mostrar el éxito y luego se cierra
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Planes de Suscripción", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(mainGradient)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Conviértete en un Héroe",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Tu suscripción mensual nos ayuda a rescatar y alimentar a más peluditos.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center
                )

                when (val state = uiState) {
                    is SuscripcionUiState.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }
                    is SuscripcionUiState.Success -> {
                        LazyColumn(
                            contentPadding = PaddingValues(24.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.planes) { plan ->
                                PlanCard(plan) {
                                    selectedPlan = plan
                                    showPaymentDialog = true
                                }
                            }
                        }
                    }
                    is SuscripcionUiState.Error -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(state.message, color = Color.Red)
                        }
                    }
                }
            }
        }
    }

    if (showPaymentDialog && selectedPlan != null) {
        SimulatedPaymentDialog(
            plan = selectedPlan!!,
            paymentState = paymentState,
            onDismiss = { 
                showPaymentDialog = false
                viewModel.resetPaymentState()
            },
            onConfirm = { cardNumber ->
                viewModel.procesarPagoSimulado(selectedPlan!!, cardNumber)
            }
        )
    }
}

@Composable
fun VisualCreditCard(number: String, expiry: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .shadow(12.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E1A7A))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Chip decorativo
            Surface(
                modifier = Modifier.padding(24.dp).size(40.dp, 30.dp),
                color = Color(0xFFFFD700).copy(alpha = 0.8f),
                shape = RoundedCornerShape(4.dp)
            ) {}

            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = if (number.isEmpty()) "**** **** **** ****" else number.chunked(4).joinToString(" "),
                    color = Color.White,
                    fontSize = 20.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Titular", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                        Text("USUARIO RESCATISTA", color = Color.White, fontSize = 14.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Expira", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                        Text(if (expiry.isEmpty()) "MM/YY" else expiry, color = Color.White, fontSize = 14.sp)
                    }
                }
            }
            
            // Icono de red de tarjeta (simulado)
            Icon(
                Icons.Default.CreditCard,
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopEnd).padding(24.dp).size(32.dp),
                tint = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun SimulatedPaymentDialog(
    plan: Suscripcion,
    paymentState: PaymentUiState,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvc by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { if (paymentState !is PaymentUiState.Loading) onDismiss() },
        modifier = Modifier.fillMaxWidth(0.95f),
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (paymentState is PaymentUiState.Success) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 32.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(80.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("¡PAGO CONFIRMADO!", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF2E7D32))
                            Text("Gracias por tu apoyo mensual.", color = Color.Gray)
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) {
                                Text("VOLVER AL INICIO")
                            }
                        }
                    } else {
                        Text("Finalizar Suscripción", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("${plan.nombrePlan} • $${plan.precio}/mes", fontSize = 14.sp, color = Color.Gray)
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        VisualCreditCard(cardNumber, expiry)
                        
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { if (it.length <= 16) cardNumber = it },
                            label = { Text("Número de Tarjeta") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = { Icon(Icons.Default.Payment, null) },
                            shape = RoundedCornerShape(12.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = expiry,
                                onValueChange = { if (it.length <= 5) expiry = it },
                                label = { Text("MM/YY") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = cvc,
                                onValueChange = { if (it.length <= 3) cvc = it },
                                label = { Text("CVC") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        if (paymentState is PaymentUiState.Loading) {
                            CircularProgressIndicator()
                            Text("Procesando pago seguro...", modifier = Modifier.padding(top = 8.dp), fontSize = 12.sp, color = Color.Gray)
                        } else {
                            Button(
                                onClick = { onConfirm(cardNumber) },
                                enabled = cardNumber.length >= 15 && expiry.length >= 4,
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                            ) {
                                Text("PAGAR $${plan.precio}", fontWeight = FontWeight.Bold)
                            }
                            TextButton(onClick = onDismiss, modifier = Modifier.padding(top = 8.dp)) {
                                Text("CANCELAR", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun PlanCard(plan: Suscripcion, onSubscribe: () -> Unit) {
    val planColor = try {
        Color(android.graphics.Color.parseColor(plan.colorHex ?: "#673AB7"))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = plan.nombrePlan, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = planColor)
                    Text(text = plan.descripcion, fontSize = 12.sp, color = Color.Gray)
                }
                Surface(color = planColor.copy(alpha = 0.1f), shape = CircleShape, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Default.Star, null, tint = planColor, modifier = Modifier.padding(12.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = "$${plan.precio}", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2E1A7A))
                Text(text = "/mes", fontSize = 16.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp, start = 4.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            plan.beneficios.forEach { beneficio ->
                Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = beneficio, fontSize = 14.sp, color = Color.DarkGray)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onSubscribe, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = planColor)) {
                Text("ELEGIR PLAN", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
