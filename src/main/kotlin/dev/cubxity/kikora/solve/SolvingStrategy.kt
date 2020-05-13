package dev.cubxity.kikora.solve

import dev.cubxity.kikora.entity.KikoraExerciseType

interface SolvingStrategy {
    val name: String

    val supportedTypes: List<KikoraExerciseType>

    /**
     * @return success
     */
    suspend fun solve(ctx: SolvingContext): Boolean
}