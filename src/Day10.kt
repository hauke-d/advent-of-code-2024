fun main() {
    fun paths(position: Coordinate, path: List<Coordinate>, map: Map<Coordinate, Int>): Set<List<Coordinate>> {
        val height = map[position]!!
        if (height == 9) {
            return setOf(path + position)
        }

        return ALL_DIRECTIONS
            .map { position + it }
            .filter { map[it] == height + 1 }
            .map { paths(it, path + position, map) }
            .flatten()
            .toSet()
    }

    fun part1(map: Map<Coordinate, Int>): Int {
        return map.filterValues { it == 0 }.keys.flatMap {
            paths(it, listOf(), map).map { path -> path.first() to path.last() }
        }.toSet().size
    }

    fun part2(map: Map<Coordinate, Int>): Int {
        return map.filterValues { it == 0 }.keys.flatMap {
            paths(it, listOf(), map)
        }.toSet().size
    }

    val map = readInput("Day10").mapPositionsNotNull { x, y, c ->
        (x to y) to c.digitToInt()
    }.toMap()

    part1(map).println()
    part2(map).println()
}
