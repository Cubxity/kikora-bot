/*
 *     Kikora-bot: Fast and powerful solving bot for Kikora
 *     Copyright (C) 2020 Cubxity
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.cubxity.kikora.solve

import dev.cubxity.kikora.solve.strategies.ChoiceBruteForceStrategy
import dev.cubxity.kikora.solve.strategies.geo.GeoAutoStrategy
import dev.cubxity.kikora.solve.strategies.geo.GeoCirclesStrategy
import dev.cubxity.kikora.solve.strategies.math.ExpressionStrategy
import org.slf4j.LoggerFactory

class SolvingManager(private val strategies: List<SolvingStrategy>) {
    companion object {
        @JvmStatic
        fun createDefault() = SolvingManager(listOf(
                GeoAutoStrategy,
                ChoiceBruteForceStrategy,

                GeoCirclesStrategy,
                ExpressionStrategy
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