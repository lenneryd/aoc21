import java.io.File
import java.math.BigInteger


fun main() {
    val input = File("input.txt").readLines().mapInput()
    val answer = when (System.getenv("part")) {
        "part2" -> solutionPart2(input)
        else -> solutionPart1(input)
    }
    println(answer)
}

sealed class Packet(open val version: Int, open val type: Int) {
    abstract fun calculatedValue(): Long

    data class ValuePacket(
        override val version: Int,
        override val type: Int,
        val value: Long
    ) : Packet(version, type) {
        override fun calculatedValue(): Long = value
    }

    data class OperatorPacket(
        override val version: Int,
        override val type: Int,
        val subPackets: List<Packet>,
    ) : Packet(version, type) {
        override fun calculatedValue(): Long = when (this.type) {
            0 -> this.subPackets.sumOf { it.calculatedValue() }
            1 -> this.subPackets.fold(1) { acc, packet -> acc * packet.calculatedValue() }
            2 -> this.subPackets.minOf { it.calculatedValue() }
            3 -> this.subPackets.maxOf { it.calculatedValue() }
            5 -> (this.subPackets[0].calculatedValue() > this.subPackets[1].calculatedValue()).toLong()
            6 -> (this.subPackets[0].calculatedValue() < this.subPackets[1].calculatedValue()).toLong()
            7 -> (this.subPackets[0].calculatedValue() == this.subPackets[1].calculatedValue()).toLong()
            else -> throw Exception("Illegal operation found")
        }
    }
}

private fun Boolean.toLong() = if (this) 1L else 0L

data class SubPacketLength(val isBitLength: Boolean, val length: Int)
data class SubPacketLengthResult(val read: Int, val result: SubPacketLength)
data class IntResult(val read: Int, val result: Int)
data class ValueGroupResult(val read: Int, val isLast: Boolean, val result: String)
data class ValueResult(val read: Int, val result: Long)
data class SubPacketsResult(val read: Int, val result: List<Packet>)
data class PacketResult(val read: Int, val result: Packet)

fun String.readPacketVersion(offset: Int): IntResult =
    IntResult(3, this.substring(offset, offset + 3).binToDec())

fun String.readPacketType(offset: Int): IntResult = IntResult(3, this.substring(offset, offset + 3).binToDec())
fun String.readValueGroup(offset: Int): ValueGroupResult = ValueGroupResult(
    5,
    this[offset] == '0', // Figure out if last.
    this.substring(offset + 1, offset + 5)
)

fun String.readFullValue(offset: Int): ValueResult = this.let { str ->
    var currentOffset = offset
    var group = str.readValueGroup(currentOffset)
    val groups = mutableListOf(group)
    while (!group.isLast) {
        currentOffset += group.read
        group = str.readValueGroup(currentOffset)
        groups.add(group)
    }

    ValueResult(groups.sumOf { it.read }, groups.joinToString("") { it.result }.binToLongDec())
}

fun String.readSubPackets(offset: Int, totalBitLength: Int): List<PacketResult> = this.let { str ->
    var currentOffset = offset

    val startSubPacketOffset = currentOffset
    val list = mutableListOf(
        str.read(currentOffset).also {
            currentOffset += it.read
        }
    )

    while (currentOffset < startSubPacketOffset + totalBitLength) {
        list.add(
            str.read(currentOffset).also {
                currentOffset += it.read
            }
        )
    }

    list
}

fun String.readNumSubPackets(offset: Int, number: Int): List<PacketResult> = this.let { str ->
    var currentOffset = offset
    val list = mutableListOf<PacketResult>()
    for (i in 0 until number) {
        list.add(str.read(currentOffset).also { currentOffset += it.read })
    }
    return list
}

fun String.readSubPacketLength(offset: Int): SubPacketLengthResult = this.let { str ->
    val isInBits = str[offset] == '0'
    val length = if (isInBits) 15 else 11
    val lengthOffset = offset + 1
    SubPacketLengthResult(
        read = length + 1, // +1 for ID value.
        result = SubPacketLength(isInBits, str.substring(lengthOffset, lengthOffset + length).binToDec())
    )
}

fun String.readSubPackets(offset: Int): SubPacketsResult = this.let { str ->
    var currentOffset = offset
    str.readSubPacketLength(currentOffset).also { currentOffset += it.read }.let { subPacketType ->
        when {
            subPacketType.result.isBitLength -> {
                val results = readSubPackets(currentOffset, totalBitLength = subPacketType.result.length)
                SubPacketsResult(
                    subPacketType.read + results.sumOf { it.read },
                    results.map { it.result }
                )
            }

            else -> {
                val results = readNumSubPackets(currentOffset, number = subPacketType.result.length)
                SubPacketsResult(
                    subPacketType.read + results.sumOf { it.read },
                    results.map { it.result }
                )
            }
        }
    }
}

fun String.readValuePacket(version: Int, type: Int, offset: Int): PacketResult = this.let { str ->
    val result = str.readFullValue(offset)
    PacketResult(
        result.read,
        Packet.ValuePacket(
            version,
            type,
            result.result
        )
    )
}

fun String.readOperatorPacket(version: Int, type: Int, offset: Int): PacketResult = this.let { str ->
    val result = str.readSubPackets(offset)
    PacketResult(
        result.read,
        Packet.OperatorPacket(
            version,
            type,
            result.result,
        )
    )
}

fun String.read(offset: Int = 0): PacketResult = this.let { str ->
    var currentOffset = offset
    var read = 0
    val version = str.readPacketVersion(currentOffset).also {
        currentOffset += it.read
        read += it.read
    }.result
    val result = when (val type = str.readPacketType(currentOffset).also {
        currentOffset += it.read
        read += it.read
    }.result) {
        4 -> str.readValuePacket(version, type, currentOffset)
        else -> str.readOperatorPacket(version, type, currentOffset)
    }
    PacketResult(read + result.read, result.result)
}

fun List<String>.mapInput() = first().hexToBin()

fun Packet.OperatorPacket.recursiveSum(): Int = this.subPackets.sumOf { subPacket ->
    when (subPacket) {
        is Packet.ValuePacket -> subPacket.version
        is Packet.OperatorPacket -> subPacket.recursiveSum()
    }
} + this.version

fun solutionPart1(input: String): Int {
    return when (val packet = input.read().result) {
        is Packet.ValuePacket -> packet.version
        is Packet.OperatorPacket -> packet.recursiveSum()
    }
}


fun solutionPart2(input: String): Long {
    return input.read().result.calculatedValue()
}


fun String.hexToBin(): String {
    return String.format("%" + this.length * 4 + "s", BigInteger(this, 16).toString(2)).replace(" ", "0")
}

fun String.binToDec(): Int {
    return BigInteger(this, 2).toString(10).toInt()
}

fun String.binToLongDec(): Long {
    return BigInteger(this, 2).toString(10).toLong()
}
