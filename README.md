# Telematics Gateway

A Kotlin-based telematics gateway for receiving, decoding, validating, and storing vehicle telemetry from GPS tracking devices over TCP.

The project is being developed as a practical implementation of a **device-to-cloud telematics ingestion pipeline**, using the Teltonika protocol as the initial device protocol.

## Overview

The gateway accepts TCP connections from telematics devices, performs the device handshake, receives AVL telemetry packets, validates and decodes the binary protocol data, converts it into structured telemetry events, and persists the resulting data in PostgreSQL.

### Current data flow

```text
GPS / Telematics Device
          │
          │ TCP
          ▼
┌──────────────────────┐
│   Telematics Gateway │
│                      │
│  TCP Connection      │
│  IMEI Handshake      │
│  Packet Framing      │
│  CRC Validation      │
│  AVL Decoding        │
│  Codec 8 Decoding    │
└──────────┬───────────┘
           │
           ▼
    Telemetry Event
           │
           ▼
┌──────────────────────┐
│      PostgresSQL     │
│                      │
│ Device / Telemetry   │
│       Data           │
└──────────────────────┘
```

## Example

A test device can establish a TCP connection and send an AVL packet containing GPS telemetry.

The decoded telemetry is converted into a structured event and stored in PostgreSQL.

Example database record:

```text
 id |      imei       |       timestamp        | latitude | longitude | altitude | angle | satellites | speed
----+-----------------+------------------------+----------+-----------+----------+-------+------------+-------
  1 | 123456789012345 | 2026-10-02 10:00:00+00 |  49.0069 |    8.4037 |      120 |    90 |         10 |    50
```

This demonstrates the complete runtime path:

```text
TCP Client
    ↓
Gateway Server
    ↓
IMEI Handshake
    ↓
Teltonika AVL Packet
    ↓
CRC Validation
    ↓
Codec 8 Decoder
    ↓
Telemetry Event
    ↓
PostgresSQL
```

## Technology Stack

| Technology         | Purpose                  |
| ------------------ | ------------------------ |
| Kotlin             | Backend implementation   |
| JVM 21             | Runtime                  |
| Gradle             | Build system             |
| TCP/IP             | Device communication     |
| Teltonika protocol | Initial device protocol  |
| PostgreSQL         | Telemetry persistence    |
| JUnit              | Automated testing        |
| Docker             | Planned containerization |
| Kubernetes         | Planned orchestration    |

## Getting Started

### Requirements

* JDK 21 & Kotlin
* Gradle
* PostgreSQL
* Git
* IntelliJ IDEA

Do not commit real database credentials to the repository.

### Build

```bash
./gradlew build
```

On Windows:

```powershell
.\gradlew.bat build
```

### Run tests

```bash
./gradlew test
```

### Start the gateway

```bash
./gradlew run
```

The gateway currently listens for incoming device connections on the configured TCP port.

## Testing

The project contains unit and integration tests covering the protocol and gateway layers.

Tests currently cover areas including:

* IMEI processing
* TCP client connections
* AVL packet decoding
* CRC validation
* Codec 8 decoding
* Invalid packet handling
* Multiple packets received in a single TCP read
* Telemetry persistence

The integration path is particularly important because it verifies that data can travel through the actual runtime pipeline rather than only through isolated decoder tests.

## Project Goals

This project is more than a protocol decoder.

The long-term goal is to build a small but realistic **telematics backend platform** capable of receiving data from connected vehicles, processing the incoming telemetry, storing historical data, and exposing the information to applications and monitoring systems.

The planned architecture is:

```text
                 Connected Vehicles
                        │
                        ▼
                ┌───────────────┐
                │ TCP Gateway   │
                └───────┬───────┘
                        │
                        ▼
                ┌───────────────┐
                │ Protocol      │
                │ Processing    │
                └───────┬───────┘
                        │
                        ▼
                ┌───────────────┐
                │ Telemetry     │
                │ Events        │
                └───────┬───────┘
                        │
              ┌─────────┴─────────┐
              ▼                   ▼
       ┌─────────────┐     ┌─────────────┐
       │ PostgresSQL │     │ Future      │
       │             │     │ Streaming   │
       └──────┬──────┘     └─────────────┘
              │
              ▼
       ┌─────────────┐
       │ REST API    │
       └──────┬──────┘
              │
              ▼
       ┌─────────────┐
       │ Web/Mobile  │
       │ Dashboard   │
       └─────────────┘
```

## Why This Project?

The project is intended as an MVP of backend engineering for connected systems, including:

* Network programming
* Binary protocol implementation
* IoT/telematics data ingestion
* Event processing
* Database persistence
* Distributed-system concepts
* Containerization
* Observability
* API development
* Scalable backend architecture

Rather than starting with a large framework or abstract architecture, the system is being built from the TCP ingestion layer upward.

## Development Approach

The project is developed incrementally through small, testable milestones.

```text
TCP Server
    ↓
IMEI Handshake
    ↓
Protocol Decoder
    ↓
CRC Validation
    ↓
AVL / Codec 8
    ↓
Session Management
    ↓
PostgresSQL
    ↓
REST API
    ↓
Dashboard
    ↓
Containerization
    ↓
Production Hardening
```

Each stage is intended to leave the system in a working state and is covered by automated tests where appropriate.

## License

This project is licensed under the MIT License.

See the [LICENSE](LICENSE) file for details.