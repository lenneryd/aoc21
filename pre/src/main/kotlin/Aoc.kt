import java.io.File
import kotlin.math.pow

fun main() {
    val input = File("input.txt").readLines().map { it.toInt() }
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

fun solutionPart1(input: List<Int>) = input.mapIndexed { index, i -> index to i }
    .filter { it.second.isPrime() }
    .sumOf { it.first * it.second }

fun solutionPart2(input: List<Int>) = input.mapIndexed { index, i -> index to i }
    .filter { it.second.isPrime().not() }
    .partition { it.first.isEven() }
    .let { it.first.sumOf { pair -> pair.second } - it.second.sumOf { pair -> pair.second } }

fun Int.isEven() = this % 2 == 0
fun Int.isPrime(): Boolean {
    when {
        // 2 and 3 are prime numbers.
        this == 2 || this == 3 -> return true
        // Anything less than or equal to 1, anything even, and anything divisible with 3 is not a prime.
        this <= 1 || this.isEven() || this % 3 == 0 -> return false
        // Anything failing the primality test with 6k +- 1 optimization is not a prime.
        // https://en.wikipedia.org/wiki/Primality_test
        else -> {

            var count = 5

            while (count.toDouble().pow(2) <= this) {
                if (this % count == 0 || this % (count + 2) == 0) return false
                count += 6
            }
            return true
        }
    }
}
