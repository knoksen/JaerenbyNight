package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.CachedMapTileEntity
import com.example.data.db.CachedTransitScheduleEntity
import com.example.data.db.EmergencyContactEntity
import com.example.data.db.FrequentDestinationEntity
import com.example.data.db.NightDatabase
import com.example.data.db.NightSafetyLogEntity
import com.example.data.db.SavedRouteEntity
import com.example.data.model.LocationSpot
import com.example.data.model.RouteOption
import com.example.data.model.TransportMode
import com.example.data.repository.NightRouteRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NightViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NightRouteRepository

    init {
        val database = NightDatabase.getDatabase(application)
        repository = NightRouteRepository(database.nightDao())

        // Seed default emergency contacts & frequent destinations if empty
        viewModelScope.launch {
            repository.emergencyContacts.collect { contacts ->
                if (contacts.isEmpty()) {
                    repository.addEmergencyContact(
                        EmergencyContactEntity(
                            name = "Alex (Trusted Buddy)",
                            phone = "+1 (555) 019-2834",
                            relationship = "Friend & Roommate",
                            isPrimary = true
                        )
                    )
                    repository.addEmergencyContact(
                        EmergencyContactEntity(
                            name = "Campus Night Helpline",
                            phone = "+1 (555) 010-9911",
                            relationship = "Campus Security Patrol",
                            isPrimary = false
                        )
                    )
                }
            }
        }

        viewModelScope.launch {
            repository.frequentDestinations.collect { list ->
                if (list.isEmpty()) {
                    repository.addFrequentDestination(
                        FrequentDestinationEntity(
                            title = "Oakridge Heights (Home)",
                            address = "314 Oakridge Rd, Residential Safe Area",
                            category = "Home",
                            preferredMode = "HYBRID",
                            latitude = 37.7620,
                            longitude = -122.4350,
                            notes = "Primary residence • Lit entry path"
                        )
                    )
                    repository.addFrequentDestination(
                        FrequentDestinationEntity(
                            title = "University Student Library",
                            address = "500 College Way, North Campus",
                            category = "Campus",
                            preferredMode = "TRANSIT_WALK",
                            latitude = 37.7830,
                            longitude = -122.4080,
                            notes = "Late night study desk"
                        )
                    )
                    repository.addFrequentDestination(
                        FrequentDestinationEntity(
                            title = "Downtown Neon Square",
                            address = "742 Main St & 5th Ave",
                            category = "Nightlife",
                            preferredMode = "RIDESHARE",
                            latitude = 37.7749,
                            longitude = -122.4194,
                            notes = "Weekend meetups & dining"
                        )
                    )
                }
            }
        }

        viewModelScope.launch {
            repository.cachedTransitSchedules.collect { list ->
                if (list.isEmpty()) {
                    repository.cacheTransitSchedule(
                        CachedTransitScheduleEntity(
                            stationId = "st_central_metro",
                            stationName = "Central Metro Terminal",
                            lineCode = "Night Line N1",
                            nextDeparturesJson = "1:28 AM, 1:45 AM, 2:05 AM, 2:30 AM",
                            isOfflineAvailable = true
                        )
                    )
                    repository.cacheTransitSchedule(
                        CachedTransitScheduleEntity(
                            stationId = "st_campus_bus",
                            stationName = "North Campus Terminal",
                            lineCode = "Night Bus N40",
                            nextDeparturesJson = "1:25 AM, 1:35 AM, 1:45 AM, 2:00 AM",
                            isOfflineAvailable = true
                        )
                    )
                    repository.cacheTransitSchedule(
                        CachedTransitScheduleEntity(
                            stationId = "st_grand_ave",
                            stationName = "Grand & 4th Rideshare Hub",
                            lineCode = "Uber/Lyft Safe Zone",
                            nextDeparturesJson = "Continuous ~3 min wait",
                            isOfflineAvailable = true
                        )
                    )
                }
            }
        }

        viewModelScope.launch {
            repository.cachedMapTiles.collect { list ->
                if (list.isEmpty()) {
                    repository.cacheMapTile(
                        CachedMapTileEntity(
                            tileKey = "z14_x4124_y6210",
                            regionName = "Downtown & Central Transit District",
                            zoomLevel = 14,
                            dataSizeBytes = 384000,
                            isOfflineReady = true
                        )
                    )
                    repository.cacheMapTile(
                        CachedMapTileEntity(
                            tileKey = "z14_x4125_y6211",
                            regionName = "North Campus & University Corridor",
                            zoomLevel = 14,
                            dataSizeBytes = 412000,
                            isOfflineReady = true
                        )
                    )
                    repository.cacheMapTile(
                        CachedMapTileEntity(
                            tileKey = "z15_x8250_y12422",
                            regionName = "Oakridge Heights Residential Vector Tile",
                            zoomLevel = 15,
                            dataSizeBytes = 256000,
                            isOfflineReady = true
                        )
                    )
                }
            }
        }
    }

    val popularLocations: List<LocationSpot> = repository.getPopularLocations()

    private val _origin = MutableStateFlow(popularLocations[0])
    val origin: StateFlow<LocationSpot> = _origin.asStateFlow()

    private val _destination = MutableStateFlow(popularLocations[5])
    val destination: StateFlow<LocationSpot> = _destination.asStateFlow()

    private val _selectedTimeSlot = MutableStateFlow("Friday 1:15 AM")
    val selectedTimeSlot: StateFlow<String> = _selectedTimeSlot.asStateFlow()

    private val _selectedModeFilter = MutableStateFlow(TransportMode.ALL)
    val selectedModeFilter: StateFlow<TransportMode> = _selectedModeFilter.asStateFlow()

    private val _routes = MutableStateFlow<List<RouteOption>>(emptyList())

    val filteredRoutes: StateFlow<List<RouteOption>> = combine(_routes, _selectedModeFilter) { routes, filter ->
        if (filter == TransportMode.ALL) routes
        else routes.filter { it.modeType == filter }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedRoute = MutableStateFlow<RouteOption?>(null)
    val selectedRoute: StateFlow<RouteOption?> = _selectedRoute.asStateFlow()

    // Active Trip State
    private val _activeTripRoute = MutableStateFlow<RouteOption?>(null)
    val activeTripRoute: StateFlow<RouteOption?> = _activeTripRoute.asStateFlow()

    private val _activeTripStepIndex = MutableStateFlow(0)
    val activeTripStepIndex: StateFlow<Int> = _activeTripStepIndex.asStateFlow()

    // Safety Timer State
    private val _safetyTimerTotalMinutes = MutableStateFlow(0)
    val safetyTimerTotalMinutes: StateFlow<Int> = _safetyTimerTotalMinutes.asStateFlow()

    private val _safetyTimerSecondsRemaining = MutableStateFlow(0)
    val safetyTimerSecondsRemaining: StateFlow<Int> = _safetyTimerSecondsRemaining.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _timerExpiredAlert = MutableStateFlow(false)
    val timerExpiredAlert: StateFlow<Boolean> = _timerExpiredAlert.asStateFlow()

    private var timerJob: Job? = null

    // Room Flows
    val savedRoutes: StateFlow<List<SavedRouteEntity>> = repository.savedRoutes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val emergencyContacts: StateFlow<List<EmergencyContactEntity>> = repository.emergencyContacts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val safetyLogs: StateFlow<List<NightSafetyLogEntity>> = repository.safetyLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val frequentDestinations: StateFlow<List<FrequentDestinationEntity>> = repository.frequentDestinations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cachedTransitSchedules: StateFlow<List<CachedTransitScheduleEntity>> = repository.cachedTransitSchedules
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cachedMapTiles: StateFlow<List<CachedMapTileEntity>> = repository.cachedMapTiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isOfflineModeActive = MutableStateFlow(false)
    val isOfflineModeActive: StateFlow<Boolean> = _isOfflineModeActive.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    private val _flashlightOn = MutableStateFlow(false)
    val flashlightOn: StateFlow<Boolean> = _flashlightOn.asStateFlow()

    init {
        // Initial route computation
        recalculateRoutes()
    }

    fun setOrigin(location: LocationSpot) {
        _origin.value = location
        recalculateRoutes()
    }

    fun setDestination(location: LocationSpot) {
        _destination.value = location
        recalculateRoutes()
    }

    fun setTimeSlot(time: String) {
        _selectedTimeSlot.value = time
        recalculateRoutes()
    }

    fun setModeFilter(filter: TransportMode) {
        _selectedModeFilter.value = filter
    }

    private fun recalculateRoutes() {
        val generated = repository.generateRoutes(_origin.value, _destination.value, _selectedTimeSlot.value)
        _routes.value = generated
        _selectedRoute.value = generated.firstOrNull { it.isRecommended } ?: generated.firstOrNull()
    }

    fun selectRoute(route: RouteOption) {
        _selectedRoute.value = route
    }

    fun startActiveTrip(route: RouteOption) {
        _activeTripRoute.value = route
        _activeTripStepIndex.value = 0
        // Automatically start a default safety timer for total route duration + 5 mins buffer
        startSafetyTimer(route.totalDurationMinutes + 5)
        _userMessage.value = "Active night commute started! Safety companion timer enabled."
    }

    fun nextTripStep() {
        val currentRoute = _activeTripRoute.value ?: return
        if (_activeTripStepIndex.value < currentRoute.steps.size - 1) {
            _activeTripStepIndex.value += 1
        } else {
            // Completed trip
            finishTrip()
        }
    }

    fun prevTripStep() {
        if (_activeTripStepIndex.value > 0) {
            _activeTripStepIndex.value -= 1
        }
    }

    fun finishTrip() {
        val route = _activeTripRoute.value
        if (route != null) {
            viewModelScope.launch {
                repository.logSafetyTrip(
                    NightSafetyLogEntity(
                        routeTitle = route.title,
                        commuteTimeMinutes = route.totalDurationMinutes,
                        safetyRatingStars = 5,
                        userFeedback = "Completed safely via Night Guide!"
                    )
                )
            }
        }
        _activeTripRoute.value = null
        _activeTripStepIndex.value = 0
        cancelSafetyTimer()
        _userMessage.value = "Arrived safely! Trip logged."
    }

    fun cancelActiveTrip() {
        _activeTripRoute.value = null
        _activeTripStepIndex.value = 0
        cancelSafetyTimer()
        _userMessage.value = "Active trip cancelled."
    }

    fun startSafetyTimer(minutes: Int) {
        timerJob?.cancel()
        _safetyTimerTotalMinutes.value = minutes
        _safetyTimerSecondsRemaining.value = minutes * 60
        _isTimerRunning.value = true
        _timerExpiredAlert.value = false

        timerJob = viewModelScope.launch {
            while (_safetyTimerSecondsRemaining.value > 0 && _isTimerRunning.value) {
                delay(1000L)
                _safetyTimerSecondsRemaining.value -= 1
            }
            if (_isTimerRunning.value && _safetyTimerSecondsRemaining.value == 0) {
                _isTimerRunning.value = false
                _timerExpiredAlert.value = true
            }
        }
    }

    fun dismissTimerAlert() {
        _timerExpiredAlert.value = false
    }

    fun cancelSafetyTimer() {
        timerJob?.cancel()
        _isTimerRunning.value = false
        _safetyTimerTotalMinutes.value = 0
        _safetyTimerSecondsRemaining.value = 0
        _timerExpiredAlert.value = false
    }

    fun toggleFlashlight() {
        _flashlightOn.value = !_flashlightOn.value
    }

    fun saveRouteToDb(route: RouteOption) {
        viewModelScope.launch {
            repository.saveRoute(
                SavedRouteEntity(
                    title = route.title,
                    origin = _origin.value.name,
                    destination = _destination.value.name,
                    modeType = route.modeType.name,
                    safetyScore = route.safetyScorePercent,
                    durationMinutes = route.totalDurationMinutes,
                    costEstimate = route.estimatedCostUSD,
                    notes = "Saved from Route Planner (${route.lightLevel.label})"
                )
            )
            _userMessage.value = "Route saved to Offline Night Guide!"
        }
    }

    fun deleteSavedRoute(id: Int) {
        viewModelScope.launch {
            repository.deleteSavedRoute(id)
            _userMessage.value = "Saved route removed."
        }
    }

    fun addEmergencyContact(name: String, phone: String, relationship: String, isPrimary: Boolean) {
        if (name.isBlank() || phone.isBlank()) return
        viewModelScope.launch {
            repository.addEmergencyContact(
                EmergencyContactEntity(
                    name = name,
                    phone = phone,
                    relationship = relationship.ifBlank { "Safety Contact" },
                    isPrimary = isPrimary
                )
            )
            _userMessage.value = "Added $name to safety contacts."
        }
    }

    fun deleteEmergencyContact(id: Int) {
        viewModelScope.launch {
            repository.deleteEmergencyContact(id)
            _userMessage.value = "Emergency contact removed."
        }
    }

    fun addFrequentDestination(
        title: String,
        address: String,
        category: String,
        preferredMode: String,
        notes: String = ""
    ) {
        if (title.isBlank() || address.isBlank()) return
        viewModelScope.launch {
            repository.addFrequentDestination(
                FrequentDestinationEntity(
                    title = title,
                    address = address,
                    category = category.ifBlank { "Favorite" },
                    preferredMode = preferredMode,
                    notes = notes
                )
            )
            _userMessage.value = "Added $title to frequent trip planner."
        }
    }

    fun deleteFrequentDestination(id: Int) {
        viewModelScope.launch {
            repository.deleteFrequentDestination(id)
            _userMessage.value = "Frequent destination removed."
        }
    }

    fun applyFrequentDestination(destinationEntity: FrequentDestinationEntity) {
        val spot = LocationSpot(
            id = "freq_${destinationEntity.id}",
            name = destinationEntity.title,
            category = destinationEntity.category,
            address = destinationEntity.address,
            isSafetyKioskNearby = true,
            latitude = destinationEntity.latitude,
            longitude = destinationEntity.longitude
        )
        _destination.value = spot
        
        // Also apply preferred mode filter if valid
        val mode = runCatching { TransportMode.valueOf(destinationEntity.preferredMode) }.getOrDefault(TransportMode.ALL)
        _selectedModeFilter.value = mode

        recalculateRoutes()
        _userMessage.value = "Applied ${destinationEntity.title} with preferred mode ${destinationEntity.preferredMode}!"
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun toggleOfflineMode() {
        _isOfflineModeActive.value = !_isOfflineModeActive.value
        val statusText = if (_isOfflineModeActive.value) "Offline Low-Connectivity Mode Enabled (Using Room Cache)" else "Online Live Data Restored"
        _userMessage.value = statusText
    }

    fun syncOfflineTransitAndTiles() {
        viewModelScope.launch {
            // Refresh/Store current route schedules and map tiles into Room
            val now = System.currentTimeMillis()
            repository.cacheTransitSchedule(
                CachedTransitScheduleEntity(
                    stationId = "st_sync_${now % 1000}",
                    stationName = "${_origin.value.name} Transit Shelter",
                    lineCode = "Night Express N-Sync",
                    nextDeparturesJson = "1:30 AM, 1:50 AM, 2:10 AM",
                    cachedAtTimestamp = now,
                    isOfflineAvailable = true
                )
            )
            repository.cacheMapTile(
                CachedMapTileEntity(
                    tileKey = "z14_x${(now / 100000) % 5000}_y${(now / 80000) % 5000}",
                    regionName = "${_origin.value.name} → ${_destination.value.name} Vector Corridor",
                    zoomLevel = 14,
                    cachedAtTimestamp = now,
                    dataSizeBytes = 450000,
                    isOfflineReady = true
                )
            )
            _userMessage.value = "Synced latest transit schedules & map tiles to Room database!"
        }
    }

    fun deleteCachedSchedule(stationId: String) {
        viewModelScope.launch {
            repository.deleteCachedTransitSchedule(stationId)
            _userMessage.value = "Removed schedule cache for $stationId."
        }
    }

    fun clearTileCache() {
        viewModelScope.launch {
            repository.clearAllMapTiles()
            _userMessage.value = "Map tiles cache cleared from Room DB."
        }
    }
}
