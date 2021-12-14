import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

data class Fold(val pos: Int, val horizontal: Boolean)
data class Paper(val grid: List<List<Int>>, val folds: List<Fold>)

fun String.asFold() = Fold(this.split("=")[1].toInt(), this.contains("y"))

fun List<String>.mapInput() = this.partition { it.contains("fold along") }.let { (folds, dots) ->
    val filtered: List<Pair<Int, Int>> =
        dots.mapNotNull { coords ->
            coords.takeUnless { it.isEmpty() }?.split(",")?.let {
                it[0].toInt() to it[1].toInt()
            }
        }
    val maxX = filtered.maxOf { it.first }
    val maxY = filtered.maxOf { it.second }
    val grid = (0..maxY).map { MutableList(maxX + 1) { 0 } }
    filtered.forEach { (x, y) -> grid[y][x] = 1 }
    Paper(grid, folds.map { it.asFold() })
}

fun List<List<Int>>.singleFold(fold: Fold) = this.let { grid ->
    val xMax = grid[0].size
    val yMax = grid.size
    val updatedGrid = if (fold.horizontal) {
        val rowsToMove = grid.subList(fold.pos + 1, yMax)
        val newGrid = (0 until fold.pos).map { idx ->
            val offset = fold.pos - 1 - idx
            grid[idx].mapIndexed { index, i -> i + rowsToMove[offset][index] }
        }
        newGrid
    } else {
        grid.map { row ->
            val toMove = row.subList(0, fold.pos)
            val newRow = row.subList(fold.pos + 1, xMax).toMutableList()
            toMove.forEachIndexed { index, i ->
                if (i > 0 && index != fold.pos) {
                    val offset = (fold.pos - 1) - index
                    val value = newRow[offset]
                    newRow[offset] = value + 1
                }
            }
            newRow
        }
    }

    updatedGrid
}

fun solutionPart1(input: Paper) = input.let { paper ->
    val updatedGrid = paper.grid.singleFold(input.folds[0])
    updatedGrid.sumOf { list -> list.count { it > 0 } }
}

fun solutionPart2(input: Paper) = input.folds.let { folds ->
    var currentGrid = input.grid
    folds.forEachIndexed { index, fold ->
        println("Starting Fold i = $index, grid has size (${currentGrid.size}, ${currentGrid[0].size})")
        val updated = currentGrid.singleFold(fold)
        currentGrid = updated
    }

    val mirrored = currentGrid.map { it.reversed() }
    for (y in mirrored.indices) {
        for (x in 0 until mirrored[y].size) {
            val dot = mirrored[y][x].takeIf { it > 0 }?.let { "#" } ?: "."
            print(dot)
        }
        println()
    }
}
