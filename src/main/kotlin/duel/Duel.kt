package com.evolvedghost.duel

import com.evolvedghost.duel.DuelConfig.allHurt
import com.evolvedghost.duel.DuelConfig.messageEndDuel
import com.evolvedghost.duel.DuelConfig.messageEndDuelAllHurt
import com.evolvedghost.utils.debugLogger
import com.evolvedghost.utils.messageGenerator
import com.evolvedghost.utils.timeFormatter
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.at
import kotlin.random.Random

class Duel(target: Member) {
    private var waiter: Member? = null
    private var isFinished = true

    init {
        waiter = target
        isFinished = false
    }

    fun isFinished(): Boolean {
        if (waiter == null || isFinished) return true
        return false
    }

    fun endByAdmin() {
        waiter = null
    }

    fun isSameMember(fighter: Member): Boolean {
        if (waiter == null) return false
        return fighter.id == waiter!!.id
    }

    suspend fun join(fighter: Member): MessageChain {
        return if (waiter != null) {
            val generator = Random(System.currentTimeMillis())
            val chance = generator.nextInt(100)
            debugLogger("随机数判断两败俱伤中，随机值为$chance")
            if (chance <= allHurt) {
                // 两败俱伤
                val pos1 = DuelPosition(generator.nextInt(totalChance) + 1)
                val pos2 = DuelPosition(generator.nextInt(totalChance) + 1)
                waiter!!.mute(pos1.getMute())
                fighter.mute(pos2.getMute())
                isFinished = true
                messageGenerator(
                    messageEndDuelAllHurt,
                    arrayOf(
                        "<target-1>",
                        "<target-2>",
                        "<position-1>",
                        "<position-2>",
                        "<mute-s-1>",
                        "<mute-s-2>",
                        "<mute-f-1>",
                        "<mute-f-2>"
                    ),
                    arrayOf(
                        waiter!!.at().serializeToMiraiCode(),
                        fighter.at().serializeToMiraiCode(),
                        pos1.getPosition(),
                        pos2.getPosition(),
                        pos1.getMute().toString(),
                        pos2.getMute().toString(),
                        timeFormatter(pos1.getMute()),
                        timeFormatter(pos2.getMute()),
                    )
                )
            } else {
                // 一方受伤
                val pos = DuelPosition(generator.nextInt(totalChance) + 1)
                val bool = generator.nextBoolean()
                debugLogger("随机数判断赢家中，随机值为$bool")
                val winner = if (bool) {
                    waiter!!
                } else {
                    fighter
                }
                val loser = if (bool) {
                    fighter
                } else {
                    waiter!!
                }
                loser.mute(pos.getMute())
                isFinished = true
                messageGenerator(
                    messageEndDuel,
                    arrayOf(
                        "<target-win>",
                        "<target-lose>",
                        "<position>",
                        "<mute-s>",
                        "<mute-f>"
                    ),
                    arrayOf(
                        winner.at().serializeToMiraiCode(),
                        loser.at().serializeToMiraiCode(),
                        pos.getPosition(),
                        pos.getMute().toString(),
                        timeFormatter(pos.getMute()),
                    )
                )
            }
        } else messageGenerator("发生错误")
    }
}