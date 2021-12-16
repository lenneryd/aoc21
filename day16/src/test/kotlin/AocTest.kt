import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    @Test
    internal fun `solutionPart1 should return part1 result`() {
        //assertEquals(6, solutionPart1(listOf("D2FE28").mapInput()))
        // Operator, length type 0
        //assertEquals(1, solutionPart1(listOf("38006F45291200").mapInput()))
        //listOf("EE00D40C823060") // Operator, length type 1
        // Operator type 4, contains Operator type 1, contains operator type 5, contains literal 6.

        assertEquals(16, solutionPart1(listOf("8A004A801A8002F478").mapInput()))
        assertEquals(12, solutionPart1(listOf("620080001611562C8802118E34").mapInput()))
        assertEquals(23, solutionPart1(listOf("C0015000016115A2E0802F182340").mapInput()))
        assertEquals(31, solutionPart1(listOf("A0016C880162017C3686B18A3D4780").mapInput()))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        assertEquals(3, solutionPart2(listOf("C200B40A82").mapInput()))
        assertEquals(54, solutionPart2(listOf("04005AC33890").mapInput()))
        assertEquals(7, solutionPart2(listOf("880086C3E88112").mapInput()))
        assertEquals(9, solutionPart2(listOf("CE00C43D881120").mapInput()))
        assertEquals(1, solutionPart2(listOf("D8005AC2A8F0").mapInput()))
        assertEquals(0, solutionPart2(listOf("F600BC2D8F").mapInput()))
        assertEquals(0, solutionPart2(listOf("9C005AC2F8F0").mapInput()))
        assertEquals(1, solutionPart2(listOf("9C0141080250320F1802104A08").mapInput()))
    }
}
