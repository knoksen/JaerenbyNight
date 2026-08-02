package com.example.data.repository

import com.example.data.db.EmergencyContactEntity
import com.example.data.db.NightDao
import com.example.data.db.NightSafetyLogEntity
import com.example.data.db.SavedRouteEntity
import com.example.data.model.CrowdLevel
import com.example.data.model.LightLevel
import com.example.data.model.LocationSpot
import com.example.data.model.RouteOption
import com.example.data.model.RouteStep
import com.example.data.model.SafeHaven
import com.example.data.model.StepType
import com.example.data.model.TransportMode
import kotlinx.coroutines.flow.Flow

class NightRouteRepository(private val dao: NightDao) {

    val savedRoutes: Flow<List<SavedRouteEntity>> = dao.getAllSavedRoutes()
    val emergencyContacts: Flow<List<EmergencyContactEntity>> = dao.getAllEmergencyContacts()
    val safetyLogs: Flow<List<NightSafetyLogEntity>> = dao.getAllSafetyLogs()

    suspend fun saveRoute(route: SavedRouteEntity) = dao.insertSavedRoute(route)
    suspend fun deleteSavedRoute(id: Int) = dao.deleteSavedRouteById(id)

    suspend fun addEmergencyContact(contact: EmergencyContactEntity) = dao.insertEmergencyContact(contact)
    suspend fun deleteEmergencyContact(id: Int) = dao.deleteEmergencyContactById(id)

    suspend fun logSafetyTrip(log: NightSafetyLogEntity) = dao.insertSafetyLog(log)

    fun getPopularLocations(): List<LocationSpot> {
        return listOf(
            LocationSpot("loc_1", "Downtown Neon Square", "Nightlife District", "742 Main St & 5th Ave", true, 37.7749, -122.4194),
            LocationSpot("loc_2", "Central Metro Terminal", "Transit Hub", "100 Grand Transit Ave", true, 37.7780, -122.4130),
            LocationSpot("loc_3", "Westside Club Plaza", "Entertainment Venue", "88 Westside Blvd", true, 37.7690, -122.4290),
            LocationSpot("loc_4", "University North Campus", "University Campus", "500 College Way", true, 37.7830, -122.4080),
            LocationSpot("loc_5", "East Harbor Wharf", "Night Market & Dining", "12 Pier Promenade", false, 37.7890, -122.4010),
            LocationSpot("loc_6", "Oakridge Heights (Home)", "Residential Area", "314 Oakridge Rd", true, 37.7620, -122.4350)
        )
    }

    fun generateRoutes(origin: LocationSpot, destination: LocationSpot, timeSlot: String = "Friday 1:15 AM"): List<RouteOption> {
        val safeHavenList = listOf(
            SafeHaven("24/7 Police Substation #4", "24/7 Police Kiosk", "200 Grand Transit Ave", "Open 24/7", 120),
            SafeHaven("Midnight Byte Diner & Cafe", "Late Night Diner", "145 Main St", "Open till 5:00 AM", 250),
            SafeHaven("7-Eleven Monitored Station", "24/7 Convenience Store", "88 College Way", "Open 24/7", 90),
            SafeHaven("Metro Security Information Desk", "Lit Transit Hub Lounge", "Central Platform B", "Open 24/7", 40)
        )

        val hybridRoute = RouteOption(
            id = "route_hybrid_1",
            title = "Recommended: Hybrid Night Express + Quick Ride",
            subtitle = "Uber short hop to Central Station → Night Metro Line N1 → 3 min lit walk",
            modeType = TransportMode.HYBRID,
            totalDurationMinutes = 22,
            estimatedCostUSD = 8.50,
            safetyScorePercent = 98,
            lightLevel = LightLevel.WELL_LIT_WITH_PATROLS,
            crowdLevel = CrowdLevel.HIGH_FOOT_TRAFFIC,
            departureTime = "1:20 AM",
            arrivalTime = "1:42 AM",
            isRecommended = true,
            safetyHighlights = listOf("CCTV Covered Transit Lounge", "Night Patrol Staff on Train", "24/7 Convenience Store at Dropoff"),
            safeHavens = safeHavenList,
            steps = listOf(
                RouteStep(
                    id = "s1",
                    stepType = StepType.RIDESHARE,
                    instruction = "Ride-Share (Uber/Lyft) to Central Metro Terminal",
                    subtext = "Board at well-lit designated rideshare zone outside ${origin.name}",
                    durationMinutes = 7,
                    distanceKm = 2.4,
                    safetyTip = "Confirm vehicle license plate & driver name before entering."
                ),
                RouteStep(
                    id = "s2",
                    stepType = StepType.SUBWAY,
                    instruction = "Board Night Metro Express Line N1 (Platform 2)",
                    subtext = "Departs 1:28 AM • Security guards present on trains",
                    durationMinutes = 12,
                    distanceKm = 6.8,
                    safetyTip = "Sit in the first car near the train conductor for maximum safety."
                ),
                RouteStep(
                    id = "s3",
                    stepType = StepType.WALKING,
                    instruction = "Walk along Monitored LED Corridor to ${destination.name}",
                    subtext = "Follow green LED sidewalk pavers • 24/7 Police Kiosk on corner",
                    durationMinutes = 3,
                    distanceKm = 0.25,
                    lightLevel = LightLevel.HIGHLY_LIT,
                    safetyTip = "High brightness lighting path; stay on marked blue-line sidewalk."
                )
            )
        )

        val transitWalkRoute = RouteOption(
            id = "route_transit_2",
            title = "Weekend Night Bus N12 & Safe Walk",
            subtitle = "Night Bus direct stop → 6 min well-lit boulevard walk",
            modeType = TransportMode.TRANSIT_WALK,
            totalDurationMinutes = 31,
            estimatedCostUSD = 2.75,
            safetyScorePercent = 92,
            lightLevel = LightLevel.HIGHLY_LIT,
            crowdLevel = CrowdLevel.MODERATE,
            departureTime = "1:22 AM",
            arrivalTime = "1:53 AM",
            isRecommended = false,
            safetyHighlights = listOf("Dedicated Night Bus Security", "Live GPS Bus Tracking", "Low Cost Option"),
            safeHavens = listOf(safeHavenList[1], safeHavenList[2]),
            steps = listOf(
                RouteStep(
                    id = "s1",
                    stepType = StepType.WALKING,
                    instruction = "Walk 2 mins to Night Bus Stop #104",
                    subtext = "Pass by open 24/7 Midnight Diner",
                    durationMinutes = 2,
                    distanceKm = 0.15,
                    safetyTip = "Wait inside the glass lit shelter."
                ),
                RouteStep(
                    id = "s2",
                    stepType = StepType.BUS,
                    instruction = "Ride Night Bus N12 towards Heights Boulevard",
                    subtext = "7 stops • Departs 1:25 AM",
                    durationMinutes = 23,
                    distanceKm = 7.1,
                    safetyTip = "Keep emergency SOS quick-dial ready on your phone."
                ),
                RouteStep(
                    id = "s3",
                    stepType = StepType.WALKING,
                    instruction = "Walk along Lit Avenue to ${destination.name}",
                    subtext = "Continuous streetlight coverage",
                    durationMinutes = 6,
                    distanceKm = 0.45,
                    safetyTip = "Avoid dark alleys, stay on main street."
                )
            )
        )

        val rideShareRoute = RouteOption(
            id = "route_rideshare_3",
            title = "Direct Door-to-Door Ride-Share",
            subtitle = "Direct Uber / Lyft pick-up to destination",
            modeType = TransportMode.RIDESHARE,
            totalDurationMinutes = 15,
            estimatedCostUSD = 18.50,
            safetyScorePercent = 95,
            lightLevel = LightLevel.HIGHLY_LIT,
            crowdLevel = CrowdLevel.QUIET,
            departureTime = "1:18 AM",
            arrivalTime = "1:33 AM",
            isRecommended = false,
            safetyHighlights = listOf("Direct Door to Door", "No Walking Required", "Share Live Location Feature"),
            safeHavens = listOf(safeHavenList[0]),
            steps = listOf(
                RouteStep(
                    id = "s1",
                    stepType = StepType.RIDESHARE,
                    instruction = "Direct Ride from ${origin.name} to ${destination.name}",
                    subtext = "Est. wait time: 3 mins • Pick up at lit curbside",
                    durationMinutes = 15,
                    distanceKm = 8.2,
                    safetyTip = "Enable Ride-Share PIN verification in your app."
                )
            )
        )

        val safeWalkRoute = RouteOption(
            id = "route_walk_4",
            title = "Monitored Night Pedestrian Path",
            subtitle = "100% well-lit pedestrian route with 24/7 safe haven stops",
            modeType = TransportMode.SAFE_WALK,
            totalDurationMinutes = 28,
            estimatedCostUSD = 0.0,
            safetyScorePercent = 89,
            lightLevel = LightLevel.WELL_LIT_WITH_PATROLS,
            crowdLevel = CrowdLevel.HIGH_FOOT_TRAFFIC,
            departureTime = "1:15 AM",
            arrivalTime = "1:43 AM",
            isRecommended = false,
            safetyHighlights = listOf("Zero Cost", "Continuous CCTV Patrols", "Emergency Help Buttons Every 200m"),
            safeHavens = safeHavenList,
            steps = listOf(
                RouteStep(
                    id = "s1",
                    stepType = StepType.WALKING,
                    instruction = "Walk north along Commercial Strip LED Walkway",
                    subtext = "Passing active nightlife storefronts & outdoor patios",
                    durationMinutes = 10,
                    distanceKm = 0.8,
                    safetyTip = "Stay on lit side of street."
                ),
                RouteStep(
                    id = "s2",
                    stepType = StepType.SAFE_HAVEN,
                    instruction = "Pass 24/7 Police Station Kiosk & Safety Refuge",
                    subtext = "Emergency phone box & public restroom available",
                    durationMinutes = 1,
                    distanceKm = 0.05,
                    safetyTip = "Safety beacon active."
                ),
                RouteStep(
                    id = "s3",
                    stepType = StepType.WALKING,
                    instruction = "Continue down Parkside Monitored Path to ${destination.name}",
                    subtext = "Bright LED lighting and emergency call boxes every 150m",
                    durationMinutes = 17,
                    distanceKm = 1.3,
                    safetyTip = "Walk with confidence and keep phone visible."
                )
            )
        )

        return listOf(hybridRoute, transitWalkRoute, rideShareRoute, safeWalkRoute)
    }
}
