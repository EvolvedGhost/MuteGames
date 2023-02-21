package com.evolvedghost.blackjack

import kotlinx.coroutines.sync.Mutex

// 储存所有命令别名并在插件启动时注入
var BlackjackSecondaryNames = arrayOf<String>()

// 为每个群创建一个Blackjack类
val blackjackMap: MutableMap<Long, Blackjack> = mutableMapOf()
val blackjackMapLock = Mutex()

fun blackjackInit() {
    BlackjackSecondaryNames = BlackjackConfig.alias.toTypedArray()
}