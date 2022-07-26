package com.evolvedghost.admin

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object AdminConfig : ReadOnlyPluginConfig("admin") {
    @ValueDescription("自定义指令触发管理指令\n请注意，该自定义指令的更改必须重启Mirai")
    val alias: MutableList<String> by value(mutableListOf("禁言游戏", "mutegame"))

    @ValueDescription("提供一些基础的Debug信息，包括插件内部运行情况并输出所有的Exception")
    val debug: Boolean by value(false)

    @ValueDescription("管理员/群主等比机器人权限同级或更高的能否游玩？")
    val canAdminPlayIt: Boolean by value(false)
}