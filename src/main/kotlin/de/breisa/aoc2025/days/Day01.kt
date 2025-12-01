package de.breisa.aoc2025.days

import de.breisa.aoc2025.getResourceAsText
import kotlin.math.absoluteValue

/*
    https://adventofcode.com/2025/day/1
 */

const val DIAL_SIZE: Int = 100
const val STARTING_POSITION: Int = 50
val EXAMPLE = """
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

fun main() {
    val input = getResourceAsText("/puzzles/01/input.txt")
    val turns = parseTurns(input)
    println("first solution: ${solveFirstPart(turns)}")
    println("second solution: ${solveSecondPart(turns)}")
}

fun solveFirstPart(turns: List<Int>): Int = turns
    .runningFold(STARTING_POSITION, ::turnDial)
    .count { it == 0 }

fun solveSecondPart(turns: List<Int>): Int {
    var position = STARTING_POSITION
    var zeroPasses = 0
    for (clicks in turns) {
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

fun turnDial(current: Int, clicks: Int) = (current + clicks).mod(DIAL_SIZE)

fun parseTurns(input: String): List<Int> = input.lines()
    .map { it.drop(1).toInt() * (if (it.first() == 'L') -1 else 1) }