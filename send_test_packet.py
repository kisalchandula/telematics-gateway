import socket
import struct


def crc16(data):
    crc = 0x0000

    for byte in data:
        crc ^= byte

        for _ in range(8):
            if (crc & 0x0001) != 0:
                crc = (crc >> 1) ^ 0x8408
            else:
                crc = crc >> 1

    return crc & 0xFFFF


host = "localhost"
port = 5000

imei = b"123456789012345"

# Timestamp: 2026-10-02T10:00:00Z
timestamp = 1790935200000

longitude = int(8.4037 * 10_000_000)
latitude = int(49.0069 * 10_000_000)

# Codec 8 GPS record
record = (
    struct.pack(">Q", timestamp)
    + bytes([1])                         # Priority
    + struct.pack(">i", longitude)       # Longitude
    + struct.pack(">i", latitude)        # Latitude
    + struct.pack(">h", 120)             # Altitude
    + struct.pack(">h", 90)              # Angle
    + bytes([10])                        # Satellites
    + struct.pack(">H", 50)              # Speed

    # IO section
    + bytes([
        1,      # Event ID
        0,      # Total IO
        0,      # 1-byte IO count
        0,      # 2-byte IO count
        0,      # 4-byte IO count
        0,      # 8-byte IO count
    ])

    # Second record count
    + bytes([1])
)

# Codec 8 data
data = (
    bytes([
        0x08,   # Codec ID
        0x01    # Number of records
    ])
    + record
)

# CRC
crc = crc16(data)

print(f"CRC: 0x{crc:04X}")

# Complete Teltonika AVL packet
packet = (
    bytes(4)                              # Preamble
    + struct.pack(">I", len(data))        # Data length
    + data
    + bytes([
        0x00,
        0x00,
        (crc >> 8) & 0xFF,
        crc & 0xFF
    ])
)

with socket.create_connection((host, port)) as sock:

    # -------------------------
    # IMEI
    # -------------------------

    sock.sendall(
        struct.pack(">H", len(imei)) + imei
    )

    response = sock.recv(1)

    print("IMEI response:", response.hex())

    if response != b"\x01":
        raise RuntimeError("Device was not accepted")

    # -------------------------
    # AVL packet
    # -------------------------

    sock.sendall(packet)

    acknowledgement = sock.recv(4)

    print(
        "AVL acknowledgement:",
        acknowledgement.hex()
    )

    if acknowledgement != b"\x00\x00\x00\x01":
        raise RuntimeError(
            "Unexpected AVL acknowledgement"
        )

print("Telemetry packet sent successfully")