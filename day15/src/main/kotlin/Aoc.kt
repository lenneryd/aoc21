import java.io.File
import java.util.*

fun main() {
    val input = File("input.txt").readLines()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input.mapInput(5))
        else -> solutionPart1(input.mapInput())
    }
    println(answer)
}

data class Node(val row: Int, val col: Int, val weight: Int) : Comparable<Node> {
    override fun compareTo(other: Node) = this.weight.compareTo(other.weight)
}

data class Graph(val start: Node, val end: Node, val grid: List<List<Node>>)

fun Node.toPos() = row to col

fun List<List<Node>>.getNode(row: Int, col: Int): Node? = this.getOrNull(row)?.getOrNull(col)
fun List<String>.mapInput(tiles: Int = 1): Graph = this.let {
    val graph = this.mapIndexed { row, line -> line.mapIndexed { col, value -> Node(row, col, value.digitToInt()) } }
    val rows = graph.size
    val cols = graph.first().size
    val fullGraph = MutableList(rows * tiles) { rowIdx ->
        MutableList(cols * tiles) { colIdx ->
            val rowNr = rowIdx / rows
            val colNr = colIdx / cols
            val weight = graph[rowIdx % rows][colIdx % cols].weight
            val modified = weight + rowNr + colNr
            val normalized = modified.takeIf { it > 9 }?.let { it - 9 } ?: modified
            Node(
                rowIdx,
                colIdx,
                normalized
            )
        }
    }

    Graph(
        fullGraph.getNode(0, 0)!!,
        fullGraph.getNode(fullGraph.size - 1, fullGraph.first().size - 1)!!,
        fullGraph
    )
}

fun Map<Pair<Int, Int>, Long>.findMinFrom(list: List<Node>) = this.let { distances ->
    list.minByOrNull { distances.getDist(it.toPos()) }!!
}

fun List<List<Node>>.neighbours(node: Node): List<Node> = listOfNotNull(
    getNode(node.row - 1, node.col),
    getNode(node.row, node.col - 1),
    getNode(node.row + 1, node.col),
    getNode(node.row, node.col + 1)
)

fun Map<Pair<Int, Int>, Long>.getDist(pos: Pair<Int, Int>) = this[pos]!!
fun Graph.djikstra(): Map<Pair<Int, Int>, Long> = this.let { graph ->
    val dist = graph.grid.flatten().map { it.row to it.col }.associateWith { Integer.MAX_VALUE.toLong() }.toMutableMap()
    dist[graph.start.toPos()] = 0

    val queue = graph.grid.flatten().toMutableList()

    while (queue.isNotEmpty()) {
        val current = dist.findMinFrom(queue).also { queue.remove(it) }
        grid.neighbours(current).filter { queue.contains(it) }.forEach { neighbour ->
            val newPath = dist.getDist(current.toPos()) + neighbour.weight.toLong()
            if (newPath < dist.getDist(neighbour.toPos())) {
                dist[neighbour.toPos()] = newPath
            }
        }
    }

    dist
}

data class Queued(val node: Node, val dist: Long)

fun Graph.djikstra2(): List<Node> = this.let { graph ->
    val dist = graph.grid.flatten().map { it.row to it.col }.associateWith { Integer.MAX_VALUE.toLong() }.toMutableMap()
    dist[graph.start.toPos()] = 0

    val adjacent = mutableMapOf<Node, Node>()
    val comparator = compareBy<Queued> { it.dist }
    val queue = PriorityQueue(comparator)
    queue.add(Queued(graph.start, 0))
    val inQueue: MutableSet<Node> = graph.grid.flatten().toSet().toMutableSet()
    val settled: MutableSet<Node> = mutableSetOf()

    while (settled.size < graph.grid.size * graph.grid.first().size) {
        val current = queue.remove().node
        inQueue.remove(current)

        if(!settled.contains(current)) {
            grid.neighbours(current).filter { !settled.contains(it) }.forEach { nb ->
                val alt = dist.getDist(current.toPos()) + nb.weight
                if (alt < dist.getDist(nb.toPos())) {
                    dist[nb.toPos()] = alt
                    adjacent[nb] = current
                }

                queue.add(Queued(nb, dist.getDist(nb.toPos())))
                inQueue.add(nb)
            }
            settled.add(current)
        }
    }

    val path = mutableListOf(end)
    while (adjacent.containsKey(path.last())) {
        path += adjacent[path.last()]!!
    }

    path
}

fun solutionPart1(input: Graph): Long = input.let { graph ->
    graph.djikstra()[graph.end.toPos()]!!
}

fun solutionPart2(input: Graph) = input.let { graph ->
    graph.djikstra2().dropLast(1).sumOf { it.weight }
}
