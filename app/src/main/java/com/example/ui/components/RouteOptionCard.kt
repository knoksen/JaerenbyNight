package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Subway
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RouteOption
import com.example.data.model.StepType
import com.example.data.model.TransportMode
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCardBorder
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.MidnightSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun RouteOptionCard(
    route: RouteOption,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onStartTrip: () -> Unit,
    onSaveRoute: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(isSelected) }

    val borderStroke = if (isSelected) Color(0xFF818CF8) else MidnightCardBorder
    val bgGradient = if (isSelected) Color(0xFF222938) else Color(0xFF181D28)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(if (isSelected) 1.5.dp else 1.dp, borderStroke, RoundedCornerShape(20.dp))
            .clickable {
                onSelect()
                expanded = !expanded
            }
            .testTag("route_option_card_${route.id}"),
        colors = CardDefaults.cardColors(containerColor = bgGradient)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Recommended Tag & Mode Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val modeColor = when (route.modeType) {
                        TransportMode.HYBRID -> NeonCyan
                        TransportMode.TRANSIT_WALK -> NeonViolet
                        TransportMode.RIDESHARE -> WarningAmber
                        TransportMode.SAFE_WALK -> SafeGreen
                        else -> NeonCyan
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(modeColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (route.modeType) {
                                TransportMode.HYBRID -> Icons.Default.Navigation
                                TransportMode.TRANSIT_WALK -> Icons.Default.DirectionsBus
                                TransportMode.RIDESHARE -> Icons.Default.DirectionsCar
                                TransportMode.SAFE_WALK -> Icons.AutoMirrored.Filled.DirectionsWalk
                                else -> Icons.Default.Navigation
                            },
                            contentDescription = route.modeType.displayName,
                            tint = modeColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        if (route.isRecommended) {
                            Text(
                                text = "RECOMMENDED NIGHT ROUTE",
                                color = SafeGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = route.title,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                IconButton(
                    onClick = onSaveRoute,
                    modifier = Modifier.testTag("save_route_button_${route.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Save Route",
                        tint = NeonCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = route.subtitle,
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Details Row: Duration, Cost, Safety Score
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MidnightBackground.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Commute Time", color = TextMuted, fontSize = 10.sp)
                    Text(
                        text = "${route.totalDurationMinutes} mins",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(text = "Est. Cost", color = TextMuted, fontSize = 10.sp)
                    Text(
                        text = if (route.estimatedCostUSD == 0.0) "FREE" else "\$${String.format("%.2f", route.estimatedCostUSD)}",
                        color = if (route.estimatedCostUSD == 0.0) SafeGreen else NeonCyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                SafetyBadge(
                    scorePercent = route.safetyScorePercent,
                    lightLabel = route.lightLevel.label
                )
            }

            // Expanded Step-by-Step Preview
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Text(
                        text = "Commute Connection Steps:",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    route.steps.forEachIndexed { index, step ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = when (step.stepType) {
                                    StepType.RIDESHARE -> Icons.Default.LocalTaxi
                                    StepType.BUS -> Icons.Default.DirectionsBus
                                    StepType.SUBWAY -> Icons.Default.Subway
                                    StepType.WALKING -> Icons.AutoMirrored.Filled.DirectionsWalk
                                    StepType.SAFE_HAVEN -> Icons.Default.Security
                                    StepType.TRANSFER -> Icons.Default.ChevronRight
                                },
                                contentDescription = step.stepType.name,
                                tint = NeonCyan,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column {
                                Text(
                                    text = step.instruction,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = step.subtext,
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                                if (step.safetyTip != null) {
                                    Text(
                                        text = "⚡ Safety: ${step.safetyTip}",
                                        color = SafeGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onStartTrip,
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = MidnightBackground),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("start_trip_button_${route.id}"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Start Active Commute", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = onSaveRoute,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(NeonCyan)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Save", fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
