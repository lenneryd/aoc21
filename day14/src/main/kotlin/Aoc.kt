import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

data class Polymer(val start: List<Char>, val rules: Map<String, String>)
data class Rule(val pattern: String, val result: String)

fun List<String>.mapInput() = this.partition { !it.contains("->") }.let { (start, ruleStrings) ->
    val startList = start.filter { it.isNotEmpty() }[0].toList()
    val rules = mapOf(*ruleStrings.map { str -> str.split(" -> ").let { it[0] to it[1] } }.toTypedArray())
    Polymer(startList, rules)
}

fun Polymer.step(): Polymer = this.let { polymer ->
    val current = polymer.start.toMutableList()
    val updated: List<List<Char>> = current.windowed(size = 2, step = 1, partialWindows = true) { window ->
        val key = window.joinToString(separator = "")
        val insert = polymer.rules[key]?.firstOrNull()
        listOfNotNull(window[0], insert)
    }
    Polymer(updated.flatten(), polymer.rules)
}

fun stepCounts(steps: Int, initial: String, rules: Map<String, String>): Long {
    val initialCounts = initial.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    val countedPairs = (0 until steps).fold(initialCounts) { current, _ ->
        current.applyRule(rules)
    }

    val countChars = mutableMapOf<Char, Long>()
    // Need to add first separately.
    countChars[initial[0]] = 1
    countedPairs.forEach { (pair, count) ->
        countChars[pair[1]] = countChars.getOrDefault(pair[1], 0) + count
    }

    val max = countChars.maxOf { it.value }
    val min = countChars.minOf { it.value }
    return max - min
}

fun Map<String, Long>.applyRule(rules: Map<String, String>): Map<String, Long> = this.let { currentMap ->
    val updated = mutableMapOf<String, Long>()
    currentMap.forEach { (pair, count) ->
        val first = pair[0] + rules.getValue(pair)
        val second = rules.getValue(pair) + pair[1]
        updated[first] = updated.getOrDefault(first, 0) + count
        updated[second] = updated.getOrDefault(second, 0) + count
    }
    updated
}

fun solutionPart1(input: Polymer): Int = input.let { polymer ->
    var current = polymer
    for (i in 0 until 10) {
        current = current.step()
    }

    current.start.groupingBy { it }.eachCount().let { counts ->
        val min = counts.minOf { it.value }
        val max = counts.maxOf { it.value }
        max - min
    }
}

fun solutionPart2(input: Polymer): Long = input.let { polymer ->
    stepCounts(40, polymer.start.joinToString(""), polymer.rules)
}
