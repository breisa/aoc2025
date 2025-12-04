package de.breisa.aoc2025.days

import kotlin.time.Duration
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

private const val WARMUP_RUNS = 1000
private const val BENCHMARK_RUNS = 1000

interface Day<I, O> {
    fun solveFirstPart(input: I): O
    fun solveSecondPart(input: I): O
    fun parseInput(data: String): I

    fun solve(exampleInput: String, puzzleInput: String, runBenchmark: Boolean = false) {
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

        if (runBenchmark) benchmarkImplementation(puzzleInput)
    }

    fun benchmarkImplementation(puzzleInput: String) {
        println("running the benchmark:")
        val firstPart = measureTimes(::solveFirstPart, puzzleInput)
        val secondPart = measureTimes(::solveSecondPart, puzzleInput)
        println(
            "  part 1 runtime over $BENCHMARK_RUNS runs:  " +
                    "average: ${firstPart.average()} min: ${firstPart.min()} max: ${firstPart.max()}"
        )
        println(
            "  part 2 runtime over $BENCHMARK_RUNS runs:  " +
                    "average: ${secondPart.average()} min: ${secondPart.min()} max: ${secondPart.max()}"
        )
    }

    private fun measureTimes(part: (I) -> O, puzzleInput: String): List<Duration> {
        print("  warming up")
        repeat(WARMUP_RUNS) {
            val puzzle = parseInput(puzzleInput)
            part(puzzle)
            print(".")
        }
        println()
        print("  benchmarking")
        return buildList {
            repeat(BENCHMARK_RUNS) {
                val puzzle = parseInput(puzzleInput)
                add(measureTime { part(puzzle) })
                print(".")
            }
            println()
        }
    }

    private fun List<Duration>.average(): Duration = this.fold(Duration.ZERO) { a, b -> a + b } / this.size
}