import java.io.File

fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}


data class Player(val nr: Int, val pos: Int, val points: Int = 0)
data class Start(val p1: Player, val p2: Player)

class DeterministicDice(private val sides: Int = 100) {
    private var roll = 1
    var totalRolls = 0
    fun roll(): Int = roll.let { next ->
        roll++
        totalRolls++
        if (next == sides) {
            roll = 1
        }

        // println("Dice rolls $next")
        next
    }
}

fun Player.turn(dice: DeterministicDice): Player {
    var roll = 0
    repeat(3) { roll += dice.roll() }
    val newPos = (pos + roll) % 10
    val score = if (newPos == 0) 10 else newPos
    return Player(nr, newPos, points + score)
}

class Game(
    private val dice: DeterministicDice,
    private val start: List<Player>,
    private val maxScore: Int
) {

    fun play(): List<Player> {
        var turn = 0
        var players = mutableListOf(start[0], start[1])

        while (players[0].points < maxScore && players[1].points < maxScore) {
            val current = players[turn % 2]
            val updated = current.turn(dice)
            players[turn % 2] = updated
            // println("Player ${updated.nr+1} lands at ${updated.pos} and now has ${updated.points} points")
            turn++
        }

        return players
    }
}

fun List<Player>.looser(score: Int) = if (this[0].points >= score) this[1] else this[0]

fun List<String>.mapInput(): Start = this.let {
    Start(Player(0, it[0].takeLast(1).toInt()), Player(1, it[1].takeLast(1).toInt()))
}

fun solutionPart1(input: Start) = input.let { start ->
    val dice = DeterministicDice()
    val players = Game(dice = dice, listOf(start.p1, start.p2), 1000).play()
    players.looser(1000).points * dice.totalRolls
}

private val nOfPaths = intArrayOf(0, 0, 0, 1, 3, 6, 7, 6, 3, 1)
fun Player.getNewPos(move: Int) = ((pos + move) % 10).let { if (it == 0) 10 else it }

fun MutableList<Long>.solve(
    playerTurn: Int,
    players: List<Player>,
    paths: Long = 1
) {
    if (players[0].points >= 21) {
        this[0] = this[0] + paths
    } else if (players[1].points >= 21) {
        this[1] = this[1] + paths
    } else {
        for (i in 3..9) { // Possible rolls on 3 rolls.
            val notPlayingIdx = (playerTurn + 1) % 2
            val updated = listOf(
                Player(
                    nr = players[playerTurn].nr,
                    pos = players[playerTurn].getNewPos(i),
                    points = players[playerTurn].points + players[playerTurn].getNewPos(i)
                ),
                Player(
                    nr = players[notPlayingIdx].nr,
                    pos = players[notPlayingIdx].pos,
                    points = players[notPlayingIdx].points
                )
            ).sortedBy { it.nr }
            this.solve(notPlayingIdx, updated, paths * nOfPaths[i])
        }
    }
}

fun solutionPart2(input: Start): Long = input.let { start ->
    val wins = MutableList(2) { 0L }
    wins.solve(0, listOf(start.p1, start.p2))

    return wins.maxOrNull()!!
}
