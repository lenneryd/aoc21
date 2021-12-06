import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input, 256)
        else -> solutionPart1(input, 80)
    }
    println(answer)
}

data class Fish(val age: Int)

fun List<String>.mapInput() = first().split(",").map { Fish(it.toInt()) }

fun List<Fish>.increment(): List<Fish> = this.let { fishList ->
    val newFish = mutableListOf<Fish>()
    fishList.map { fish ->
        when (fish.age) {
            0 -> {
                newFish.add(Fish(8))
                Fish(6)
            }
            else -> {
                Fish(fish.age.dec())
            }
        }
    }.toMutableList().apply { this.addAll(newFish) }
}

fun solutionPart1(input: List<Fish>, days: Int): Int {
    var current = input
    for (d in 0 until days) {
        current = current.increment()
    }
    return current.size
}

fun MutableMap<Int, Long>.increment(): MutableMap<Int, Long> = this.let { fishMap ->
    val updated = mutableMapOf<Int, Long>()
    fishMap.keys.sorted().map { age ->
        when (age) {
            0 -> {
                updated[6] = fishMap[0]!!
                updated[8] = fishMap[0]!!
            }
            else -> {
                updated[age - 1] = updated.getOrDefault(age-1, 0) + fishMap[age]!!
            }
        }
    }
    updated
}

fun solutionPart2(input: List<Fish>, days: Int): Long {
    val fishesWithAge = mutableMapOf<Int, Long>()
    input.forEach { fish -> fishesWithAge[fish.age] = fishesWithAge.getOrDefault(fish.age, 0) + 1 }

    var current = fishesWithAge
    for (d in 0 until days) {
        current = current.increment()
        val sum = current.values.toList().sumOf { it }
    }
    return current.values.toList().sumOf { it }
}
