package domain

import protocol.AvlRecord
import java.time.Instant

object TelemetryEventMapper {

    fun map(
        imei: String,
        record: AvlRecord
    ): TelemetryEvent {

        val gps = record.gps

        return TelemetryEvent(
            imei = imei,
            timestamp = Instant.ofEpochMilli(gps.timestamp),
            latitude = gps.latitude,
            longitude = gps.longitude,
            altitude = gps.altitude,
            angle = gps.angle,
            satellites = gps.satellites,
            speed = gps.speed,
            ioElements = record.ioElements
        )
    }
}