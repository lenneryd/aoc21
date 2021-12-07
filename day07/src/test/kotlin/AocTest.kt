import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val list = listOf("16,1,2,0,4,2,7,1,2,14")

    @Test
    internal fun `solutionPart1 should return part1 result`() {
        assertEquals(37, solutionPart1(list.mapInput()))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        assertEquals(168, solutionPart2(list.mapInput()))
    }
}
