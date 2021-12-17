import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

data class StartingPoint(val x: Int, val y: Int)

data class TargetArea(val x1: Int, val x2: Int, val y1: Int, val y2: Int) {
    operator fun contains(point: Pair<Int, Int>) = point.first in x1..x2 && point.second in y1..y2

    operator fun contains(startingPoint: StartingPoint): Boolean {
        val xCoords = generateSequence(0 to startingPoint.x) { (x, dx) -> x + dx to maxOf(0, dx - 1) }
            .takeWhile { it.first <= this.x2 } // x2 is larger than x1.
            .map { it.first }

        val yCoords = generateSequence(0 to startingPoint.y) { (y, dy) -> y + dy to dy - 1 }
            .takeWhile { it.first >= this.y1 } // y1 is more negative than y2.
            .map { it.first }

        return (xCoords zip yCoords).any { coords -> coords in this }
    }
}

fun List<String>.mapInput() = first().let { str ->
    str.split("=").let { split ->
        val x = split[1].dropLast(3).split("..")
        val y = split[2].split("..")
        TargetArea(
            x[0].toInt(), x[1].toInt(), y[0].toInt(), y[1].toInt()
        )
    }
}

fun Int.triangularNumber() = (1 + this) * this / 2

// x velocity drops by 1, so total x distance becomes for example 4 + 3 + 2 + 1 = 10.
// Which is the triangular number of 4 since (5 * 4) / 2.
// We want to use the lowest point of the target area, therefore we calculate the triangular value of (-y1) - 1
fun solutionPart1(input: TargetArea) = (-input.y1 - 1).triangularNumber()

fun solutionPart2(input: TargetArea): Int {
    val minY = input.y1 // Minimum allowed y is y2.
    val maxY = -input.y1 - 1 // We know max height is this from the above.
    // We need to reach the input area in the X direction, therefore min has to be whatever x that reaches target x.
    val minX = (1..input.x1).first { x -> x.triangularNumber() >= input.x1 }
    val maxX = input.x2

    return (minX..maxX).sumOf { x ->
        (minY..maxY).count { y ->
            StartingPoint(x, y) in input
        }
    }
}

