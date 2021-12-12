import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

data class CaveSystem(val start: Cave, val end: Cave, val caves: Set<Cave>, val graph: Map<Cave, List<Cave>>)
data class Cave(val node: String, val small: Boolean)

fun String.toCave(): Cave = Cave(this, this.let { it != it.uppercase() })

fun List<String>.mapInput(): CaveSystem {
    val caves = mutableSetOf<Cave>()
    val graph = mutableMapOf<Cave, MutableList<Cave>>()
    this.forEach { connection ->
        val (fromCave, toCave) = connection.split("-").let { it[0].toCave() to it[1].toCave() }
        if (!caves.contains(fromCave)) caves.add(fromCave)
        if (!caves.contains(toCave)) caves.add(toCave)

        graph[fromCave] = graph.getOrDefault(fromCave, mutableListOf()).apply { this.add(toCave) }
        graph[toCave] = graph.getOrDefault(toCave, mutableListOf()).apply { this.add(fromCave) }
    }
    return CaveSystem(
        Cave("start", small = true),
        Cave("end", small = true),
        caves,
        graph
    )
}

fun Map<Cave, List<Cave>>.dfsTraverse(
    current: Cave,
    end: Cave,
    path: List<Cave> = emptyList(),
    isCaveVisitProhibited: (current: Cave, path: List<Cave>) -> Boolean
): Int {
    return when {
        current == end -> 1
        isCaveVisitProhibited(current, path) -> 0
        else -> {
            val connections: List<Cave> = this[current]!!
            connections.sumOf { this.dfsTraverse(it, end, path + current, isCaveVisitProhibited) }
        }
    }
}

fun solutionPart1(input: CaveSystem): Int {
    return input.graph.dfsTraverse(input.start, input.end) { cave, path ->
        cave.small && path.contains(cave)
    }
}

fun solutionPart2(input: CaveSystem): Int {
    return input.graph.dfsTraverse(input.start, input.end) { cave, path ->
        val grouping = path.filter { it.small }.groupingBy { it }.eachCount()
        when {
            cave.node == "start" && grouping.containsKey(cave) -> true
            cave.node == "end" && grouping.containsKey(cave) -> true
            cave.small && grouping.containsKey(cave) && grouping.any { it.value > 1 } -> true
            else -> false
        }
    }
}
