package com.kote.martin.wats.model

class GooglePlaceDetails {
    val result: Result? = null
    val status: String? = null
    val error_message: String? = null
}

class Result {
    val geometry: Geometry? = null
    val name: String? = null
}

class Geometry {
    val location: Location? = null
}

class Location {
    val lat: Double? = null
    val lng: Double? = null
}



