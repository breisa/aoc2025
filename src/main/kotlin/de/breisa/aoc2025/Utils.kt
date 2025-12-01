package de.breisa.aoc2025

/**
 * Loads a text resource using the class loader.
 */
fun getResourceAsText(path: String): String =
    object {}.javaClass.getResource(path)?.readText() ?: error("failed to load text from file '$path'")
