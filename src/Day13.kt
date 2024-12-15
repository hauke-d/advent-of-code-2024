import kotlin.math.roundToLong

fun main() {
    val numberRegex = "[0-9]+".toRegex()

    operator fun Pair<Long, Long>.times(other: Long): Pair<Long, Long> {
        return first * other to second * other
    }

    operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>): Pair<Long, Long> {
        return first + other.first to second + other.second
    }

    operator fun Pair<Long, Long>.minus(other: Pair<Long, Long>): Pair<Long, Long> {
        return first - other.first to second - other.second
    }

    fun findSolution(a: Pair<Long, Long>, b: Pair<Long, Long>, prize: Pair<Long, Long>): Long? {
        val y = ((prize.second - prize.first * a.second.toDouble() / a.first) /
                (b.second - b.first * a.second.toDouble() / a.first)).roundToLong()
        val x = ((prize.first - b.first * y) / a.first.toDouble()).roundToLong()

        return if (prize == a * x + b * y) {
            3 * x + y
        } else {
            null
        }
    }

    fun part2(input: List<Triple<Pair<Long, Long>, Pair<Long, Long>, Pair<Long, Long>>>): Long {
        val add = 10_000_000_000_000L
        return input.sumOf {
            val prize = it.third.first + add to it.third.second + add
            findSolution(it.first, it.second, prize) ?: 0L
        }
    }

    fun part1(input: List<Triple<Pair<Long, Long>, Pair<Long, Long>, Pair<Long, Long>>>): Long {
        return input.sumOf {
            findSolution(it.first, it.second, it.third) ?: 0L
        }
    }

    val input = readInput("Day13").foldIndexed(listOf(emptyList<Pair<Long, Long>>())) { i,  machines, line ->
        if (i % 4 == 3) {
            machines + listOf(listOf())
        } else {
            val numbers = numberRegex.findAll(line).map { it.value.toLong() }.toList()
            machines.subList(0, machines.size - 1) + listOf(machines[machines.size - 1] + (numbers[0] to numbers[1]))
        }
    }.map {
        Triple(it[0], it[1], it[2])
    }

    part1(input).println()
    part2(input).println()
}
