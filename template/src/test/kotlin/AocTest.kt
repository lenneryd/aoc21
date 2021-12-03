import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val list = listOf("1337", "42")

    @Test
    internal fun `solutionPart1 should return sum of input`() {
        assertEquals(solutionPart1(list.mapInput()), 1379)
    }

    @Test
    internal fun `solutionPart2 should return product of input`() {
        assertEquals(solutionPart2(list.mapInput()), 56154)
    }
}
