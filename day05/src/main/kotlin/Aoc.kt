import java.io.File
import kotlin.math.sign

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

data class Point(val x: Int, val y: Int)
data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

fun List<String>.mapInput() = map { line ->
    line.split(" -> ").let { parts ->
        Line(
            x1 = parts[0].split(",")[0].toInt(),
            y1 = parts[0].split(",")[1].toInt(),
            x2 = parts[1].split(",")[0].toInt(),
            y2 = parts[1].split(",")[1].toInt()
        )
    }
}

fun Line.getGradient(): Float = (y2 - y1).toFloat() / (x2 - x1).toFloat()
fun Line.getPoints(): List<Point> = this.let { line ->
    val m = line.getGradient()
    val c = line.y2 - m * line.x2
    val points = mutableListOf<Point>()
    val stepX = sign(x2.toFloat() - x1.toFloat()).toInt()

    val rangeX = if (x1 <= x2) x1..x2 else x2..x1
    val rangeY = if (y1 <= y2) y1..y2 else y2..y1

    if (x1 == x2) {
        for (y in rangeY) {
            points.add(Point(x1, y))
        }
    } else {
        for (x in rangeX) {
            points.add(Point(x, (m * x + c).toInt()))
        }
    }
    points
}

fun solutionPart1(input: List<Line>): Int = input.let { lines ->
    val points = mutableMapOf<Point, Int>()
    lines.mapNotNull { line -> line.takeIf { it.x1 == it.x2 || it.y1 == it.y2 } }.forEach { l ->
        l.getPoints().forEach { point -> points[point] = points.getOrDefault(point, 0) + 1 }
    }
    points.keys.count { point -> points[point]!! >= 2 }
}

fun solutionPart2(input: List<Line>) = input.let { lines ->
    val points = mutableMapOf<Point, Int>()
    lines.forEach { l ->
        l.getPoints().forEach { point -> points[point] = points.getOrDefault(point, 0) + 1 }
    }
    points.keys.count { point -> points[point]!! >= 2 }
}
