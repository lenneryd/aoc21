import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    @Test
    internal fun `solutionPart1 should return number of times measurement increases`() {
        assertEquals(
            solutionPart1(
                listOf(
                    199,
                    200,
                    208,
                    210,
                    200,
                    207,
                    240,
                    269,
                    260,
                    263,
                )
            ), 7
        )
    }

    @Test
    internal fun `solutionPart2 should return number of times the 3-measurement sliding windows are increasing`() {
        assertEquals(
            solutionPart2(
                listOf(
                    199,
                    200,
                    208,
                    210,
                    200,
                    207,
                    240,
                    269,
                    260,
                    263,
                )
            ), 5
        )
    }
}
