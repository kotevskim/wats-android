package com.kote.martin.wats.model

class GooglePlaceDetails {
    val result: Result? = null

    // in case of error
    val status: String? = null
    val error_message: String? = null

    inner class Result {
        val geometry: Geometry? = null
        val name: String? = null
    }

    inner class Geometry {
        val location: Location? = null
    }

    inner class Location {
        val lat: Double? = null
        val lng: Double? = null
    }
}




