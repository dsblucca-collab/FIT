package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProgressoLogEntity
import com.example.ui.Screen
import com.example.ui.WorkoutViewModel
import com.example.ui.components.CyberpunkBackground
import com.example.ui.theme.*

@Composable
fun ProgressoesScreen(viewModel: WorkoutViewModel) {
    val logs by viewModel.logsFlow.collectAsState()

    CyberpunkBackground {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(64.dp)
                        .background(CyberBlack.copy(alpha = 0.9f))
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Logo",
                            tint = PrimaryPink,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "NEO_FIT",
                            color = PrimaryPink,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            fontStyle = FontStyle.Italic,
                            letterSpacing = 2.sp
                        )
                    }

                    Text(
                        text = "SYNCED OPTIMAL",
                        color = SecondaryCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            },
            bottomBar = {
                // Bottom custom styled Nav Bar
                NavigationBar(
                    containerColor = CyberDarkSurface.copy(alpha = 0.95f),
                    modifier = Modifier.navigationBarsPadding(),
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = false,
                        onClick = { viewModel.navigateTo(Screen.MeusTreinos) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                        label = { Text("INÍCIO", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryPink,
                            selectedTextColor = PrimaryPink,
                            unselectedTextColor = CyberTextSecondary,
                            unselectedIconColor = CyberTextSecondary,
                            indicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.testTag("inicio_nav_tab")
                    )
                    NavigationBarItem(
                        selected = true,
                        onClick = {},
                        icon = { Icon(Icons.Default.Person, contentDescription = "Progressoes") },
                        label = { Text("PERFIL", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SecondaryCyan,
                            selectedTextColor = SecondaryCyan,
                            unselectedTextColor = CyberTextSecondary,
                            unselectedIconColor = CyberTextSecondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    // Header state status blinking
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(SecondaryCyan, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SYSTEM STATUS: ANALYZING_GAINS",
                            color = SecondaryCyan,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "PROGRESSÕES",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.width(80.dp).height(2.dp).background(PrimaryPink))
                }

                // Systems bento grid
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Card 1
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, SecondaryCyan.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .background(CyberDarkSurface.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text("TOTAL VOLUME WEEK", color = CyberTextSecondary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text("14.2t", color = SecondaryCyan, fontSize = 20.sp, fontWeight = FontWeight.Black)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("+12%", color = SecondaryCyan.copy(alpha = 0.6f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Card 2
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, DarkOutline, RoundedCornerShape(8.dp))
                                .background(CyberDarkSurface.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text("PROGRESSION RATE", color = CyberTextSecondary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("88.5%", color = PrimaryPink, fontSize = 20.sp, fontWeight = FontWeight.Black)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = null, tint = PrimaryPink, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Card 3
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, DarkOutline, RoundedCornerShape(8.dp))
                                .background(CyberDarkSurface.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text("ACTIVE SESSIONS", color = CyberTextSecondary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text("42", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("LEVEL", color = CyberTextSecondary, fontSize = 9.sp)
                                }
                            }
                        }

                        // Card 4
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, DarkOutline, RoundedCornerShape(8.dp))
                                .background(CyberDarkSurface.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text("NEURAL SYNC", color = CyberTextSecondary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("OPTIMAL", color = AccentYellow, fontSize = 18.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }

                // Compound lifts bar charts
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, PrimaryPink.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .background(CyberDarkSurface.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column {
                                    Text("SQUAT (BACK)", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
                                    Text("COMPOUND_LIFT // ALPHA_CORE", color = PrimaryPink, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("CURRENT_MAX", color = CyberTextSecondary, fontSize = 8.sp)
                                    Text("145 KG", color = PrimaryPink, fontSize = 22.sp, fontWeight = FontWeight.Black)
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Custom Chart layout bars
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                val barHeights = listOf(0.5f, 0.61f, 0.72f, 0.85f, 1f)
                                val labels = listOf("130kg", "132kg", "135kg", "140kg", "145kg")
                                
                                barHeights.forEachIndexed { idx, ht ->
                                    val isMax = idx == 4
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = labels[idx],
                                            color = if (isMax) PrimaryPink else CyberTextSecondary,
                                            fontSize = 8.sp,
                                            fontWeight = if (isMax) FontWeight.Bold else FontWeight.Normal
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(ht)
                                                .background(
                                                    if (isMax) PrimaryPink else PrimaryPink.copy(alpha = 0.2f),
                                                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                                )
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DarkOutline))
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("LAST UPDATE", color = CyberTextSecondary, fontSize = 8.sp)
                                    Text("24 OCT 2077", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("INCREMENT", color = CyberTextSecondary, fontSize = 8.sp)
                                    Text("+5.0 KG", color = SecondaryCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Recent Logs block
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DarkOutline, RoundedCornerShape(12.dp))
                            .background(CyberDarkSurface.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                "RECENT_LOGS",
                                color = CyberTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            if (logs.isEmpty()) {
                                Text(
                                    text = "Nenhum histórico disponível.",
                                    color = CyberTextSecondary,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                                )
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                    logs.forEach { log ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                        .background(CyberMediumSurface, RoundedCornerShape(4.dp))
                                                        .border(0.5.dp, SecondaryCyan.copy(alpha = 0.3f), RoundedCornerShape(4.dp)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.PlayArrow,
                                                        contentDescription = null,
                                                        tint = SecondaryCyan,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Text(log.exerciseName, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                                    Text(log.dateString, color = CyberTextSecondary, fontSize = 9.sp)
                                                }
                                            }

                                            Column(horizontalAlignment = Alignment.End) {
                                                Text("${log.previousWeight.toInt()}kg → ${log.currentWeight.toInt()}kg", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                                Text(log.status, color = if (log.status == "INCREASED") SecondaryCyan else CyberTextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Circular Advice card
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, AccentYellow.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .background(Color(0xFF141422).copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .fillMaxHeight()
                                .background(AccentYellow)
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = AccentYellow, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("NEURAL_ADVICE", color = AccentYellow, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "\"Your central nervous system recovery is at 94%. Optimal window for PR attempt in DEADLIFT during the next 48 cycles.\"",
                                color = CyberTextSecondary,
                                fontStyle = FontStyle.Italic,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
