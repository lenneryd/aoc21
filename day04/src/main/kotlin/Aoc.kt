import java.io.File
import java.util.regex.Pattern

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

data class Bingo(val numbers: List<Int>, val boards: List<List<Int>>)
data class Win(val number: Int, val board: List<Int>, val score: Int)

fun List<String>.mapInput() = this.toMutableList().apply {
    removeIf { it.isEmpty() }
}.let { list ->
    Bingo(
        list.first().split(",").map { it.toInt() },
        list.subList(1, list.size).chunked(5) {
            it.map { row ->
                row.split(regex = Pattern.compile("\\s+")).mapNotNull { it.takeUnless { it.isEmpty() } }
                    .map { number -> number.toInt() }
            }.flatten()
        }
    )
}

fun List<Int>.checkBoard(number: Int, drawnNumbers: Set<Int>): Win? = this.let { board ->
    board.indexOf(number).takeUnless { it == -1 }
        ?.takeIf { idx ->
            board.checkRow(idx) { drawnNumbers.contains(it) } || board.checkColumn(idx) { drawnNumbers.contains(it) }
        }?.let {
            Win(number, board, board.filter { !drawnNumbers.contains(it) }.sum() * number)
        }
}

fun List<Int>.checkRow(idx: Int, isDrawn: (Int) -> Boolean): Boolean = this.let { board ->
    val rowIdx = idx / 5
    var marked = 0
    for (i in 0..4) {
        if (isDrawn(board[rowIdx * 5 + i])) {
            marked++
        }
    }
    return@let marked == 5
}

fun List<Int>.checkColumn(idx: Int, isDrawn: (Int) -> Boolean): Boolean = this.let { board ->
    val colId = idx % 5
    var marked = 0
    for (i in 0..4) {
        if (isDrawn(board[colId + 5 * i])) {
            marked++
        }
    }
    return@let marked == 5
}

fun List<Int>.findWin(numbers: List<Int>): Pair<Int, Win> =
    numbers.mapIndexed { index, number -> index to number }
        .firstNotNullOf { numberPair ->
            this.checkBoard(numberPair.second, numbers.subList(0, numberPair.first + 1).toSet())
                ?.let { win -> numberPair.first to win }
        }

fun solutionPart1(input: Bingo): Int = input.let { bingo ->
    val numbers = bingo.numbers.toMutableList()
    val drawnNumbers = mutableSetOf<Int>()
    var win: Win? = null
    while (numbers.isNotEmpty() && win == null) {
        val current = numbers.removeFirst()
        drawnNumbers.add(current)
        bingo.boards.forEach { board ->
            board.checkBoard(current, drawnNumbers)?.let { winningBoard ->
                win = winningBoard
            }
        }
    }

    win!!.score
}

fun solutionPart2(input: Bingo): Int = input.let { bingo ->
    bingo.boards.map { board -> board.findWin(bingo.numbers) }.maxByOrNull { it.first }?.second?.score ?: 0
}
