package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FichaEntity
import com.example.data.TreinoEntity
import com.example.ui.Screen
import com.example.ui.WorkoutViewModel
import com.example.ui.components.CyberpunkBackground
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeusTreinosScreen(viewModel: WorkoutViewModel) {
    val fichas by viewModel.fichasFlow.collectAsState()
    val treinos by viewModel.currentTreinosFlow.collectAsState()
    
    var showDropdown by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    
    var newWorkoutName by remember { mutableStateOf("") }
    var newWorkoutDay by remember { mutableStateOf("SEGUNDA") }

    val daysOfWeek = listOf("SEGUNDA", "TERÇA", "QUARTA", "QUINTA", "SEXTA", "SÁBADO", "DOMINGO")

    CyberpunkBackground {
        Image(
            painter = painterResource(id = com.example.R.drawable.img_bg_home_1782074321784),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            alpha = 0.08f
        )
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

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Dropdown Selector Fichas
                        Box {
                            Row(
                                modifier = Modifier
                                    .border(
                                        1.dp,
                                        SecondaryCyan.copy(alpha = 0.6f),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .background(Color(0xFF1E1E30))
                                    .clickable { showDropdown = true }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("ficha_selector_dropdown"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val currentFicha = fichas.find { it.id == viewModel.selectedFichaId }
                                Text(
                                    text = currentFicha?.name ?: "FICHA A",
                                    color = SecondaryCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Expand",
                                    tint = SecondaryCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = showDropdown,
                                onDismissRequest = { showDropdown = false },
                                modifier = Modifier.background(CyberDarkSurface)
                            ) {
                                fichas.forEach { f ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = f.name,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        },
                                        onClick = {
                                            viewModel.selectFicha(f.id)
                                            showDropdown = false
                                        },
                                        modifier = Modifier.testTag("ficha_option_${f.id}")
                                    )
                                }
                            }
                        }

                        // Logout Icon
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { viewModel.logout() }
                                .testTag("logout_button")
                        )
                    }
                }
            },
            bottomBar = {
                // Bottom M3 Styled Nav Bar representing INICIO, TREINOS, PERFIL
                NavigationBar(
                    containerColor = CyberDarkSurface.copy(alpha = 0.95f),
                    modifier = Modifier.navigationBarsPadding(),
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = true,
                        onClick = {},
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                        label = { Text("INÍCIO", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryPink,
                            selectedTextColor = PrimaryPink,
                            unselectedTextColor = CyberTextSecondary,
                            unselectedIconColor = CyberTextSecondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { viewModel.navigateTo(Screen.Progressoes) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Progressoes") },
                        label = { Text("PERFIL", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SecondaryCyan,
                            selectedTextColor = SecondaryCyan,
                            unselectedTextColor = CyberTextSecondary,
                            unselectedIconColor = CyberTextSecondary,
                            indicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.testTag("perfil_nav_tab")
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = PrimaryPink,
                    contentColor = Color.White,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .testTag("add_workout_fab")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar treino",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // Header Area
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SISTEMA // ATIVIDADE",
                        color = SecondaryCyan.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(SecondaryCyan.copy(alpha = 0.2f))
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MEUS TREINOS",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic,
                        letterSpacing = (-1).sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (treinos.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Sem Treinos",
                                tint = CyberTextSecondary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "NENHUM TREINO REGISTRADO",
                                color = CyberTextSecondary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Toque no botão '+' abaixo para criar um novo.",
                                color = CyberTextSecondary.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(treinos, key = { it.id }) { treino ->
                            TreinoCardItem(
                                treino = treino,
                                onStartSession = {
                                    viewModel.navigateTo(Screen.SessaoAtiva(treino.id))
                                },
                                onArchitectClick = {
                                    viewModel.navigateTo(Screen.Arquiteto(treino.id))
                                },
                                onMoveUp = { viewModel.moveTreinoUp(treino.id) },
                                onMoveDown = { viewModel.moveTreinoDown(treino.id) },
                                onDelete = { viewModel.deleteTreino(treino) }
                            )
                        }
                    }
                }
            }
        }

        // Add Dialog to create customized custom workouts
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = {
                    Text(
                        text = "NOVO PLANO DE TREINO",
                        color = PrimaryPink,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = newWorkoutName,
                            onValueChange = { newWorkoutName = it },
                            label = { Text("NOME DO TREINO", color = SecondaryCyan) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = SecondaryCyan,
                                unfocusedBorderColor = CyberTextSecondary
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("dialog_workout_name_input")
                        )

                        // Day of week drop element
                        Column {
                            Text(
                                text = "DIA DA SEMANA",
                                color = CyberTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            var dayExpanded by remember { mutableStateOf(false) }
                            Box {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(CyberMediumSurface, RoundedCornerShape(4.dp))
                                        .border(1.dp, CyberTextSecondary, RoundedCornerShape(4.dp))
                                        .clickable { dayExpanded = true }
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = newWorkoutDay, color = Color.White, fontSize = 14.sp)
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                                }

                                DropdownMenu(
                                    expanded = dayExpanded,
                                    onDismissRequest = { dayExpanded = false },
                                    modifier = Modifier.background(CyberDarkSurface)
                                ) {
                                    daysOfWeek.forEach { d ->
                                        DropdownMenuItem(
                                            text = { Text(text = d, color = Color.White) },
                                            onClick = {
                                                newWorkoutDay = d
                                                dayExpanded = false
                                            },
                                            modifier = Modifier.testTag("dialog_day_option_$d")
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newWorkoutName.isNotEmpty()) {
                                viewModel.createNewTreino(newWorkoutDay, newWorkoutName)
                                newWorkoutName = ""
                                showAddDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink),
                        modifier = Modifier.testTag("dialog_confirm_button")
                    ) {
                        Text("CRIAR", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("CANCELAR", color = CyberTextSecondary)
                    }
                },
                containerColor = CyberDarkSurface
            )
        }
    }
}

@Composable
fun TreinoCardItem(
    treino: TreinoEntity,
    onStartSession: () -> Unit,
    onArchitectClick: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit
) {
    val imageId = getTreinoCapaImageId(treino.name, treino.isRestDay)
    
    if (treino.isRestDay) {
        // Rest Day UI Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AccentYellow.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                .background(Color(0xFF141422).copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
        ) {
            Column {
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = treino.dayOfWeek,
                                color = AccentYellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Rest",
                                tint = AccentYellow,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = treino.name,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        )
                    }
                    // Interactive Workout operations: Up/Down reordering sequences, Architect Screen edit, delete!
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        IconButton(onClick = onMoveUp, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Subir", tint = SecondaryCyan, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = onMoveDown, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Descer", tint = SecondaryCyan, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = onArchitectClick, modifier = Modifier.size(28.dp).testTag("edit_workout_${treino.id}")) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Recuperação ativa recomendada. Aproveite para alongar e descansar a musculatura.",
                    color = CyberTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
                }
            }
        }
    } else {
        // Normal Workout UI Card matching screenshots
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (treino.progressPercent >= 100) SecondaryCyan.copy(alpha = 0.5f) else PrimaryPink.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(12.dp)
                )
                .background(Color(0xFF141422).copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
        ) {
            Column {
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    // Top Header Row with Days and sequence reorder buttons + edit / remove!
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = treino.dayOfWeek,
                            color = PrimaryPink,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = treino.name,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontStyle = FontStyle.Italic,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Interactive Workout operations: Up/Down reordering sequences, Architect Screen edit, delete!
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        IconButton(onClick = onMoveUp, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Subir", tint = SecondaryCyan, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = onMoveDown, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Descer", tint = SecondaryCyan, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = onArchitectClick, modifier = Modifier.size(28.dp).testTag("edit_workout_${treino.id}")) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Indicator
                if (treino.progressPercent > 0) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "ESTADO DA ATIVIDADE",
                                color = CyberTextSecondary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${treino.progressPercent}%",
                                color = if (treino.progressPercent >= 100) SecondaryCyan else PrimaryPink,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { treino.progressPercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp),
                            color = if (treino.progressPercent >= 100) SecondaryCyan else PrimaryPink,
                            trackColor = CyberMediumSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Call To Action: Iniciar Sessão Button
                Button(
                    onClick = onStartSession,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("iniciar_sessao_${treino.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (treino.progressPercent >= 100) Color.Transparent else PrimaryPink,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryPink)
                ) {
                    Text(
                        text = if (treino.progressPercent >= 100) "REPETIR SESSÃO" else "INICIAR SESSÃO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
                }
            }
        }
    }
}
