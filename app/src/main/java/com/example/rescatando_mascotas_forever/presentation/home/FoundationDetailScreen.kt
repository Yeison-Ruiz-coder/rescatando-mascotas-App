package com.example.rescatando_mascotas_forever.presentation.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundationDetailScreen(foundationName: String, navController: NavHostController) {
    val context = LocalContext.current
    val WebDarkBg = Color(0xFF0A0E14)
    val AccentPurple = Color(0xFF9575CD)
    val PurpleGradient = Brush.verticalGradient(listOf(Color(0xFF9575CD), Color(0xFF7E57C2)))
    val direccion = "Carrera 25 # 10-05, Bucaramanga"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(foundationName, color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WebDarkBg)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).background(WebDarkBg).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp)).background(PurpleGradient), contentAlignment = Alignment.Center) {
                    Text(foundationName, color = Color.White.copy(alpha = 0.1f), fontSize = 32.sp, fontWeight = FontWeight.Black)
                }
            }
            item {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22))) {
                    Column(Modifier.padding(16.dp)) {
                        Text("CONTACTO", color = AccentPurple, fontWeight = FontWeight.Bold)
                        Text(direccion, color = Color.White)
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${Uri.encode(direccion)}"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                        ) {
                            Text("Ver en Google Maps")
                        }
                    }
                }
            }
        }
    }
}
