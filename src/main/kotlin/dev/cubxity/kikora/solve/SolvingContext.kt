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