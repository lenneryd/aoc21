package aoc

import aoc.days.day1.solve

class AocApp {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            println(
                DaySolution.Day1Solution(
                    getPartToRun(),
                    Day("2021-12-01", 1)
                ).solve()
            )
        }
    }
}

fun getPartToRun(): Part = when(System.getenv("part")) {
    "part1" -> Part.Part1
    "part2" -> Part.Part2
    else -> Part.Part1
}

sealed class Part {
    object Part1: Part()
    object Part2: Part()
}

data class Day(val date: String, val number: Int)

sealed class DaySolution(open val part: Part, open val day: Day) {
    data class Day1Solution(override val part: Part, override val day: Day): DaySolution(part, day)
}
