import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

fun List<String>.mapInput(): List<List<Int>> = map { diagnostics -> diagnostics.toCharArray().map { it.digitToInt() } }

fun List<List<Int>>.findMostCommon() = first().indices.map { idx -> count { it[idx] == 1 } }
    .map { if (it >= size - it) 1 else 0 }

fun solutionPart1(input: List<List<Int>>) = input.findMostCommon().let { binaryList ->
    // Gamma * Epsilon rate by inverting Epsilon list using xor with 1 on gamma rate.
    binaryList.joinToString(separator = "").toLong(2) * binaryList.map { bitAsInt -> bitAsInt xor 1 }
        .joinToString(separator = "").toLong(2)
}


fun solutionPart2(input: List<List<Int>>) =
    input.calcRating { fromDiagnostic, mostCommon -> fromDiagnostic != mostCommon } *
            input.calcRating { fromDiagnostic, mostCommon -> fromDiagnostic == mostCommon }

fun List<List<Int>>.calcRating(exclusionTest: (Int, Int) -> Boolean): Long {
    val rating = this.toMutableList()
    var currentBitIdx = 0
    while (rating.size > 1) {
        val mostCommonValue = rating.findMostCommon()[currentBitIdx]
        rating.removeIf { diagnostic -> exclusionTest(diagnostic[currentBitIdx], mostCommonValue) }
        currentBitIdx++
    }
    return rating.first().joinToString("").toLong(2)
}
