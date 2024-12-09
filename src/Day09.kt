sealed interface Block
data object Gap : Block
data class File(val id: Long) : Block

sealed interface BlockV2 {
    val size: Int
}
data class GapV2(override val size: Int) : BlockV2
data class FileV2(val id: Long, override val size: Int) : BlockV2

fun main() {
    fun part1(input: List<Int>): Long {
        val blocks = input.flatMapIndexed { index, size ->
            List(size) {
                if (index % 2 == 0) {
                    File((index / 2).toLong())
                } else {
                    Gap
                }
            }
        }

        var i = 0
        var j = blocks.size - 1
        val result = mutableListOf<Block>()
        while (i < j) {
            if (blocks[i] is File){
                result.add(blocks[i])
                i++
            } else if (blocks[j] is Gap) {
                j--
            } else {
                result.add(blocks[j])
                j--
                i++
            }
        }
        return result.map { it as File }.mapIndexed { idx, file -> idx * file.id }.sum()
    }

    fun part2(input: List<Int>): Long {
        var fileindex = 0L
        var blocks = input.mapIndexed { index, size ->
            if (index % 2 == 0) {
                FileV2(fileindex++, size)
            } else {
                GapV2(size)
            }
        }.toMutableList()

        // Iterate backwards through blocks
        var i = blocks.size - 1
        val moved = mutableSetOf<FileV2>()
        while (i > 0) {
            // Only move files we have not moved yet
            if (blocks[i] is GapV2 || blocks[i] in moved) {
                i--
                continue
            }
            // Iterate from the beginning to find the first matching gap
            var j = 0
            while(j < i && (blocks[j] is FileV2 || blocks[j].size < blocks[i].size)) {
                j++
            }
            // Move items around
            if (blocks[j] is GapV2) {
                val sizeDiff = blocks[j].size - blocks[i].size
                val move = blocks[i]
                moved.add(move as FileV2)
                blocks[i] = GapV2(move.size)
                blocks[j] = move
                // Split the gap into new block + smaller gap
                if (sizeDiff > 0) {
                    blocks = (blocks.subList(0, j + 1) + listOf(GapV2(sizeDiff)) + blocks.subList(j+1, blocks.size)).toMutableList()
                    i++ // List increased in size
                }
            }
            i--
        }

        // Compute checksum
        return blocks
            .fold(mutableListOf<Block>()) { acc, block ->
                val add = List(block.size) {
                    if (block is GapV2) {
                        Gap
                    } else {
                        File((block as FileV2).id)
                    }
                }
                acc.apply { addAll(add) }
            }.mapIndexed { idx, block -> if (block is File) { idx * block.id } else { 0L }}.sum()
    }


    val input = readInput("Day09").first()
        .map { it.digitToInt() }


    part1(input).println()
    part2(input).println()
}
