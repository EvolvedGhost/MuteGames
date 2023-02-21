package com.evolvedghost.blackjack

import com.evolvedghost.MuteGames
import com.evolvedghost.blackjack.BlackjackConfig.alias
import com.evolvedghost.blackjack.BlackjackConfig.aliasAdd
import com.evolvedghost.blackjack.BlackjackConfig.aliasCheck
import com.evolvedghost.blackjack.BlackjackConfig.aliasGiveUp
import com.evolvedghost.blackjack.BlackjackConfig.aliasHelp
import com.evolvedghost.blackjack.BlackjackConfig.aliasStart
import com.evolvedghost.blackjack.BlackjackConfig.aliasStop
import com.evolvedghost.blackjack.BlackjackConfig.bustMultiplier
import com.evolvedghost.blackjack.BlackjackConfig.giveUpMultiplier
import com.evolvedghost.blackjack.BlackjackConfig.loserMultiplier
import com.evolvedghost.utils.checkPermit
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.at
import net.mamoe.mirai.message.data.buildMessageChain
import java.util.*

object BlackjackCommand : SimpleCommand(
    MuteGames,
    primaryName = "blackjack",
    secondaryNames = BlackjackSecondaryNames,
    description = "21点（Blackjack）指令",
) {
    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true

    @Handler
    suspend fun blackjack(sender: CommandSender) {
        val permit = checkPermit(sender)
        if (permit.canReturn) return
        val target = permit.target ?: return
        val group = permit.group ?: return
        blackjackMapLock.withLock {
            val bj = blackjackMap[group.id]
            if (bj == null || bj.isEnd()) {
                blackjackMap[group.id] = Blackjack(target, sender)
            } else if (!bj.isStart()) {
                bj.join(target)
            } else {
                val arg0 = if (alias.size > 0) alias[0]
                else "blackjack"
                val argHelp = if (aliasHelp.size > 0) aliasHelp[0]
                else "未定义！请联系机器人所有者！"
                sender.sendMessage(buildMessageChain {
                    +target.at()
                    +PlainText(" 21点已开始，你可以通过以下指令获取帮助：\n")
                    +PlainText("$arg0 $argHelp")
                })
            }
        }
    }

    @Handler
    suspend fun blackjack(sender: CommandSender, command: String) {
        val permit = checkPermit(sender)
        if (permit.canReturn) return
        val target = permit.target ?: return
        val group = permit.group ?: return
        val commandLowerCase = command.lowercase(Locale.ENGLISH)
        val stats = if (aliasStart.contains(commandLowerCase)) 1
        else if (aliasAdd.contains(commandLowerCase)) 2
        else if (aliasStop.contains(commandLowerCase)) 3
        else if (aliasGiveUp.contains(commandLowerCase)) 4
        else if (aliasCheck.contains(commandLowerCase)) 5
        else if (aliasHelp.contains(commandLowerCase)) 0
        else -1
        val arg0 = if (alias.size > 0) alias[0]
        else "blackjack"
        val argHelp = if (aliasHelp.size > 0) aliasHelp[0]
        else "未定义！请联系机器人所有者！"
        if (stats == -1) {
            sender.sendMessage(buildMessageChain {
                +target.at()
                +PlainText(" 无效的指令，你可以通过以下组合来寻求帮助：\n")
                +PlainText("$arg0 $argHelp")
            })
            return
        }
        if (stats == 0) {
            val bustBanTime = 21 * bustMultiplier
            val argStart = if (aliasStart.size > 0) aliasStart[0]
            else "未定义！请联系机器人所有者！"
            val argAdd = if (aliasAdd.size > 0) aliasAdd[0]
            else "未定义！请联系机器人所有者！"
            val argStop = if (aliasStop.size > 0) aliasStop[0]
            else "未定义！请联系机器人所有者！"
            val argGiveUp = if (aliasGiveUp.size > 0) aliasGiveUp[0]
            else "未定义！请联系机器人所有者！"
            sender.sendMessage(buildMessageChain {
                +target.at()
                +PlainText(
                    " 欢迎游玩禁言21点\n" +
                            "游戏玩法：拿到21点者获胜，如果无人拿到21点，则最接近21点者获胜；" +
                            "其余的人则会被禁言 (21-所拿点数)×$loserMultiplier 秒；" +
                            "你可以提前放弃投降，此时你会被禁言 (21-所拿点数)×$giveUpMultiplier 秒；" +
                            "如果你超过21点爆牌，则会直接禁言 $bustBanTime 秒\n" +
                            "游戏规则：2-10表示2-10，J、Q、K表示10，" +
                            "A在未满21时表示11，已满21后表示1，所有人停牌后系统自动结算，" +
                            "爆牌和投降的玩家会被直接移除\n" +
                            "游戏指令：\n$arg0 > 举行或加入一场21点\n" +
                            "$arg0 $argStart > 开始刚刚举行的21点\n" +
                            "$arg0 $argAdd > 游戏开始后，进行拿牌\n" +
                            "$arg0 $argStop > 游戏开始后，进行停牌\n" +
                            "$arg0 $argGiveUp > 游戏开始后，放弃/投降\n" +
                            "$arg0 $argHelp > 游戏帮助"
                )
            })
            return
        }
        blackjackMapLock.withLock {
            val bj = blackjackMap[group.id]
            if (bj == null || bj.isEnd()) {
                sender.sendMessage(buildMessageChain {
                    +target.at()
                    +PlainText(" 当前没有任何进行中的21点")
                })
            } else if (bj.isStart()) {
                when (stats) {
                    2 -> bj.addCard(target)
                    3 -> bj.stopCard(target)
                    4 -> bj.giveUp(target)
                    5 -> bj.checkCard(target)

                    else -> sender.sendMessage(buildMessageChain {
                        +target.at()
                        +PlainText(" 21点已经开始了")
                    })
                }
            } else {
                if (stats == 1) {
                    bj.start(target)
                } else {
                    sender.sendMessage(buildMessageChain {
                        +target.at()
                        +PlainText(" 21点还没开始")
                    })
                }
            }
        }
    }
}