package com.example.rescatando_mascotas_forever.presentation.donaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonacionesScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    val bankInfo = """
        ${stringResource(R.string.donations_bank_label)}: Bancolombia
        ${stringResource(R.string.donations_account_type_label)}: 123-456789-01
        ${stringResource(R.string.donations_nit_label)}: 900.123.456-7
        ${stringResource(R.string.donations_holder_label)}: ${stringResource(R.string.donations_foundation_name)}
    """.trimIndent()

    AppDrawer(navController = navController, drawerState = drawerState, scope = scope) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { AppBottomBar(navController = navController) },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F9FA))
            ) {
                item {
                    DonacionHeader()
                }

                item {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = stringResource(R.string.donations_how_to_help),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        DonationOptionCard(
                            title = stringResource(R.string.donations_money_title),
                            description = stringResource(R.string.donations_money_desc),
                            icon = Icons.Default.Payments,
                            color = Color(0xFF4CAF50)
                        )
                        
                        DonationOptionCard(
                            title = stringResource(R.string.donations_food_title),
                            description = stringResource(R.string.donations_food_desc),
                            icon = Icons.Default.Pets,
                            color = Color(0xFFFF9800)
                        )
                        
                        DonationOptionCard(
                            title = stringResource(R.string.donations_medical_title),
                            description = stringResource(R.string.donations_medical_desc),
                            icon = Icons.Default.MedicalServices,
                            color = Color(0xFF2196F3)
                        )
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                text = stringResource(R.string.donations_bank_info_title),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF673AB7)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            BankDetailRow(stringResource(R.string.donations_bank_label), "Bancolombia")
                            BankDetailRow(stringResource(R.string.donations_account_type_label), "123-456789-01")
                            BankDetailRow(stringResource(R.string.donations_nit_label), "900.123.456-7")
                            BankDetailRow(stringResource(R.string.donations_holder_label), stringResource(R.string.donations_foundation_name))
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            Button(
                                onClick = { 
                                    clipboardManager.setText(AnnotatedString(bankInfo))
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                            ) {
                                Icon(Icons.Default.ContentCopy, null)
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(R.string.donations_btn_copy))
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
fun DonacionHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF673AB7), Color(0xFF3F51B5))
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.donations_hero_title),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.donations_hero_desc),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DonationOptionCard(title: String, description: String, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(description, color = Color.Gray, fontSize = 12.sp, lineHeight = 16.sp)
            }
        }
    }
}

@Composable
fun BankDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}
