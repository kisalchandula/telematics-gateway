package protocol

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TeltonikaDecoderTest {

    @Test
    fun shouldDecodeImei() {

        val data = byteArrayOf(
            0x00,
            0x0F,
            0x33,
            0x35,
            0x36,
            0x33,
            0x30,
            0x37,
            0x30,
            0x34,
            0x32,
            0x34,
            0x34,
            0x31,
            0x30,
            0x31,
            0x33
        )

        val decoder = TeltonikaDecoder()

        val result = decoder.decode(data)

        assertEquals(
            "356307042441013",
            (result as ProtocolMessage.Imei).value
        )
    }
}