package dev.cubxity.kikora.solve

import dev.cubxity.kikora.KikoraAPI
import dev.cubxity.kikora.entity.KikoraContainerContent
import dev.cubxity.kikora.entity.KikoraExercise
import dev.cubxity.kikora.requests.KikoraExercisePersonRequest
import dev.cubxity.kikora.requests.KikoraPersonInfoRequest
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class SolvingContext(
        val personInfo: KikoraPersonInfoRequest.Response,
        val api: KikoraAPI,
        val exercise: KikoraExercise,
        val container: KikoraContainerContent
) {
    private val mutex = Mutex()
    var exercisePerson: KikoraExercisePersonRequest.Response? = null

    suspend fun exercisePerson() = mutex.withLock {
        exercisePerson ?: api.exercisePerson(container.containerId, exercise.exerciseDefinition.exerciseId.toString())
                .also { exercisePerson = it }
    }
}