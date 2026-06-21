package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.Screen
import com.example.ui.WorkoutViewModel
import com.example.ui.screens.ArquitetoScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MeusTreinosScreen
import com.example.ui.screens.ProgressoesScreen
import com.example.ui.screens.SessaoAtivaScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private val viewModel: WorkoutViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          when (val screen = viewModel.currentScreen) {
            is Screen.Login -> LoginScreen(viewModel)
            is Screen.MeusTreinos -> MeusTreinosScreen(viewModel)
            is Screen.Arquiteto -> ArquitetoScreen(viewModel, screen.treinoId)
            is Screen.SessaoAtiva -> SessaoAtivaScreen(viewModel, screen.treinoId)
            is Screen.Progressoes -> ProgressoesScreen(viewModel)
          }
        }
      }
    }
  }
}
