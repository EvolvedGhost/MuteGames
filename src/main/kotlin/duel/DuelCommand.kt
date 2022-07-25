package com.evolvedghost.duel

import com.evolvedghost.MuteGames
import com.evolvedghost.duel.DuelConfig.messageSameMember
import com.evolvedghost.duel.DuelConfig.messageStartDuel
import com.evolvedghost.utils.messageGenerator
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.GroupAwareCommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.contact.isOwner
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
        if (sender is GroupAwareCommandSender) {
            val group = sender.group
            val bot = group.botAsMember
            val target = group.members[sender.user.id]
            if (target != null) {
                if (bot.isOwner() || (!target.isOperator() && bot.isOperator())) {
                    duelMapLock.withLock {
                        if (duelMap[group.id] == null || duelMap[group.id]!!.isFinished()) {
                            duelMap[group.id] = Duel(target)
                            sender.sendMessage(
                                messageGenerator(
                                    messageStartDuel, arrayOf("<target>"),
                                    arrayOf(
                                        target.at().serializeToMiraiCode()
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
                                sender.sendMessage(duelMap[group.id]!!.join(target))
                            }
                        }
                    }
                }
            }
        }
    }
}