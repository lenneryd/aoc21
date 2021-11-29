import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    @Test
    internal fun `solutionPart1 should return sum of primes multiplied with their list index`() {
        assertEquals(
            solutionPart1(
                listOf(
                    0,
                    3,
                    4,
                    42,
                    106,
                    107,
                    267,
                    269,
                )
            ), 2421
        )
    }

    @Test
    internal fun `solutionPart2 should return sum of non-primes added or subtracted based on index`() {
        assertEquals(
            solutionPart2(
                listOf(
                    0,
                    3,
                    4,
                    42,
                    106,
                    107,
                    267,
                    269,
                )
            ), 335
        )
    }
}
