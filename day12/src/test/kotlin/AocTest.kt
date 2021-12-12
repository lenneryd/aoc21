import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AocTest {

    private val list = listOf(
        "start-A",
        "start-b",
        "A-c",
        "A-b",
        "b-d",
        "A-end",
        "b-end"
    )

    private val list2 = listOf(
        "dc-end",
        "HN-start",
        "start-kj",
        "dc-start",
        "dc-HN",
        "LN-dc",
        "HN-end",
        "kj-sa",
        "kj-HN",
        "kj-dc"
    )

    private val list3 = listOf(
        "fs-end",
        "he-DX",
        "fs-he",
        "start-DX",
        "pj-DX",
        "end-zg",
        "zg-sl",
        "zg-pj",
        "pj-he",
        "RW-he",
        "fs-DX",
        "pj-RW",
        "zg-RW",
        "start-pj",
        "he-WI",
        "zg-he",
        "pj-fs",
        "start-RW"
    )

    @Test
    internal fun `solutionPart1 should return part1 result`() {
        assertEquals(10, solutionPart1(list.mapInput()))
        assertEquals(19, solutionPart1(list2.mapInput()))
        assertEquals(226, solutionPart1(list3.mapInput()))
    }

    @Test
    internal fun `solutionPart2 should return part2 result`() {
        assertEquals(36, solutionPart2(list.mapInput()))
        assertEquals(103, solutionPart2(list2.mapInput()))
        assertEquals(3509, solutionPart2(list3.mapInput()))
    }
}
