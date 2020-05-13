package dev.cubxity.kikora.solve

import dev.cubxity.kikora.solve.strategies.ChoiceBruteForceStrategy
import dev.cubxity.kikora.solve.strategies.geo.GeoAutoStrategy
import dev.cubxity.kikora.solve.strategies.geo.GeoCirclesStrategy
import org.slf4j.LoggerFactory

class SolvingManager(private val strategies: List<SolvingStrategy>) {
    companion object {
        @JvmStatic
        fun createDefault() = SolvingManager(listOf(
                GeoAutoStrategy,
                ChoiceBruteForceStrategy,

                GeoCirclesStrategy
        ))
    }

    private val logger = LoggerFactory.getLogger(javaClass)

    suspend fun solve(ctx: SolvingContext): Boolean {
        val def = ctx.exercise.exerciseDefinition
        val type = def.exerciseType

        strategies.filter { type in it.supportedTypes }.forEach { strategy ->
            try {
                if (strategy.solve(ctx)) return true
            } catch (ex: Exception) {
                logger.error("An error occurred whilst solving with strategy '${strategy.name}'", ex)
            }
        }
        return false
    }
}