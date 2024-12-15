import kotlin.math.pow

fun main() {
    val numberRegex = "-?[0-9]+".toRegex()

    fun move(
        input: Iterable<Pair<Coordinate, Coordinate>>,
        lx: Int,
        ly: Int
    ): List<Pair<Coordinate, Coordinate>> {
        val newInput = mutableListOf<Pair<Coordinate, Coordinate>>()
        input.forEach {
            val newX = (it.first.first + it.second.first) % lx
            val newY = (it.first.second + it.second.second) % ly
            val updatedPos = (newX + (if(newX < 0) 1 else 0) * lx) to
                    (newY + (if(newY < 0) 1 else 0) * ly)
            newInput.add(updatedPos to it.second)
        }
        return newInput
    }

    fun part1(input: List<Pair<Coordinate, Coordinate>>, lx: Int, ly: Int): Long {
        return (1..100).fold(input) { acc, _ ->
            move(acc, lx, ly)
        }.groupBy {
            val pos = it.first
            val isLeft = pos.first < lx / 2
            val isRight = pos.first > lx / 2
            val isUpper = pos.second < ly / 2
            val isLower = pos.second > ly / 2
            when {
                isLeft && isUpper -> 1
                isRight && isUpper -> 2
                isRight && isLower -> 3
                isLeft && isLower -> 4
                else -> 0
            }
        }.filterKeys { it > 0 }
            .mapValues { it.value.size }
            .entries.fold(1L) { acc, entry -> acc * entry.value}
    }

    fun part2(input: List<Pair<Coordinate, Coordinate>>, lx: Int, ly: Int): Int {
        val n = input.size
        val initial = input.toSet()
        var current = initial
        val scores = mutableListOf<Double>()
        do {
            val sums = current.map { it.first }.fold(0L to 0L) { acc, p ->
                acc.first + p.first to acc.second + p.second
            }
            val centerOfMass = sums.first / n to sums.second / n
            val msd = current.sumOf {
                (it.first.first - centerOfMass.first).toDouble().pow(2) +
                        (it.first.second - centerOfMass.second).toDouble().pow(2)
            } / n
            scores.add(msd)
            current = move(current, lx, ly).toSet()
        } while (current != initial)

        return scores.indexOfFirst { it == scores.min() }
    }

    val input = readInput("Day14").fold(listOf<Pair<Coordinate, Coordinate>>()) { acc, line ->
        val numbers = numberRegex.findAll(line).map { it.value.toInt() }.toList()
        acc + ((numbers[0] to numbers[1]) to (numbers[2] to numbers[3]))
    }

    // part1(input, 11, 7).println() // test
    part1(input, 101, 103).println()
    part2(input, 101, 103).println()
}
