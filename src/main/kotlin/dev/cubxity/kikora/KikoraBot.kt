package dev.cubxity.kikora

import dev.cubxity.kikora.entity.KikoraContainerContent
import dev.cubxity.kikora.entity.KikoraExerciseStatus
import dev.cubxity.kikora.requests.KikoraPersonInfoRequest
import dev.cubxity.kikora.solve.SolvingContext
import dev.cubxity.kikora.solve.SolvingManager
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger

class KikoraBot(private val api: KikoraAPI) : CoroutineScope {
    override val coroutineContext = Dispatchers.Default + Job()

    private val mutex = Mutex()
    private var person: KikoraPersonInfoRequest.Response? = null
    private val solvingManager = SolvingManager.createDefault()

    fun execute(container: KikoraContainerContent, listener: Listener) = execute(listOf(container), listener)

    fun execute(containers: List<KikoraContainerContent>, listener: Listener) = launch {
        try {
            val person = mutex.withLock {
                person ?: withContext(Dispatchers.IO) {
                    api.personInfo().also { person = it }
                }
            }

            val exercises = containers.flatMap { container ->
                val content = api.containerContent(container.containerId)
                content.leafContainer?.containerExercises?.filter { it.containerExerciseUserData.status != KikoraExerciseStatus.FINISHED }
                        ?.map { container to it.exercise } ?: emptyList()
            }

            val time = System.currentTimeMillis()
            val stats = Statistics(exercises.size)
            try {
                listener.onStart(stats)

                val jobs = exercises.map { (container, exercise) ->
                    launch {
                        val ctx = SolvingContext(person, api, exercise, container)
                        val success = solvingManager.solve(ctx)
                        stats.incrementVisited()
                        if (success) stats.incrementSolved()

                        listener.onProgress(stats)
                    }
                }
                jobs.forEach { it.join() }
            } catch (t: Throwable) {
                listener.onError(t)
            } finally {
                listener.onFinish(stats, System.currentTimeMillis() - time)
            }
        } catch (t: Throwable) {
            listener.onError(t)
        }
    }

    class Statistics(val total: Int) {
        private val _solved = AtomicInteger()
        private val _visited = AtomicInteger()

        val solved: Int
            get() = _solved.get()
        val visited: Int
            get() = _visited.get()

        fun incrementSolved() = _solved.incrementAndGet()
        fun incrementVisited() = _visited.incrementAndGet()
    }

    interface Listener {
        fun onError(error: Throwable) {}

        fun onStart(stats: Statistics) {}

        fun onProgress(stats: Statistics) {}

        /**
         * This will always be called after [onStart]
         * @param time time used in ms
         */
        fun onFinish(stats: Statistics, time: Long) {}
    }
}