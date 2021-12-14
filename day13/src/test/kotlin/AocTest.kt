import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val list = listOf(
        "6,10",
        "0,14",
        "9,10",
        "0,3",
        "10,4",
        "4,11",
        "6,0",
        "6,12",
        "4,1",
        "0,13",
        "10,12",
        "3,4",
        "3,0",
        "8,4",
        "1,10",
        "2,14",
        "8,10",
        "9,0",
        "",
        "fold along y=7",
        "fold along x=5"
    )
    private val list2 = listOf(
        "0,0",
        "2,0",
        "3,0",
        "6,0",
        "9,0",
        "0,1",
        "4,1",
        "6,2",
        "10,2",
        "0,3",
        "4,3",
        "1,4",
        "3,4",
        "6,4",
        "8,4",
        "9,4",
        "10,4",
        "",
        "fold along x=5"
    )

    @Test
    internal fun `solutionPart1 should return part1 result`() {
        assertEquals(17, solutionPart1(list.mapInput()))
        assertEquals(16, solutionPart1(list2.mapInput()))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        //assertEquals(0, solutionPart2(list.mapInput()))
    }
}
