package com.evolvedghost.duel

import com.evolvedghost.duel.DuelConfig.position
import com.evolvedghost.utils.debugLogger
import kotlinx.coroutines.sync.Mutex

// 储存所有命令别名并在插件启动时注入
var duelSecondaryNames = arrayOf<String>()

// 为每个群创建一个Duel类
val duelMap: MutableMap<Long, Duel> = mutableMapOf()
val duelMapLock = Mutex()

// 协助计算概率
val positionMuteList = mutableListOf<Int>()
val positionChanceList = mutableListOf<Int>()
val positionNameList = mutableListOf<String>()
var totalChance = -1

fun duelInit() {
    duelSecondaryNames = DuelConfig.alias.toTypedArray()
    positionChanceList.clear()
    positionNameList.clear()
    positionMuteList.clear()
    totalChance = 0
    val iterator = position.entries.iterator()
    while (iterator.hasNext()) {
        val element = iterator.next()
        val chance = element.value["chance"]
        val mute = element.value["ban"]
        val name = element.key
        if (chance != null && chance > 0 && mute != null && mute > 0 && mute <= 2592000) {
            totalChance += chance
            positionChanceList.add(totalChance)
            positionNameList.add(name)
            positionMuteList.add(mute)
        }
    }
    debugLogger("决斗已加载${positionNameList.size}个击中部位，共加载概率为$totalChance")
}