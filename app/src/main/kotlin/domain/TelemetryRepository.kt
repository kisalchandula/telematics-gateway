package domain

interface TelemetryRepository {

    fun save(event: TelemetryEvent)
}