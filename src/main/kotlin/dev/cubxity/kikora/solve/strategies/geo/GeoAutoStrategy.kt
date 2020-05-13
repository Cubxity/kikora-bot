package dev.cubxity.kikora.solve.strategies.geo

import dev.cubxity.kikora.entity.KikoraExerciseType
import dev.cubxity.kikora.solve.SolvingContext
import dev.cubxity.kikora.solve.TermsSolvingStrategy
import dev.cubxity.kikora.utils.KikoraUtils

object GeoAutoStrategy : TermsSolvingStrategy {
    override val name = "GeoAuto"
    override val supportedTypes = listOf(KikoraExerciseType.GEO_AUTO)

    override suspend fun solveTerms(ctx: SolvingContext): Map<String, String>? {
        val hash = KikoraUtils.hashCode("${ctx.exercise.exerciseDefinition.exerciseId}${ctx.personInfo.personId}")
        return mapOf("0" to "$hash")
    }
}