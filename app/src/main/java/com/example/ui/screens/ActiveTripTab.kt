package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material.icons.filled.Subway
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RouteOption
import com.example.data.model.StepType
import com.example.ui.components.SafetyBadge
import com.example.ui.components.SafetyTimerCard
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCardBorder
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.MidnightSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.NightViewModel

@Composable
fun ActiveTripTab(
    viewModel: NightViewModel,
    activeRoute: RouteOption?,
    currentStepIndex: Int,
    safetyTimerSeconds: Int,
    isTimerRunning: Boolean,
    isFlashlightOn: Boolean,
    onNavigateToPlanner: () -> Unit,
    onEmergencyTrigger: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (activeRoute == null) {
            // Empty Active Commute State
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .border(1.dp, MidnightCardBorder, RoundedCornerShape(16.dp))
                        .testTag("empty_active_trip_card"),
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(NeonCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Navigation,
                                contentDescription = "Navigation",
                                tint = NeonCyan,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No Active Night Commute",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Select a multimodal route from the Route Planner to start step-by-step turn guidance and live safety companion monitoring.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = onNavigateToPlanner,
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = MidnightBackground),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("go_to_planner_button")
                        ) {
                            Text("Open Route Planner", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // Active Trip Header
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NeonCyan, RoundedCornerShape(16.dp))
                        .testTag("active_trip_header_card"),
                    colors = CardDefaults.cardColors(containerColor = MidnightSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(SafeGreen)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "LIVE COMMUTE IN PROGRESS",
                                    color = SafeGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            SafetyBadge(
                                scorePercent = activeRoute.safetyScorePercent,
                                lightLabel = activeRoute.lightLevel.label
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = activeRoute.title,
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Target Arrival: ${activeRoute.arrivalTime} (${activeRoute.totalDurationMinutes} mins total)",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Step Progress Bar
                        val progress = (currentStepIndex + 1).toFloat() / activeRoute.steps.size.toFloat()
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = SafeGreen,
                            trackColor = MidnightCardBorder
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Step ${currentStepIndex + 1} of ${activeRoute.steps.size}",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Current Active Step Focus Card
            item {
                val step = activeRoute.steps.getOrNull(currentStepIndex)
                if (step != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MidnightCardBorder, RoundedCornerShape(16.dp))
                            .testTag("current_active_step_card"),
                        colors = CardDefaults.cardColors(containerColor = MidnightSurfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(NeonCyan.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
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
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "CURRENT INSTRUCTION",
                                        color = NeonCyan,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = step.instruction,
                                        color = TextPrimary,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = step.subtext,
                                color = TextSecondary,
                                fontSize = 13.sp
                            )

                            if (step.safetyTip != null) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MidnightBackground, RoundedCornerShape(10.dp))
                                        .border(1.dp, SafeGreen.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                        .padding(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.Top) {
                                        Icon(
                                            imageVector = Icons.Default.ShieldMoon,
                                            contentDescription = "Safety Tip",
                                            tint = SafeGreen,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = "Safety & Light Advisory",
                                                color = SafeGreen,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = step.safetyTip,
                                                color = TextPrimary,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Step Control Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (currentStepIndex > 0) {
                                    OutlinedButton(
                                        onClick = { viewModel.prevTripStep() },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Previous Step", fontSize = 12.sp)
                                    }
                                }

                                Button(
                                    onClick = { viewModel.nextTripStep() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (currentStepIndex == activeRoute.steps.size - 1) SafeGreen else NeonCyan,
                                        contentColor = MidnightBackground
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("next_step_button"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (currentStepIndex == activeRoute.steps.size - 1) "I Arrived Safely! ✔" else "Next Step ➔",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Safe Havens along Active Route
            item {
                Column {
                    Text(
                        text = "SAFE HAVENS ALONG THIS ROUTE",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    activeRoute.safeHavens.forEach { haven ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MidnightSurface)
                                .border(1.dp, MidnightCardBorder, RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Safe Haven",
                                    tint = SafeGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = haven.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "${haven.type} • ${haven.openStatus}", color = TextMuted, fontSize = 11.sp)
                                }
                            }
                            Text(text = "${haven.distMeters}m away", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Cancel Commute Option
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Cancel Commute Session",
                        color = TextMuted,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .clickable { viewModel.cancelActiveTrip() }
                            .padding(8.dp)
                    )
                }
            }
        }

        // Safety Timer Companion Card always visible in Active Trip tab
        item {
            SafetyTimerCard(
                secondsRemaining = safetyTimerSeconds,
                isTimerRunning = isTimerRunning,
                onStartTimer = { mins -> viewModel.startSafetyTimer(mins) },
                onExtendTimer = { viewModel.startSafetyTimer((safetyTimerSeconds / 60) + 5) },
                onCancelTimer = { viewModel.cancelSafetyTimer() },
                onShareStatus = {
                    val smsText = "Hey! I'm commuting via Night Guide from ${activeRoute?.title ?: "my location"}. ETA: ${activeRoute?.arrivalTime ?: "soon"}. Track my safety status!"
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("smsto:")
                        putExtra("sms_body", smsText)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share Night Status"))
                },
                onEmergencyTrigger = onEmergencyTrigger,
                onToggleFlashlight = { viewModel.toggleFlashlight() },
                isFlashlightOn = isFlashlightOn
            )
        }
    }
}
