fun main() {

    fun isPossible(design: String, patterns: List<String>): Boolean {
        if (design.isEmpty()) {
            return true
        }
        return patterns
            .filter { design.startsWith(it) }
            .any {
                isPossible(design.substring(it.length), patterns)
            }
    }

    val cache = mutableMapOf<String, Long>()
    fun designOptions(remainder: String, patterns: List<String>): Long {
        if (remainder.isEmpty()) {
            return 1L
        }
        return cache.getOrPut(remainder) {
            patterns.filter { remainder.startsWith(it) }
                .sumOf {
                    designOptions(remainder.substring(it.length), patterns)
                }
        }
    }


    fun part1(patterns: List<String>, designs: List<String>): Int {
        return designs.count {
            isPossible(it, patterns)
        }
    }

    fun part2(patterns: List<String>, designs: List<String>): Long {
        return designs.sumOf {
            designOptions(it, patterns)
        }
    }

    val lines = readInput("Day19")
    val patterns = lines.first().split(", ")
    val designs = lines.takeLastWhile { it.isNotBlank() }

    part1(patterns, designs).println()
    part2(patterns, designs).println()
}
