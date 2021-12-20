import java.io.File
import java.math.BigInteger

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

typealias Position = Pair<Int, Int>
typealias InputGrid = Array<IntArray>

class Input(val enhancement: List<Int>, val input: InputGrid)

fun List<String>.mapInput() = this.let { list ->
    val enhancement = list.first().map { if (it == '#') 1 else 0 }
    val gridList = list.drop(2)

    val grid: Array<IntArray> = Array(gridList.size) { row ->
        IntArray(gridList[row].length) { idx ->
            if (gridList[row][idx] == '#') 1 else 0
        }
    }

    Input(enhancement, grid)
}


class Image(
    val pixels: Array<IntArray>,
    private val topLeft: Position = Position(0, 0),
    private val background: Int = 0
) {
    private val width = pixels[0].size
    private val height = pixels.size

    fun countLitPixels() = if (background == 0)
        pixels.fold(0) { cnt, pixelLine ->
            cnt + pixelLine.count { it == 1 }
        } else -1

    fun enhance(encoding: List<Int>): Image {
        val newTopLeft = Pair(topLeft.first - 1, topLeft.second - 1)
        val (x0, y0) = newTopLeft
        val pixelLines = mutableListOf<IntArray>()

        for (y in y0 until y0 + height + 2) {
            val pixelLine = IntArray(width + 2)
            for (x in x0 until x0 + width + 2) {
                pixelLine[x - x0] = calcOutputColor(x, y, encoding)
            }
            pixelLines.add(pixelLine)
        }

        val newPixels = pixelLines.toTypedArray()
        val newBgColor = if (background == 0)
            encoding[0]
        else
            encoding.last()

        return Image(newPixels, newTopLeft, newBgColor)
    }

    private fun getColor(pos: Position): Int {
        val (x, y) = pos
        val (x0, y0) = topLeft

        return if (x in x0 until x0 + width && y in y0 until y0 + height) {
            pixels[y - y0][x - x0]
        } else {
            background
        }
    }

    private fun calcOutputColor(x: Int, y: Int, encoding: List<Int>): Int {
        val positions = listOf(
            x - 1 to y - 1,
            x to y - 1,
            x + 1 to y - 1,
            x - 1 to y,
            x to y,
            x + 1 to y,
            x - 1 to y + 1,
            x to y + 1,
            x + 1 to y + 1
        )

        val colorIdx = positions.fold(0) { code, pos ->
            2 * code + getColor(pos)
        }

        return encoding[colorIdx]
    }
}

fun solutionPart1(input: Input): Int {
    var image = Image(input.input)
    repeat(2) {
        image = image.enhance(input.enhancement)
    }

    return image.countLitPixels()
}

fun solutionPart2(input: Input): Int {
    var image = Image(input.input)
    repeat(50) {
        image = image.enhance(input.enhancement)
    }

    return image.countLitPixels()
}
