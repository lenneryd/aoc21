import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

data class Position(val row: Int, val col: Int)
data class Octupus(val row: Int, val col: Int, var energy: Int)

fun Octupus.toPos() = Position(row, col)

fun List<String>.mapInput(): List<List<Octupus>> =
    mapIndexed { rowIndex, line ->
        line.split("").filter { it.isNotEmpty() }
            .mapIndexed { colIndex, energy -> Octupus(rowIndex, colIndex, energy.toInt()) }
    }

fun List<List<Octupus>>.findNeighbors(position: Position): List<Position> = listOfNotNull(
    this.getOrNull(position.row - 1)?.getOrNull(position.col - 1),
    this.getOrNull(position.row - 1)?.getOrNull(position.col),
    this.getOrNull(position.row - 1)?.getOrNull(position.col + 1),
    this.getOrNull(position.row)?.getOrNull(position.col - 1),
    this.getOrNull(position.row)?.getOrNull(position.col + 1),
    this.getOrNull(position.row + 1)?.getOrNull(position.col - 1),
    this.getOrNull(position.row + 1)?.getOrNull(position.col),
    this.getOrNull(position.row + 1)?.getOrNull(position.col + 1),
).map { it.toPos() }

tailrec fun findFlashing(
    octopuses: List<List<Octupus>>,
    flashing: Set<Position>,
    allFlashing: Set<Position> = emptySet()
): Set<Position> {

    if (flashing.isEmpty()) {
        return allFlashing
    }

    val nextFlashers = mutableSetOf<Position>()
    val nextAllFlashers = allFlashing + flashing

    for (current in flashing) {
        val neighbors = octopuses.findNeighbors(current)
            .filter { it !in nextAllFlashers && it !in nextFlashers }
            .toSet()

        for (neighbor in neighbors) {
            octopuses[neighbor.row][neighbor.col].energy++

            if (octopuses[neighbor.row][neighbor.col].energy > 9) {
                nextFlashers.add(neighbor)
            }
        }
    }

    return findFlashing(octopuses, nextFlashers, nextAllFlashers)
}

fun List<List<Octupus>>.step(): Int {
    forEach { row -> row.forEach { col -> col.energy++ } }

    val flashing = findFlashing(
        this,
        flatten().filter { it.energy > 9 }.map { it.toPos() }.toSet()
    )

    flashing.forEach { this[it.row][it.col].energy = 0 }
    return flashing.size
}

fun solutionPart1(input: List<List<Octupus>>): Long {
    var flash = 0L
    for (i in 0 until 100) {
        flash += input.step()
    }
    return flash
}

fun solutionPart2(input: List<List<Octupus>>): Long {
    var iteration = 1L
    while (true) {
        val count = input.step()
        if (count == 100) {
            return iteration
        }
        iteration++
    }
}
