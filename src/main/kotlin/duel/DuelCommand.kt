package com.evolvedghost.duel

import com.evolvedghost.MuteGames
import com.evolvedghost.duel.DuelConfig.messageSameMember
import com.evolvedghost.duel.DuelConfig.messageStartDuel
import com.evolvedghost.duel.DuelConfig.waitTime
import com.evolvedghost.utils.checkPermit
import com.evolvedghost.utils.messageGenerator
import com.evolvedghost.utils.timeFormatter
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.at

object DuelCommand : SimpleCommand(
    MuteGames,
    primaryName = "duel",
    secondaryNames = duelSecondaryNames,
    description = "决斗指令",
) {
    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true

    @Handler
    suspend fun duel(sender: CommandSender) {
        val permit = checkPermit(sender)
        if (permit.canReturn) return
        val group = permit.group ?: return
        val target = permit.target ?: return
        val isTargetAdmin = permit.isTargetAdmin ?: return
        duelMapLock.withLock {
            if (duelMap[group.id] == null || duelMap[group.id]!!.isFinished()) {
                duelMap[group.id] = Duel(target, sender, isTargetAdmin)
                sender.sendMessage(
                    messageGenerator(
                        messageStartDuel, arrayOf("<target>","<timeout-s>","<timeout-f>"),
                        arrayOf(
                            target.at().serializeToMiraiCode(),
                            waitTime.toString(),
                            timeFormatter(waitTime)
                        )
                    )
                )
            } else {
                if (duelMap[group.id]!!.isSameMember(target)) {
                    sender.sendMessage(
                        messageGenerator(
                            messageSameMember, arrayOf("<target>"),
                            arrayOf(
                                target.at().serializeToMiraiCode()
                            )
                        )
                    )
                } else {
                    sender.sendMessage(duelMap[group.id]!!.join(target, isTargetAdmin))
                }
            }
        }


    }
}