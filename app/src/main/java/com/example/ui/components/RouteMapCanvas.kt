package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LocationSpot
import com.example.data.model.RouteOption
import com.example.data.model.TransportMode
import com.example.ui.theme.MidnightCardBorder
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.MidnightSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.WarningAmber

@Composable
fun RouteMapCanvas(
    origin: LocationSpot,
    destination: LocationSpot,
    selectedRoute: RouteOption?,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulsePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulsePhase"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MidnightSurface)
            .border(1.dp, MidnightCardBorder, RoundedCornerShape(16.dp))
            .testTag("route_map_canvas")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Draw subtle night grid lines
            val gridStep = 40.dp.toPx()
            var x = 0f
            while (x < width) {
                drawLine(
                    color = Color(0xFF1E273A),
                    start = Offset(x, 0f),
                    end = Offset(x, height),
                    strokeWidth = 1f
                )
                x += gridStep
            }
            var y = 0f
            while (y < height) {
                drawLine(
                    color = Color(0xFF1E273A),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f
                )
                y += gridStep
            }

            // Nodes
            val startPt = Offset(width * 0.15f, height * 0.70f)
            val midPt1 = Offset(width * 0.40f, height * 0.35f)
            val midPt2 = Offset(width * 0.70f, height * 0.55f)
            val endPt = Offset(width * 0.88f, height * 0.25f)

            // Determine line color based on route mode
            val mainLineColor = when (selectedRoute?.modeType) {
                TransportMode.HYBRID -> NeonCyan
                TransportMode.TRANSIT_WALK -> NeonViolet
                TransportMode.RIDESHARE -> WarningAmber
                TransportMode.SAFE_WALK -> SafeGreen
                else -> NeonCyan
            }

            // Path 1: Primary active route curve
            val path = Path().apply {
                moveTo(startPt.x, startPt.y)
                cubicTo(
                    midPt1.x - 20, midPt1.y + 40,
                    midPt1.x + 20, midPt1.y - 40,
                    midPt1.x, midPt1.y
                )
                cubicTo(
                    midPt2.x - 30, midPt2.y - 20,
                    midPt2.x + 20, midPt2.y + 30,
                    midPt2.x, midPt2.y
                )
                lineTo(endPt.x, endPt.y)
            }

            // Glow line background
            drawPath(
                path = path,
                color = mainLineColor.copy(alpha = 0.3f),
                style = Stroke(width = 14f)
            )

            // Main path line
            drawPath(
                path = path,
                color = mainLineColor,
                style = Stroke(
                    width = 5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(25f, 15f), pulsePhase * 40f)
                )
            )

            // Draw Safe Haven Pins (Lit Stop Nodes)
            val havenPoints = listOf(
                Offset(midPt1.x, midPt1.y),
                Offset(midPt2.x, midPt2.y)
            )

            havenPoints.forEachIndexed { idx, pt ->
                // Outer ring pulse
                drawCircle(
                    color = SafeGreen.copy(alpha = 0.3f * (1f - pulsePhase)),
                    radius = 18.dp.toPx() * pulsePhase + 4.dp.toPx(),
                    center = pt
                )
                // Haven pin inner
                drawCircle(color = SafeGreen, radius = 6.dp.toPx(), center = pt)
                drawCircle(color = MidnightSurface, radius = 3.dp.toPx(), center = pt)
            }

            // Draw Start Point Node (Origin)
            drawCircle(color = NeonCyan.copy(alpha = 0.25f), radius = 18.dp.toPx(), center = startPt)
            drawCircle(color = NeonCyan, radius = 8.dp.toPx(), center = startPt)
            drawCircle(color = Color.White, radius = 3.dp.toPx(), center = startPt)

            // Draw End Point Node (Destination)
            drawCircle(color = SafeGreen.copy(alpha = 0.25f), radius = 18.dp.toPx(), center = endPt)
            drawCircle(color = SafeGreen, radius = 8.dp.toPx(), center = endPt)
            drawCircle(color = Color.White, radius = 3.dp.toPx(), center = endPt)
        }

        // Overlay labels for map with Frosted Glass styling
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MidnightSurfaceVariant.copy(alpha = 0.85f))
                .border(1.dp, MidnightCardBorder, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "START: ${origin.name}",
                color = NeonCyan,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Top Right Map Controls
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MidnightSurfaceVariant.copy(alpha = 0.85f))
                .border(1.dp, MidnightCardBorder, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "END: ${destination.name}",
                color = SafeGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Bottom Safe Zone Verified Floating Banner (Frosted Glass)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xE64F46E5))
                .border(1.dp, Color(0x66A5B4FC), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🛡️", fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "SAFE ZONE VERIFIED",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Walking paths currently well-lit & active",
                        color = Color(0xFFE0E7FF),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
