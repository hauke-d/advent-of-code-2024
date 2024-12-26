import kotlin.math.pow
import kotlin.math.sign

fun main() {

    fun combo(value: Int, registers: Triple<Long, Long, Long>): Long {
        return when {
            value <= 3 -> value.toLong()
            value == 4 -> registers.first
            value == 5 -> registers.second
            value == 6 -> registers.third
            else -> error("Invalid combo operator")
        }
    }

    fun div(operand: Int, registers: Triple<Long, Long, Long>): Long {
        val dv = 2L.toDouble()
            .pow(combo(operand, registers).toDouble())
            .toLong()
        return registers.first / dv
    }

    fun part1(registers: Triple<Long, Long, Long>, program: List<Int>): List<Int> {
        var pointer = 0
        var reg = registers
        val output = mutableListOf<Int>()
        while (pointer < program.size) {
            val opcode = program[pointer]
            val op = program[pointer + 1]

            reg = when (opcode) {
                0 -> {
                    reg.copy(first = div(op, reg))
                }
                1 -> reg.copy(second = reg.second xor op.toLong())
                2 -> reg.copy(second = combo(op, reg) % 8)
                3 -> {
                    if (reg.first != 0L) {
                        pointer = op
                        continue
                    }
                    reg
                }
                4 -> reg.copy(second = reg.second xor reg.third)
                5 -> {
                    output.add((combo(op, reg) % 8).toInt())
                    reg
                }
                6 -> reg.copy(second = div(op, reg))
                7 -> reg.copy(third = div(op, reg))
                else -> error("Invalid opcode")
            }

            pointer += 2
        }

        return output
    }


    fun matchPos(
        program: List<Int>,
        targetOutput: List<Int>,
        matchPos: Int,
        tryValues: List<Long>
    ): Long? {
        return tryValues.firstNotNullOfOrNull { a ->
            val digit = part1(Triple(a, 0, 0), program).first()
            if (digit == targetOutput[matchPos] && matchPos == 0) {
                a
            } else if (digit == targetOutput[matchPos]) {
                val newTryValues = (0..7L).map { it + 8 * a }
                matchPos(program, targetOutput, matchPos - 1, newTryValues)
            } else null
        }
    }

    fun part2(program: List<Int>): Long {
        val progNoRepeat = program.subList(0, program.size - 2)
        return matchPos(progNoRepeat, program, program.size - 1, (0..7L).toList())!!
    }

    val lines = readInput("Day17")

    val registers = lines.takeWhile { it.isNotEmpty() }
        .map { it.split(": ")[1].toLong() }
        .let {
            Triple(it[0], it[1], it[2])
        }

    val program = lines
        .last()
        .split(": ")[1]
        .split(",")
        .map { it.toInt() }

    part1(registers, program).joinToString(",").println()
    part2(program).println()
}
