import java.util.concurrent.ConcurrentHashMap

fun main() {
    fun expand(number: Long, stepsRemaining: Int, cache: MutableMap<Long, MutableMap<Int, Long>> = ConcurrentHashMap<Long, MutableMap<Int, Long>>()): Long {
        val numberStr = "$number"
        val numDigits = numberStr.length
        if (stepsRemaining == 0) {
            return 1L
        }

        return (if (number == 0L) {
            listOf(1L)
        } else if (numDigits % 2 == 0) {
            listOf(
                numberStr.substring(0, numDigits / 2).toLong(),
                numberStr.substring(numDigits / 2, numDigits).toLong()
            )
        } else {
            listOf(number * 2024L)
        }).sumOf { new ->
            val cached = cache[new]?.get(stepsRemaining - 1)
            if (cached == null) {
                val computed = expand(new, stepsRemaining - 1, cache)
                cache.computeIfAbsent(new) { ConcurrentHashMap() }[stepsRemaining - 1] = computed
                computed
            } else {
                cached
            }
        }
    }

    fun part1(input: List<Long>): Long {
        return input.sumOf { expand(it, 25) }
    }

    fun part2(input: List<Long>): Long {
        return input.sumOf { expand(it, 75) }
    }

    val input = readInput("Day11").splitEach { it.toLong() }.first()

    part1(input).println()
    part2(input).println()
}
