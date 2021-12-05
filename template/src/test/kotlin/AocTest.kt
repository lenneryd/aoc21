import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val list = listOf("1337", "42")

    @Test
    internal fun `solutionPart1 should return part1 result`() {
        assertEquals(1379, solutionPart1(list.mapInput()))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        assertEquals(56154, solutionPart2(list.mapInput()))
    }
}
