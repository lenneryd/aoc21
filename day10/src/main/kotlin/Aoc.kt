import java.io.File
import java.util.*

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

fun List<String>.mapInput() = this

sealed class Chunk {
    sealed class OpenChunk(val opens: Char) : Chunk() {
        object Parenthesis : OpenChunk('(')
        object Square : OpenChunk('[')
        object Curly : OpenChunk('{')
        object Arrow : OpenChunk('<')
    }

    sealed class CloseChunk(val c: Char, val closes: OpenChunk, val corruptionPoints: Int, val completionPoints: Int) :
        Chunk() {
        object Parenthesis : CloseChunk(')', OpenChunk.Parenthesis, 3, 1)
        object Square : CloseChunk(']', OpenChunk.Square, 57, 2)
        object Curly : CloseChunk('}', OpenChunk.Curly, 1197, 3)
        object Arrow : CloseChunk('>', OpenChunk.Arrow, 25137, 4)
    }

    companion object {
        val open: List<OpenChunk> =
            listOf(OpenChunk.Parenthesis, OpenChunk.Square, OpenChunk.Curly, OpenChunk.Arrow)

        val close: List<CloseChunk> =
            listOf(CloseChunk.Parenthesis, CloseChunk.Square, CloseChunk.Curly, CloseChunk.Arrow)
    }
}


fun Char.asChunk(): Chunk = Chunk.open.find { it.opens == this } ?: Chunk.close.first { it.c == this }

fun <T> Stack<T>.peekOrNull(): T? = if (isNotEmpty()) peek() else null
fun Stack<Pair<Char, Chunk.OpenChunk>>.updateWith(input: Char): Pair<Char, Chunk.OpenChunk>? =
    this.peekOrNull().let { pair ->
        val currentChunk = pair?.second
        when (val newChunk = input.asChunk()) {
            is Chunk.OpenChunk -> this.push(input to newChunk)
            is Chunk.CloseChunk -> newChunk.takeIf { it.closes == currentChunk }?.let { this.pop() }
        }
    }

fun String.findCorruption(): Chunk.CloseChunk? = this.let { line ->
    val stack: Stack<Pair<Char, Chunk.OpenChunk>> = Stack()
    line.toCharArray().toList().mapNotNull { c ->
        c.takeIf { stack.updateWith(c) == null }?.asChunk()
    }.firstOrNull().let { it as? Chunk.CloseChunk }
}

fun String.completeLine(): List<Chunk.CloseChunk> = this.let { line ->
    val stack: Stack<Pair<Char, Chunk.OpenChunk>> = Stack()
    line.toCharArray().toList().map { c ->
        stack.updateWith(c)!!
    }

    val list = mutableListOf<Chunk.CloseChunk>()
    while (stack.isNotEmpty()) {
        val current = stack.pop().second
        list.add(Chunk.close.first { current == it.closes })
    }
    list
}

fun solutionPart1(input: List<String>) = input.mapNotNull { line ->
    line.findCorruption()
}.sumOf { it.corruptionPoints }

fun solutionPart2(input: List<String>) = input.filter { line ->
    line.findCorruption() == null
}.map { line ->
    line.completeLine().fold(0L) { acc, chunk ->
        acc * 5 + chunk.completionPoints
    }
}.sorted().let { it[(it.size - 1) / 2] }
