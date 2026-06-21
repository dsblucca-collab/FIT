package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ExercicioEntity
import com.example.ui.WorkoutViewModel
import com.example.ui.components.CyberpunkBackground
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SessaoAtivaScreen(viewModel: WorkoutViewModel, treinoId: Int) {
    val treinos by viewModel.currentTreinosFlow.collectAsState()
    val treino = treinos.find { it.id == treinoId }

    val exercicios by viewModel.getExerciciosForTreino(treinoId).collectAsState(initial = emptyList())

    // Active state tracker
    var currentExerciseIndex by remember { mutableStateOf(0) }
    var completedSetsCount by remember { mutableStateOf(0) }
    var activeTimerSeconds by remember { mutableStateOf(0) }
    var isRestTimerOpen by remember { mutableStateOf(false) }
    var restTimeLeft by remember { mutableStateOf(60) }

    // Run active session clock
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            activeTimerSeconds++
        }
    }

    // Run rest overlay clock
    LaunchedEffect(isRestTimerOpen, restTimeLeft) {
        if (isRestTimerOpen && restTimeLeft > 0) {
            delay(1000)
            restTimeLeft--
            if (restTimeLeft == 0) {
                isRestTimerOpen = false
                restTimeLeft = 60
            }
        }
    }

    val activeExercise: ExercicioEntity? = if (exercicios.isNotEmpty() && currentExerciseIndex < exercicios.size) {
        exercicios[currentExerciseIndex]
    } else {
        null
    }

    // Interactive weight modifier state
    var isEditingWeight by remember { mutableStateOf(false) }
    var editableWeightText by remember(activeExercise) { mutableStateOf(activeExercise?.weight?.toString() ?: "0.0") }

    val sessionTimerFormatted = remember(activeTimerSeconds) {
        val hrs = activeTimerSeconds / 3600
        val mins = (activeTimerSeconds % 3600) / 60
        val secs = activeTimerSeconds % 60
        String.format("%02d:%02d:%02d", hrs, mins, secs)
    }

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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Cancelar",
                            tint = PrimaryPink,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { viewModel.navigateBack() }
                                .testTag("sessao_back_button")
                        )
                        Spacer(modifier = Modifier.width(16.dp))
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
                        text = "SESSÃO_ATIVA // $sessionTimerFormatted",
                        color = SecondaryCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
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
                if (exercicios.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryPink)
                    }
                } else if (activeExercise == null) {
                    // Session Complete screen
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Concluido",
                                tint = SecondaryCyan,
                                modifier = Modifier.size(72.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "SESSÃO FINALIZADA!",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Seus dados de performance foram sincronizados com a nuvem neural do NEO_FIT.",
                                color = CyberTextSecondary,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            Button(
                                onClick = {
                                    // Complete and save log
                                    viewModel.saveSessionProgress(
                                        treinoId = treinoId,
                                        exerciseName = exercicios.firstOrNull()?.name ?: "TREINO",
                                        currentWeight = exercicios.firstOrNull()?.weight ?: 60f,
                                        reps = exercicios.firstOrNull()?.reps ?: 10
                                    )
                                    viewModel.navigateBack()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("finalizar_treino_button")
                            ) {
                                Text(
                                    text = "VOLTAR PARA INÍCIO",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                )
                            }
                        }
                    }
                } else {
                    // Progress calculations
                    val totalProgress = ((currentExerciseIndex.toFloat() / exercicios.size) * 100).toInt() + 
                        ((completedSetsCount.toFloat() / activeExercise.series) * (100f / exercicios.size)).toInt()

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress scale
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "FASE ${currentExerciseIndex + 1}/${exercicios.size}: ${activeExercise.name.uppercase()}",
                            color = CyberTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "$totalProgress%",
                            color = PrimaryPink,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { totalProgress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = PrimaryPink,
                        trackColor = CyberMediumSurface
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Exercise visual card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, PrimaryPink.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .background(CyberDarkSurface.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                    ) {
                        Column {
                            // Dummy visual banner represent cyberpunk athlete workout with clean vector brushes
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(Color(0xFF0F0F24)),
                                contentAlignment = Alignment.Center
                            ) {
                                val imageId = remember(treino, activeExercise) {
                                    val workoutName = treino?.name ?: ""
                                    getTreinoCapaImageId(workoutName, treino?.isRestDay ?: false)
                                }

                                Image(
                                    painter = painterResource(id = imageId),
                                    contentDescription = "Capa",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                )
                                // Design representation of a futuristic neon sports glow wireframe in Canvas
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val brushColor = PrimaryPink.copy(alpha = 0.3f)
                                    val centerY = size.height * 0.5f
                                    val startX = size.width * 0.25f
                                    val endX = size.width * 0.75f
                                    
                                    // Dumbbell bar
                                    drawLine(
                                        color = brushColor,
                                        start = Offset(startX, centerY),
                                        end = Offset(endX, centerY),
                                        strokeWidth = 14f
                                    )
                                    // Weights left
                                    drawRect(
                                        color = SecondaryCyan.copy(alpha = 0.6f),
                                        topLeft = Offset(startX - 30f, centerY - 50f),
                                        size = androidx.compose.ui.geometry.Size(30f, 100f)
                                    )
                                    // Weights right
                                    drawRect(
                                        color = SecondaryCyan.copy(alpha = 0.6f),
                                        topLeft = Offset(endX, centerY - 50f),
                                        size = androidx.compose.ui.geometry.Size(30f, 100f)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color.Transparent, Color(0xFF0F0F24))
                                            )
                                        )
                                )

                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .border(1.dp, PrimaryPink, RoundedCornerShape(4.dp))
                                            .background(PrimaryPink.copy(alpha = 0.1f))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(text = "ALVO: FORÇA", color = PrimaryPink, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = activeExercise.name.uppercase(),
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                            }

                            // Exercise Stats row: Reps, Weight
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(CyberMediumSurface.copy(alpha = 0.6f))
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "REPETIÇÕES EM ALVO", color = SecondaryCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "${activeExercise.reps}", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { isEditingWeight = true }
                                ) {
                                    Text(text = "CARGA_KG", color = PrimaryPink, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    if (isEditingWeight) {
                                        OutlinedTextField(
                                            value = editableWeightText,
                                            onValueChange = { editableWeightText = it },
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White,
                                                focusedBorderColor = PrimaryPink
                                            ),
                                            singleLine = true,
                                            trailingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Save",
                                                    tint = PrimaryPink,
                                                    modifier = Modifier.clickable {
                                                        isEditingWeight = false
                                                        val w = editableWeightText.toFloatOrNull() ?: activeExercise.weight
                                                        viewModel.updateExercicio(activeExercise.copy(weight = w))
                                                    }
                                                )
                                            },
                                            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("sessao_weight_input")
                                        )
                                    } else {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = "${activeExercise.weight} KG", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(Icons.Default.Edit, contentDescription = "Edit weight", tint = CyberTextSecondary.copy(alpha = 0.5f), modifier = Modifier.size(14.dp))
                                        }
                                    }
                                }
                            }

                            // Completed Sets Box checkboxes indicators
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    repeat(activeExercise.series) { index ->
                                        val isCompleted = index < completedSetsCount
                                        Box(
                                            modifier = Modifier
                                                .size(14.dp)
                                                .border(
                                                    width = 1.dp,
                                                    color = if (isCompleted) SecondaryCyan else CyberTextSecondary.copy(alpha = 0.3f),
                                                    shape = RoundedCornerShape(2.dp)
                                                )
                                                .background(
                                                    if (isCompleted) SecondaryCyan.copy(alpha = 0.8f) else Color.Transparent,
                                                    RoundedCornerShape(2.dp)
                                                )
                                        )
                                    }
                                }
                                Text(
                                    text = "SÉRIE $completedSetsCount DE ${activeExercise.series}",
                                    color = CyberTextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Buttons Action Panel
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // rest timer opener
                        Button(
                            onClick = {
                                if (activeExercise != null && completedSetsCount < activeExercise.series) {
                                    completedSetsCount++
                                }
                                restTimeLeft = 60
                                isRestTimerOpen = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .testTag("iniciar_descanso_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = CyberMediumSurface),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SecondaryCyan.copy(alpha = 0.3f))
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Timer", tint = SecondaryCyan)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "INICIAR DESCANSO", color = SecondaryCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        // Complete current series set activator
                        Button(
                            onClick = {
                                if (completedSetsCount < activeExercise.series) {
                                    completedSetsCount++
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .testTag("completa_serie_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Done", tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "SÉRIE CONCLUÍDA", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Next Exercise Selector
                    Button(
                        onClick = {
                            currentExerciseIndex++
                            completedSetsCount = 0
                            // Update temporary weight state
                            isEditingWeight = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("proximo_exercicio_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberMediumSurface.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "PRÓXIMO EXERCÍCIO", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = PrimaryPink, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }

        // Rest Timer Overlay (matches website rest timer design perfectly!)
        AnimatedVisibility(
            visible = isRestTimerOpen,
            enter = fadeIn() + scaleIn(initialScale = 0.95f),
            exit = fadeOut() + scaleOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CyberBlack.copy(alpha = 0.98f))
                    .clickable { /* Block clicks */ },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Timer count circle shape
                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .border(width = 4.dp, color = SecondaryCyan.copy(alpha = 0.2f), shape = CircleShape)
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$restTimeLeft",
                                color = Color.White,
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "SEGUNDOS DESCANSO",
                                color = SecondaryCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Button(
                        onClick = {
                            isRestTimerOpen = false
                            restTimeLeft = 60
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SecondaryCyan),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .width(200.dp)
                            .height(48.dp)
                            .testTag("pular_descanso_button")
                    ) {
                        Text(
                            text = "PULAR DESCANSO",
                            color = SecondaryCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }
        }
    }
}

fun getTreinoCapaImageId(name: String, isRestDay: Boolean): Int {
    val upper = name.uppercase()
    if (isRestDay || upper.contains("DESCANSO") || upper.contains("REST") || upper.contains("CARDIO") || upper.isEmpty()) {
        return com.example.R.drawable.img_cover_descanso_1782073654642
    }
    return when {
        upper.contains("PERNA") || upper.contains("LEG") || upper.contains("COXA") || upper.contains("PANTURRILHA") -> {
            com.example.R.drawable.img_cover_perna_1782073673719
        }
        upper.contains("PEITO") || upper.contains("CHEST") || upper.contains("TORAX") -> {
            com.example.R.drawable.img_cover_peito_1782073685642
        }
        upper.contains("COSTAS") || upper.contains("BACK") || upper.contains("DORSO") || upper.contains("LAT") -> {
            com.example.R.drawable.img_cover_costas_1782073699441
        }
        upper.contains("BRAÇO") || upper.contains("BICEPS") || upper.contains("TRICEPS") || upper.contains("OMBRO") || upper.contains("ARM") || upper.contains("SHOULDER") -> {
            com.example.R.drawable.img_cover_bracos_1782073714987
        }
        else -> {
            com.example.R.drawable.img_cover_peito_1782073685642
        }
    }
}
