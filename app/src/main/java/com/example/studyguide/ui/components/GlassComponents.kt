package com.example.studyguide.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyguide.ui.theme.AccentAmber
import com.example.studyguide.ui.theme.AccentCyan
import com.example.studyguide.ui.theme.AccentEmerald
import com.example.studyguide.ui.theme.AccentIndigo
import com.example.studyguide.ui.theme.AccentRose
import com.example.studyguide.ui.theme.AccentViolet
import com.example.studyguide.ui.theme.BorderZinc
import com.example.studyguide.ui.theme.DeepCharcoal
import com.example.studyguide.ui.theme.HeroGradientBrush
import com.example.studyguide.ui.theme.LightZinc
import com.example.studyguide.ui.theme.MutedZinc
import com.example.studyguide.ui.theme.PureWhite
import com.example.studyguide.ui.theme.SoftAmber
import com.example.studyguide.ui.theme.SoftCyan
import com.example.studyguide.ui.theme.SoftEmerald
import com.example.studyguide.ui.theme.SoftIndigo
import com.example.studyguide.ui.theme.SoftPurple
import com.example.studyguide.ui.theme.SoftRose

// Helper Modifier for spring-press feedback
@Composable
fun Modifier.pressScaleClickable(onClick: () -> Unit): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "pressScale"
    )
    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = ripple(bounded = true),
            onClick = onClick
        )
}

data class CategoryStyle(
    val label: String,
    val icon: ImageVector,
    val primaryColor: Color,
    val softBackground: Color,
    val gradient: Brush
)

object CategoryThemes {
    fun getStyle(category: String): CategoryStyle {
        return when (category.lowercase()) {
            "visa" -> CategoryStyle(
                label = "Visa & Legal",
                icon = Icons.Default.AssignmentInd,
                primaryColor = AccentIndigo,
                softBackground = SoftIndigo,
                gradient = Brush.linearGradient(listOf(Color(0xFF4F46E5), Color(0xFF7C3AED)))
            )
            "housing" -> CategoryStyle(
                label = "Housing",
                icon = Icons.Default.Apartment,
                primaryColor = AccentCyan,
                softBackground = SoftCyan,
                gradient = Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF06B6D4)))
            )
            "banking", "finance" -> CategoryStyle(
                label = "Banking & Finance",
                icon = Icons.Default.AccountBalanceWallet,
                primaryColor = AccentEmerald,
                softBackground = SoftEmerald,
                gradient = Brush.linearGradient(listOf(Color(0xFF059669), Color(0xFF10B981)))
            )
            "health" -> CategoryStyle(
                label = "Health & Insurance",
                icon = Icons.Default.LocalHospital,
                primaryColor = AccentRose,
                softBackground = SoftRose,
                gradient = Brush.linearGradient(listOf(Color(0xFFE11D48), Color(0xFFF43F5E)))
            )
            "academic" -> CategoryStyle(
                label = "Academics",
                icon = Icons.Default.School,
                primaryColor = AccentViolet,
                softBackground = SoftPurple,
                gradient = Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFFA855F7)))
            )
            else -> CategoryStyle(
                label = category,
                icon = Icons.Default.Category,
                primaryColor = AccentAmber,
                softBackground = SoftAmber,
                gradient = Brush.linearGradient(listOf(Color(0xFFD97706), Color(0xFFF59E0B)))
            )
        }
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(22.dp),
    backgroundColor: Color = PureWhite,
    borderColor: Color = BorderZinc,
    elevation: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val borderBrush = Brush.verticalGradient(
        colors = listOf(
            PureWhite.copy(alpha = 0.9f),
            borderColor.copy(alpha = 0.8f)
        )
    )

    val cardModifier = modifier
        .shadow(
            elevation = elevation,
            shape = shape,
            clip = false,
            ambientColor = DeepCharcoal.copy(alpha = 0.04f),
            spotColor = DeepCharcoal.copy(alpha = 0.06f)
        )
        .clip(shape)
        .border(BorderStroke(1.dp, borderBrush), shape)

    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = cardModifier,
            color = backgroundColor,
            shape = shape
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    } else {
        Surface(
            modifier = cardModifier,
            color = backgroundColor,
            shape = shape
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
fun GlassHeroCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(26.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val heroBorderBrush = Brush.linearGradient(
        colors = listOf(
            PureWhite.copy(alpha = 0.35f),
            PureWhite.copy(alpha = 0.08f)
        )
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = DeepCharcoal.copy(alpha = 0.25f),
                spotColor = DeepCharcoal.copy(alpha = 0.35f)
            )
            .clip(shape)
            .background(HeroGradientBrush)
            .border(BorderStroke(1.dp, heroBorderBrush), shape)
            .padding(22.dp)
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
            .clip(RoundedCornerShape(30.dp))
            .background(backgroundColor)
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(30.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun CategoryBadge(
    category: String,
    modifier: Modifier = Modifier
) {
    val style = CategoryThemes.getStyle(category)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(style.softBackground)
            .border(1.dp, style.primaryColor.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = style.icon,
                contentDescription = category,
                tint = style.primaryColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = category,
                style = MaterialTheme.typography.labelSmall,
                color = style.primaryColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CircularReadinessGauge(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 68.dp,
    strokeWidth: Dp = 6.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "gaugeProgress"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            // Track
            drawArc(
                color = PureWhite.copy(alpha = 0.18f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )
            // Progress
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        AccentEmerald,
                        AccentCyan,
                        AccentViolet,
                        AccentEmerald
                    )
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = stroke
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = PureWhite
            )
        }
    }
}
