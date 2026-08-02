package com.example.data.model

data class SafeHaven(
    val name: String,
    val type: String, // "24/7 Police Kiosk", "Late Night Diner", "Lit Subway Hub Lounge", "24/7 Convenience Store"
    val address: String,
    val openStatus: String = "Open 24/7",
    val distMeters: Int
)

data class RouteStep(
    val id: String,
    val stepType: StepType,
    val instruction: String,
    val subtext: String,
    val durationMinutes: Int,
    val distanceKm: Double,
    val safetyTip: String? = null,
    val lightLevel: LightLevel = LightLevel.HIGHLY_LIT
)

data class RouteOption(
    val id: String,
    val title: String,
    val subtitle: String,
    val modeType: TransportMode,
    val totalDurationMinutes: Int,
    val estimatedCostUSD: Double,
    val safetyScorePercent: Int,
    val lightLevel: LightLevel,
    val crowdLevel: CrowdLevel,
    val departureTime: String,
    val arrivalTime: String,
    val steps: List<RouteStep>,
    val safeHavens: List<SafeHaven>,
    val isRecommended: Boolean = false,
    val safetyHighlights: List<String> = emptyList()
)
