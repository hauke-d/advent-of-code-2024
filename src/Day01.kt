import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val pairs = input.splitEachToPairs { it.toInt() }
        val left = pairs.map { it.first }.sorted()
        val right = pairs.map { it.second }.sorted()

        return left.zip(right).sumOf { (a, b) -> (a - b).absoluteValue }
    }

    fun part2(input: List<String>): Int {
        val pairs = input.splitEachToPairs { it.toInt() }
        val left = pairs.map { it.first }
        val counts = pairs.map { it.second }.groupingBy { it }.eachCount()

        return left.fold(0) { acc, v ->
            acc + v * (counts[v] ?: 0)
        }
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
