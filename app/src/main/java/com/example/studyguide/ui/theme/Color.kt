package com.example.studyguide.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Minimal Modern White & Black Base
val PureBlack = Color(0xFF000000)
val DeepCharcoal = Color(0xFF0F172A)
val DarkZinc = Color(0xFF1E293B)
val MediumZinc = Color(0xFF334155)
val MutedZinc = Color(0xFF64748B)
val BorderZinc = Color(0xFFE2E8F0)
val LightZinc = Color(0xFFF1F5F9)
val OffWhite = Color(0xFFF8FAFC)
val PureWhite = Color(0xFFFFFFFF)

// Luxury Accent Palette (Vibrant Neo-Modern 2026)
val AccentIndigo = Color(0xFF6366F1)
val AccentViolet = Color(0xFF8B5CF6)
val AccentEmerald = Color(0xFF10B981)
val AccentMintDark = Color(0xFF047857)
val AccentAmber = Color(0xFFF59E0B)
val AccentAmberDark = Color(0xFFB45309)
val AccentRose = Color(0xFFF43F5E)
val AccentRoseDark = Color(0xFFBE123C)
val AccentCyan = Color(0xFF06B6D4)
val AccentCyanDark = Color(0xFF0E7490)
val AccentBlue = Color(0xFF3B82F6)

// Soft Pastel Accent Tints
val SoftIndigo = Color(0xFFEEF2FF)
val SoftEmerald = Color(0xFFECFDF5)
val SoftAmber = Color(0xFFFFFBEB)
val SoftRose = Color(0xFFFFF1F2)
val SoftCyan = Color(0xFFECFEFF)
val SoftPurple = Color(0xFFF5F3FF)

// Legacy compatibility
val PastelPurple = SoftPurple
val OnPastelPurple = AccentViolet
val PastelMint = SoftEmerald
val OnPastelMint = AccentMintDark
val PastelAmber = SoftAmber
val OnPastelAmber = AccentAmberDark
val PastelRose = SoftRose
val OnPastelRose = AccentRoseDark
val PastelBlue = SoftIndigo
val OnPastelBlue = AccentBlue

// Liquid Glass Translucent Surface Helpers
val GlassWhite80 = Color(0xCCFFFFFF)
val GlassWhite90 = Color(0xE6FFFFFF)
val GlassWhite60 = Color(0x99FFFFFF)
val GlassWhite30 = Color(0x4DFFFFFF)
val GlassBlack80 = Color(0xCC0F172A)
val GlassBlack95 = Color(0xF20F172A)

val GlassBorderLight = Color(0x80FFFFFF)
val GlassBorderHighlight = Color(0xCCFFFFFF)

// Gradients
val HeroGradientBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFF0F172A),
        Color(0xFF1E1B4B),
        Color(0xFF1E293B)
    )
)

val IndigoVioletGradient = Brush.horizontalGradient(
    colors = listOf(AccentIndigo, AccentViolet)
)

val EmeraldGradient = Brush.horizontalGradient(
    colors = listOf(AccentEmerald, Color(0xFF059669))
)

val AmberGradient = Brush.horizontalGradient(
    colors = listOf(AccentAmber, Color(0xFFD97706))
)

val RoseGradient = Brush.horizontalGradient(
    colors = listOf(AccentRose, Color(0xFFE11D48))
)

val CyanGradient = Brush.horizontalGradient(
    colors = listOf(AccentCyan, Color(0xFF0284C7))
)

// Material 3 Mappings
val PrimaryBlack = PureBlack
val OnPrimaryWhite = PureWhite
val PrimaryContainerBlack = DeepCharcoal
val OnPrimaryContainerWhite = PureWhite

val SecondaryZinc = MediumZinc
val OnSecondaryWhite = PureWhite
val SecondaryContainerZinc = LightZinc
val OnSecondaryContainerBlack = DarkZinc

val TertiaryZinc = MutedZinc
val OnTertiaryWhite = PureWhite
val TertiaryContainerZinc = LightZinc
val OnTertiaryContainerBlack = DarkZinc

val BackgroundWhite = OffWhite
val OnBackgroundBlack = DeepCharcoal
val SurfaceWhite = PureWhite
val OnSurfaceBlack = DeepCharcoal
val SurfaceVariantZinc = LightZinc
val OnSurfaceVariantMuted = MutedZinc
val OutlineZinc = BorderZinc
val OutlineVariantZinc = LightZinc

val SurfaceContainerLowestWhite = PureWhite
val SurfaceContainerLowZinc = OffWhite
val SurfaceContainerZinc = LightZinc
val SurfaceContainerHighZinc = BorderZinc
val SurfaceContainerHighestZinc = MutedZinc

val ErrorRed = Color(0xFFE11D48)
val OnErrorWhite = PureWhite
val ErrorContainerRed = SoftRose
val OnErrorContainerRed = AccentRoseDark
