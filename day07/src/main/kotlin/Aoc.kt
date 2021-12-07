import java.io.File
import kotlin.math.abs
import kotlin.math.absoluteValue

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

data class Crab(val pos: Int)

fun List<String>.mapInput() = this.first().split(",").map { Crab(it.toInt()) }

fun solutionPart1(input: List<Crab>) =
    (0..input.maxOf { it.pos }).minOf { current -> input.sumOf { (it.pos - current).absoluteValue } }

fun solutionPart2(input: List<Crab>) = (input.minOf { it.pos }..input.maxOf { it.pos }).map { range ->
    // Sum cost based on number of steps required for specific crab.
    input.sumOf { crab -> crab.pos numStepsTo range }
}.minOf { it }

infix fun Int.numStepsTo(other: Int) = (1..(abs(this - other))).sum()
