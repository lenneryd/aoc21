import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

fun List<String>.mapInput() = this.map { it.split(" ") }.map { Heading(it[0].asDirection(), it[1].toInt()) }

data class Heading(val direction: Direction, val distance: Int)
data class Current(val pos: Int, val depth: Int, val aim: Int)
sealed class Direction(val dir: Int) {
    object Down : Direction(1)
    object Up : Direction(-1)
    object Forward : Direction(1)
}

fun String.asDirection() = when (this) {
    "down" -> Direction.Down
    "up" -> Direction.Up
    "forward" -> Direction.Forward
    else -> throw IllegalStateException()
}

fun solutionPart1(input: List<Heading>) = input.let { list ->
    list.partition { it.direction is Direction.Forward }
}.let { lists ->
    lists.first.sumOf { it.distance } * lists.second.sumOf { it.direction.dir * it.distance }
}

fun solutionPart2(input: List<Heading>) = input.fold(Current(0, 0, 0)) { current, heading ->
    when (heading.direction) {
        is Direction.Forward -> Current(
            current.pos + heading.distance,
            current.depth + current.aim * heading.distance * heading.direction.dir,
            current.aim
        )
        else -> Current(current.pos, current.depth, current.aim + heading.direction.dir * heading.distance)
    }
}.let { it.pos * it.depth }
