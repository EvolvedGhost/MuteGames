package com.evolvedghost.roulette

import com.evolvedghost.roulette.RouletteConfig.messageTimeout
import com.evolvedghost.roulette.RouletteConfig.waitTime
import com.evolvedghost.utils.debugLogger
import com.evolvedghost.utils.exceptionLogger
import com.evolvedghost.utils.messageGenerator
import com.evolvedghost.utils.timeFormatter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandSender
import kotlin.random.Random

class Roulette(sender: CommandSender) {
    private var finish = true
    private var sender: CommandSender? = null
    private var current = -1
    private var chamber = -1
    private var bulletTotal = -1
    private var bullet = -1
    private var mute = -1
    private var loaded = arrayOf<Boolean>()

    @OptIn(DelicateCoroutinesApi::class)
    private val endRoulette = Thread {
        run {
            try {
                Thread.sleep(waitTime.toLong() * 1000)
                GlobalScope.launch {
                    sender.sendMessage(
                        messageGenerator(
                            messageTimeout,
                            arrayOf(
                                "<bullet>",
                                "<chamber>",
                                "<remain-bullet>",
                                "<remain-chamber>",
                                "<mute-s>",
                                "<mute-f>",
                                "<timeout-s>",
                                "<timeout-f>"
                            ),
                            arrayOf(
                                getBullet(),
                                getChamber(),
                                getBulletRemain(),
                                getChamberRemain(),
                                getMute(),
                                getMuteFormat(),
                                waitTime.toString(),
                                timeFormatter(waitTime)
                            )
                        )
                    )
                }
            } catch (_: Exception) {
            } finally {
                finish = true
            }
        }
    }

    init {
        this.sender = sender
        initRoulette()
        try {
            endRoulette.start()
        } catch (e: Exception) {
            exceptionLogger(e)
        }
    }

    fun isFinished(): Boolean {
        return finish
    }

    private fun initRoulette() {
        val generator = Random(System.currentTimeMillis())
        chamber = if (RouletteConfig.randomChamber) {
            generator.nextInt(RouletteConfig.chamberMax - RouletteConfig.chamber + 1) + RouletteConfig.chamber
        } else {
            RouletteConfig.chamber
        }
        bullet = if (RouletteConfig.randomBullet) {
            generator.nextInt(RouletteConfig.bulletMax - RouletteConfig.bullet + 1) + RouletteConfig.bullet
        } else {
            RouletteConfig.bullet
        }
        bulletTotal = bullet
        mute = if (RouletteConfig.randomMute) {
            generator.nextInt(RouletteConfig.muteMax - RouletteConfig.mute + 1) + RouletteConfig.mute
        } else {
            RouletteConfig.mute
        }
        loaded = Array(chamber) { false }
        val hs = HashSet<Int>()
        while (true) {
            val tmp = generator.nextInt(chamber)
            hs.add(tmp)
            if (hs.size == bullet) break
        }
        for (number in hs.toTypedArray()) {
            loaded[number] = true
        }
        current = 0
        finish = false
        debugLogger("插件生成了一把 $chamber 发的左轮，膛中弹药情况为${loaded.contentToString()}")
    }

    fun endByAdmin() {
        try {
            endRoulette.interrupt()
        } catch (e: Exception) {
            exceptionLogger(e)
        }
    }

    fun forceEnd(): Boolean {
        if (RouletteConfig.forceEndWhenAllRamin) {
            if (chamber - current == bullet) {
                try {
                    endRoulette.interrupt()
                } catch (e: Exception) {
                    exceptionLogger(e)
                }
                return true
            }
        }
        return false
    }

    fun shot(): Boolean {
        val shot = loaded[current]
        if (shot) {
            bullet--
        }
        current++
        if (bullet == 0 || current >= chamber) {
            try {
                endRoulette.interrupt()
            } catch (e: Exception) {
                exceptionLogger(e)
            }
        }
        return shot
    }

    fun getBullet(): String {
        return bulletTotal.toString()
    }

    fun getChamber(): String {
        return chamber.toString()
    }

    fun getBulletRemain(): String {
        return bullet.toString()
    }

    fun getChamberRemain(): String {
        return (chamber - current).toString()
    }

    fun getMute(): String {
        return mute.toString()
    }

    fun getMuteInt(): Int {
        return mute
    }

    fun getMuteFormat(): String {
        return timeFormatter(mute)
    }
}