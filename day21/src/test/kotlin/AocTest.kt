import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val list = listOf(
        "Player 1 starting position: 4",
        "Player 2 starting position: 8"
    )

    @Test
    internal fun `solutionPart1 should return part1 result`() {
        assertEquals(739785, solutionPart1(list.mapInput()))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        assertEquals(444356092776315L, solutionPart2(list.mapInput()))
    }
}
