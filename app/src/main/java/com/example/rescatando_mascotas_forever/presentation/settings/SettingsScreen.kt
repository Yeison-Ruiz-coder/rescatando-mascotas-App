package com.example.rescatando_mascotas_forever.presentation.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.settings_title), fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.settings_back))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            bottomBar = { AppBottomBar(navController = navController) },
            containerColor = Color(0xFFFDF7F2)
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(20.dp)) }

                item {
                    SettingsSectionTitle(stringResource(R.string.settings_section_account))
                    SettingsGroup {
                        SettingsItem(Icons.Default.Person, stringResource(R.string.settings_edit_profile), stringResource(R.string.settings_edit_profile_desc)) { }
                        SettingsDivider()
                        SettingsItem(Icons.Default.Lock, stringResource(R.string.settings_security), stringResource(R.string.settings_security_desc)) { }
                        SettingsDivider()
                        SettingsItem(Icons.Default.Notifications, stringResource(R.string.settings_notifications), stringResource(R.string.settings_notifications_desc)) { }
                    }
                }

                item {
                    SettingsSectionTitle(stringResource(R.string.settings_section_preferences))
                    SettingsGroup {
                        // Selector de Idioma
                        LanguageSelectorItem()
                        SettingsDivider()
                        SettingsToggleItem(Icons.Default.Face, stringResource(R.string.settings_dark_mode), stringResource(R.string.settings_dark_mode_desc))
                        SettingsDivider()
                        SettingsItem(Icons.Default.LocationOn, stringResource(R.string.settings_location), stringResource(R.string.settings_location_desc)) { }
                    }
                }

                item {
                    SettingsSectionTitle(stringResource(R.string.settings_section_legal))
                    SettingsGroup {
                        SettingsItem(Icons.Default.Info, stringResource(R.string.settings_terms), stringResource(R.string.settings_terms_desc)) { }
                        SettingsDivider()
                        SettingsItem(Icons.Default.Build, stringResource(R.string.settings_privacy), stringResource(R.string.settings_privacy_desc)) { }
                    }
                }

                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
fun LanguageSelectorItem() {
    val currentLanguage = AppCompatDelegate.getApplicationLocales()[0]?.language ?: "es"
    
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Language, null, tint = Color(0xFF673AB7), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.settings_language), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                Text(stringResource(R.string.settings_language_desc), fontSize = 12.sp, color = Color.Gray)
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LanguageButton(
                text = stringResource(R.string.lang_spanish),
                isSelected = currentLanguage == "es",
                onClick = { 
                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("es")
                    AppCompatDelegate.setApplicationLocales(appLocale)
                },
                modifier = Modifier.weight(1f)
            )
            LanguageButton(
                text = stringResource(R.string.lang_english),
                isSelected = currentLanguage == "en",
                onClick = { 
                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("en")
                    AppCompatDelegate.setApplicationLocales(appLocale)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun LanguageButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val backgroundColor = if (isSelected) Color(0xFF673AB7) else Color.White
    val contentColor = if (isSelected) Color.White else Color(0xFF673AB7)
    val borderColor = Color(0xFF673AB7)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = contentColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF673AB7),
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
    )
}

@Composable
fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(content = content)
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Color(0xFF673AB7), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.LightGray)
    }
}

@Composable
fun SettingsToggleItem(icon: ImageVector, title: String, subtitle: String) {
    var checked by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Color(0xFF673AB7), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF673AB7))
        )
    }
}

@Composable
fun SettingsDivider() {
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F5F5))
}
