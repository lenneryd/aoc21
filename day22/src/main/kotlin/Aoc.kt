import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}


typealias Point2D = Pair<Int, Int>
typealias Point3D = Triple<Int, Int, Int>

fun Point3D.x() = this.first
fun Point3D.y() = this.second
fun Point3D.z() = this.third
data class Polygon(val isEnable: Boolean, val points: List<Point3D>) {
    val minX = points.minByOrNull { it.x() }!!.x()
    val maxX = points.maxByOrNull { it.x() }!!.x()
    val minY = points.minByOrNull { it.y() }!!.y()
    val maxY = points.maxByOrNull { it.y() }!!.y()
    val minZ = points.minByOrNull { it.z() }!!.z()
    val maxZ = points.maxByOrNull { it.z() }!!.z()

    fun asSet(): Set<Point3D> = buildSet() {
        (minX..maxX).forEach { x ->
            (minY..maxY).forEach { y ->
                (minZ..maxZ).forEach { z ->
                    add(Point3D(x, y, z))
                }
            }
        }
    }
}

fun String.toComponents(): Pair<Int, Int> = this.split("..").let { it[0].toInt() to it[1].toInt() }
fun List<String>.mapInput(): List<Polygon> = this.map { line ->
    // on x=10..12,y=10..12,z=10..12
    val onOff = if (line.startsWith("on")) 3 else 4
    val components = line.drop(onOff).split(",")
    val (x0, x1) = components[0].drop(2).toComponents()
    val (y0, y1) = components[1].drop(2).toComponents()
    val (z0, z1) = components[2].drop(2).toComponents()
    Polygon(
        line.startsWith("on"),
        listOf(
            Point3D(x0, y0, z0),
            Point3D(x0, y0, z1),
            Point3D(x0, y1, z0),
            Point3D(x0, y1, z1),
            Point3D(x1, y0, z0),
            Point3D(x1, y0, z1),
            Point3D(x1, y1, z0),
            Point3D(x1, y1, z1)
        )
    )
}

fun Polygon.test(point: Point3D): Boolean {
    return point.x() in minX..maxX &&
            point.y() in minY..maxY &&
            point.z() in minZ..maxZ
}

fun solutionPart1(input: List<Polygon>): Int {
    val results = mutableSetOf<Point3D>()
    input.forEach { polygon ->
        if (polygon.minX >= -50 && polygon.maxX <= 50 && polygon.minY >= -50 && polygon.maxY <= 50 && polygon.minZ >= -50 && polygon.maxZ <= 50) {
            val current = polygon.asSet()
            println("Polygon (on = ${polygon.isEnable}) has ${current.size} points. We have ${results.size} before operation")
            if (polygon.isEnable) {
                results.addAll(current)
            } else {
                results.removeAll(current)
            }
            println("Now we have ${results.size} enabled")
        }
    }
    return results.size
}

fun solutionPart2(input: List<Polygon>) = 0
