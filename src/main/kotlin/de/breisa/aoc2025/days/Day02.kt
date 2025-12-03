package de.breisa.aoc2025.days

import de.breisa.aoc2025.getResourceAsText
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

private const val EXAMPLE = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449," +
                            "38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"

/*
    https://adventofcode.com/2025/day/2
 */
class Day02: Day<List<LongRange>, Long> {
    companion object {
        private val FIRST_ID_REGEX = Regex("(\\d+)\\1")
        private val SECOND_ID_REGEX = Regex("(\\d+)\\1+")
    }

    override fun solveFirstPart(input: List<LongRange>): Long = input
        .flatMap { range ->
            range.filter(::isInvalidFirstId)
        }
        .sum()

    override fun solveSecondPart(input: List<LongRange>): Long = input
        .flatMap { range ->
            range.filter(::isInvalidSecondId)
        }.sum()

    override fun parseInput(data: String): List<LongRange> = data
        .split(',', '-')
        .map { it.toLong() }
        .chunked(2) { it[0]..it[1] }

    private fun isInvalidFirstId(id: Long): Boolean = FIRST_ID_REGEX.matches(id.toString())

    private fun isInvalidSecondId(id: Long): Boolean = SECOND_ID_REGEX.matches(id.toString())
}

fun main() {
    Day02().solve(
        exampleInput = EXAMPLE,
        puzzleInput = getResourceAsText("/puzzles/02/input.txt")
    )
}


// manual id checking code I wrote before discovering the regular expression version:

private fun isInvalidId(id: Long): Boolean {
    if (id.digits() % 2 != 0) return false
    val split = (10.0.pow(id.digits() / 2.0))
    val upper = (id / split).toLong()
    val lower = id - upper * split.toLong()
    val r = upper == lower
    return r
}

private fun isInvalidId(id: Long, repetitions: Int): Boolean {
    if (id.digits() % repetitions != 0) return false
    val split = 10.0.pow(id.digits() / repetitions)
    var lastPart = 0L
    var lastValue = 0L
    for (n in (repetitions - 1) downTo 0) {
        val part = (id / split.pow(n)).toLong()
        val now = part - lastPart
        if (n != (repetitions - 1) && now != lastValue) {
            return false
        }
        lastPart = part * split.toLong()
        lastValue = now
    }
    return true
}

private fun Long.digits(): Int = when {
    this == 0L -> 1
    this < 0   -> this.absoluteValue.digits()
    else       -> floor(log10(this.toDouble())).toInt() + 1
}