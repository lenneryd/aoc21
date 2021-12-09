import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> {
            solutionPart1(input)
        }
    }
    println(answer)
}

data class Point(val row: Int, val col: Int, val value: Int)

fun List<String>.mapInput(): List<List<Point>> = mapIndexed { rowIdx, row ->
    listOf(*row.split("").filter { it.isNotEmpty() }
        .mapIndexed { colIdx, value -> Point(rowIdx, colIdx, value.toInt()) }
        .toTypedArray())
}

fun List<List<Point>>.getValidNeighbors(current: Point) = listOfNotNull(
    this.getOrNull(current.row - 1)?.getOrNull(current.col),
    this.getOrNull(current.row + 1)?.getOrNull(current.col),
    this.getOrNull(current.row)?.getOrNull(current.col - 1),
    this.getOrNull(current.row)?.getOrNull(current.col + 1)
)

private fun List<List<Point>>.findLowPoints(): List<Point> {
    return this.fold(emptyList()) { allPoints, row ->
        row.fold(allPoints) { points, point ->
            this.getValidNeighbors(point)
                .all { (x, y) -> this[x][y].value > point.value }
                .let { isLowest ->
                    if (isLowest) {
                        points + point // Accumulate the lowest point
                    } else {
                        // Leave list of low points as it is.
                        points
                    }
                }
        }
    }
}

fun solutionPart1(input: List<List<Point>>): Int = input.findLowPoints().sumOf { point -> point.value + 1 }

fun solutionPart2(input: List<List<Point>>) = input.findLowPoints().asSequence().map { lowPoint ->
    input.findBasinForPoint(lowPoint).distinct()
}.map { it.size }.sortedDescending().take(3).reduce { acc, size -> acc * size }


fun List<List<Point>>.findBasinForPoint(lowPoint: Point): List<Point> = this.getValidNeighbors(lowPoint)
    .filter { it.value != 9 }
    .fold(
        initial = listOf(lowPoint)
    ) { basinContents, currentPoint ->
        if (currentPoint.value - lowPoint.value >= 1) {
            basinContents + this.findBasinForPoint(currentPoint)
        } else {
            basinContents
        }
    }.distinct() // We may get duplicates when recursing.
