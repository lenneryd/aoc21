import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val transformed = listOf(
        listOf(
            listOf(listOf(0, listOf(5, 8)), listOf(listOf(1, 7), listOf(9, 6))),
            listOf(listOf(4, listOf(1, 2)), listOf(listOf(1, 4), 2))
        ),
        listOf(listOf(listOf(5, listOf(2, 8)), 4), listOf(5, listOf(listOf(9, 9), 0))),
        listOf(6, listOf(listOf(listOf(6, 2), listOf(5, 6)), listOf(listOf(7, 6), listOf(4, 7)))),
        listOf(listOf(listOf(6, listOf(0, 7)), listOf(0, 9)), listOf(4, listOf(9, listOf(9, 0)))),
        listOf(listOf(listOf(7, listOf(6, 4)), listOf(3, listOf(1, 3))), listOf(listOf(listOf(5, 5), 1), 9)),
        listOf(listOf(6, listOf(listOf(7, 3), listOf(3, 2))), listOf(listOf(listOf(3, 8), listOf(5, 7)), 4)),
        listOf(listOf(listOf(listOf(5, 4), listOf(7, 7)), 8), listOf(listOf(8, 3), 8)),
        listOf(listOf(9, 3), listOf(listOf(9, 9), listOf(6, listOf(4, 9)))),
        listOf(listOf(2, listOf(listOf(7, 7), 7)), listOf(listOf(5, 8), listOf(listOf(9, 3), listOf(0, 2)))),
        listOf(listOf(listOf(listOf(5, 2), 5), listOf(8, listOf(3, 7))), listOf(listOf(5, listOf(7, 5)), listOf(4, 4))),
    )


    @Test
    internal fun `solutionPart1 should return part1 result`() {
        assertEquals(4140, solutionPart1(transformed))
    }

    @Test
    internal fun `testForStackOverflow`() {
        val overflowingList = listOf(
            listOf(
                listOf(listOf(listOf(7, 0), listOf(7, 8)), listOf(listOf(7, 9), listOf(0, 6))),
                listOf(listOf(listOf(7, 0), listOf(6, 6)), listOf(listOf(7, 7), listOf(0, 9)))
            ),
            listOf(6, listOf(listOf(listOf(6, 2), listOf(5, 6)), listOf(listOf(7, 6), listOf(4, 7))))
        )
        assertEquals(0, solutionPart1(overflowingList))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        assertEquals(3993, solutionPart2(transformed))
    }
}
