package com.evolvedghost.roulette

import com.evolvedghost.MuteGames
import com.evolvedghost.roulette.RouletteConfig.messageEnd
import com.evolvedghost.roulette.RouletteConfig.messageForceEnd
import com.evolvedghost.roulette.RouletteConfig.messagePass
import com.evolvedghost.roulette.RouletteConfig.messageShot
import com.evolvedghost.roulette.RouletteConfig.messageStart
import com.evolvedghost.utils.checkPermit
import com.evolvedghost.utils.messageGenerator
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.at

object RouletteCommand : SimpleCommand(
    MuteGames,
    primaryName = "roulette",
    secondaryNames = rouletteSecondaryNames,
    description = "俄罗斯轮盘指令"
) {
    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true

    private val keywordArray =
        arrayOf("<bullet>", "<chamber>", "<remain-bullet>", "<remain-chamber>", "<mute-s>", "<mute-f>", "<target>")

    @Handler
    suspend fun roulette(sender: CommandSender) {
        val permit = checkPermit(sender)
        if (permit.canReturn) return
        val group = permit.group ?: return
        val target = permit.target ?: return
        if (dataCorrect) {
            rouletteMapLock.withLock {
                if (rouletteMap[group.id] == null || rouletteMap[group.id]!!.isFinished()) {
                    rouletteMap[group.id] = Roulette()
                    sender.sendMessage(
                        messageGenerator(
                            messageStart, keywordArray,
                            arrayOf(
                                rouletteMap[group.id]!!.getBullet(),
                                rouletteMap[group.id]!!.getChamber(),
                                rouletteMap[group.id]!!.getBulletRemain(),
                                rouletteMap[group.id]!!.getChamberRemain(),
                                rouletteMap[group.id]!!.getMute(),
                                rouletteMap[group.id]!!.getMuteFormat(),
                                target.at().serializeToMiraiCode()
                            )
                        )
                    )
                }
                if (rouletteMap[group.id]!!.shot()) {
                    target.mute(rouletteMap[group.id]!!.getMuteInt())
                    sender.sendMessage(
                        messageGenerator(
                            messageShot, keywordArray,
                            arrayOf(
                                rouletteMap[group.id]!!.getBullet(),
                                rouletteMap[group.id]!!.getChamber(),
                                rouletteMap[group.id]!!.getBulletRemain(),
                                rouletteMap[group.id]!!.getChamberRemain(),
                                rouletteMap[group.id]!!.getMute(),
                                rouletteMap[group.id]!!.getMuteFormat(),
                                target.at().serializeToMiraiCode()
                            )
                        )
                    )
                } else {
                    sender.sendMessage(
                        messageGenerator(
                            messagePass, keywordArray,
                            arrayOf(
                                rouletteMap[group.id]!!.getBullet(),
                                rouletteMap[group.id]!!.getChamber(),
                                rouletteMap[group.id]!!.getBulletRemain(),
                                rouletteMap[group.id]!!.getChamberRemain(),
                                rouletteMap[group.id]!!.getMute(),
                                rouletteMap[group.id]!!.getMuteFormat(),
                                target.at().serializeToMiraiCode()
                            )
                        )
                    )
                }
                if (rouletteMap[group.id]!!.isFinished()) {
                    sender.sendMessage(
                        messageGenerator(
                            messageEnd, keywordArray,
                            arrayOf(
                                rouletteMap[group.id]!!.getBullet(),
                                rouletteMap[group.id]!!.getChamber(),
                                rouletteMap[group.id]!!.getBulletRemain(),
                                rouletteMap[group.id]!!.getChamberRemain(),
                                rouletteMap[group.id]!!.getMute(),
                                rouletteMap[group.id]!!.getMuteFormat(),
                                target.at().serializeToMiraiCode()
                            )
                        )
                    )
                }
                if (rouletteMap[group.id]!!.forceEnd()) {
                    sender.sendMessage(
                        messageGenerator(
                            messageForceEnd, keywordArray,
                            arrayOf(
                                rouletteMap[group.id]!!.getBullet(),
                                rouletteMap[group.id]!!.getChamber(),
                                rouletteMap[group.id]!!.getBulletRemain(),
                                rouletteMap[group.id]!!.getChamberRemain(),
                                rouletteMap[group.id]!!.getMute(),
                                rouletteMap[group.id]!!.getMuteFormat(),
                                target.at().serializeToMiraiCode()
                            )
                        )
                    )
                }
            }
        } else {
            sender.sendMessage("配置项有误：$errorMessage")
        }

    }
}
