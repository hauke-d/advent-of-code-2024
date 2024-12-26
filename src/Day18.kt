fun main() {
    fun part1(input: Set<Coordinate>, size: Int): Int? {
        val walkable = (0..<size).map { y ->
            (0..<size).map { x ->
                x to y
            }
        }.flatten().filter { it !in input }.toSet()

        val target = size - 1 to size - 1
        val distances = mutableMapOf((0 to 0) to 0)
        while (distances[target] == null) {
            val step = distances.values.max()
            val newReachable = walkable.filter { maybeReachable ->
                ALL_DIRECTIONS.any { dir ->
                    (maybeReachable + dir) in distances && maybeReachable !in distances
                }
            }
            if (newReachable.isEmpty()) {
                break
            }
            distances.putAll(newReachable.map { it to step + 1 })
        }
        return distances[target]
    }

    fun part2(bytes: List<Coordinate>, size: Int): Coordinate {
        var nBytes = 1024
        while (part1(bytes.take(++nBytes).toSet(), size) != null) {}
        return bytes[nBytes - 1]
    }

    val testInput = readInput("Day18_test")
        .splitEach(",") { it.toInt() }
        .map { it[0] to it[1] }

    val input = readInput("Day18")
        .splitEach(",") { it.toInt() }
        .map { it[0] to it[1] }

    part1(testInput.take(12).toSet(), 7).println()
    part1(input.take(1024).toSet(), 71).println()
    part2(input, 71).println()
}
