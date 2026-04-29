package com.example.rescatando_mascotas_forever.presentation.rescates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
fun FormularioRescateScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Estados del formulario
    var nombreMascota by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("M") } // "M" o "H"
    var estadoSalud by remember { mutableStateOf("") }
    var refugioAsignado by remember { mutableStateOf("") }

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
                    .background(Color(0xFFFDF7F2)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 24.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color(0xFFD1C4E9), Color(0xFF9575CD))
                                    )
                                )
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFD54F)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Face,
                                    contentDescription = null,
                                    modifier = Modifier.size(100.dp),
                                    tint = Color.White.copy(alpha = 0.5f)
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = { },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4527A0)),
                                modifier = Modifier.fillMaxWidth(0.8f)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AddCircle, null, tint = Color.White)
                                    Spacer(Modifier.width(8.dp))
                                    Text(stringResource(R.string.rescue_form_upload_photo), fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            ReportaField(stringResource(R.string.rescue_form_pet_name), nombreMascota) { nombreMascota = it }
                            ReportaField(stringResource(R.string.rescue_form_species), especie) { especie = it }
                            ReportaField(stringResource(R.string.rescue_form_breed), raza) { raza = it }

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(stringResource(R.string.rescue_form_gender), fontSize = 14.sp, color = Color.DarkGray)
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    GeneroOption(stringResource(R.string.rescue_form_gender_m), genero == "M") { genero = "M" }
                                    Spacer(modifier = Modifier.width(20.dp))
                                    GeneroOption(stringResource(R.string.rescue_form_gender_h), genero == "H") { genero = "H" }
                                }
                            }

                            ReportaField(stringResource(R.string.rescue_form_health_status), estadoSalud) { estadoSalud = it }
                            ReportaField(stringResource(R.string.rescue_form_assigned_shelter), refugioAsignado) { refugioAsignado = it }

                            Spacer(modifier = Modifier.height(30.dp))

                            Button(
                                onClick = { },
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(15.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Done, null, tint = Color.White)
                                    Spacer(Modifier.width(8.dp))
                                    Text(stringResource(R.string.rescue_form_btn_save), fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { navController.popBackStack() },
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(15.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Close, null, tint = Color.White)
                                    Spacer(Modifier.width(8.dp))
                                    Text(stringResource(R.string.rescue_form_btn_cancel), fontWeight = FontWeight.Bold, color = Color.White)
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
fun ReportaField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(text = label, fontSize = 14.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(4.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun GeneroOption(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF4527A0)) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color(0xFF4527A0) else Color.LightGray)
            )
            Text(text = label, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        }
    }
}
