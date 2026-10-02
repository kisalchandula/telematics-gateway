import domain.TelemetryEvent
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class TelemetryEventTest {

    @Test
    fun `create telemetry event`() {

        val event = TelemetryEvent(
            imei = "123456789012345",
            timestamp = Instant.parse("2026-10-02T10:00:00Z"),
            latitude = 49.0069,
            longitude = 8.4037,
            altitude = 120,
            angle = 90,
            satellites = 10,
            speed = 50
        )

        assertEquals("123456789012345", event.imei)
        assertEquals(49.0069, event.latitude)
        assertEquals(8.4037, event.longitude)
        assertEquals(120, event.altitude)
        assertEquals(90, event.angle)
        assertEquals(10, event.satellites)
        assertEquals(50, event.speed)
    }
}