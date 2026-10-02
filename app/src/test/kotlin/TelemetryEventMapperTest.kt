import domain.TelemetryEventMapper
import protocol.AvlRecord
import protocol.GpsData
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import protocol.IoElement

class TelemetryEventMapperTest {

    @Test
    fun `map AVL record to telemetry event`() {

        val record = AvlRecord(
            gps = GpsData(
                timestamp = 1790935200000L,
                priority = 1,
                longitude = 8.4037,
                latitude = 49.0069,
                altitude = 120,
                angle = 90,
                satellites = 10,
                speed = 50
            )
        )

        val event = TelemetryEventMapper.map(
            imei = "123456789012345",
            record = record
        )

        assertEquals("123456789012345", event.imei)
        assertEquals(
            Instant.ofEpochMilli(1790935200000L),
            event.timestamp
        )
        assertEquals(49.0069, event.latitude)
        assertEquals(8.4037, event.longitude)
        assertEquals(120, event.altitude)
        assertEquals(90, event.angle)
        assertEquals(10, event.satellites)
        assertEquals(50, event.speed)
    }

    @Test
    fun `map AVL record with IO elements`() {

        val record = AvlRecord(
            gps = GpsData(
                timestamp = 1_700_000_000_000L,
                priority = 1,
                longitude = 8.0,
                latitude = 49.0,
                altitude = 120,
                angle = 90,
                satellites = 10,
                speed = 50
            ),
            eventId = 1,
            ioElements = listOf(
                IoElement(
                    id = 66,
                    value = 1L
                ),
                IoElement(
                    id = 67,
                    value = 500L
                )
            )
        )

        val event =
            TelemetryEventMapper.map(
                imei = "123456789012345",
                record = record
            )

        assertEquals(
            2,
            event.ioElements.size
        )

        assertEquals(
            66,
            event.ioElements[0].id
        )

        assertEquals(
            1L,
            event.ioElements[0].value
        )

        assertEquals(
            67,
            event.ioElements[1].id
        )

        assertEquals(
            500L,
            event.ioElements[1].value
        )
    }
}