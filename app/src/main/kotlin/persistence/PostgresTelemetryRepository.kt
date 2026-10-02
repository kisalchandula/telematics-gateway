package persistence

import domain.TelemetryEvent
import domain.TelemetryRepository
import java.sql.DriverManager

class PostgresTelemetryRepository(
    private val jdbcUrl: String,
    private val username: String,
    private val password: String
) : TelemetryRepository {

    override fun save(event: TelemetryEvent) {

        val sql = """
            INSERT INTO telemetry_events (
                imei,
                timestamp,
                latitude,
                longitude,
                altitude,
                angle,
                satellites,
                speed
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        DriverManager.getConnection(
            jdbcUrl,
            username,
            password
        ).use { connection ->

            connection.prepareStatement(sql).use { statement ->

                statement.setString(1, event.imei)
                statement.setTimestamp(
                    2,
                    java.sql.Timestamp.from(event.timestamp)
                )
                statement.setDouble(3, event.latitude)
                statement.setDouble(4, event.longitude)
                statement.setInt(5, event.altitude)
                statement.setInt(6, event.angle)
                statement.setInt(7, event.satellites)
                statement.setInt(8, event.speed)

                statement.executeUpdate()
            }
        }
    }

    fun findLatest(): TelemetryEvent? {

        val sql = """
        SELECT
            imei,
            timestamp,
            latitude,
            longitude,
            altitude,
            angle,
            satellites,
            speed
        FROM telemetry_events
        ORDER BY id DESC
        LIMIT 1
    """.trimIndent()

        DriverManager.getConnection(
            jdbcUrl,
            username,
            password
        ).use { connection ->

            connection.prepareStatement(sql).use { statement ->

                statement.executeQuery().use { resultSet ->

                    if (!resultSet.next()) {
                        return null
                    }

                    return TelemetryEvent(
                        imei = resultSet.getString("imei"),
                        timestamp = resultSet
                            .getTimestamp("timestamp")
                            .toInstant(),
                        latitude = resultSet.getDouble("latitude"),
                        longitude = resultSet.getDouble("longitude"),
                        altitude = resultSet.getInt("altitude"),
                        angle = resultSet.getInt("angle"),
                        satellites = resultSet.getInt("satellites"),
                        speed = resultSet.getInt("speed")
                    )
                }
            }
        }
    }
}