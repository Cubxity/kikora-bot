package dev.cubxity.kikora.utils

import dev.cubxity.kikora.entity.KikoraEventStep
import dev.cubxity.kikora.entity.KikoraExpressionStatus

val KikoraEventStep.isCorrect: Boolean
    get() = markedFinal || expression.expressionStatus == KikoraExpressionStatus.ANSWER_TROPHY