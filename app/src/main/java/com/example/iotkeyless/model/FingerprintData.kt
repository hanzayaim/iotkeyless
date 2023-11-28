package com.example.iotkeyless.model

class FingerprintData {
    var fingerprintId: Int? = null
    var fingerprintName: String? = null

    constructor(fingerprintId: Int?, fingerprintName: String?) {
        this.fingerprintId = fingerprintId
        this.fingerprintName = fingerprintName
    }

    constructor() {}
}