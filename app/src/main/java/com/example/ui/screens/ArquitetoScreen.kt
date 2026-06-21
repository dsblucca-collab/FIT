package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ExercicioEntity
import com.example.ui.WorkoutViewModel
import com.example.ui.components.CyberpunkBackground
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArquitetoScreen(viewModel: WorkoutViewModel, treinoId: Int) {
    val treinos by viewModel.currentTreinosFlow.collectAsState()
    val treino = treinos.find { it.id == treinoId }

    val exercicios by viewModel.getExerciciosForTreino(treinoId).collectAsState(initial = emptyList())

    var editableName by remember(treino) { mutableStateOf(treino?.name ?: "") }
    var isRestDay by remember(treino) { mutableStateOf(treino?.isRestDay ?: false) }
    
    // Create local state controllers to add a new exercise module
    var showAddModuleDialog by remember { mutableStateOf(false) }
    var newExName by remember { mutableStateOf("") }
    var newExSeries by remember { mutableStateOf("4") }
    var newExReps by remember { mutableStateOf("12") }
    var newExWeight by remember { mutableStateOf("60") }

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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = PrimaryPink,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { viewModel.navigateBack() }
                            .testTag("arquiteto_back_button")
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
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // Screen Headers
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "SOBREPOSIÇÃO_MANUAL",
                            color = SecondaryCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "ARQUITETO",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontStyle = FontStyle.Italic
                        )
                    }

                    // Save / Done Button
                    Button(
                        onClick = {
                            if (editableName.isNotEmpty() && treino != null) {
                                viewModel.updateTreino(treino.copy(name = editableName, isRestDay = isRestDay))
                            }
                            viewModel.navigateBack()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryPink),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("arquiteto_save_button")
                    ) {
                        Text(
                            text = "SALVAR_ROTINA",
                            color = PrimaryPink,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Workout Name Editable Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DarkOutline, RoundedCornerShape(12.dp))
                        .background(CyberDarkSurface.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "NOMENCLATURA_DO_TREINO",
                            color = CyberTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editableName,
                            onValueChange = { editableName = it },
                            placeholder = { Text("DIGITE_O_NOME...", color = CyberTextSecondary.copy(alpha = 0.4f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = PrimaryPink,
                                unfocusedTextColor = PrimaryPink,
                                focusedBorderColor = PrimaryPink,
                                unfocusedBorderColor = DarkOutline
                            ),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("arquiteto_workout_name_input")
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isRestDay = !isRestDay }
                        ) {
                            Checkbox(
                                checked = isRestDay,
                                onCheckedChange = { isRestDay = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = PrimaryPink,
                                    uncheckedColor = CyberTextSecondary,
                                    checkmarkColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DIA DE DESCANSO (REST DAY)",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Exercises List title
                Text(
                    text = "MÓDULOS DE EXERCÍCIO",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Exercises Lazy List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(exercicios, key = { it.id }) { exercicio ->
                        ExercicioEditCard(
                            exercicio = exercicio,
                            onUpdate = { updated -> viewModel.updateExercicio(updated) },
                            onMoveUp = { viewModel.moveExercicioUp(exercicio) },
                            onMoveDown = { viewModel.moveExercicioDown(exercicio) },
                            onDelete = { viewModel.deleteExercicio(exercicio) }
                        )
                    }

                    item {
                        // "ADICIONAR_MÓDULO" Dashed Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .border(
                                    width = 1.5.dp,
                                    color = SecondaryCyan.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .background(SecondaryCyan.copy(alpha = 0.02f), RoundedCornerShape(12.dp))
                                .clickable { showAddModuleDialog = true }
                                .padding(24.dp)
                                .testTag("arquiteto_add_ex_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = "Add module",
                                    tint = SecondaryCyan,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "ADICIONAR_MÓDULO",
                                    color = SecondaryCyan,
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

        // Add Module Dialog
        if (showAddModuleDialog) {
            AlertDialog(
                onDismissRequest = { showAddModuleDialog = false },
                title = {
                    Text(
                        text = "ADICIONAR MÓDULO EXERCÍCIO",
                        color = SecondaryCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = newExName,
                            onValueChange = { newExName = it },
                            label = { Text("NOME DO EXERCÍCIO", color = SecondaryCyan) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = SecondaryCyan,
                                unfocusedBorderColor = CyberTextSecondary
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("dialog_ex_name_input")
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newExSeries,
                                onValueChange = { newExSeries = it },
                                label = { Text("SÉRIES", color = SecondaryCyan) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = SecondaryCyan,
                                    unfocusedBorderColor = CyberTextSecondary
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("dialog_ex_series_input")
                            )

                            OutlinedTextField(
                                value = newExReps,
                                onValueChange = { newExReps = it },
                                label = { Text("REPETIÇÕES", color = SecondaryCyan) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = SecondaryCyan,
                                    unfocusedBorderColor = CyberTextSecondary
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("dialog_ex_reps_input")
                            )
                        }

                        OutlinedTextField(
                            value = newExWeight,
                            onValueChange = { newExWeight = it },
                            label = { Text("PESO (KG)", color = SecondaryCyan) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = SecondaryCyan,
                                unfocusedBorderColor = CyberTextSecondary
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("dialog_ex_weight_input")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newExName.isNotEmpty()) {
                                val s = newExSeries.toIntOrNull() ?: 4
                                val r = newExReps.toIntOrNull() ?: 12
                                val w = newExWeight.toFloatOrNull() ?: 60f
                                viewModel.addExercicioToTreino(treinoId, newExName, s, r, w)
                                
                                // Reset
                                newExName = ""
                                newExSeries = "4"
                                newExReps = "12"
                                newExWeight = "60"
                                showAddModuleDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryCyan),
                        modifier = Modifier.testTag("dialog_ex_confirm_button")
                    ) {
                        Text("ADICIONAR", color = CyberBlack, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddModuleDialog = false }) {
                        Text("CANCELAR", color = CyberTextSecondary)
                    }
                },
                containerColor = CyberDarkSurface
            )
        }
    }
}

@Composable
fun ExercicioEditCard(
    exercicio: ExercicioEntity,
    onUpdate: (ExercicioEntity) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit
) {
    var isEditingName by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(exercicio.name) }

    var seriesValue by remember(exercicio.series) { mutableStateOf(exercicio.series.toString()) }
    var repsValue by remember(exercicio.reps) { mutableStateOf(exercicio.reps.toString()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkOutline, RoundedCornerShape(12.dp))
            .background(CyberMediumSurface.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column {
            // Exercise Title Line + action operations (subir, descer, deletar)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "MÓDULO_DE_EXERCÍCIO",
                        color = CyberTextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    
                    if (isEditingName) {
                        OutlinedTextField(
                            value = tempName,
                            onValueChange = { tempName = it },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = SecondaryCyan,
                                unfocusedBorderColor = DarkOutline
                            ),
                            singleLine = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Save name",
                                    tint = SecondaryCyan,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            isEditingName = false
                                            onUpdate(exercicio.copy(name = tempName))
                                        }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("edit_ex_name_input_${exercicio.id}")
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { isEditingName = true }
                        ) {
                            Text(
                                text = exercicio.name.uppercase(),
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit name",
                                tint = CyberTextSecondary.copy(alpha = 0.4f),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                // Interactive Sequence reordering controls + delete module!!
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onMoveUp, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Subir exercicio", tint = SecondaryCyan, modifier = Modifier.size(14.dp))
                    }
                    IconButton(onClick = onMoveDown, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Descer exercicio", tint = SecondaryCyan, modifier = Modifier.size(14.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(28.dp).testTag("delete_ex_${exercicio.id}")) {
                        Icon(Icons.Default.Delete, contentDescription = "Apagar", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats grid layout: Series and Repetitions input parameters matching web design
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Series Field
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(CyberBlack.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                        .border(0.5.dp, DarkOutline, RoundedCornerShape(4.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Column {
                        Text(
                            text = "SÉRIES",
                            color = CyberTextSecondary,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            BasicTextField(
                                value = seriesValue,
                                onValueChange = { newVal ->
                                    seriesValue = newVal
                                    val i = newVal.toIntOrNull()
                                    if (i != null) {
                                        onUpdate(exercicio.copy(series = i))
                                    }
                                },
                                textStyle = LocalTextStyle.current.copy(
                                    color = SecondaryCyan,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.width(40.dp).testTag("series_field_${exercicio.id}")
                            )
                            Text(text = "X", color = CyberTextSecondary.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Repetitions Field
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(CyberBlack.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                        .border(0.5.dp, DarkOutline, RoundedCornerShape(4.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Column {
                        Text(
                            text = "REPETIÇÕES",
                            color = CyberTextSecondary,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            BasicTextField(
                                value = repsValue,
                                onValueChange = { newVal ->
                                    repsValue = newVal
                                    val i = newVal.toIntOrNull()
                                    if (i != null) {
                                        onUpdate(exercicio.copy(reps = i))
                                    }
                                },
                                textStyle = LocalTextStyle.current.copy(
                                    color = SecondaryCyan,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.width(40.dp).testTag("reps_field_${exercicio.id}")
                            )
                            Text(text = "REP", color = CyberTextSecondary.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
