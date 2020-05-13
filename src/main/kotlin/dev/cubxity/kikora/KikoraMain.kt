package dev.cubxity.kikora

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import dev.cubxity.kikora.utils.filterLeaf
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

object KikoraMain : CliktCommand(name = "kikora-bot"), KikoraBot.Listener {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val sessionId: String by option(help = "Kikora's JSESSIONID").required()
    private val tile: String? by option(help = "Tile to run the bot on")
    private val container: String? by option(help = "Container to run the bot on")

    override fun run() = runBlocking {
        val api = KikoraAPI(sessionId)
        val bot = KikoraBot(api)

        logger.info("Fetching tiles...")
        val tiles = api.tileSet().tiles

        val tileId = tile
        val containerId = container

        if (tileId != null) {
            logger.info("Fetching exercises...")
            val tile = tiles.find { it.id == tileId }
            if (tile != null) {
                if (containerId != null) {
                    val container = api.containerSet(tile.containers.first().containerId).containers.map { it.containerContent }
                            .filterLeaf().toList()
                            .find { it.containerId == containerId }
                    if (container != null) {
                        bot.execute(container, this@KikoraMain).join()
                    } else {
                        logger.error("Unable to find container with id $containerId")
                        exitProcess(1)
                    }
                } else {
                    val containers = api.containerSet(tile.containers.first().containerId).containers.map { it.containerContent }
                            .filterLeaf().toList()
                    bot.execute(containers, this@KikoraMain).join()
                }
            } else {
                logger.error("Unable to find tile with id $tileId")
                exitProcess(1)
            }
        } else {
            logger.error("Tile not specified")
        }
    }

    override fun onError(error: Throwable) {
        logger.error("An error occurred", error)
    }

    override fun onStart(stats: KikoraBot.Statistics) {
        logger.info("Solving ${stats.total} exercises...")
    }

    override fun onProgress(stats: KikoraBot.Statistics) {
        val percent = 100.0 / stats.total * stats.visited
        val percentDisplay = "%.2f".format(percent)

        val arrows = (40.0 / stats.total * stats.visited).toInt()
        val arrow = ("=".repeat(arrows) + ">").padEnd(41, ' ')
        print("\r$percentDisplay% [$arrow] ${stats.visited}/${stats.total}")
    }

    override fun onFinish(stats: KikoraBot.Statistics, time: Long) {
        val percent = 100.0 / stats.visited * stats.solved
        val percentDisplay = "%.2f".format(percent)
        println()
        logger.info("Finished in $time ms. Solved: ${stats.solved}/${stats.visited} ($percentDisplay%).")
        exitProcess(0)
    }
}

fun main(args: Array<String>) = KikoraMain.main(args)