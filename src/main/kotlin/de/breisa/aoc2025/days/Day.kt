package de.breisa.aoc2025.days

import kotlin.time.measureTimedValue

interface Day<I, O> {
    fun solveFirstPart(input: I): O
    fun solveSecondPart(input: I): O
    fun parseInput(data: String): I

    fun solve(exampleInput: String, puzzleInput: String) {
        val example = parseInput(exampleInput)
        val puzzle = parseInput(puzzleInput)

        println("solving part 1:")
        println("  solution of the example: ${solveFirstPart(example)}")
        val (firstActualSolution, firstDuration) = measureTimedValue { solveFirstPart(puzzle) }
        println("  solution of the actual puzzle: $firstActualSolution ($firstDuration)")

        println("solving part 2:")
        println("  solution of the example: ${solveSecondPart(example)}")
        val (secondActualSolution, secondDuration) = measureTimedValue { solveSecondPart(puzzle) }
        println("  solution of the actual puzzle: $secondActualSolution ($secondDuration)")
    }
}