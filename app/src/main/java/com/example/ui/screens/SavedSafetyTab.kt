package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.EmergencyContactEntity
import com.example.data.db.NightSafetyLogEntity
import com.example.data.db.SavedRouteEntity
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
fun SavedSafetyTab(
    viewModel: NightViewModel,
    savedRoutes: List<SavedRouteEntity>,
    emergencyContacts: List<EmergencyContactEntity>,
    safetyLogs: List<NightSafetyLogEntity>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAddContactForm by remember { mutableStateOf(false) }

    var newName by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }
    var newRel by remember { mutableStateOf("") }

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
                    text = "OFFLINE & EMERGENCY VAULT",
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Saved Routes & Safety Contacts",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Emergency Contacts Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MidnightCardBorder, RoundedCornerShape(20.dp))
                    .testTag("emergency_contacts_card"),
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
                                    .background(DangerRed.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Shield, contentDescription = "Contacts", tint = DangerRed, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "Emergency Safety Contacts", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Alerted if safety timer expires", color = TextMuted, fontSize = 11.sp)
                            }
                        }

                        IconButton(
                            onClick = { showAddContactForm = !showAddContactForm },
                            modifier = Modifier.testTag("toggle_add_contact_button")
                        ) {
                            Icon(imageVector = Icons.Default.PersonAdd, contentDescription = "Add Contact", tint = NeonCyan)
                        }
                    }

                    // Add Contact Form
                    AnimatedVisibility(visible = showAddContactForm) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .background(MidnightSurfaceVariant, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(text = "ADD NEW SAFETY CONTACT", color = NeonCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newName,
                                onValueChange = { newName = it },
                                label = { Text("Contact Name", color = TextMuted) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonCyan, unfocusedBorderColor = MidnightCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                                modifier = Modifier.fillMaxWidth().testTag("add_contact_name_input")
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = newPhone,
                                onValueChange = { newPhone = it },
                                label = { Text("Phone Number", color = TextMuted) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonCyan, unfocusedBorderColor = MidnightCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                                modifier = Modifier.fillMaxWidth().testTag("add_contact_phone_input")
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = newRel,
                                onValueChange = { newRel = it },
                                label = { Text("Relationship (e.g. Roommate, Partner)", color = TextMuted) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonCyan, unfocusedBorderColor = MidnightCardBorder, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    if (newName.isNotBlank() && newPhone.isNotBlank()) {
                                        viewModel.addEmergencyContact(newName, newPhone, newRel, emergencyContacts.isEmpty())
                                        newName = ""
                                        newPhone = ""
                                        newRel = ""
                                        showAddContactForm = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SafeGreen, contentColor = MidnightBackground),
                                modifier = Modifier.fillMaxWidth().testTag("save_contact_button"),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Save Contact", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (emergencyContacts.isEmpty()) {
                        Text(text = "No safety contacts added yet.", color = TextMuted, fontSize = 12.sp)
                    } else {
                        emergencyContacts.forEach { contact ->
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
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = contact.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        if (contact.isPrimary) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "PRIMARY",
                                                color = SafeGreen,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                                    .background(SafeGreen.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Text(text = "${contact.phone} • ${contact.relationship}", color = TextMuted, fontSize = 11.sp)
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
                                            context.startActivity(intent)
                                        }
                                    ) {
                                        Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = SafeGreen)
                                    }

                                    IconButton(
                                        onClick = { viewModel.deleteEmergencyContact(contact.id) }
                                    ) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Saved Offline Night Routes
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MidnightCardBorder, RoundedCornerShape(20.dp))
                    .testTag("saved_routes_card"),
                colors = CardDefaults.cardColors(containerColor = MidnightSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NeonCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Bookmark, contentDescription = "Saved Routes", tint = NeonCyan, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Saved Offline Night Routes (${savedRoutes.size})", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Available offline without data connection", color = TextMuted, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (savedRoutes.isEmpty()) {
                        Text(text = "No saved routes yet. Tap 'Save' on any route card in the planner.", color = TextMuted, fontSize = 12.sp)
                    } else {
                        savedRoutes.forEach { route ->
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
                                    Text(text = route.title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "${route.origin} ➔ ${route.destination}", color = NeonCyan, fontSize = 11.sp)
                                    Text(text = "${route.durationMinutes} mins • Safety: ${route.safetyScore}%", color = TextMuted, fontSize = 11.sp)
                                }

                                IconButton(onClick = { viewModel.deleteSavedRoute(route.id) }) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Route", tint = TextMuted)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Commute Safety History Log
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MidnightCardBorder, RoundedCornerShape(20.dp))
                    .testTag("safety_logs_card"),
                colors = CardDefaults.cardColors(containerColor = MidnightSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SafeGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.History, contentDescription = "History", tint = SafeGreen, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Completed Commute Safety Logs", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Track your recent safe arrivals", color = TextMuted, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (safetyLogs.isEmpty()) {
                        Text(text = "No completed trips logged yet.", color = TextMuted, fontSize = 12.sp)
                    } else {
                        safetyLogs.forEach { log ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(MidnightSurfaceVariant, RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = log.routeTitle, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "${log.commuteTimeMinutes} mins • ${log.userFeedback}", color = TextMuted, fontSize = 11.sp)
                                }

                                Row {
                                    repeat(log.safetyRatingStars) {
                                        Icon(imageVector = Icons.Default.Star, contentDescription = "Star", tint = WarningAmber, modifier = Modifier.size(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
