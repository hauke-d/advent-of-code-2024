import kotlin.math.absoluteValue

fun main() {
    fun isSafe(levels: List<Int>): Boolean {
        // Pairwise differences
        val steps = levels.zipWithNext().map { (a, b) -> a-b }

        return (steps.all { it > 0 } ||
                steps.all { it < 0 }) &&
                steps.none { it.absoluteValue > 3 }
    }

    fun part1(input: List<String>): Int {
        val levels = input.splitEach { it.toInt() }
        return levels.filter { isSafe(it) }.size
    }

    fun part2(input: List<String>): Int {
        val levelList = input.splitEach { it.toInt() }
        // Try removing every item until safe (-1 = do not remove any item)
        return levelList.filter { levels ->
            (-1..<levels.size).any { removeIdx ->
                isSafe(levels.filterIndexed { i, _ -> i != removeIdx })
            }
        }.size
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
