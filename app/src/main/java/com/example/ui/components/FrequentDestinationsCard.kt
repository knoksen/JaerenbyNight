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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.FrequentDestinationEntity
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

@Composable
fun FrequentDestinationsCard(
    frequentDestinations: List<FrequentDestinationEntity>,
    onSelectDestination: (FrequentDestinationEntity) -> Unit,
    onAddDestination: (title: String, address: String, category: String, preferredMode: String, notes: String) -> Unit,
    onDeleteDestination: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddForm by remember { mutableStateOf(false) }

    var titleInput by remember { mutableStateOf("") }
    var addressInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Home") }
    var selectedMode by remember { mutableStateOf("HYBRID") }
    var notesInput by remember { mutableStateOf("") }

    val categories = listOf("Home", "Campus", "Nightlife", "Work", "Transit Hub")
    val modeOptions = listOf(
        "HYBRID" to "⚡ Hybrid",
        "TRANSIT_WALK" to "🚌 Transit",
        "RIDESHARE" to "🚗 Rideshare",
        "SAFE_WALK" to "🚶 Safe Walk"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MidnightCardBorder, RoundedCornerShape(20.dp))
            .testTag("frequent_destinations_room_card"),
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
                            .background(NeonCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Saved Trip Planner",
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "TRIP PLANNER (ROOM DB)",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Frequent Late-Night Destinations",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                IconButton(
                    onClick = { showAddForm = !showAddForm },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MidnightSurfaceVariant)
                        .testTag("add_frequent_destination_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Trip Destination",
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = "Save your recurring late-night spots and preferred transit modes for instant 1-tap route planning.",
                color = TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
            )

            // Form to Add New Destination
            AnimatedVisibility(visible = showAddForm) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MidnightBackground)
                        .border(1.dp, MidnightCardBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "NEW TRIP PLANNER DESTINATION",
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Destination Name (e.g. Home, Dorm)") },
                        modifier = Modifier.fillMaxWidth().testTag("frequent_title_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = MidnightCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = addressInput,
                        onValueChange = { addressInput = it },
                        label = { Text("Address / Location Landmark") },
                        modifier = Modifier.fillMaxWidth().testTag("frequent_address_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = MidnightCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Category Tag:", color = TextSecondary, fontSize = 11.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat, fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonCyan,
                                    selectedLabelColor = Color.Black,
                                    containerColor = MidnightSurfaceVariant,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Preferred Night Transport Mode:", color = TextSecondary, fontSize = 11.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        modeOptions.forEach { (modeKey, label) ->
                            FilterChip(
                                selected = selectedMode == modeKey,
                                onClick = { selectedMode = modeKey },
                                label = { Text(label, fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = WarningAmber,
                                    selectedLabelColor = Color.Black,
                                    containerColor = MidnightSurfaceVariant,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("Notes (e.g. Call guard on arrival)") },
                        modifier = Modifier.fillMaxWidth().testTag("frequent_notes_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = MidnightCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (titleInput.isNotBlank() && addressInput.isNotBlank()) {
                                onAddDestination(
                                    titleInput.trim(),
                                    addressInput.trim(),
                                    selectedCategory,
                                    selectedMode,
                                    notesInput.trim()
                                )
                                titleInput = ""
                                addressInput = ""
                                notesInput = ""
                                showAddForm = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_frequent_destination_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save to Room Database", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // List of Saved Frequent Destinations
            if (frequentDestinations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No saved trip planner destinations. Add one above!",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    frequentDestinations.forEach { dest ->
                        FrequentDestinationItemRow(
                            item = dest,
                            onQuickPlan = { onSelectDestination(dest) },
                            onDelete = { onDeleteDestination(dest.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FrequentDestinationItemRow(
    item: FrequentDestinationEntity,
    onQuickPlan: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MidnightBackground)
            .border(1.dp, MidnightCardBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        when (item.category) {
                            "Home" -> SafeGreen.copy(alpha = 0.2f)
                            "Campus" -> NeonCyan.copy(alpha = 0.2f)
                            "Nightlife" -> WarningAmber.copy(alpha = 0.2f)
                            else -> MidnightSurfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = when (item.category) {
                        "Home" -> SafeGreen
                        "Campus" -> NeonCyan
                        "Nightlife" -> WarningAmber
                        else -> TextPrimary
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.title,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MidnightSurfaceVariant)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.category,
                            color = TextSecondary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Text(
                    text = item.address,
                    color = TextMuted,
                    fontSize = 11.sp
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val modeIcon = when (item.preferredMode) {
                        "HYBRID" -> Icons.Default.ElectricBolt
                        "TRANSIT_WALK" -> Icons.Default.DirectionsBus
                        "RIDESHARE" -> Icons.Default.DirectionsCar
                        else -> Icons.Default.DirectionsRun
                    }
                    val modeColor = when (item.preferredMode) {
                        "HYBRID" -> WarningAmber
                        "TRANSIT_WALK" -> NeonCyan
                        "RIDESHARE" -> Color(0xFFC084FC)
                        else -> SafeGreen
                    }

                    Icon(
                        imageVector = modeIcon,
                        contentDescription = null,
                        tint = modeColor,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Prefers: ${item.preferredMode.replace("_", " ")}",
                        color = modeColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (item.notes.isNotBlank()) {
                    Text(
                        text = "Note: ${item.notes}",
                        color = TextMuted,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(NeonCyan.copy(alpha = 0.15f))
                    .clickable { onQuickPlan() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "1-Tap Plan",
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete frequent destination",
                    tint = DangerRed.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
