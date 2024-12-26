sealed interface Tile
data object Wall : Tile
data class Box(val id: Int) : Tile


fun main() {

    fun part1(
        robot: Coordinate,
        input: Map<Coordinate, Tile>,
        instructions: List<Coordinate>
    ): Int {
        val finalState = instructions.fold(robot to input) { acc, move ->
            val newRobot = acc.first + move
            var check = newRobot
            val boxes = mutableSetOf<Coordinate>()
            while (acc.second[check] is Box) {
                boxes.add(check)
                check += move
            }
            if (input[check] != Wall) {
                newRobot to acc.second.mapKeys {
                    if (it.key in boxes) {
                        it.key + move
                    } else {
                        it.key
                    }
                }
            } else {
                acc
            }
        }

        return finalState.second.filter { it.value is Box }.keys.map { pos ->
            pos.first + 100 * pos.second
        }.sum()
    }

    fun part2(
        robot: Coordinate,
        input: Map<Coordinate, Tile>,
        instructions: List<Coordinate>
    ): Int {
        val finalState = instructions.fold(robot to input) { acc, move ->
            val newRobot = acc.first + move
            val boxes = mutableSetOf<Box>()
            val toCheck = mutableSetOf(newRobot)
            var blocked = false
            while (toCheck.isNotEmpty()) {
                val check = toCheck.first()
                val checkTile = acc.second[check]
                if (checkTile is Wall) {
                    blocked = true
                    break
                } else if (checkTile is Box) {
                    // check the other box + one ahead
                    toCheck.add(check + move)
                    val otherHalf = acc.second
                        .entries
                        .find { it.value == checkTile && it.key != check }!!
                    toCheck.add(otherHalf.key + move)
                    boxes.add(checkTile)
                }
                toCheck.remove(check)
            }

            if (!blocked) {
                newRobot to acc.second.mapKeys {
                    if (it.value in boxes) {
                        it.key + move
                    } else {
                        it.key
                    }
                }
            } else {
                acc
            }
        }

        return finalState.second.filter { it.value is Box }.entries.groupBy { it.value }.map {
            it.value.map { it.key.first }.min() + 100*it.value.first().key.second
        }.sum()
    }

    val input = readInput("Day15")
    val warehouse = input
        .takeWhile { it.isNotBlank() }
        .mapPositionsNotNull { x, y, c ->
            (x to y) to when (c) {
                '#' -> Wall
                'O' -> Box(0)
                else -> return@mapPositionsNotNull null
            }
        }.toMap()

    val warehouseP2 = input
        .takeWhile { it.isNotBlank() }
        .mapPositionsNotNull { x, y, c ->
            val value = when (c) {
                '#' -> Wall
                'O' -> Box(1000*x+y)
                else -> return@mapPositionsNotNull null
            }
            listOf(
                2*x to y,
                2*x + 1 to y
            ).map { it to value }
        }.flatten().toMap()

    val robot = input
        .takeWhile { it.isNotBlank() }
        .mapPositionsNotNull { x, y, c -> if (c == '@') x to y else null }
        .first()

    val robotP2 = robot.first * 2 to robot.second

    val instructions = input
        .takeLastWhile { it.isNotBlank() }
        .joinToString("")
        .map {
            when (it) {
                '>' -> 1 to 0
                '<' -> -1 to 0
                '^' -> 0 to -1
                'v' -> 0 to 1
                else -> error("Should not happen")
            }
        }

    part1(robot, warehouse, instructions).println()
    part2(robotP2, warehouseP2, instructions).println()
}
