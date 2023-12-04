package com.example.iotkeyless.model

class FingerprintData {
    var fingerprintCheck: Boolean? = null
    var fingerprintId: Int? = null
    var fingerprintStatus: Boolean? = null

    constructor(fingerprintCheck: Boolean?, fingerprintId: Int?, fingerprintStatus: Boolean?) {
        this.fingerprintCheck = fingerprintCheck
        this.fingerprintId = fingerprintId
        this.fingerprintStatus = fingerprintStatus
    }

    constructor() {}
}