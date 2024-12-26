import java.util.concurrent.ConcurrentHashMap
import kotlin.math.absoluteValue

private sealed interface Tile16
private data object Free : Tile16
private data object Start : Tile16
private data object End : Tile16

fun main() {

    fun stepCost(current: Coordinate, next: Coordinate, direction: Coordinate): Int {
        return if (next == current + direction) {
            1
        } else {
            1001
        }
    }

    val scoreCache: MutableMap<List<Coordinate>, Int> = mutableMapOf()
    fun score(path: List<Coordinate>): Int {
        if (path.size > 2) {
            val prev = path.subList(0, path.size - 1)
            if (prev in scoreCache) {
                val direction = path[path.size - 2] - path[path.size - 3]
                val newScore = stepCost(path[path.size - 2], path[path.size - 1], direction) + scoreCache[prev]!!
                scoreCache[path] = newScore
                return newScore
            }
        }

        val startDirection = 0 to 1 // start facing east
        val score = path.zipWithNext().fold(startDirection to 0) { acc, pair ->
            val current = pair.first
            val next = pair.second
            val dir = acc.first
            val score = acc.second
            (next - current) to score + stepCost(current, next, dir)
        }.second
        scoreCache[path] = score
        return score
    }

    fun paths(
        path: List<Coordinate>,
        goal: Coordinate,
        maze: Map<Coordinate, Tile16>,
        scores: MutableMap<Coordinate, Int> = ConcurrentHashMap()
    ): Set<List<Coordinate>> {
        val position = path.last()
        if (position == goal) {
            return setOf(path)
        }
        return ALL_DIRECTIONS.map {
            position + it
        }.filter {
            it in maze && it !in path
        }.flatMap { newPos ->
            val newPath = path + newPos
            val score = score(newPath)
            val minFinalScore = (goal.first - newPos.first).absoluteValue + (goal.second - newPos.second).absoluteValue + score
            if (
                (newPos !in scores || scores[newPos]!! >= score - 1000) && // no shorter path to current pos
                (goal !in scores || minFinalScore <= scores[goal]!!) // cannot be shorter than current minimum
            ) {
                scores[newPos] = score
                paths(newPath, goal, maze, scores)
            } else {
                setOf()
            }
        }.toSet()
    }

    fun part1(maze: Map<Coordinate, Tile16>): Int {
        val start = maze.entries.find { it.value is Start }!!.key
        val end = maze.entries.find { it.value is End }!!.key
        return paths(listOf(start), end, maze).map { path ->
            score(path)
        }.min()
    }

    fun part2(maze: Map<Coordinate, Tile16>): Int {
        val start = maze.entries.find { it.value is Start }!!.key
        val end = maze.entries.find { it.value is End }!!.key
        val paths = paths(listOf(start), end, maze)
        val scores = paths.associateWith { path -> score(path) }
        val minScore = scores.values.min()

        return scores.filter {
            it.value == minScore
        }.keys.flatten().toSet().size
    }

    val maze = readInput("Day16").mapPositionsNotNull { x, y, c ->
        (x to y) to when (c) {
            '.' -> Free
            'S' -> Start
            'E' -> End
            else -> return@mapPositionsNotNull null
        }
    }.toMap()

    part1(maze).println()
    part2(maze).println()
}
