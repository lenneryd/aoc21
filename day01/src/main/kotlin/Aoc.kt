import java.io.File
import kotlin.math.sign

fun main() {
    val input = File("input.txt").readLines().map { it.toInt() }
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

fun solutionPart1(input: List<Int>) = input.mapIndexed { index, i ->
    input.getOrNull(index - 1) to i
}.count {
    it.first?.let { previous -> previous < it.second } ?: false
}

fun solutionPart2(input: List<Int>) = input.mapIndexedNotNull { index, i ->
    val previous = input.measurement(index - 1) ?: return@mapIndexedNotNull null
    val current = input.measurement(index) ?: return@mapIndexedNotNull null

    previous to current
}.count { it.first < it.second }

fun List<Int>.measurement(index: Int): Int? =
    this.getOrNull(index) + this.getOrNull(index + 1) + this.getOrNull(index + 2)

operator fun Int?.plus(other: Int?): Int? = if (this != null && other != null) this + other else null
