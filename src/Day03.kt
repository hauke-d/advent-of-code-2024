
fun main() {
    val mulRegex = "mul\\(([0-9]+),([0-9]+)\\)".toRegex()

    fun part1(input: String): Int {
        return mulRegex.findAll(input).map { match ->
            match.groupValues[1].toInt() * match.groupValues[2].toInt()
        }.sum()
    }

    fun part2(input: String): Int {
        val commandRegex = "(do\\(\\)|don't\\(\\)|mul\\(([0-9]+),([0-9]+)\\))".toRegex()
        var enabled = true
        return commandRegex.findAll(input).map { match ->
            if (match.value.startsWith("don't")) {
                enabled = false
            } else if (match.value.startsWith("do")) {
                enabled = true
            }

            if (enabled && match.value.startsWith("mul")) {
                match.groupValues[2].toInt() * match.groupValues[3].toInt()
            } else {
                0
            }
        }.sum()
    }

    val input = readInput("Day03").joinToString("")
    part1(input).println()
    part2(input).println()
}
