package com.example.studyguide.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.studyguide.ui.theme.BorderZinc
import com.example.studyguide.ui.theme.DarkZinc
import com.example.studyguide.ui.theme.GlassBlack95
import com.example.studyguide.ui.theme.GlassBorderHighlight
import com.example.studyguide.ui.theme.GlassWhite90
import com.example.studyguide.ui.theme.LightZinc
import com.example.studyguide.ui.theme.PureBlack
import com.example.studyguide.ui.theme.PureWhite

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    backgroundColor: Color = GlassWhite90,
    borderColor: Color = BorderZinc,
    elevation: Dp = 2.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val glassBorderBrush = Brush.verticalGradient(
        colors = listOf(
            GlassBorderHighlight,
            borderColor
        )
    )

    Surface(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                clip = false,
                ambientColor = DarkZinc.copy(alpha = 0.05f),
                spotColor = DarkZinc.copy(alpha = 0.08f)
            )
            .clip(shape)
            .border(BorderStroke(1.dp, glassBorderBrush), shape)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        color = backgroundColor,
        shape = shape
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

@Composable
fun GlassHeroCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val heroGlassBorderBrush = Brush.linearGradient(
        colors = listOf(
            PureWhite.copy(alpha = 0.35f),
            PureWhite.copy(alpha = 0.08f)
        )
    )

    val heroGradientBackground = Brush.linearGradient(
        colors = listOf(
            PureBlack,
            GlassBlack95,
            Color(0xFF18181B)
        )
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = shape,
                ambientColor = PureBlack.copy(alpha = 0.2f),
                spotColor = PureBlack.copy(alpha = 0.3f)
            )
            .clip(shape)
            .background(heroGradientBackground)
            .border(BorderStroke(1.dp, heroGlassBorderBrush), shape)
            .padding(20.dp)
    ) {
        content()
    }
}

@Composable
fun GlassPill(
    modifier: Modifier = Modifier,
    backgroundColor: Color = LightZinc,
    borderColor: Color = BorderZinc,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        content()
    }
}
