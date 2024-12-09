sealed interface Location {}
data class Antenna(val frequency: Char) : Location
data object Nothing : Location

fun main() {

    fun part1(antennas: Map<Location, List<Pair<Int, Int>>>, grid: Set<Pair<Int, Int>>): Int {
        return antennas.flatMap {
            val locations = it.value
            // All possible pairs
            locations.indices.flatMap { i ->
                (i + 1..<locations.size).flatMap { j ->
                    val a = locations[i]
                    val b = locations[j]
                    val diff = a - b
                    listOf(a + diff, b - diff).filter { node ->
                        node in grid
                    }
                }
            }
        }.toSet().size
    }

    fun part2(antennas: Map<Location, List<Pair<Int, Int>>>, grid: Set<Pair<Int, Int>>): Int {
        return antennas.flatMap {
            val locations = it.value
            // All possible pairs
            locations.indices.flatMap { i ->
                (i + 1..<locations.size).flatMap { j ->
                    val a = locations[i]
                    val b = locations[j]
                    val diff = a - b
                    val nodes = mutableListOf(a, b)
                    var p = a
                    while (p in grid) {
                        nodes.add(p)
                        p += diff
                    }
                    p = b
                    while (p in grid) {
                        nodes.add(p)
                        p -= diff
                    }
                    nodes
                }
            }
        }.toSet().size
    }

    val input = readInput("Day08").mapPositionsNotNull { x, y, c ->
        if (c != '.') { Antenna(c) } else { Nothing } to (x to y)
    }.groupBy { it.first }.mapValues { it.value.map { it.second } }

    val antennas = input.filter { it.key is Antenna }
    val grid = input.flatMap { it.value }.toSet()

    part1(antennas, grid).println()
    part2(antennas, grid).println()
}
