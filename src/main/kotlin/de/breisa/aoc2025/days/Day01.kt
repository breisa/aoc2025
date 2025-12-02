package de.breisa.aoc2025.days

import de.breisa.aoc2025.getResourceAsText
import kotlin.math.absoluteValue

private const val DIAL_SIZE: Int = 100
private const val STARTING_POSITION: Int = 50
private val EXAMPLE = """
    L68
    L30
    R48
    L5
    R60
    L55
    L1
    L99
    R14
    L82
""".trimIndent()

/*
    https://adventofcode.com/2025/day/1
 */
class Day01: Day<List<Int>, Int> {
    override fun solveFirstPart(input: List<Int>): Int = input
        .runningFold(STARTING_POSITION, ::turnDial)
        .count { it == 0 }

    override fun solveSecondPart(input: List<Int>): Int {
        var position = STARTING_POSITION
        var zeroPasses = 0
        for (clicks in input) {
            val immediateZeroPasses = clicks.absoluteValue / DIAL_SIZE
            zeroPasses += immediateZeroPasses
            val remainingClicks = clicks % DIAL_SIZE // only in -99..99
            // check if we pass zero when turning the remainingClicks
            if ((position + remainingClicks) !in 1..<DIAL_SIZE && remainingClicks != 0 && position != 0) {
                zeroPasses++
            }
            position = turnDial(position, clicks)
        }
        return zeroPasses
    }

    override fun parseInput(data: String): List<Int> = data.lines()
        .map { it.drop(1).toInt() * (if (it.first() == 'L') -1 else 1) }

    private fun turnDial(current: Int, clicks: Int) = (current + clicks).mod(DIAL_SIZE)
}

fun main() {
    Day01().solve(
        exampleInput = EXAMPLE,
        puzzleInput = getResourceAsText("/puzzles/01/input.txt")
    )
}