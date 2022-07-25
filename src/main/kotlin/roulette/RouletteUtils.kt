package com.evolvedghost.roulette

import kotlinx.coroutines.sync.Mutex

// 储存所有命令别名并在插件启动时注入
var rouletteSecondaryNames = arrayOf<String>()

// 为每个群创建一个Roulette类
val rouletteMap: MutableMap<Long, Roulette> = mutableMapOf()
val rouletteMapLock = Mutex()

// 校验自定义数据是否正确
var dataCorrect = true

// 如自定义数据为什么不正确
var errorMessage = String()

// 数据校验与相关数据写入
fun rouletteInit() {
    rouletteSecondaryNames = RouletteConfig.alias.toTypedArray()
    if (RouletteConfig.randomMute) {
        if (RouletteConfig.mute >= RouletteConfig.muteMax) {
            dataCorrect = false
            errorMessage = "禁言时间的最小值大于或等于最大值"
        } else if (RouletteConfig.mute < 1 || RouletteConfig.muteMax > 2592000) {
            dataCorrect = false
            errorMessage = "禁言时间超过范围"
        }
    } else {
        if (RouletteConfig.mute < 1 || RouletteConfig.mute > 2592000) {
            dataCorrect = false
            errorMessage = "禁言时间超过范围"
        }
    }
    if (RouletteConfig.randomChamber) {
        if (RouletteConfig.chamber >= RouletteConfig.chamberMax) {
            dataCorrect = false
            errorMessage = "随机弹膛的最小值大于或等于最大值"
        } else {
            verifyBullet()
        }
    } else {
        verifyBullet()
    }
}

// 校验子弹数量
fun verifyBullet() {
    if (RouletteConfig.randomBullet) {
        if (RouletteConfig.bullet >= RouletteConfig.bulletMax) {
            dataCorrect = false
            errorMessage = "随机子弹的最小值大于或等于最大值"
        } else if (RouletteConfig.bulletMax >= RouletteConfig.chamber) {
            dataCorrect = false
            errorMessage = "子弹的数量大于或等于弹膛数量"
        }
    } else {
        if (RouletteConfig.bullet >= RouletteConfig.chamber) {
            dataCorrect = false
            errorMessage = "子弹的数量大于或等于弹膛数量"
        }
    }
}