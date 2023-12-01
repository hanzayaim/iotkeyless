package com.example.iotkeyless.model

class LocationData {
    var lat: Double? = null
    var long: Double? = null

    constructor(lat: Double?, long: Double?) {
        this.lat = lat
        this.long = long
    }

    constructor() {}
}