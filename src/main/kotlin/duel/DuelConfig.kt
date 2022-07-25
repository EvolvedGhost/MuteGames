package com.evolvedghost.duel

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object DuelConfig : ReadOnlyPluginConfig("duel") {
    @ValueDescription("自定义指令触发决斗\n请注意，该自定义指令的更改必须重启Mirai")
    val alias: MutableList<String> by value(mutableListOf("决斗"))

    @ValueDescription("""
        随机决斗击中部位表<部位名:<禁言时间:秒,击中概率:Int>>
        概率可为任意值，值越大概率越高，插件会将全部作为整体计算概率（别超过Int范围即可）
        ban为禁言时间，以秒为单位，范围1-2592000，超出范围的会被抛弃
        """)
    val position: MutableMap<String, MutableMap<String, Int>> by value(
        mutableMapOf(
            "头" to mutableMapOf(
                "ban" to 120,
                "chance" to 10
            ),
            "腿" to mutableMapOf(
                "ban" to 60,
                "chance" to 120
            ),
            "脚" to mutableMapOf(
                "ban" to 30,
                "chance" to 10
            ),
            "身体" to mutableMapOf(
                "ban" to 90,
                "chance" to 160
            ),
            "手臂" to mutableMapOf(
                "ban" to 60,
                "chance" to 100
            ),
            "手掌" to mutableMapOf(
                "ban" to 30,
                "chance" to 10
            ),
            "牛子" to mutableMapOf(
                "ban" to 180,
                "chance" to 1
            )
        )
    )

    @ValueDescription("随机决斗是否可能两败俱伤，设置-1为不可能事件，设置100为必然事件，设置0-100中间的数量为触发概率")
    val allHurt: Int by value(10)

    @ValueDescription(
        """
        起始消息，可用替代项目为:
        <target> @触发人
    """
    )
    val messageStartDuel: String by value("<target> 开启了决斗，他渴望一个有价值的对手")

    @ValueDescription(
        """
        起始消息，可用替代项目为:
        <target> @触发人
    """
    )
    val messageSameMember: String by value("<target> 你不能自己决斗自己")

    @ValueDescription(
        """
        决斗消息，可用替代项目为:
        <target-win> @获胜者
        <target-lose> @失败者
        <position> 击伤部位
        <mute-s> 禁言时间（单位秒）（例:5）
        <mute-f> 禁言时间（例:11天4时5分14秒）
    """
    )
    val messageEndDuel: String by value("<target-lose> 的<position>被<target-win> 开枪击中，获得<mute-f>养伤时间")

    @ValueDescription(
        """
        两败俱伤消息，可用替代项目为:
        <target-1> @决斗者1
        <target-2> @决斗者2
        <position-1> 击伤部位1
        <position-2> 击伤部位2
        <mute-s-1> 禁言时间1（单位秒）（例:5）
        <mute-s-2> 禁言时间2
        <mute-f-1> 禁言时间1（例:11天4时5分14秒）
        <mute-f-2> 禁言时间2
    """
    )
    val messageEndDuelAllHurt: String by value("两位决斗者同时开枪，<target-1> 的<position-1>被<target-2> 开枪击中，获得<mute-f-1>养伤时间，<target-2> 的<position-2>被<target-1> 开枪击中，获得<mute-f-2>养伤时间")
}