package dev.cubxity.kikora.solve.strategies.geo

import dev.cubxity.kikora.entity.KikoraExerciseType
import dev.cubxity.kikora.solve.SolvingContext
import dev.cubxity.kikora.solve.TermsSolvingStrategy

object GeoCirclesStrategy : TermsSolvingStrategy {
    private val SETVALUE_REGEX = "SetValue\\((?<key>[^,]+),(?<value>[^)]+)\\)".toRegex()
    private val NN_REGEX = "n=(\\d+)".toRegex()

    override val name = "Geo Circles"
    override val supportedTypes = listOf(KikoraExerciseType.GEO_ECHO_PLAIN)

    override suspend fun solveTerms(ctx: SolvingContext): Map<String, String>? {
        if (!ctx.exercise.task.contains("Hvor mange sirkler viser figuren?")) return null

        // Variant 1: SetValue circles
        // Gilder1: rows
        // Glider2: columns
        // Glider3: x duplicates
        // Glider4: y duplicates

        // Variant 2: nn circles
        // nn=?

        val geoInfo = ctx.exercisePerson().geoInfo ?: return null
        val cmd = geoInfo.geoCommand

        val circles = when {
            "SetValue(Glider" in cmd -> {
                val values = SETVALUE_REGEX.findAll(geoInfo.geoCommand).map { it.groupValues[1] to it.groupValues[2] }.toMap()

                val rows = values["Glider1"]?.toIntOrNull() ?: return null
                val columns = values["Glider2"]?.toIntOrNull() ?: return null
                val xDuplicates = values["Glider3"]?.toIntOrNull() ?: return null
                val yDuplicates = values["Glider4"]?.toIntOrNull() ?: return null

                // Circles:
                rows * columns * xDuplicates * yDuplicates
            }
            "n=" in cmd -> NN_REGEX.find(cmd)?.let { it.groupValues[1] }?.toIntOrNull() ?: return null
            else -> return null
        }

        return mapOf("0" to "$circles")
    }
}