
fun main() {
    fun expand(
        start: Coordinate,
        area: Set<Coordinate>,
        map: Map<Coordinate, Char>
    ): Set<Coordinate> {
        val chr = map[start]
        val newArea = (area + start).toMutableSet()
        ALL_DIRECTIONS.forEach { dir ->
            val newPos = start + dir
            if (newPos in map && map[newPos] == chr && newPos !in area) {
                newArea.addAll(expand(newPos, newArea, map))
            }
        }
        return newArea
    }

    fun areas(map: Map<Coordinate, Char>): List<Set<Coordinate>> {
        val visited = mutableSetOf<Coordinate>()
        val areas = mutableListOf<Set<Coordinate>>()
        map.keys.forEach {
            if (it !in visited) {
                val area = expand(it, emptySet(), map)
                visited.addAll(area)
                areas.add(area)
            }
        }
        return areas
    }

    fun part1(map: Map<Coordinate, Char>): Int {
        return areas(map).sumOf {
            it.size * it.sumOf { pos ->
                ALL_DIRECTIONS.count { dir ->
                    map[pos + dir] != map[pos]
                }
            }
        }
    }

    fun part2(map: Map<Coordinate, Char>): Int {
        return areas(map).sumOf { area ->
            area.size * ALL_DIRECTIONS.associateWith { dir ->
                area.filter { pos -> map[pos] != map[pos + dir] }
            }.mapValues {
                areas(it.value.associateWith { pos -> map[pos]!! }).size
            }.values.sum()
        }
    }

    val map = readInput("Day12").mapPositionsNotNull { x, y, c ->
        (x to y) to c
    }.toMap()

    part1(map).println()
    part2(map).println()
}
