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