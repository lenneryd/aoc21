import kotlin.math.ceil
import kotlin.math.floor

fun main() {
    val input = getInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

sealed class Node(open var parent: Snail?) {
    class Regular(var value: Int, override var parent: Snail?) : Node(parent) {
        operator fun plus(other: Regular) = Regular(value + other.value, parent)
        override fun toString(): String {
            return "$value"
        }
    }

    class Snail(var left: Node, var right: Node, override var parent: Snail?) : Node(parent) {
        operator fun plus(other: Snail) = Snail(this, other, parent).also { newParent ->
            this.parent = newParent
            other.parent = newParent
        }.reduce()

        override fun toString(): String {
            return "[$left, $right]"
        }
    }
}

fun Node.Snail.reduce(): Node.Snail = this.let { snail ->
    val original = snail.toString()
    var current = snail
    var performedOperation = true
    while (performedOperation) {
        performedOperation = false

        val list = this.asList()

        val candidate = findExplosionCandidate(0)
        if (candidate != null) {
            candidate.explode(list)
            performedOperation = true
        }

        if (!performedOperation) {
            list.firstOrNull { node ->
                node is Node.Regular && node.value >= 10
            }?.let {
                val node = it as Node.Regular
                val parent = node.parent!!
                val isLeftSide = node.isLeftSide(parent)
                if (isLeftSide) {
                    parent.left = node.split()
                } else {
                    parent.right = node.split()
                }

                performedOperation = true
            }
        }
    }

    current
}

fun Node.findExplosionCandidate(depth: Int): Node.Snail? {
    when (this) {
        is Node.Regular -> return null
        is Node.Snail -> {
            // Find any left side candidate, if any.
            val leftSideCandidate = this.left.findExplosionCandidate(depth + 1)
            if (leftSideCandidate != null) return leftSideCandidate

            // Check whether this node is our candidate.
            if (depth >= 4 && this.left is Node.Regular && this.right is Node.Regular) {
                return this
            }

            // Find any right side candidate, if any.
            return right.findExplosionCandidate(depth + 1)
        }
    }
}

fun Node.Snail.explode(list: List<Node>) =
    this.let { snail ->
        val parent = this.parent!!
        val isLeftSide = this.isLeftSide(parent)
        // List of regular numbers with parents.
        val idx = list.indexOfFirst { it.parent == snail }
        val left = idx - 1 downTo 0 // Remove one since indexOf points at first occurance of snail as parent.
        val right = idx + 2 until list.size // Add two since node has a pair of regular numbers.

        for (i in left) {
            val node = list[i]
            if (node is Node.Regular) {
                node.value += (snail.left as Node.Regular).value
                break
            }
        }

        for (i in right) {
            val node = list[i]
            if (node is Node.Regular) {
                node.value += (snail.right as Node.Regular).value
                break
            }
        }

        if (isLeftSide) {
            parent.left = Node.Regular(0, parent)
        } else {
            parent.right = Node.Regular(0, parent)
        }
    }

fun Node.isLeftSide(parent: Node.Snail): Boolean = parent.left == this
fun Node.asList(): List<Node> {
    return when (this) {
        is Node.Regular -> listOf(this)
        is Node.Snail -> this.left.asList() + this.right.asList()
    }
}

fun Node.magnitude(): Long {
    return when (this) {
        is Node.Regular -> this.value.toLong()
        is Node.Snail -> this.left.magnitude() * 3L + this.right.magnitude() * 2L
    }
}

fun Node.Regular.split(): Node.Snail = this.value.toFloat().let { number ->
    val left = Node.Regular(floor(number / 2.0f).toInt(), null)
    val right = Node.Regular(ceil(number / 2.0f).toInt(), null)
    Node.Snail(left, right, this.parent).also {
        left.parent = it
        right.parent = it
    }
}

fun List<List<Any>>.mapInput(): List<Node> = map { line ->
    line.parse()
}


fun List<*>.parse(): Node.Snail = this.let { contents ->
    val left: Node = when (val left = contents[0]) {
        is List<*> -> left.parse()
        else -> Node.Regular(left as Int, null)
    }

    val right = when (val right = contents[1]) {
        is List<*> -> right.parse()
        else -> Node.Regular(right as Int, null)
    }
    Node.Snail(left, right, null).also { root ->
        left.parent = root
        right.parent = root
    }
}

fun solutionPart1(input: List<List<Any>>): Long {
    val nodes = input.mapInput()

    val initial: Node.Snail? = null
    val result = nodes.fold(initial) { acc, node ->
        val foldResult = if (acc != null) {
            acc + node as Node.Snail
        } else node as Node.Snail

        foldResult
    }
    return result!!.magnitude()
}

fun solutionPart2(input: List<List<Any>>): Long {
    var currentMax = 0L
    for (i in input.indices) {
        for (j in input.indices) {
            if (i != j) {
                val nodes = input.mapInput()
                val magnitude = (nodes[i] as Node.Snail + nodes[j] as Node.Snail).magnitude()
                if (magnitude > currentMax) {
                    currentMax = magnitude
                }
            }
        }
    }

    return currentMax
}
