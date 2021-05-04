package com.imdatcandan.vehicles.model

import com.google.android.gms.maps.model.BitmapDescriptorFactory.*

enum class State(val iconColor: Float) {
    DAMAGED(HUE_RED),
    LOW_BATTERY(HUE_ORANGE),
    MAINTENANCE(HUE_YELLOW),
    LAST_SEARCH(HUE_CYAN),
    ACTIVE(HUE_GREEN),
    LOST(HUE_BLUE),
    MISSING(HUE_VIOLET),
    OUT_OF_ORDER(HUE_MAGENTA),
    GPS_ISSUE(HUE_ROSE),
    ALL(HUE_AZURE),
}