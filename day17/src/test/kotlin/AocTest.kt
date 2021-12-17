import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val list = listOf("target area: x=20..30, y=-10..-5")

    @Test
    internal fun `solutionPart1 should return part1 result`() {
        assertEquals(45, solutionPart1(list.mapInput()))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        assertEquals(112, solutionPart2(list.mapInput()))
    }
}
