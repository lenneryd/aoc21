import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

enum class Unique(val segments: Int) {
    One(2),
    Four(4),
    Seven(3),
    Eight(7)
}

data class Display(val input: List<String>, val output: List<String>)

fun List<String>.mapInput() = map { line ->
    line.split(" | ").let { split ->
        Display(input = split[0].split(" "), output = split[1].split(" "))
    }
}

fun solutionPart1(input: List<Display>): Int = input.map { it.output }.flatten().count { output ->
    output.length in Unique.values().map { digit -> digit.segments }
}

fun solutionPart2(input: List<Display>) = input.let { fullList ->
    fullList.sumOf { display ->

        val solved: MutableMap<String, Int> = mutableMapOf()
        val unsolved = display.input.toMutableList()

        unsolved.removeIf { str ->
            when (str.length) {
                2 -> {
                    solved[str] = 1
                    true
                }
                3 -> {
                    solved[str] = 7
                    true
                }
                4 -> {
                    solved[str] = 4
                    true
                }
                7 -> {
                    solved[str] = 8
                    true
                }
                else -> false
            }
        }

        unsolved.removeIf { str ->
            when (str.length) {
                5 -> {
                    when {
                        solved.isThree(str) -> {
                            solved[str] = 3
                            true
                        }

                        solved.isFive(str) -> {
                            solved[str] = 5
                            true
                        }

                        solved.isTwo(str) -> {
                            solved[str] = 2
                            true
                        }
                        else -> false
                    }
                }
                else -> false
            }
        }

        unsolved.removeIf { str ->
            when (str.length) {
                6 -> {
                    when {
                        solved.isNine(str) -> {
                            solved[str] = 9
                            true
                        }

                        solved.isZero(str) -> {
                            solved[str] = 0
                            true
                        }

                        solved.isSix(str) -> {
                            solved[str] = 6
                            true
                        }
                        else -> false
                    }
                }
                else -> false
            }
        }

        display.output.map {
            solved.entries.find { entry -> entry.key.toSet() == it.toSet() }!!.value
        }
            .joinToString(separator = "").toInt()
    }
}

fun Map<String, Int>.isThree(str: String): Boolean = str.length == 5 && str.toSet().containsAll(
    this.entries.find { it.value == 1 }!!.key.toSet()
)

fun Map<String, Int>.isFive(str: String): Boolean = str.length == 5 && str.count { c ->
    this.entries.find { it.value == 4 }!!.key.contains(c)
} == 3

fun Map<String, Int>.isTwo(str: String): Boolean = str.length == 5 && !isThree(str) && !isFive(str)

fun Map<String, Int>.isNine(str: String): Boolean = str.length == 6 && str.toSet().containsAll(
    this.entries.find { it.value == 4 }!!.key.toSet()
)

fun Map<String, Int>.isZero(str: String): Boolean = str.length == 6 && str.toSet().containsAll(
    this.entries.find { it.value == 7 }!!.key.toSet()
)

fun Map<String, Int>.isSix(str: String): Boolean = str.length == 6 && !isNine(str) && !isZero(str)
