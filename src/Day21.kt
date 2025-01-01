import kotlin.math.absoluteValue

val keyToCoordinate = mapOf(
    '7' to (0 to 0),
    '8' to (1 to 0),
    '9' to  (2 to 0),
    '4' to (0 to 1),
    '5' to (1 to 1),
    '6' to (2 to 1),
    '1' to  (0 to 2),
    '2' to (1 to 2),
    '3' to  (2 to 2),
    '0' to (1 to 3),
    'A' to (2 to 3)
)

val coordinateToKey = keyToCoordinate.map { it.value to it.key }.toMap()

val arrowToCoordinate = mapOf(
    '^' to (1 to 0),
    'A' to (2 to 0),
    '<' to (0 to 1),
    'v' to (1 to 1),
    '>' to (2 to 1)
)

val arrowToDirection = mapOf(
    '^' to (0 to -1),
    '<' to (-1 to 0),
    '>' to (1 to 0),
    'v' to (0 to 1)
)

val coordinateToArrow = arrowToCoordinate.map { it.value to it.key }.toMap()
val directionToArrow = arrowToDirection.map { it.value to it.key }.toMap()

sealed interface State
data object InvalidState : State
sealed interface RobotState : State {
    fun move(direction: Coordinate) : State
    fun activate(): State
    fun output(): String
    fun position(): Char
}

data class ArrowState(val state: Char, val child: RobotState) : RobotState {
    override fun move(direction: Coordinate): State {
        val newCoordinate = arrowToCoordinate[state]!! + direction
        return if (newCoordinate in coordinateToArrow) {
            copy(state = coordinateToArrow[newCoordinate]!!)
        } else {
            InvalidState
        }
    }

    override fun activate(): State {
        return if (state == 'A') {
            when (val activated = child.activate()) {
                is InvalidState -> InvalidState
                is RobotState -> copy(child = activated)
            }
        } else {
            when (val moved = child.move(arrowToDirection[state]!!)) {
                is InvalidState -> InvalidState
                is RobotState -> copy(child = moved)
            }
        }
    }

    override fun output(): String {
        return child.output()
    }

    override fun position(): Char {
        return child.position()
    }
}

data class KeypadState(val state: Char, val output: String = "") : RobotState {
    override fun move(direction: Coordinate): State {
        val newCoordinate = keyToCoordinate[state]!! + direction
        return if (newCoordinate in coordinateToKey) {
            copy(state = coordinateToKey[newCoordinate]!!)
        } else {
            InvalidState
        }
    }

    override fun activate(): State = copy(state, output = output + state)

    override fun output(): String {
        return output
    }

    override fun position(): Char {
        return state
    }
}

fun main() {
    fun shortestPath(code: String, initialState: RobotState): Int {
        var states: Set<RobotState> = setOf(initialState)
        var rounds = 0
        while (states.none { it.output() == code }) {
            rounds++
            val newPaths = mutableSetOf<RobotState>()
            states.forEach { path ->
                "<>^vA".map { key ->
                    key to if (key == 'A') {
                        path.activate()
                    } else {
                        path.move(arrowToDirection[key]!!)
                    }
                }.filter {
                    val s = it.second
                    s is RobotState && code.startsWith(s.output())
                }.forEach {
                    newPaths.add(it.second as RobotState)
                }
            }
            states = newPaths

            // trim states by matching output length
            val maxLength = states.maxOf { it.output().length }
            states = states.filter { it.output().length == maxLength }.toSet()
        }
        return rounds
    }

    fun part1(codes: List<String>): Long {
        val initialState = ArrowState('A', ArrowState('A', KeypadState('A')))
        return codes.sumOf { code ->
            shortestPath(code, initialState) * code.trimEnd('A').toLong()
        }
    }

    fun part2(codes: List<String>): Long {
        val inner: RobotState = KeypadState('5')
        val initialState = (0..24).fold(inner) { acc, i ->
            println("--- $i ---")
            shortestPath("6", acc).println()
            shortestPath("4", acc).println()
            shortestPath("2", acc).println()
            shortestPath("8", acc).println()
            ArrowState('A', acc)
        }

        return 0
    }

    val codes = readInput("Day21_test")

    part1(codes).println()
    part2(codes).println()
}
