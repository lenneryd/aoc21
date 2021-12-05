import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val list = listOf(
        "0,9 -> 5,9",
        "8,0 -> 0,8",
        "9,4 -> 3,4",
        "2,2 -> 2,1",
        "7,0 -> 7,4",
        "6,4 -> 2,0",
        "0,9 -> 2,9",
        "3,4 -> 1,4",
        "0,0 -> 8,8",
        "5,5 -> 8,2",
    )

    @Test
    internal fun `solutionPart1 should return result of first part`() {
        assertEquals(5, solutionPart1(list.mapInput()))
    }

    @Test
    internal fun `solutionPart2 should return result of first part`() {
        assertEquals(12, solutionPart2(list.mapInput()))
    }
}
