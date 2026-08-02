package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Subway
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.DangerRed
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCardBorder
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.MidnightSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.NightViewModel

@Composable
fun MainScreen(
    viewModel: NightViewModel
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    var showEmergencyDialog by remember { mutableStateOf(false) }

    // ViewModel State Flow Collections
    val origin by viewModel.origin.collectAsStateWithLifecycle()
    val destination by viewModel.destination.collectAsStateWithLifecycle()
    val selectedTimeSlot by viewModel.selectedTimeSlot.collectAsStateWithLifecycle()
    val selectedModeFilter by viewModel.selectedModeFilter.collectAsStateWithLifecycle()
    val filteredRoutes by viewModel.filteredRoutes.collectAsStateWithLifecycle()
    val selectedRoute by viewModel.selectedRoute.collectAsStateWithLifecycle()

    val activeTripRoute by viewModel.activeTripRoute.collectAsStateWithLifecycle()
    val activeTripStepIndex by viewModel.activeTripStepIndex.collectAsStateWithLifecycle()

    val safetyTimerSeconds by viewModel.safetyTimerSecondsRemaining.collectAsStateWithLifecycle()
    val isTimerRunning by viewModel.isTimerRunning.collectAsStateWithLifecycle()
    val timerExpiredAlert by viewModel.timerExpiredAlert.collectAsStateWithLifecycle()

    val savedRoutes by viewModel.savedRoutes.collectAsStateWithLifecycle()
    val emergencyContacts by viewModel.emergencyContacts.collectAsStateWithLifecycle()
    val safetyLogs by viewModel.safetyLogs.collectAsStateWithLifecycle()

    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val flashlightOn by viewModel.flashlightOn.collectAsStateWithLifecycle()

    // Handle snackbar user messages
    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF16191F),
                contentColor = TextPrimary,
                modifier = Modifier
                    .border(width = 1.dp, color = MidnightCardBorder)
                    .testTag("main_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(imageVector = Icons.Default.Navigation, contentDescription = "Route Planner")
                    },
                    label = { Text("Route Planner", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBackground,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("tab_route_planner")
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        if (activeTripRoute != null) {
                            BadgedBox(
                                badge = { Badge(containerColor = SafeGreen) }
                            ) {
                                Icon(imageVector = Icons.Default.ShieldMoon, contentDescription = "Active Trip")
                            }
                        } else {
                            Icon(imageVector = Icons.Default.ShieldMoon, contentDescription = "Active Trip")
                        }
                    },
                    label = { Text("Active Trip", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBackground,
                        selectedTextColor = SafeGreen,
                        indicatorColor = SafeGreen,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("tab_active_trip")
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(imageVector = Icons.Default.Map, contentDescription = "Night Guide")
                    },
                    label = { Text("Night Directory", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBackground,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("tab_night_guide")
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = "Saved & Safety")
                    },
                    label = { Text("Saved & Contacts", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBackground,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("tab_saved_safety")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> RoutePlannerTab(
                    viewModel = viewModel,
                    origin = origin,
                    destination = destination,
                    selectedTimeSlot = selectedTimeSlot,
                    selectedModeFilter = selectedModeFilter,
                    filteredRoutes = filteredRoutes,
                    selectedRoute = selectedRoute,
                    popularLocations = viewModel.popularLocations,
                    onStartTrip = { route ->
                        viewModel.startActiveTrip(route)
                        selectedTab = 1
                    }
                )

                1 -> ActiveTripTab(
                    viewModel = viewModel,
                    activeRoute = activeTripRoute,
                    currentStepIndex = activeTripStepIndex,
                    safetyTimerSeconds = safetyTimerSeconds,
                    isTimerRunning = isTimerRunning,
                    isFlashlightOn = flashlightOn,
                    onNavigateToPlanner = { selectedTab = 0 },
                    onEmergencyTrigger = { showEmergencyDialog = true }
                )

                2 -> NightGuideTab()

                3 -> SavedSafetyTab(
                    viewModel = viewModel,
                    savedRoutes = savedRoutes,
                    emergencyContacts = emergencyContacts,
                    safetyLogs = safetyLogs
                )
            }
        }
    }

    // Emergency SOS Dialog
    if (showEmergencyDialog) {
        AlertDialog(
            onDismissRequest = { showEmergencyDialog = false },
            containerColor = MidnightSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.PhoneInTalk, contentDescription = "SOS", tint = DangerRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("EMERGENCY SOS TOOLKIT", color = DangerRed, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Trigger immediate emergency assistance or alert your trusted safety contacts.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:911"))
                            context.startActivity(intent)
                            showEmergencyDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DangerRed, contentColor = Color.White),
                        modifier = Modifier.fillMaxWidth().testTag("call_911_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = "Call 911")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Call Emergency 911 Direct", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val primaryContact = emergencyContacts.firstOrNull { it.isPrimary } ?: emergencyContacts.firstOrNull()
                    if (primaryContact != null) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${primaryContact.phone}"))
                                context.startActivity(intent)
                                showEmergencyDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SafeGreen, contentColor = MidnightBackground),
                            modifier = Modifier.fillMaxWidth().testTag("call_primary_contact_button"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.PhoneInTalk, contentDescription = "Call Contact")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call Primary: ${primaryContact.name}", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = {
                                val smsText = "EMERGENCY ALERT: I am on my night commute and need immediate assistance. Current route: ${activeTripRoute?.title ?: "local area"}. Contact me!"
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("smsto:${primaryContact.phone}")
                                    putExtra("sms_body", smsText)
                                }
                                context.startActivity(intent)
                                showEmergencyDialog = false
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningAmber),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(WarningAmber)),
                            modifier = Modifier.fillMaxWidth().testTag("send_sos_sms_button"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Sms, contentDescription = "Send SMS")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Send SOS SMS Alert", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                OutlinedButton(
                    onClick = { showEmergencyDialog = false },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMuted)
                ) {
                    Text("Close")
                }
            }
        )
    }

    // Timer Expired Safety Alert Dialog
    if (timerExpiredAlert) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissTimerAlert() },
            containerColor = MidnightSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ShieldMoon, contentDescription = "Safety Alert", tint = WarningAmber)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Safety Check-In Alert!", color = WarningAmber, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    text = "Your safety check-in timer has expired. Please confirm that you arrived safely, or extend the timer if your commute is still ongoing.",
                    color = TextPrimary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.finishTrip()
                        viewModel.dismissTimerAlert()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SafeGreen, contentColor = MidnightBackground),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("I'm Safe! ✔", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        viewModel.startSafetyTimer(10)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MidnightCardBorder, contentColor = TextPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+10 Mins")
                }
            }
        )
    }
}
