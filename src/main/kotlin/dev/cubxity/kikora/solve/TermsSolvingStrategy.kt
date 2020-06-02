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

import dev.cubxity.kikora.entity.KikoraExpressionStatus
import kotlin.random.Random

interface TermsSolvingStrategy : SolvingStrategy {
    override suspend fun solve(ctx: SolvingContext): Boolean {
        val def = ctx.exercise.exerciseDefinition
        val terms = solveTerms(ctx)
        if (terms != null) {
            val res = ctx.api.check(ctx.container.containerId, def.exerciseId, Random.nextInt(100).toLong(), terms)
            if (res.step.markedFinal || res.step.expression.expressionStatus == KikoraExpressionStatus.ANSWER_TROPHY) {
                return true
            }
        }
        return false
    }

    /**
     * @return terms or null
     */
    suspend fun solveTerms(ctx: SolvingContext): Map<String, String>?
}