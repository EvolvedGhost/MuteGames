package com.evolvedghost.roulette

import com.evolvedghost.utils.debugLogger
import com.evolvedghost.utils.timeFormatter
import kotlin.random.Random

class Roulette {
    private var finish = true
    private var current = -1
    private var chamber = -1
    private var bulletTotal = -1
    private var bullet = -1
    private var mute = -1
    private var loaded = arrayOf<Boolean>()

    init {
        restart()
    }

    fun isFinished(): Boolean {
        return finish
    }

    private fun restart() {
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
        finish = true
    }

    fun forceEnd(): Boolean {
        if (RouletteConfig.forceEndWhenAllRamin) {
            if (chamber - current == bullet) {
                finish = true
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
            finish = true
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