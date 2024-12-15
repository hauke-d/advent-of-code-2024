sealed interface Point
data object Obstacle : Point
data object NoObstacle : Point

sealed class Direction(val x: Int, val y: Int) {
    abstract fun next(): Direction
    fun add(position: Coordinate): Coordinate {
        return x + position.first to y + position.second
    }
}

data object Up : Direction(0, -1) {
    override fun next(): Direction = Right
}

data object Right : Direction(1, 0) {
    override fun next(): Direction = Down
}

data object Down : Direction(0, 1) {
    override fun next(): Direction = Left
}

data object Left : Direction(-1, 0) {
    override fun next(): Direction = Up
}

sealed interface WalkResult
data object Loop : WalkResult
data class Done(
    val position: Coordinate,
    val direction: Direction,
    val visited: Map<Coordinate, Set<Direction>>
) : WalkResult
data class OutOfBounds(val visited: Map<Coordinate, Set<Direction>>) : WalkResult

fun main() {
    fun walk(
        startPosition: Coordinate,
        startDirection: Direction,
        grid: Map<Coordinate, Point>,
        startVisited: Map<Coordinate, Set<Direction>> = emptyMap(),
        maxSteps: Int = -1
    ): WalkResult {
        var position = startPosition
        var direction: Direction = startDirection
        val visited = startVisited.toMutableMap()
        var stepsRemaining = maxSteps
        while (maxSteps == -1 || stepsRemaining > 0) {
            if (position in visited && direction in visited[position]!!) {
                return Loop
            }
            visited[position] = visited[position].orEmpty() + direction
            val newPos = direction.add(position)
            when (grid[newPos]) {
                is Obstacle -> direction = direction.next()
                is NoObstacle -> position = newPos
                else -> return OutOfBounds(visited)
            }
            stepsRemaining -= 1
        }
        return Done(position, direction, visited)
    }

    fun part1(start: Coordinate, grid: Map<Coordinate, Point>): Int {
        return (walk(start, Up, grid) as OutOfBounds).visited.size
    }

    fun part2(start: Coordinate, grid: Map<Coordinate, Point>): Int {
        // One step at a time and branch out
        var position = start
        var direction: Direction = Up
        val visited = mapOf<Coordinate, Set<Direction>>()
        var done = false
        val loopObstacles = mutableSetOf<Coordinate>()
        while (!done) {
            when (val result = walk(position, direction, grid, visited, 1)) {
                is OutOfBounds -> {
                    done = true
                }
                is Done -> {
                    position = result.position
                    direction = result.direction
                    // Check if we can create a loop by adding obstacle right in front of us
                    val newObstacle = direction.add(position)
                    val newGrid = grid + (newObstacle to Obstacle)
                    if (newObstacle in grid && walk(start, Up, newGrid, visited) is Loop) {
                        loopObstacles.add(newObstacle)
                    }
                }
                else -> throw IllegalStateException("Should not happen.")
            }
        }
        return loopObstacles.size
    }

    val lines = readInput("Day06")

    val grid = lines.mapPositionsNotNull { x, y, c ->
        if (c == '#') { (x to y) to Obstacle } else { (x to y) to NoObstacle }
    }.toMap()

    val position = lines.mapPositionsNotNull { x, y, c ->
        if (c == '^') { x to y } else { null }
    }.first()

    part1(position, grid).println()
    part2(position, grid).println()
}
