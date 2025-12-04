package de.breisa.aoc2025.days

import de.breisa.aoc2025.getResourceAsText

private val EXAMPLE = """
    ..@@.@@@@.
    @@@.@.@.@@
    @@@@@.@.@@
    @.@@@@..@.
    @@.@@@@.@@
    .@@@@@@@.@
    .@.@.@.@@@
    @.@@@.@@@@
    .@@@@@@@@.
    @.@.@@@.@.
""".trimIndent()

/*
    https://adventofcode.com/2025/day/4
 */
class Day04 : Day<Grid<Cell>, Int> {
    override fun solveFirstPart(input: Grid<Cell>): Int = getRemovableRolls(input).count()

    override fun solveSecondPart(input: Grid<Cell>): Int {
        var removedRolls = 0
        do {
            val removableRolls = getRemovableRolls(input)
            removableRolls.forEach { input.set(it, Cell.EMPTY) }
            removedRolls += removableRolls.size
        } while (removableRolls.isNotEmpty())
        return removedRolls
    }

    private fun getRemovableRolls(input: Grid<Cell>): List<Position> = input.positions
        .filter { input.get(it) == Cell.ROLL}
        .filter { position ->
            val adjacentRolls = position.adjacentPositions
                .filter { input.isOnGrid(it) }
                .count { input.get(it) == Cell.ROLL }
            adjacentRolls < 4
        }

    override fun parseInput(data: String): Grid<Cell> = Grid(data) { it.toCell() }
}


enum class Cell(val symbol: Char) {
    EMPTY('.'), ROLL('@');
    override fun toString(): String = symbol.toString()
}

fun Char.toCell(): Cell = Cell.entries.firstOrNull { it.symbol == this } ?: error("unexpected cell character: '$this'")

data class Position(val x: Int, val y: Int) {
    val adjacentPositions: List<Position> by lazy {
        (-1..1).flatMap { sy ->
            (-1..1).map { sx ->
                Position(x + sx, y + sy)
            }.filterNot { it == this }
        }
    }
}

class Grid<C>(inputData: String, cellParser: (Char) -> C) {
    private val content: MutableList<MutableList<C>> = inputData.lines().map { line ->
        line.map(cellParser).toMutableList()
    }.toMutableList()

    val width: Int = content.first().size
    val height: Int = content.size
    val positions: List<Position> = (0..<height).flatMap { y -> (0..<width).map { x -> Position(x, y) } }

    init {
        require(content.distinctBy { it.size }.count() == 1) { "all grid lines have to be of the same length" }
    }

    fun get(p: Position): C {
        require(isOnGrid(p)) { "can not access position outside of grid" }
        return content[p.y][p.x]
    }

    fun set(p: Position, value: C) {
        require(isOnGrid(p)) { "can not access position outside of grid" }
        content[p.y][p.x] = value
    }

    fun isOnGrid(p: Position): Boolean = p.x in (0..<width) && p.y in (0..<height)

    override fun toString(): String =
        content.joinToString(separator = "\n") { line -> line.joinToString(separator = "") }
}


fun main() {
    Day04().solve(
        exampleInput = EXAMPLE,
        puzzleInput = getResourceAsText("/puzzles/04/input.txt"),
        runBenchmark = false
    )
}