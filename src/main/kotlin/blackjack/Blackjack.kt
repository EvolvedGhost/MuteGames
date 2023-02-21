package com.evolvedghost.blackjack

import com.evolvedghost.utils.exceptionLogger
import com.evolvedghost.utils.messageGenerator
import com.evolvedghost.utils.messageGeneratorString
import com.evolvedghost.utils.timeFormatter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.at
import net.mamoe.mirai.message.data.buildMessageChain
import java.lang.StringBuilder
import java.util.*

@OptIn(DelicateCoroutinesApi::class)
class Blackjack(private val promoter: Member, private val sender: CommandSender) {
    private val participants = mutableListOf(promoter)
    private val ownedCard = mutableMapOf<Long, MutableList<BlackjackCard>>()
    private val isFinish = mutableSetOf<Long>()
    private var start = false
    private var end = false
    private var startWarning = true
    private val card = mutableListOf<BlackjackCard>()
    private fun containsMember(member: Member): Boolean {
        participants.forEach {
            if (it.id == member.id) {
                return true
            }
        }
        return false
    }

    private suspend fun removeMember(member: Member) {
        for (i in 0 until participants.size) {
            if (participants[i].id == member.id) {
                participants.removeAt(i)
                break
            }
        }
        if (isFinish.size == participants.size) {
            finish()
        }
    }

    private val autoClose = Thread {
        run {
            try {
                Thread.sleep(BlackjackConfig.autoCloseRoundTimer.toLong() * 1000)
                GlobalScope.launch {
                    sender.sendMessage(
                        messageGenerator(
                            BlackjackConfig.messageAutoClose,
                            arrayOf(
                                "<timeout-s>",
                                "<timeout-f>",
                                "<target>"
                            ),
                            arrayOf(
                                BlackjackConfig.autoCloseRoundTimer.toString(),
                                timeFormatter(BlackjackConfig.autoCloseRoundTimer),
                                promoter.at().serializeToMiraiCode(),
                            )
                        )
                    )
                }
            } catch (_: Exception) {
            } finally {
                if (!start) {
                    end = true
                }
            }
        }
    }

    private val autoFinish = Thread {
        run {
            try {
                Thread.sleep(BlackjackConfig.autoFinishRoundTimer.toLong() * 1000)
                GlobalScope.launch {
                    sender.sendMessage(
                        messageGenerator(
                            BlackjackConfig.messageAutoFinish,
                            arrayOf(
                                "<timeout-s>",
                                "<timeout-f>",
                                "<target>"
                            ),
                            arrayOf(
                                BlackjackConfig.autoFinishRoundTimer.toString(),
                                timeFormatter(BlackjackConfig.autoFinishRoundTimer),
                                promoter.at().serializeToMiraiCode()
                            )
                        ) + messageGenerator(getFinish(true))
                    )
                }
            } catch (_: Exception) {
            } finally {
                end = true
            }
        }
    }

    init {
        try {
            autoClose.start()
        } catch (e: Exception) {
            exceptionLogger(e)
        }
        GlobalScope.launch {
            sender.sendMessage(
                messageGenerator(
                    BlackjackConfig.messageOpenRound,
                    arrayOf(
                        "<timeout-s>",
                        "<timeout-f>",
                        "<target>"
                    ),
                    arrayOf(
                        BlackjackConfig.autoCloseRoundTimer.toString(),
                        timeFormatter(BlackjackConfig.autoCloseRoundTimer),
                        promoter.at().serializeToMiraiCode()
                    )
                )
            )
        }
    }

    suspend fun join(player: Member) {
        if (containsMember(player)) {
            val arg0 = if (BlackjackConfig.alias.size > 0) BlackjackConfig.alias[0]
            else "blackjack"
            val argHelp = if (BlackjackConfig.aliasHelp.size > 0) BlackjackConfig.aliasHelp[0]
            else "未定义！请联系机器人所有者！"
            sender.sendMessage(buildMessageChain {
                +player.at()
                +PlainText(" 请不要重复加入，你可以通过以下指令获取帮助：\n")
                +PlainText("$arg0 $argHelp")
            })
            return
        }
        participants.add(player)
        startWarning = false
        // 加入成功
        sender.sendMessage(buildMessageChain {
            +player.at()
            +PlainText(" 加入成功")
        })
    }

    suspend fun start(player: Member) {
        if (player.id != promoter.id) {
            // 并非发起人
            sender.sendMessage(buildMessageChain {
                +player.at()
                +PlainText("只有发起人可以开始本轮")
            })
            return
        }
        if (startWarning) {
            sender.sendMessage(buildMessageChain {
                +player.at()
                +PlainText(" 目前只有你一个人，再次输入开局指令即可强行开局")
            })
            startWarning = false
            return
        }
        start = true
        try {
            autoClose.interrupt()
        } catch (e: Exception) {
            exceptionLogger(e)
        }
        // 开局处理
        val colors = arrayOf("♠", "♥", "♣", "♦")
        val nums = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        colors.forEach {
            nums.forEachIndexed { index, element ->
                if (index == 0) {
                    card.add(BlackjackCard(11, it + element))
                } else if (index > 8) {
                    card.add(BlackjackCard(10, it + element))
                } else {
                    card.add(BlackjackCard(index + 1, it + element))
                }
            }
        }
        // 洗牌
        val rand = Random()
        rand.setSeed(System.currentTimeMillis())
        for (i in card.size - 1 downTo 1) {
            val j = rand.nextInt(i + 1)
            val temp = card[i]
            card[i] = card[j]
            card[j] = temp
        }
        // 发牌
        for (p in participants) {
            ownedCard[p.id] = mutableListOf(card.removeLast(), card.removeLast())
        }
        try {
            autoFinish.start()
        } catch (e: Exception) {
            exceptionLogger(e)
        }
        // 成功开局
        sender.sendMessage(
            messageGenerator(
                BlackjackConfig.messageStart,
                arrayOf(
                    "<target>"
                ),
                arrayOf(
                    promoter.at().serializeToMiraiCode()
                )
            ) + messageGenerator(getStart())
        )
    }

    suspend fun addCard(player: Member) {
        if (isPlayerFinish(player)) {
            return
        }
        if (!containsMember(player)) {
            sender.sendMessage(buildMessageChain {
                +player.at()
                +PlainText(" 你不在玩家列表中")
            })
            return
        }
        if (card.size == 0) {
            // 没牌了
            sender.sendMessage(buildMessageChain {
                +player.at()
                +PlainText(" 所有的牌均已被拿")
            })
            return
        }
        if (ownedCard[player.id].isNullOrEmpty()) {
            // 内部错误
            sender.sendMessage(buildMessageChain {
                +player.at()
                +PlainText(" 内部错误")
            })
            return
        }
        val lastCard = card.removeLast()
        ownedCard[player.id]!!.add(lastCard)
        val totalNumber = getTotal(player)
        if (totalNumber > 21 && checkCardA(player, totalNumber)) {
            // 爆掉
            removeMember(player)
            sender.sendMessage(getPlayerBust(player))
        } else {
            sender.sendMessage(
                messageGenerator(
                    BlackjackConfig.messageAddCard,
                    arrayOf(
                        "<getcard-t>",
                        "<getcard-n>",
                        "<cardlist>",
                        "<target>"
                    ), arrayOf(
                        lastCard.text,
                        lastCard.number.toString(),
                        getPlayerCard(player),
                        player.at().serializeToMiraiCode()
                    )
                )
            )
        }
    }

    private fun checkCardA(player: Member, totalNumber: Int): Boolean {
        val cards = ownedCard[player.id]
        var total = totalNumber
        if (cards.isNullOrEmpty()) {
            return true
        }
        for (c in cards) {
            if (c.number == 11) {
                c.number = 1
                total -= 10
                if (total <= 21) {
                    return false
                }
            }
        }
        return true
    }

    suspend fun giveUp(player: Member) {
        if (isPlayerFinish(player)) {
            return
        }
        if (!containsMember(player)) {
            sender.sendMessage(buildMessageChain {
                +player.at()
                +PlainText(" 你不在玩家列表中")
            })
            return
        }
        // 投降
        removeMember(player)
        sender.sendMessage(getPlayerGiveUp(player))
    }

    suspend fun stopCard(player: Member) {
        if (isPlayerFinish(player)) {
            return
        }
        if (!containsMember(player)) {
            sender.sendMessage(buildMessageChain {
                +player.at()
                +PlainText(" 你不在玩家列表中")
            })
            return
        }
        isFinish.add(player.id)
        // 停牌
        sender.sendMessage(buildMessageChain {
            +player.at()
            +PlainText(" 你已停牌")
        })
        if (isFinish.size == participants.size) {
            finish()
        }
    }

    suspend fun checkCard(player: Member) {
        if (!containsMember(player)) {
            sender.sendMessage(buildMessageChain {
                +player.at()
                +PlainText(" 你不在玩家列表中")
            })
            return
        }
        sender.sendMessage(buildMessageChain {
            +player.at()
            +PlainText(getPlayerCard(player))
        })
    }

    fun endByAdmin() {
        end = true
        try {
            autoFinish.interrupt()
        } catch (e: Exception) {
            exceptionLogger(e)
        }
        try {
            autoClose.interrupt()
        } catch (e: Exception) {
            exceptionLogger(e)
        }
    }

    private suspend fun isPlayerFinish(player: Member): Boolean {
        val finish = isFinish.contains(player.id)
        if (finish) {
            sender.sendMessage(buildMessageChain {
                +player.at()
                +PlainText(" 你已不能再进行任何操作")
            })
        }
        return finish
    }

    private suspend fun finish() {
        sender.sendMessage(
            messageGenerator(
                BlackjackConfig.messageFinish,
                arrayOf(
                    "<target>"
                ),
                arrayOf(
                    promoter.at().serializeToMiraiCode()
                )
            ) + messageGenerator(getFinish(false))
        )
    }

    private suspend fun getFinish(fromAutoFinish: Boolean): String {
        end = true
        if (!fromAutoFinish) {
            try {
                autoFinish.interrupt()
            } catch (e: Exception) {
                exceptionLogger(e)
            }
        }
        if (participants.size == 0) return ""
        val totalScore = mutableMapOf<Member, Int>()
        participants.forEach {
            totalScore[it] = getTotal(it)
        }
        val max = Collections.max(totalScore.values)
        val sb = StringBuilder("\n\n")
        for ((member, total) in totalScore.entries) {
            if (total == max) {
                sb.append(getPlayerWin(member))
            } else {
                sb.append(getPlayerLose(member))
            }
            sb.append("\n\n")
        }
        return sb.toString()
    }

    private fun getStart(): String {
        val sb = StringBuilder("\n\n")
        for (p in participants) {
            sb.append(
                messageGeneratorString(
                    BlackjackConfig.messageStartCardList,
                    arrayOf(
                        "<owner>",
                        "<cardlist>"
                    ),
                    arrayOf(
                        p.at().serializeToMiraiCode(),
                        getPlayerCard(p)
                    )
                )
            )
            sb.append("\n\n")
        }
        return sb.toString()
    }

    private fun getTotal(player: Member): Int {
        val cards = ownedCard[player.id]
        return if (cards.isNullOrEmpty()) {
            0
        } else {
            var total = 0
            cards.forEach { card ->
                total += card.number
            }
            total
        }
    }

    private fun getPlayerCard(player: Member): String {
        val sb = StringBuilder()
        val cards = if (ownedCard[player.id].isNullOrEmpty()) {
            mutableListOf()
        } else {
            ownedCard[player.id]!!
        }
        for (card in cards) {
            sb.append(
                messageGeneratorString(
                    BlackjackConfig.subMessageCardListMessage,
                    arrayOf(
                        "<card-t>",
                        "<card-n>"
                    ),
                    arrayOf(
                        card.text,
                        card.number.toString()
                    )
                )
            )
        }
        return messageGeneratorString(
            BlackjackConfig.subMessageCardList,
            arrayOf(
                "<total>",
                "<card>"
            ),
            arrayOf(
                getTotal(player).toString(),
                sb.toString()
            )
        )
    }

    private fun getPlayerWin(player: Member): String {
        return messageGeneratorString(
            BlackjackConfig.messageWin,
            arrayOf(
                "<target>",
                "<cardlist>"
            ),
            arrayOf(
                player.at().serializeToMiraiCode(),
                getPlayerCard(player)
            )
        )
    }

    private suspend fun getPlayerLose(player: Member): String {
        val banTime = (21 - getTotal(player)) * BlackjackConfig.loserMultiplier
        player.mute(banTime)
        return messageGeneratorString(
            BlackjackConfig.messageLose,
            arrayOf(
                "<bantime-s>",
                "<bantime-f>",
                "<target>",
                "<cardlist>"
            ),
            arrayOf(
                banTime.toString(),
                timeFormatter(banTime),
                player.at().serializeToMiraiCode(),
                getPlayerCard(player)
            )
        )
    }

    private suspend fun getPlayerBust(player: Member): MessageChain {
        val banTime = 21 * BlackjackConfig.bustMultiplier
        player.mute(banTime)
        return messageGenerator(
            BlackjackConfig.messageBust,
            arrayOf(
                "<bantime-s>",
                "<bantime-f>",
                "<target>",
                "<cardlist>"
            ),
            arrayOf(
                banTime.toString(),
                timeFormatter(banTime),
                player.at().serializeToMiraiCode(),
                getPlayerCard(player)
            )
        )
    }

    private suspend fun getPlayerGiveUp(player: Member): MessageChain {
        val banTime = (21 - getTotal(player)) * BlackjackConfig.giveUpMultiplier
        player.mute(banTime)
        return messageGenerator(
            BlackjackConfig.messageGiveUp,
            arrayOf(
                "<bantime-s>",
                "<bantime-f>",
                "<target>",
                "<cardlist>"
            ),
            arrayOf(
                banTime.toString(),
                timeFormatter(banTime),
                player.at().serializeToMiraiCode(),
                getPlayerCard(player)
            )
        )
    }

    fun isStart(): Boolean {
        return start
    }

    fun isEnd(): Boolean {
        return end
    }
}