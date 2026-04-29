package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioAdopcionScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var nombreCompleto by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var tieneOtrasMascotas by remember { mutableStateOf(false) }

    AppDrawer(navController = navController, drawerState = drawerState, scope = scope) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { AppBottomBar(navController = navController) }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF5F5F5))
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            stringResource(R.string.adop_form_title),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF673AB7)
                        )
                        Text(
                            stringResource(R.string.adop_form_subtitle),
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                ModernTextField(value = nombreCompleto, onValueChange = { nombreCompleto = it }, label = stringResource(R.string.adop_form_label_name), icon = Icons.Default.Person)
                                ModernTextField(value = edad, onValueChange = { edad = it }, label = stringResource(R.string.adop_form_label_age), icon = Icons.Default.DateRange)
                                ModernTextField(value = direccion, onValueChange = { direccion = it }, label = stringResource(R.string.adop_form_label_address), icon = Icons.Default.Home)
                                ModernTextField(value = telefono, onValueChange = { telefono = it }, label = stringResource(R.string.adop_form_label_phone), icon = Icons.Default.Phone)
                                
                                OutlinedTextField(
                                    value = motivo,
                                    onValueChange = { motivo = it },
                                    label = { Text(stringResource(R.string.adop_form_label_motive)) },
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    minLines = 3
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                                ) {
                                    Checkbox(checked = tieneOtrasMascotas, onCheckedChange = { tieneOtrasMascotas = it })
                                    Text(stringResource(R.string.adop_form_label_others), fontSize = 14.sp)
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Button(
                                    onClick = { /* Enviar */ },
                                    modifier = Modifier.fillMaxWidth().height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                                ) {
                                    Text(stringResource(R.string.adop_form_btn_send), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ModernTextField(value: String, onValueChange: (String) -> Unit, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = Color(0xFF673AB7)) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF673AB7),
            focusedLabelColor = Color(0xFF673AB7)
        )
    )
}
