fun main() {
    fun part1(updates: List<List<Int>>, comparator: Comparator<Int>): Int {
        return updates.sumOf { update ->
            val sorted = update.sortedWith(comparator)
            if (update == sorted) {
                update[update.size / 2]
            } else {
                0
            }
        }
    }

    fun part2(updates: List<List<Int>>, comparator: Comparator<Int>): Int {
        return updates.sumOf { update ->
            val sorted = update.sortedWith(comparator)
            if (update != sorted) {
                sorted[sorted.size / 2]
            } else {
                0
            }
        }
    }

    val lines = readInput("Day05")

    // A map of Int -> List<Int>: page -> page numbers that should come after this page
    val orderings = lines
        .takeWhile { it.contains("|") }
        .map { it.split("|").map(String::toInt) }
        .groupBy { ab -> ab[0] }
        .mapValues { it.value.map { ab -> ab[1] } }

    val updates = lines
        .takeLastWhile { it.contains(",") }
        .map { it.split(",").map(String::toInt) }

    // Comparator to sort page numbers
    val comparator = Comparator<Int> { p1, p2 ->
        if (orderings[p1]?.contains(p2) == true) {
            -1
        } else if (orderings[p2]?.contains(p1) == true) {
            1
        } else {
            0
        }
    }

    part1(updates, comparator).println()
    part2(updates, comparator).println()
}
