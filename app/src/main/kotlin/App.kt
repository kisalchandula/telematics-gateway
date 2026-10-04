import persistence.PostgresTelemetryRepository
import server.GatewayServer

fun main() {

    println("=================================")
    println("       TELEMATICS GATEWAY")
    println("=================================")

    val jdbcUrl = System.getenv("DB_URL")
        ?: "jdbc:postgresql://localhost:5432/telematics"

    val username = System.getenv("DB_USERNAME")
        ?: "postgres"

    val password = System.getenv("DB_PASSWORD")
        ?: error("DB_PASSWORD environment variable is not set")

    val repository =
        PostgresTelemetryRepository(
            jdbcUrl = jdbcUrl,
            username = username,
            password = password
        )

    val server =
        GatewayServer(
            port = 5000,
            telemetryRepository = repository
        )

    server.start()
}