package com.example.ui.components

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LocationSpot
import com.example.data.model.RouteOption
import com.example.data.model.TransportMode
import com.example.ui.theme.DangerRed
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

enum class MarkerCategory {
    ALL, RIDESHARE, TRANSIT, SAFE_HAVEN
}

data class RealtimeMapMarker(
    val id: String,
    val title: String,
    val snippet: String,
    val latLng: LatLng,
    val category: MarkerCategory,
    val waitTimeOrFrequency: String,
    val safetyScore: String,
    val isLit: Boolean = true
)

@Composable
fun RouteMapCanvas(
    origin: LocationSpot,
    destination: LocationSpot,
    selectedRoute: RouteOption?,
    modifier: Modifier = Modifier
) {
    var useGoogleMapsView by remember { mutableStateOf(true) }
    var selectedFilterCategory by remember { mutableStateOf(MarkerCategory.ALL) }
    var activeMarker by remember { mutableStateOf<RealtimeMapMarker?>(null) }
    var isSatelliteView by remember { mutableStateOf(false) }

    // Coordinates setup
    val originLatLng = remember(origin) { LatLng(origin.latitude, origin.longitude) }
    val destLatLng = remember(destination) { LatLng(destination.latitude, destination.longitude) }

    // Real-time Rideshare pickup points & Public transit stations surrounding origin & destination
    val realTimeMarkers = remember(origin, destination) {
        val midLat = (origin.latitude + destination.latitude) / 2
        val midLng = (origin.longitude + destination.longitude) / 2

        listOf(
            RealtimeMapMarker(
                id = "m_rs_1",
                title = "Uber/Lyft Safe Pickup Bay A",
                snippet = "742 Main St • Monitored Security Cameras & Lighting",
                latLng = LatLng(origin.latitude + 0.0015, origin.longitude + 0.0012),
                category = MarkerCategory.RIDESHARE,
                waitTimeOrFrequency = "3 min wait",
                safetyScore = "98% Safety Score"
            ),
            RealtimeMapMarker(
                id = "m_rs_2",
                title = "Grand Ave Night Rideshare Station",
                snippet = "Grand & 4th • Illuminated Curb & Emergency Pillar",
                latLng = LatLng(midLat - 0.0010, midLng + 0.0020),
                category = MarkerCategory.RIDESHARE,
                waitTimeOrFrequency = "4 min wait",
                safetyScore = "96% Safety Score"
            ),
            RealtimeMapMarker(
                id = "m_tr_1",
                title = "Central Metro Hub (Night Line N1)",
                snippet = "Platform 2 • 24/7 Security Patrol Onsite",
                latLng = LatLng(midLat + 0.0018, midLng - 0.0015),
                category = MarkerCategory.TRANSIT,
                waitTimeOrFrequency = "Departs in 6 min",
                safetyScore = "99% Safety Score"
            ),
            RealtimeMapMarker(
                id = "m_tr_2",
                title = "North Campus Night Bus Terminal (N40)",
                snippet = "College Way • High Visibility Bus Shelter",
                latLng = LatLng(destination.latitude - 0.0012, destination.longitude - 0.0018),
                category = MarkerCategory.TRANSIT,
                waitTimeOrFrequency = "Every 10 min",
                safetyScore = "95% Safety Score"
            ),
            RealtimeMapMarker(
                id = "m_sh_1",
                title = "Safe Haven Kiosk #14",
                snippet = "5th Ave Corner • 24/7 Intercom & SOS Button",
                latLng = LatLng(midLat - 0.0005, midLng - 0.0008),
                category = MarkerCategory.SAFE_HAVEN,
                waitTimeOrFrequency = "Open 24/7",
                safetyScore = "100% Guarded"
            )
        )
    }

    val filteredMarkers = remember(realTimeMarkers, selectedFilterCategory) {
        if (selectedFilterCategory == MarkerCategory.ALL) {
            realTimeMarkers
        } else {
            realTimeMarkers.filter { it.category == selectedFilterCategory }
        }
    }

    // Camera position centered between origin and destination
    val cameraPositionState = rememberCameraPositionState {
        val centerLat = (origin.latitude + destination.latitude) / 2
        val centerLng = (origin.longitude + destination.longitude) / 2
        position = CameraPosition.fromLatLngZoom(LatLng(centerLat, centerLng), 14.5f)
    }

    // Keep camera updated if origin or destination changes
    LaunchedEffect(origin, destination) {
        val centerLat = (origin.latitude + destination.latitude) / 2
        val centerLng = (origin.longitude + destination.longitude) / 2
        cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(centerLat, centerLng), 14.5f)
    }

    val routeColor = when (selectedRoute?.modeType) {
        TransportMode.HYBRID -> NeonCyan
        TransportMode.TRANSIT_WALK -> NeonViolet
        TransportMode.RIDESHARE -> WarningAmber
        TransportMode.SAFE_WALK -> SafeGreen
        else -> NeonCyan
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, MidnightCardBorder, RoundedCornerShape(20.dp))
            .testTag("route_google_maps_card"),
        colors = CardDefaults.cardColors(containerColor = MidnightSurface)
    ) {
        Column {
            // Header Controls Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MidnightSurfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (useGoogleMapsView) Icons.Default.Map else Icons.Default.Radar,
                        contentDescription = "Map Mode",
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (useGoogleMapsView) "GOOGLE MAPS LIVE ROUTE" else "RADAR VECTOR MAP",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (useGoogleMapsView) {
                        // Satellite Toggle
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSatelliteView) NeonCyan else MidnightBackground)
                                .clickable { isSatelliteView = !isSatelliteView }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isSatelliteView) "Satellite" else "Vector",
                                color = if (isSatelliteView) Color.Black else TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    // Mode Toggle (Google Maps vs Vector Radar)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MidnightBackground)
                            .border(1.dp, MidnightCardBorder, RoundedCornerShape(6.dp))
                            .clickable { useGoogleMapsView = !useGoogleMapsView }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (useGoogleMapsView) "Switch to Radar" else "Switch to Maps",
                            color = NeonCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Category Filter Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MidnightBackground)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LAYERS:",
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )

                FilterChip(
                    selected = selectedFilterCategory == MarkerCategory.ALL,
                    onClick = { selectedFilterCategory = MarkerCategory.ALL },
                    label = { Text("All", fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = MidnightSurfaceVariant,
                        labelColor = TextSecondary
                    )
                )

                FilterChip(
                    selected = selectedFilterCategory == MarkerCategory.RIDESHARE,
                    onClick = { selectedFilterCategory = MarkerCategory.RIDESHARE },
                    label = { Text("🚗 Rideshare", fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = WarningAmber,
                        selectedLabelColor = Color.Black,
                        containerColor = MidnightSurfaceVariant,
                        labelColor = TextSecondary
                    )
                )

                FilterChip(
                    selected = selectedFilterCategory == MarkerCategory.TRANSIT,
                    onClick = { selectedFilterCategory = MarkerCategory.TRANSIT },
                    label = { Text("🚌 Transit", fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonViolet,
                        selectedLabelColor = Color.White,
                        containerColor = MidnightSurfaceVariant,
                        labelColor = TextSecondary
                    )
                )

                FilterChip(
                    selected = selectedFilterCategory == MarkerCategory.SAFE_HAVEN,
                    onClick = { selectedFilterCategory = MarkerCategory.SAFE_HAVEN },
                    label = { Text("🛡️ Havens", fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SafeGreen,
                        selectedLabelColor = Color.Black,
                        containerColor = MidnightSurfaceVariant,
                        labelColor = TextSecondary
                    )
                )
            }

            // Map Display Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                if (useGoogleMapsView) {
                    GoogleMapViewContainer(
                        originLatLng = originLatLng,
                        destLatLng = destLatLng,
                        originName = origin.name,
                        destName = destination.name,
                        selectedRoute = selectedRoute,
                        routeColor = routeColor,
                        markers = filteredMarkers,
                        cameraPositionState = cameraPositionState,
                        isSatelliteView = isSatelliteView,
                        onMarkerClicked = { marker -> activeMarker = marker }
                    )
                } else {
                    FallbackVectorCanvasMap(
                        origin = origin,
                        destination = destination,
                        selectedRoute = selectedRoute,
                        routeColor = routeColor,
                        markers = filteredMarkers,
                        onMarkerClicked = { marker -> activeMarker = marker }
                    )
                }

                // Dynamic Countdown Overlay estimating combined ride-share & public transit wait time
                DynamicConnectionCountdownOverlay(
                    selectedRoute = selectedRoute,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )

                // Selected Marker Info Popup Card
                androidx.compose.animation.AnimatedVisibility(
                    visible = activeMarker != null,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(10.dp)
                ) {
                    activeMarker?.let { marker ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, NeonCyan, RoundedCornerShape(14.dp)),
                            colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.95f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when (marker.category) {
                                                    MarkerCategory.RIDESHARE -> WarningAmber.copy(alpha = 0.2f)
                                                    MarkerCategory.TRANSIT -> NeonViolet.copy(alpha = 0.2f)
                                                    MarkerCategory.SAFE_HAVEN -> SafeGreen.copy(alpha = 0.2f)
                                                    else -> NeonCyan.copy(alpha = 0.2f)
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = when (marker.category) {
                                                MarkerCategory.RIDESHARE -> Icons.Default.DirectionsCar
                                                MarkerCategory.TRANSIT -> Icons.Default.DirectionsBus
                                                MarkerCategory.SAFE_HAVEN -> Icons.Default.Security
                                                else -> Icons.Default.Place
                                            },
                                            contentDescription = null,
                                            tint = when (marker.category) {
                                                MarkerCategory.RIDESHARE -> WarningAmber
                                                MarkerCategory.TRANSIT -> NeonViolet
                                                MarkerCategory.SAFE_HAVEN -> SafeGreen
                                                else -> NeonCyan
                                            },
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
                                        Text(
                                            text = marker.title,
                                            color = TextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = marker.snippet,
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                        Row(
                                            modifier = Modifier.padding(top = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "⏱️ ${marker.waitTimeOrFrequency}",
                                                color = NeonCyan,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "🛡️ ${marker.safetyScore}",
                                                color = SafeGreen,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                IconButton(
                                    onClick = { activeMarker = null },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close Marker Info",
                                        tint = TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Real-Time Waypoints Route Legend Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MidnightSurfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(NeonCyan)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Origin", color = TextSecondary, fontSize = 10.sp)

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(WarningAmber)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Rideshare Point", color = TextSecondary, fontSize = 10.sp)

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(NeonViolet)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Transit Station", color = TextSecondary, fontSize = 10.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(SafeGreen)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Destination", color = TextSecondary, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun GoogleMapViewContainer(
    originLatLng: LatLng,
    destLatLng: LatLng,
    originName: String,
    destName: String,
    selectedRoute: RouteOption?,
    routeColor: Color,
    markers: List<RealtimeMapMarker>,
    cameraPositionState: CameraPositionState,
    isSatelliteView: Boolean,
    onMarkerClicked: (RealtimeMapMarker) -> Unit
) {
    val mapProperties = remember(isSatelliteView) {
        MapProperties(
            mapType = if (isSatelliteView) MapType.SATELLITE else MapType.NORMAL,
            isMyLocationEnabled = false
        )
    }

    val uiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = true,
            myLocationButtonEnabled = false,
            mapToolbarEnabled = false
        )
    }

    // Polyline Waypoints between Origin, intermediate Transit / Rideshare stations, and Destination
    val polylinePoints = remember(originLatLng, destLatLng, markers) {
        val midLat = (originLatLng.latitude + destLatLng.latitude) / 2
        val midLng = (originLatLng.longitude + destLatLng.longitude) / 2

        val intermediateWaypoints = markers.take(2).map { it.latLng }

        buildList {
            add(originLatLng)
            addAll(intermediateWaypoints)
            add(destLatLng)
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings
    ) {
        // Draw Polyline for selected route
        Polyline(
            points = polylinePoints,
            color = routeColor,
            width = 12f
        )

        // Marker for Origin
        Marker(
            state = MarkerState(position = originLatLng),
            title = "Start: $originName",
            snippet = "Origin Spot",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
        )

        // Marker for Destination
        Marker(
            state = MarkerState(position = destLatLng),
            title = "Destination: $destName",
            snippet = "End Point",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        )

        // Real-time Rideshare & Transit Markers
        markers.forEach { markerItem ->
            val hue = when (markerItem.category) {
                MarkerCategory.RIDESHARE -> BitmapDescriptorFactory.HUE_ORANGE
                MarkerCategory.TRANSIT -> BitmapDescriptorFactory.HUE_VIOLET
                MarkerCategory.SAFE_HAVEN -> BitmapDescriptorFactory.HUE_AZURE
                else -> BitmapDescriptorFactory.HUE_YELLOW
            }

            Marker(
                state = MarkerState(position = markerItem.latLng),
                title = markerItem.title,
                snippet = "${markerItem.snippet} • ${markerItem.waitTimeOrFrequency}",
                icon = BitmapDescriptorFactory.defaultMarker(hue),
                onClick = {
                    onMarkerClicked(markerItem)
                    false // return false so info window shows or custom callback fires
                }
            )
        }
    }
}

@Composable
private fun FallbackVectorCanvasMap(
    origin: LocationSpot,
    destination: LocationSpot,
    selectedRoute: RouteOption?,
    routeColor: Color,
    markers: List<RealtimeMapMarker>,
    onMarkerClicked: (RealtimeMapMarker) -> Unit
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
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightSurface)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Night grid
            val gridStep = 36.dp.toPx()
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

            // Canvas nodes for vector representation
            val startPt = Offset(width * 0.15f, height * 0.70f)
            val midPt1 = Offset(width * 0.40f, height * 0.35f)
            val midPt2 = Offset(width * 0.70f, height * 0.55f)
            val endPt = Offset(width * 0.88f, height * 0.25f)

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

            drawPath(
                path = path,
                color = routeColor.copy(alpha = 0.3f),
                style = Stroke(width = 14f)
            )

            drawPath(
                path = path,
                color = routeColor,
                style = Stroke(
                    width = 5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(25f, 15f), pulsePhase * 40f)
                )
            )

            // Rideshare Node (Mid 1)
            drawCircle(color = WarningAmber.copy(alpha = 0.3f * (1f - pulsePhase)), radius = 20.dp.toPx() * pulsePhase + 4.dp.toPx(), center = midPt1)
            drawCircle(color = WarningAmber, radius = 7.dp.toPx(), center = midPt1)
            drawCircle(color = Color.Black, radius = 3.dp.toPx(), center = midPt1)

            // Transit Hub Node (Mid 2)
            drawCircle(color = NeonViolet.copy(alpha = 0.3f * (1f - pulsePhase)), radius = 20.dp.toPx() * pulsePhase + 4.dp.toPx(), center = midPt2)
            drawCircle(color = NeonViolet, radius = 7.dp.toPx(), center = midPt2)
            drawCircle(color = Color.White, radius = 3.dp.toPx(), center = midPt2)

            // Origin Pin
            drawCircle(color = NeonCyan.copy(alpha = 0.25f), radius = 18.dp.toPx(), center = startPt)
            drawCircle(color = NeonCyan, radius = 8.dp.toPx(), center = startPt)
            drawCircle(color = Color.White, radius = 3.dp.toPx(), center = startPt)

            // Destination Pin
            drawCircle(color = SafeGreen.copy(alpha = 0.25f), radius = 18.dp.toPx(), center = endPt)
            drawCircle(color = SafeGreen, radius = 8.dp.toPx(), center = endPt)
            drawCircle(color = Color.White, radius = 3.dp.toPx(), center = endPt)
        }

        // Clickable interactive hotspots for vector map
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MidnightSurfaceVariant.copy(alpha = 0.85f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(text = "START: ${origin.name}", color = NeonCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MidnightSurfaceVariant.copy(alpha = 0.85f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(text = "END: ${destination.name}", color = SafeGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DynamicConnectionCountdownOverlay(
    selectedRoute: RouteOption?,
    modifier: Modifier = Modifier
) {
    // Total wait estimate in seconds (default ~ 6 min 15 sec if no route, or route's wait time * 60)
    val totalInitialSeconds = remember(selectedRoute) {
        val waitMins = (selectedRoute?.totalDurationMinutes ?: 12) / 2
        waitMins * 60 + 15
    }

    var remainingSeconds by remember(selectedRoute) { mutableStateOf(totalInitialSeconds) }
    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
        } else {
            // Auto refresh cycle when countdown reaches zero
            remainingSeconds = totalInitialSeconds
        }
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    val rideshareSeconds = (remainingSeconds * 0.55).toInt()
    val transitSeconds = remainingSeconds - rideshareSeconds
    val rideshareFormatted = String.format("%02dm %02ds", rideshareSeconds / 60, rideshareSeconds % 60)
    val transitFormatted = String.format("%02dm %02ds", transitSeconds / 60, transitSeconds % 60)

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, NeonCyan.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
            .clickable { isExpanded = !isExpanded }
            .testTag("dynamic_countdown_overlay"),
        colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.94f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (remainingSeconds < 60) DangerRed else WarningAmber)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "EST. WAIT:",
                        color = TextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formattedTime,
                        color = NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (isExpanded) "▲ Hide" else "▼ Details",
                    color = TextMuted,
                    fontSize = 9.sp
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MidnightBackground)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "REAL-TIME CONNECTION SPLIT",
                        color = NeonCyan,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "🚗 Rideshare Pickup ETA:", color = TextSecondary, fontSize = 10.sp)
                        Text(text = rideshareFormatted, color = WarningAmber, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "🚌 Public Transit Departure:", color = TextSecondary, fontSize = 10.sp)
                        Text(text = transitFormatted, color = NeonViolet, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "⚡ Safe Connection Buffer:", color = TextSecondary, fontSize = 10.sp)
                        Text(text = "+3.5 min buffer", color = SafeGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
