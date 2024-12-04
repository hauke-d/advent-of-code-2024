
fun main() {


    fun part1(input: List<String>): Int {
        val search = "XMAS"
        
        val points = search.associateWith { mutableSetOf<Pair<Int, Int>>() }
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                points[char]?.add(x to y)
            }
        }

        val directions = (-1..1).flatMap { x ->
            (-1..1).map { y -> x to y }
        }.filter { p -> p.first != 0 || p.second != 0 }

        return points['X']?.sumOf { first -> // For all starting points X
            directions.map { direction -> // For each direction
                var i = 0
                search.takeWhile { char -> // Walk as long as chars match
                    val new = first.first + i * direction.first to first.second + i * direction.second
                    i++
                    points[char]?.contains(new) ?: false
                } == search
            }.count { it }
        } ?: 0
    }

    fun part2(input: List<String>): Int {
        val points = mutableMapOf<Pair<Int, Int>, Char>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                points[x to y] = char
            }
        }

        val directions = listOf(1 to 1, -1 to 1)
        val other = mapOf('M' to 'S', 'S' to 'M')

        return points.filterValues { it == 'A' }.keys.count { center ->
            directions.all { direction ->
                val a = center.first + direction.first to center.second + direction.second
                val b = center.first - direction.first to center.second - direction.second
                points.contains(a) && points.contains(b) && points[a] == other[points[b]]
            }
        }
    }

    val lines = readInput("Day04")
    part1(lines).println()
    part2(lines).println()
}
