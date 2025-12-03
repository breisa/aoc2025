package de.breisa.aoc2025.days

import de.breisa.aoc2025.getResourceAsText

private val EXAMPLE = """
    987654321111111
    811111111111119
    234234234234278
    818181911112111
""".trimIndent()

/*
    https://adventofcode.com/2025/day/3
 */
class Day03: Day<List<List<Long>>, Long> {
    override fun solveFirstPart(input: List<List<Long>>): Long {
        return input.sumOf { bank ->
            var left = 0L
            var right = 0L
            var maxValue = 0L
            for (digit in bank) {
                val newRight = left * 10 + digit
                val shifted = right * 10 + digit
                if (shifted > maxValue && shifted > newRight) {
                    left = right
                    right = digit
                } else if (newRight > maxValue) {
                    right = digit
                }
                maxValue = left * 10 + right
            }
            maxValue
        }
    }

    override fun solveSecondPart(input: List<List<Long>>): Long {
        // generalized algorithm: add digit to the end and drop the first digit that is lower than the next one
        val BATTERY_COUNT = 12
        return input.sumOf { bank ->
            val joltages = bank.take(BATTERY_COUNT).toMutableList()
            bank.drop(BATTERY_COUNT).forEach { nextDigit ->
                joltages.addLast(nextDigit)
                val dropIndex = (0..<BATTERY_COUNT).firstOrNull { index ->
                    joltages[index] < joltages[index + 1]
                } ?: 12
                joltages.removeAt(dropIndex)
            }
            joltages.joinToString("").toLong()
        }
    }

    override fun parseInput(data: String): List<List<Long>> = data
        .lines()
        .map { line ->
            line.map { digit ->
                digit.digitToInt().toLong()
            }
        }
}

fun main() {
    Day03().solve(
        exampleInput = EXAMPLE,
        puzzleInput = getResourceAsText("/puzzles/03/input.txt")
    )
}