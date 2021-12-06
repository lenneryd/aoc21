import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val list = listOf("3,4,3,1,2")

    @Test
    internal fun `solutionPart1 should return part1 result`() {
        assertEquals(26, solutionPart1(list.mapInput(), 18))
        assertEquals(5934, solutionPart1(list.mapInput(), 80))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        assertEquals(26984457539L, solutionPart2(list.mapInput(), 256))
    }
}
