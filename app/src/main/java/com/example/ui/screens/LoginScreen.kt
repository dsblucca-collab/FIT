package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.WorkoutViewModel
import com.example.ui.components.CyberpunkBackground
import com.example.ui.theme.PrimaryPink
import com.example.ui.theme.SecondaryCyan
import com.example.ui.theme.AccentYellow

@Composable
fun LoginScreen(viewModel: WorkoutViewModel) {
    CyberpunkBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header spacing padding
            Spacer(modifier = Modifier.height(30.dp))

            // Large Glowing Logo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "NEO_FIT",
                    color = PrimaryPink,
                    fontSize = 62.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = (-2).sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "ATLETISMO_AVANÇADO",
                    color = SecondaryCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 5.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Auth Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "INICIALIZAÇÃO_DO_SISTEMA",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Conecte seu perfil neural para sincronizar dados de performance na rede.",
                    color = Color(0xFF9EA3C0),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Single Prominent Google Login BUTTON
                if (viewModel.isAuthenticating) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(Color(0xFF141422), RoundedCornerShape(8.dp))
                            .border(1.dp, SecondaryCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = SecondaryCyan,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "AUTENTICANDO...",
                            color = SecondaryCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(Color(0xFF141422), RoundedCornerShape(8.dp))
                            .border(1.dp, SecondaryCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .clickable {
                                viewModel.login {}
                            }
                            .testTag("google_login_button"),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Drawing custom colored Google circles in-place for safety without outer drawables
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            // Render a simple aesthetic multi-color indicator showing Google identity
                            Row {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color(0xFF4285F4), RoundedCornerShape(2.dp))
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color(0xFF34A853), RoundedCornerShape(2.dp))
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "ENTRAR COM GOOGLE",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // Footer Warnings
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.clickable { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info protocol",
                        tint = AccentYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "INFO_PROTOCOLO_SEGURANÇA",
                        color = Color(0xFF9EA3C0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(1.dp)
                        .background(Color(0xFF302840))
                )

                Text(
                    text = "ACESSO AUTORIZADO APENAS. ATIVIDADE DO TERMINAL MONITORADA PARA OTIMIZAÇÃO DE PERFORMANCE.",
                    color = Color(0xFF9EA3C0).copy(alpha = 0.5f),
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.2.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
