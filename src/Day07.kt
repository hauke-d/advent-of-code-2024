
fun possibleResults(values: List<Long>, max: Long, isPart2: Boolean = false): Set<Long> {
    if (values.size == 1) {
        return values.toSet()
    }
    if (values[0] > max) {
        return emptySet()
    }
    val rest = values.drop(2)
    val add = possibleResults(listOf(values[0] + values[1]) + rest, max, isPart2)
    val mul = possibleResults(listOf(values[0] * values[1]) + rest, max, isPart2)
    val concat = if (isPart2) {
        possibleResults(listOf("${values[0]}${values[1]}".toLong()) + rest, max, true)
    } else {
        emptySet()
    }
    return add + mul + concat
}

fun main() {
    fun part1(input: List<Pair<Long, List<Long>>>): Long {
        return input.filter {
            it.first in possibleResults(it.second, it.first)
        }.sumOf { it.first }
    }

    fun part2(input: List<Pair<Long, List<Long>>>): Long {
        return input.filter {
            it.first in possibleResults(it.second, it.first, true)
        }.sumOf { it.first }
    }

    val input = readInput("Day07").map { line ->
        val kv = line.split(": ".toRegex())
        kv[0].toLong() to kv[1].split("\\s+".toRegex()).map { it.toLong() }
    }

    part1(input).println()
    part2(input).println()
}
