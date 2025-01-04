fun main() {
    fun part1(connections: Map<String, Set<String>>): Int {
        return connections
            .flatMap { (node, others) ->
                others.flatMap { other1 ->
                    others.mapNotNull { other2 ->
                        if (other2 in connections[other1]!!) {
                            setOf(node, other1, other2)
                        } else {
                            null
                        }
                    }
                }
            }
            .toSet()
            .filter { it.any { v -> v.startsWith("t") } }
            .size
    }

    fun part2(connections: Map<String, Set<String>>): String {
        val nodes = connections.keys
        return nodes
            .fold(nodes.map { setOf(it) }) { acc, node ->
                acc + acc.mapNotNull { group ->
                    if (connections[node]!!.containsAll(group)) {
                        group + node
                    } else {
                        null
                    }
                }
            }
            .maxBy { it.size }
            .sorted()
            .joinToString(",")
    }

    val links = readInput("Day23")
        .splitEachToPairs("-") { it }

    val connections = links
        .flatMap { listOf(it, it.second to it.first) }
        .groupBy { it.first }
        .mapValues { it.value.map { v -> v.second }.toSet() }

    part1(connections).println()
    part2(connections).println()
}
