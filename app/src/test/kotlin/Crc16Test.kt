package protocol

import kotlin.test.Test
import kotlin.test.assertEquals

class Crc16Test {

    @Test
    fun `calculate CRC16`() {

        val data = byteArrayOf(
            0x01,
            0x02,
            0x03,
            0x04
        )

        val crc = Crc16.calculate(data)

        assertEquals(
            0xC54F,
            crc
        )
    }
}