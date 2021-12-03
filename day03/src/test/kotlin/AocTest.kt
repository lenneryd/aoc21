import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val list = listOf(
        "00100",
        "11110",
        "10110",
        "10111",
        "10101",
        "01111",
        "00111",
        "11100",
        "10000",
        "11001",
        "00010",
        "01010"
    )

    @Test
    internal fun `solutionPart1 should return power consumption as gamma rate multiplied by epsilon rate `() {
        assertEquals(
            solutionPart1(
                list.mapInput()
            ), 198
        )
    }

    @Test
    internal fun `solutionPart2 should return product of input`() {
        assertEquals(
            solutionPart2(
                list.mapInput()
            ), 230
        )
    }
}
