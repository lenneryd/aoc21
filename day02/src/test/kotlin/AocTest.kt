import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    @Test
    internal fun `solutionPart1 should be multiplied sums of position and depth`() {
        assertEquals(
            solutionPart1(
                listOf(
                    "forward 5",
                    "down 5",
                    "forward 8",
                    "up 3",
                    "down 8",
                    "forward 2",
                ).mapInput()
            ), 150
        )
    }

    @Test
    internal fun `solutionPart2 should be multipled sums of position and depth from aim operations`() {
        assertEquals(
            solutionPart2(
                listOf(
                    "forward 5",
                    "down 5",
                    "forward 8",
                    "up 3",
                    "down 8",
                    "forward 2",
                ).mapInput()
            ), 900
        )
    }
}
