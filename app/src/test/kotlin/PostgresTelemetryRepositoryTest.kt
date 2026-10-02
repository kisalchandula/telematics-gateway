package persistence

import domain.TelemetryEvent
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import java.sql.DriverManager
import java.time.Instant
import kotlin.test.assertEquals

class PostgresTelemetryRepositoryTest {

    @Test
    fun `save telemetry event`() {

        PostgreSQLContainer("postgres:16-alpine").use { postgres ->

            postgres.start()

            DriverManager.getConnection(
                postgres.jdbcUrl,
                postgres.username,
                postgres.password
            ).use { connection ->

                val schema =
                    this::class.java
                        .classLoader
                        .getResource("schema.sql")!!
                        .readText()

                connection.createStatement().use { statement ->
                    statement.execute(schema)
                }
            }

            val repository =
                PostgresTelemetryRepository(
                    jdbcUrl = postgres.jdbcUrl,
                    username = postgres.username,
                    password = postgres.password
                )

            val event = TelemetryEvent(
                imei = "123456789012345",
                timestamp = Instant.parse("2026-10-02T12:00:00Z"),
                latitude = 49.0069,
                longitude = 8.4037,
                altitude = 120,
                angle = 90,
                satellites = 12,
                speed = 50
            )

            repository.save(event)

            val savedEvent =
                repository.findLatest()

            assertEquals(
                event,
                savedEvent
            )
        }
    }
}