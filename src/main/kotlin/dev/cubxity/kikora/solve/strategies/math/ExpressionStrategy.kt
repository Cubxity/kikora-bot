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

package dev.cubxity.kikora.solve.strategies.math

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.udojava.evalex.Expression
import dev.cubxity.kikora.entity.KikoraExercise
import dev.cubxity.kikora.entity.KikoraExerciseType
import dev.cubxity.kikora.solve.SolvingContext
import dev.cubxity.kikora.solve.TermsSolvingStrategy
import dev.cubxity.kikora.utils.asFraction
import dev.cubxity.kikora.utils.asLatex

object ExpressionStrategy : TermsSolvingStrategy {
    private val REGEX = "\\[\\[km:(\\{[^]]+)]]".toRegex()
    private val PERCENT_REGEX = "(\\d+[,.]\\d+)%".toRegex()
    private val jackson = jacksonObjectMapper()

    override val name = "Expression"
    override val supportedTypes = listOf(KikoraExerciseType.ECHO_PLAIN)

    override suspend fun solveTerms(ctx: SolvingContext): Map<String, String>? {
        val expressions = REGEX.findAll(ctx.exercise.task).map { jackson.readValue<ExprData>(it.groupValues[1]) }.toList()
        if (expressions.size != 1) return null

        val expr = expressions.first()
        val res = runCatching { Expression(translate(ctx.exercise, expr.text)).eval() }.getOrNull()
                ?: runCatching { Expression(translate(ctx.exercise, expr.latex)).eval() }.getOrNull()

        return res?.toDouble()?.let { asAnswer(ctx.exercise, it) }
                ?.let { mapOf("0" to it) }
    }

    private fun asAnswer(exercise: KikoraExercise, result: Double) = when {
        "brøk" in exercise.task -> result.asFraction().asLatex()
        else -> "$result"
    }

    private fun translate(exercise: KikoraExercise, text: String): String {
        var newText = text.replace(PERCENT_REGEX) { (it.groupValues[1].toDouble() / 100.0).toString() }

        if (exercise.languageCode == "nb")
            newText = newText.replace(',', '.')
                    .replace('.', ',')
                    .replace(':', '/')
        return newText
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ExprData(val text: String, val latex: String, val size: Int, @JsonProperty("displaystyle") val displayStyle: Boolean)
}