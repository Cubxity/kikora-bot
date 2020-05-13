package dev.cubxity.kikora.solve.strategies

import dev.cubxity.kikora.entity.KikoraExerciseType
import dev.cubxity.kikora.requests.KikoraCheckRequest
import dev.cubxity.kikora.solve.SolvingContext
import dev.cubxity.kikora.solve.SolvingStrategy
import dev.cubxity.kikora.utils.asChoiceChar
import dev.cubxity.kikora.utils.isCorrect
import kotlin.random.Random

object ChoiceBruteForceStrategy : SolvingStrategy {
    override val name = "Choice BruteForce"
    override val supportedTypes = listOf(KikoraExerciseType.ECHO_MCHOICE, KikoraExerciseType.GEO_ECHO_MCHOICE)

    private val REGEX = "<li class=\"mci\">".toRegex()

    override suspend fun solve(ctx: SolvingContext): Boolean {
        val choiceCount = REGEX.findAll(ctx.exercise.task).count()
        if (choiceCount > 0) {
            // I know this is not the best thing that you should do
            val choices = (0 until choiceCount).map { it.asChoiceChar().toString() }
            val res = check(ctx, choices)
            if (res.step.isCorrect) return true

            val wrongAlternatives = res.step.expression.wrongAlternatives ?: return false

            return check(ctx, choices - wrongAlternatives).step.isCorrect
        }
        return false
    }

    private suspend fun check(ctx: SolvingContext, choices: List<String>): KikoraCheckRequest.Response {
        val term = choices.joinToString(" ")
        val terms = mapOf("0" to term)

        return ctx.api.check(ctx.container.containerId, ctx.exercise.exerciseDefinition.exerciseId, Random.nextInt(40).toLong(), terms)
    }
}