package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.PrimaryPink
import com.example.ui.theme.SecondaryCyan

@Composable
fun CyberpunkBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
            .drawBehind {
                // Draw cyber grid
                val gridSize = 40.dp.toPx()
                val pinkGlow = PrimaryPink.copy(alpha = 0.04f)
                
                // Vertical lines
                var x = 0f
                while (x < size.width) {
                    drawLine(
                        color = pinkGlow,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                    x += gridSize
                }
                
                // Horizontal lines
                var y = 0f
                while (y < size.height) {
                    drawLine(
                        color = pinkGlow,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                    y += gridSize
                }
            }
    ) {
        content()
    }
}

@Composable
fun GlowButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(SecondaryCyan.copy(alpha = 0.8f), PrimaryPink.copy(alpha = 0.3f))),
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color(0xFF141422).copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Composable
fun NeonCard(
    modifier: Modifier = Modifier,
    borderColor: Color = PrimaryPink.copy(alpha = 0.3f),
    hasLeftPill: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .background(Color(0xFF10101F).copy(alpha = 0.9f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        if (hasLeftPill) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .background(PrimaryPink, RoundedCornerShape(3.dp))
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}

@Composable
fun TopBar(
    title: String = "NEO_FIT",
    subtitle: String = "",
    onMenuClick: () -> Unit = {},
    trailing: @Composable (RowScope.() -> Unit)? = null
) {
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
            Text(
                text = title,
                color = PrimaryPink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                letterSpacing = 2.sp
            )
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = subtitle,
                    color = SecondaryCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
        if (trailing != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                trailing()
            }
        }
    }
}
