package com.evolvedghost.banme

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object BanMeConfig : ReadOnlyPluginConfig("banMe") {
    @ValueDescription("自定义指令触发自裁\n请注意，该自定义指令的更改必须重启Mirai")
    val alias: MutableList<String> by value(mutableListOf("自裁"))

    @ValueDescription("是否随机自裁禁言时间")
    val randomBanMe: Boolean by value(true)

    @ValueDescription("自裁禁言时间(秒)\n如果随机自裁禁言为true则此值为随机的最小值")
    val banMeTime: Int by value(60)

    @ValueDescription("最大自裁禁言时间(秒)\n如果随机自裁禁言为false则不会有任何作用")
    val banMeTimeMax: Int by value(600)

    @ValueDescription(
        """
        触发消息，可用替代项目为:
        <target> @触发人
        <mute-s> 禁言时间（单位秒）（例:5）
        <mute-f> 禁言时间（例:11天4时5分14秒）
    """
    )
    val messageBanMe: String by value("恭喜<target> 获得<mute-f>禁言")
}