package com.evolvedghost.banme

// 储存所有命令别名并在插件启动时注入
var BanMeSecondaryNames = arrayOf<String>()

fun banMeInit() {
    BanMeSecondaryNames = BanMeConfig.alias.toTypedArray()
}