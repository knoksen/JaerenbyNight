package com.example.data.model

enum class TransportMode(val displayName: String, val iconResName: String) {
    ALL("All Modes", "ic_all"),
    HYBRID("Hybrid (Transit + Ride)", "ic_hybrid"),
    TRANSIT_WALK("Transit & Walk", "ic_bus"),
    RIDESHARE("Ride-Share Direct", "ic_car"),
    SAFE_WALK("Safe Lit Walkway", "ic_walk")
}

enum class LightLevel(val label: String, val description: String) {
    HIGHLY_LIT("Well-Lit Corridor", "Continuous LED streetlights & active storefronts"),
    MODERATE_LIT("Moderately Lit", "Standard street lamps with occasional dim patches"),
    WELL_LIT_WITH_PATROLS("Lit & Monitored", "CCTV coverage & active night patrol area")
}

enum class CrowdLevel(val label: String) {
    HIGH_FOOT_TRAFFIC("High Nightlife Traffic"),
    MODERATE("Moderate Foot Traffic"),
    QUIET("Quiet Residential Area")
}

enum class StepType {
    WALKING,
    BUS,
    SUBWAY,
    RIDESHARE,
    TRANSFER,
    SAFE_HAVEN
}
