package de.breisa.aoc2025.days

import de.breisa.aoc2025.getResourceAsText

private val EXAMPLE = """
    3-5
    10-14
    16-20
    12-18

    1
    5
    8
    11
    17
    32
""".trimIndent()

/*
    https://adventofcode.com/2025/day/5
 */
class Day05 : Day<IdPuzzle, ULong> {
    override fun solveFirstPart(input: IdPuzzle): ULong = input.ids.count { id ->
        input.ranges.any { range -> id in range }
    }.toULong()

    override fun solveSecondPart(input: IdPuzzle): ULong {
        val queue = input.ranges.toMutableList()
        val ranges = mutableListOf<ULongRange>()
        processRanges@while (queue.isNotEmpty()) {
            var new = queue.removeFirst()
            for (existing in ranges) {
                when {
                    existing.contains(new) -> continue@processRanges // skip entirely
                    new.contains(existing) -> { // split into outer parts that are not already covered
                        val left = new.first..(existing.first - 1UL)
                        val right = (existing.last + 1UL)..new.last
                        if (new.first != existing.first) queue.addLast(left)
                        if (new.last != existing.last) queue.addLast(right)
                        continue@processRanges
                    }
                    new.first in existing  -> new = (existing.last + 1UL)..new.last // drop overlapping part
                    new.last in existing   -> new = new.first..(existing.first - 1UL) // same
                }
            }
            ranges.add(new)
        }
        return ranges.sumOf { range -> range.last - range.first + 1UL}
    }

    override fun parseInput(data: String): IdPuzzle {
        val (rangeData, idData) = data.split("\n\n")
        val ranges = rangeData.lines()
            .map { line ->
                line.split('-')
                    .let { (l, r) -> l.toULong()..r.toULong() }
            }
        val ids = idData.lines().map { it.toULong() }
        return IdPuzzle(ranges, ids)
    }

    fun ULongRange.contains(other: ULongRange): Boolean = (other.first in this && other.last in this)
}

data class IdPuzzle(val ranges: List<ULongRange>, val ids: List<ULong>)

fun main() {
    Day05().solve(
        exampleInput = EXAMPLE,
        puzzleInput = getResourceAsText("/puzzles/05/input.txt"),
        runBenchmark = false
    )
}