import kotlin.math.max
import kotlin.math.sqrt

fun main() {

    fun sequence(initial: Long): List<Long> {
        return (0..<2000).fold(listOf(initial)) { numbers, _ ->
            val acc = numbers.last()
            val first = (acc * 64 xor acc) % 16777216
            val second = (first / 32 xor first) % 16777216
            numbers + ((second * 2048) xor second) % 16777216
        }
    }

    fun part1(numbers: List<Long>): Long {
        return numbers.sumOf {
            sequence(it).last()
        }
    }

    fun part2(numbers: List<Long>): Int {
        val allBananas : MutableMap<List<Int>, Int> = mutableMapOf()
        numbers.forEach { number ->
            val bananas : MutableMap<List<Int>, Int> = mutableMapOf()
            val prices = sequence(number).map { (it % 10).toInt() }
            val diffs = prices.zipWithNext().map { (a, b) ->
                b - a
            }
            prices.forEachIndexed { i, price ->
                if (i >= 4) {
                    val seq = diffs.subList(i - 4, i)
                    bananas.putIfAbsent(seq, price)
                }
            }
            bananas.forEach {
                allBananas[it.key] = allBananas.getOrPut(it.key) { 0 } + it.value
            }
        }

        val max = allBananas.values.max()
        val best = allBananas.filter { it.value == max }
        println(best)
        return max
    }

    val input = readInput("Day22").map { it.toLong() }

    part1(input).println()
    part2(input).println()

}
