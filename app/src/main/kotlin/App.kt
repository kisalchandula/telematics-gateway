import persistence.PostgresTelemetryRepository
import server.GatewayServer

fun main() {

    println("=================================")
    println("       TELEMATICS GATEWAY")
    println("=================================")

    val repository =
        PostgresTelemetryRepository(
            jdbcUrl = "jdbc:postgresql://localhost:5432/telematics",
            username = "postgres",
            password = "postgres"
        )

    val server =
        GatewayServer(
            port = 5000,
            telemetryRepository = repository
        )

    server.start()
}