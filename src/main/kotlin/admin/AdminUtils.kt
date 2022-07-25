package com.evolvedghost.admin

// 储存一级并在插件启动时注入
var adminMainSecondaryNames = arrayOf<String>()

fun adminInit() {
    adminMainSecondaryNames = AdminConfig.alias.toTypedArray()
}

