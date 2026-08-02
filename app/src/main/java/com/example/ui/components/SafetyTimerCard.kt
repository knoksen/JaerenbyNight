package com.example.ui.components

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
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DangerRed
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCardBorder
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun SafetyTimerCard(
    secondsRemaining: Int,
    isTimerRunning: Boolean,
    onStartTimer: (Int) -> Unit,
    onExtendTimer: () -> Unit,
    onCancelTimer: () -> Unit,
    onShareStatus: () -> Unit,
    onEmergencyTrigger: () -> Unit,
    onToggleFlashlight: () -> Unit,
    isFlashlightOn: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MidnightCardBorder, RoundedCornerShape(20.dp))
            .testTag("safety_timer_card"),
        colors = CardDefaults.cardColors(containerColor = MidnightSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShieldMoon,
                        contentDescription = "Safe Companion",
                        tint = SafeGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Night Safety Companion",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Flashlight Toggle Quick Control
                IconButton(
                    onClick = onToggleFlashlight,
                    modifier = Modifier
                        .background(
                            if (isFlashlightOn) WarningAmber else MidnightBackground,
                            CircleShape
                        )
                        .testTag("flashlight_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashlightOn,
                        contentDescription = "Flashlight Toggle",
                        tint = if (isFlashlightOn) MidnightBackground else WarningAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isTimerRunning) {
                val mins = secondsRemaining / 60
                val secs = secondsRemaining % 60
                val formattedTime = String.format("%02d:%02d", mins, secs)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MidnightBackground, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "AUTOMATED SAFETY CHECK-IN",
                            color = SafeGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = formattedTime,
                            color = NeonCyan,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Timer active during your night commute",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Button(
                            onClick = onExtendTimer,
                            colors = ButtonDefaults.buttonColors(containerColor = MidnightCardBorder, contentColor = TextPrimary),
                            modifier = Modifier.height(36.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("+5 Mins", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Cancel Timer",
                            color = TextMuted,
                            fontSize = 11.sp,
                            modifier = Modifier.clickable { onCancelTimer() }
                        )
                    }
                }
            } else {
                Text(
                    text = "Set an automated check-in timer. If you don't confirm arrival, your primary emergency contact will be prompted.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onStartTimer(15) },
                        colors = ButtonDefaults.buttonColors(containerColor = MidnightCardBorder, contentColor = NeonCyan),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("timer_15m_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("15 Min Timer", fontSize = 12.sp)
                    }

                    Button(
                        onClick = { onStartTimer(30) },
                        colors = ButtonDefaults.buttonColors(containerColor = MidnightCardBorder, contentColor = NeonCyan),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("timer_30m_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("30 Min Timer", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Actions: Share Trip & SOS Emergency Call
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onShareStatus,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(NeonCyan)),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("share_trip_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Status",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share Status", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onEmergencyTrigger,
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed, contentColor = Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("sos_emergency_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneInTalk,
                        contentDescription = "SOS Emergency",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("EMERGENCY SOS", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
