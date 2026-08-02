package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Subway
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun NightGuideTab(
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
        // Header
        item {
            Column {
                Text(
                    text = "WEEKEND NIGHT DIRECTORY",
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Night Transit & Safety Knowledge",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Essential transit schedules, safe havens, and ride-share guidelines for weekend late nights.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        // Night Transit Schedule Guide Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MidnightCardBorder, RoundedCornerShape(20.dp))
                    .testTag("night_transit_schedules_card"),
                colors = CardDefaults.cardColors(containerColor = MidnightSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NeonViolet.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Subway, contentDescription = "Subway", tint = NeonViolet, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Weekend Late-Night Lines", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Fri & Sat 10:00 PM - 5:00 AM Frequency", color = TextMuted, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val transitLines = listOf(
                        Triple("Night Express Line N1 (Metro)", "Runs every 15 mins till 4:30 AM", "Security staff in cars 1 & 4"),
                        Triple("Boulevard Night Bus N12", "Runs every 20 mins 24/7", "Front door boarding only"),
                        Triple("Campus Night Owl Shuttle N40", "Runs every 10 mins 11 PM - 3 AM", "Free with Student / Visitor ID")
                    )

                    transitLines.forEach { line ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(MidnightSurfaceVariant, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Check", tint = SafeGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = line.first, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(text = "${line.second} • ${line.third}", color = TextMuted, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // Ride-Share Safety & Surge Guidelines
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MidnightCardBorder, RoundedCornerShape(20.dp))
                    .testTag("rideshare_guide_card"),
                colors = CardDefaults.cardColors(containerColor = MidnightSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(WarningAmber.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.LocalTaxi, contentDescription = "Ride Share", tint = WarningAmber, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Ride-Share Safety Protocol", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Uber, Lyft, and Taxi tips for midnight hours", color = TextMuted, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val tips = listOf(
                        "Always match the driver name, vehicle model, and license plate before opening the door.",
                        "Use official ride-share designated pickup zones located under bright streetlights.",
                        "Enable PIN verification in your app settings so drivers must enter your 4-digit code.",
                        "Avoid high surge prices at 2:00 AM bar close by taking a short-hop bus to a quiet zone first."
                    )

                    tips.forEach { tip ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "•", color = WarningAmber, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 6.dp))
                            Text(text = tip, color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Safe Havens Directory Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MidnightCardBorder, RoundedCornerShape(20.dp))
                    .testTag("safe_havens_directory_card"),
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(SafeGreen.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Security, contentDescription = "Safe Haven", tint = SafeGreen, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "24/7 Safe Haven Network", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Refuge points along night routes", color = TextMuted, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val havens = listOf(
                        Triple("Central Transit Police Substation", "200 Grand Transit Ave", "+1 (555) 019-9110"),
                        Triple("Midnight Byte 24/7 Diner", "145 Main St", "+1 (555) 012-4433"),
                        Triple("7-Eleven Monitored Store #8", "88 College Way", "+1 (555) 018-7711"),
                        Triple("Westside Security Information Desk", "88 Westside Blvd", "+1 (555) 014-9900")
                    )

                    havens.forEach { (name, address, phone) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MidnightSurfaceVariant)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = address, color = TextMuted, fontSize = 11.sp)
                            }

                            Row {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = "Call Haven",
                                    tint = NeonCyan,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MidnightBackground)
                                        .clickable {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                            context.startActivity(intent)
                                        }
                                        .padding(7.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
