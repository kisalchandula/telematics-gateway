package domain

class InMemoryTelemetryRepository : TelemetryRepository {

    val events = mutableListOf<TelemetryEvent>()

    override fun save(event: TelemetryEvent) {
        events.add(event)
    }
}