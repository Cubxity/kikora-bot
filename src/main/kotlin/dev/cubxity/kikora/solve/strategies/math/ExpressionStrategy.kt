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
        "Regn ut" in exercise.task -> "$result"
        else -> null
    }

    private fun translate(exercise: KikoraExercise, text: String): String {
        var newText = text
        if (exercise.languageCode == "nb")
            newText = newText.replace(',', '.')
                    .replace('.', ',')
                    .replace(':', '/')
        return newText
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ExprData(val text: String, val latex: String, val size: Int, @JsonProperty("displaystyle") val displayStyle: Boolean)
}