package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LocationSpot
import com.example.data.model.RouteOption
import com.example.data.model.TransportMode
import com.example.ui.components.RouteMapCanvas
import com.example.ui.components.RouteOptionCard
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
fun RoutePlannerTab(
    viewModel: NightViewModel,
    origin: LocationSpot,
    destination: LocationSpot,
    selectedTimeSlot: String,
    selectedModeFilter: TransportMode,
    filteredRoutes: List<RouteOption>,
    selectedRoute: RouteOption?,
    popularLocations: List<LocationSpot>,
    onStartTrip: (RouteOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var showOriginMenu by remember { mutableStateOf(false) }
    var showDestMenu by remember { mutableStateOf(false) }
    var showTimeMenu by remember { mutableStateOf(false) }

    val timeSlots = listOf("Friday 1:15 AM", "Friday 2:30 AM", "Saturday 1:00 AM", "Saturday 2:45 AM", "Sunday 12:30 AM")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightBackground),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Frosted Glass Top Search & Profile Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(MidnightSurfaceVariant.copy(alpha = 0.6f))
                        .border(1.dp, MidnightCardBorder, CircleShape)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🔍", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Find a safe route home...",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(NeonCyan.copy(alpha = 0.3f))
                        .border(1.5.dp, NeonCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JD",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Hero Header Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_night_city),
                    contentDescription = "Night City Skyline",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MidnightBackground.copy(alpha = 0.7f),
                                    MidnightBackground
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "WEEKEND NIGHT COMMUTE",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Safe Local Travel Guide",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Location & Time Pickers Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MidnightSurface)
                    .border(1.dp, MidnightCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PLAN COMMUTE",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Schedule Selector Dropdown
                    Box {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(MidnightSurfaceVariant)
                                .clickable { showTimeMenu = true }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                .testTag("time_slot_selector"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Schedule Time",
                                tint = NeonCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = selectedTimeSlot, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }

                        DropdownMenu(
                            expanded = showTimeMenu,
                            onDismissRequest = { showTimeMenu = false },
                            modifier = Modifier.background(MidnightSurface)
                        ) {
                            timeSlots.forEach { slot ->
                                DropdownMenuItem(
                                    text = { Text(slot, color = TextPrimary) },
                                    onClick = {
                                        viewModel.setTimeSlot(slot)
                                        showTimeMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Origin Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MidnightSurfaceVariant)
                        .clickable { showOriginMenu = true }
                        .padding(12.dp)
                        .testTag("origin_location_selector"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Nightlife, contentDescription = "Origin", tint = NeonCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Starting Point (Origin)", color = TextMuted, fontSize = 10.sp)
                        Text(text = origin.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "Change", color = NeonCyan, fontSize = 11.sp)

                    DropdownMenu(
                        expanded = showOriginMenu,
                        onDismissRequest = { showOriginMenu = false },
                        modifier = Modifier.background(MidnightSurface)
                    ) {
                        popularLocations.forEach { loc ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(loc.name, color = TextPrimary, fontWeight = FontWeight.Bold)
                                        Text(loc.category, color = TextMuted, fontSize = 10.sp)
                                    }
                                },
                                onClick = {
                                    viewModel.setOrigin(loc)
                                    showOriginMenu = false
                                }
                            )
                        }
                    }
                }

                // Swap Icon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            val temp = origin
                            viewModel.setOrigin(destination)
                            viewModel.setDestination(temp)
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MidnightCardBorder)
                            .testTag("swap_locations_button")
                    ) {
                        Icon(imageVector = Icons.Default.SwapVert, contentDescription = "Swap Locations", tint = NeonCyan, modifier = Modifier.size(18.dp))
                    }
                }

                // Destination Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MidnightSurfaceVariant)
                        .clickable { showDestMenu = true }
                        .padding(12.dp)
                        .testTag("destination_location_selector"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Place, contentDescription = "Destination", tint = SafeGreen, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Destination", color = TextMuted, fontSize = 10.sp)
                        Text(text = destination.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "Change", color = SafeGreen, fontSize = 11.sp)

                    DropdownMenu(
                        expanded = showDestMenu,
                        onDismissRequest = { showDestMenu = false },
                        modifier = Modifier.background(MidnightSurface)
                    ) {
                        popularLocations.forEach { loc ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(loc.name, color = TextPrimary, fontWeight = FontWeight.Bold)
                                        Text(loc.category, color = TextMuted, fontSize = 10.sp)
                                    }
                                },
                                onClick = {
                                    viewModel.setDestination(loc)
                                    showDestMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Mode Filter Chips
        item {
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(TransportMode.entries.toTypedArray()) { mode ->
                    val isSelected = selectedModeFilter == mode
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setModeFilter(mode) },
                        label = { Text(mode.displayName, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonCyan,
                            selectedLabelColor = MidnightBackground,
                            containerColor = MidnightSurface,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = MidnightCardBorder,
                            enabled = true,
                            selected = isSelected
                        ),
                        modifier = Modifier.testTag("mode_filter_chip_${mode.name}")
                    )
                }
            }
        }

        // Route Map Preview Canvas
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "VISUAL COMMUTE MAP",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                RouteMapCanvas(
                    origin = origin,
                    destination = destination,
                    selectedRoute = selectedRoute
                )
            }
        }

        // Route Options Section Header
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MULTIMODAL NIGHT ROUTES (${filteredRoutes.size})",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Sorted by Safety & Speed",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // List of Route Options
        items(filteredRoutes, key = { it.id }) { route ->
            val isSelected = selectedRoute?.id == route.id
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                RouteOptionCard(
                    route = route,
                    isSelected = isSelected,
                    onSelect = { viewModel.selectRoute(route) },
                    onStartTrip = { onStartTrip(route) },
                    onSaveRoute = { viewModel.saveRouteToDb(route) }
                )
            }
        }
    }
}
