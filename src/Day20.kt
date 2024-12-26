import kotlin.math.absoluteValue

fun main() {

    fun reachableByCheat(current: Set<Coordinate>, stepsRemaining: Int, maze: Set<Coordinate>): Set<Coordinate> {
        if (stepsRemaining == 0) {
            return current.filter { it in maze }.toSet()
        }
        val newReachable = current.toMutableSet()
        current.forEach { pos ->
            newReachable.addAll(ALL_DIRECTIONS.map { pos + it })
        }
        return reachableByCheat(newReachable, stepsRemaining - 1, maze)
    }

    fun part2(maze: Set<Coordinate>, start: Coordinate, end: Coordinate): Int {
        val distances = mutableMapOf(end to 0)
        var position = end
        var distance = 0
        while (position != start) {
            position = ALL_DIRECTIONS
                .map { it + position }
                .first { it in maze && it !in distances }
            distances[position] = ++distance
        }

        val cheats = mutableMapOf<Pair<Coordinate, Coordinate>, Int>()
        position = start
        while (position != end) {
            reachableByCheat(setOf(position), 20, maze)
                .filter { it in distances }
                .map {
                    val diff = position - it
                    val cheatDist = diff.first.absoluteValue + diff.second.absoluteValue
                    (position to it) to distances[position]!! - cheatDist - distances[it]!!
                }
                .filter { it.second >= 50 }
                .forEach { cheats += it }

            position = ALL_DIRECTIONS
                .map { position + it }
                .first {
                    it in maze && distances[position]!! > distances[it]!!
                }
        }
        println(cheats.entries.groupBy { it.value }.map { it.key to it.value.size }.toMap())
        return cheats.filterValues { it >= 100 }.size
    }

    fun part1(maze: Set<Coordinate>, start: Coordinate, end: Coordinate): Int {
        val distances = mutableMapOf(end to 0)
        var position = end
        var distance = 0
        while (position != start) {
            position = ALL_DIRECTIONS
                .map { it + position }
                .first { it in maze && it !in distances }
            distances[position] = ++distance
        }

        val cheats = mutableMapOf<Pair<Coordinate, Coordinate>, Int>()
        position = start
        while (position != end) {
            ALL_DIRECTIONS
                .map { it * 2 + position }
                .filter { it in distances }
                .map { (position to it) to distances[position]!! - 2 - distances[it]!! }
                .forEach { cheats += it }

            position = ALL_DIRECTIONS
                .map { position + it }
                .first {
                    it in maze && distances[position]!! > distances[it]!!
                }
        }
        return cheats.filter { it.value >= 100 }.size
    }

    val input = readInput("Day20")

    val maze = input.mapPositionsNotNull { x, y, c ->
        if (c in setOf('.', 'S', 'E')) {
            (x to y)
        } else {
            null
        }
    }.toSet()

    val start = input.mapPositionsNotNull { x, y, c -> if (c == 'S') x to y else null }.first()
    val end = input.mapPositionsNotNull { x, y, c -> if (c == 'E') x to y else null }.first()

    part1(maze, start, end).println()
    part2(maze, start, end).println()
}
