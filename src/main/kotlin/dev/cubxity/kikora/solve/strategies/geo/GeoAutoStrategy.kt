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

package dev.cubxity.kikora.solve.strategies.geo

import dev.cubxity.kikora.entity.KikoraExerciseType
import dev.cubxity.kikora.solve.SolvingContext
import dev.cubxity.kikora.solve.TermsSolvingStrategy
import dev.cubxity.kikora.utils.KikoraUtils

object GeoAutoStrategy : TermsSolvingStrategy {
    override val name = "GeoAuto"
    override val supportedTypes = listOf(KikoraExerciseType.GEO_AUTO, KikoraExerciseType.GEO_MANUAL)

    override suspend fun solveTerms(ctx: SolvingContext): Map<String, String>? {
        val hash = KikoraUtils.hashCode("${ctx.exercise.exerciseDefinition.exerciseId}${ctx.personInfo.personId}")
        return mapOf("0" to "$hash")
    }
}