package com.evolvedghost.blackjack

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object BlackjackConfig : ReadOnlyPluginConfig("blackjack") {
    @ValueDescription("自定义指令触发21点\n请注意，该自定义指令的更改必须重启Mirai")
    val alias: MutableList<String> by value(mutableListOf("21点"))

    @ValueDescription("自定义指令触发21点的开始指令\n该自定义指令的更改不必重启Mirai，可插件内reload\n英文请使用小写字母")
    val aliasStart: MutableList<String> by value(mutableListOf("开始", "start"))

    @ValueDescription("自定义指令触发21点的拿牌指令\n该自定义指令的更改不必重启Mirai，可插件内reload\n英文请使用小写字母")
    val aliasAdd: MutableList<String> by value(mutableListOf("拿牌", "add"))

    @ValueDescription("自定义指令触发21点的停牌指令\n该自定义指令的更改不必重启Mirai，可插件内reload\n英文请使用小写字母")
    val aliasStop: MutableList<String> by value(mutableListOf("停牌", "stop"))

    @ValueDescription("自定义指令触发21点的投降指令\n该自定义指令的更改不必重启Mirai，可插件内reload\n英文请使用小写字母")
    val aliasGiveUp: MutableList<String> by value(mutableListOf("投降", "放弃", "giveup"))

    @ValueDescription("自定义指令触发21点的看牌指令\n该自定义指令的更改不必重启Mirai，可插件内reload\n英文请使用小写字母")
    val aliasCheck: MutableList<String> by value(mutableListOf("看牌", "check"))

    @ValueDescription("自定义指令触发21点的帮助指令\n该自定义指令的更改不必重启Mirai，可插件内reload\n英文请使用小写字母")
    val aliasHelp: MutableList<String> by value(mutableListOf("帮助", "help"))

    @ValueDescription("输家禁言倍率，计算方式：倍率×(21-输家所持点数)秒")
    val loserMultiplier: Int by value(30)

    @ValueDescription("爆牌禁言倍率，计算方式：倍率×21秒")
    val bustMultiplier: Int by value(30)

    @ValueDescription("投降禁言倍率，计算方式：倍率×(21-投降者当前所持点数)秒")
    val giveUpMultiplier: Int by value(15)

    @ValueDescription("发起者开始后如没开局自动关闭回合时间(秒)")
    val autoCloseRoundTimer: Int by value(300)

    @ValueDescription("开始后自动停牌时间(秒)")
    val autoFinishRoundTimer: Int by value(300)

    @ValueDescription(
        """
        发起信息，可用替代项目为:
        <timeout-s> 超时时间（单位秒）（例:5）
        <timeout-f> 超时时间（例:11天4时5分14秒）
        <target> @触发人
    """
    )
    val messageOpenRound: String by value("<target> 发起了一局21点，等待各位的入局")

    @ValueDescription(
        """
        发起信息，可用替代项目为:
        <target> @触发人
    """
    )
    val messageStart: String by value("<target> 开启了本轮21点")

    @ValueDescription(
        """
        初始牌组，将接在发起信息之后，可用替代项目为:
        <owner> @拥有人
        <cardlist> 当前牌组
    """
    )
    val messageStartCardList: String by value("<owner> <cardlist>")

    @ValueDescription(
        """
        发起的21点被自动关闭信息，可用替代项目为:
        <timeout-s> 超时时间（单位秒）（例:5）
        <timeout-f> 超时时间（例:11天4时5分14秒）
        <target> @触发人
    """
    )
    val messageAutoClose: String by value("因为超过<timeout-f>，<target>的21点被关闭")

    @ValueDescription(
        """
        21点结束信息，可用替代项目为:
        <target> @发起人
    """
    )
    val messageFinish: String by value("<target> 发起的本轮21点结束")

    @ValueDescription(
        """
        发起的21点被自动结束信息，可用替代项目为:
        <timeout-s> 超时时间（单位秒）（例:5）
        <timeout-f> 超时时间（例:11天4时5分14秒）
        <target> @发起人
    """
    )
    val messageAutoFinish: String by value("因为超过<timeout-f>，<target> 发起的本轮21点结束")

    @ValueDescription(
        """
        投降放弃信息，可用替代项目为:
        <bantime-s> 禁言时间（单位秒）（例:5）
        <bantime-f> 禁言时间（例:11天4时5分14秒）
        <target> @触发人
        <cardlist> 当前牌组
    """
    )
    val messageGiveUp: String by value("<target> 放弃了本轮，获得禁言时长：<bantime-f>\n牌组：<cardlist>")

    @ValueDescription(
        """
        爆牌信息，可用替代项目为:
        <bantime-s> 禁言时间（单位秒）（例:5）
        <bantime-f> 禁言时间（例:11天4时5分14秒）
        <target> @触发人
        <cardlist> 当前牌组
    """
    )
    val messageBust: String by value("<target> 爆牌，获得禁言时长：<bantime-f>\n牌组：<cardlist>")

    @ValueDescription(
        """
        输掉信息，将接在结束信息之后，可用替代项目为:
        <bantime-s> 禁言时间（单位秒）（例:5）
        <bantime-f> 禁言时间（例:11天4时5分14秒）
        <target> @触发人
        <cardlist> 当前牌组
    """
    )
    val messageLose: String by value("<target> 输了，禁言<bantime-f>\n牌组：<cardlist>")

    @ValueDescription(
        """
        发起信息，将接在结束信息之后，可用替代项目为:
        <target> @触发人
        <cardlist> 当前牌组
    """
    )
    val messageWin: String by value("<target> 赢得了此轮21点\n牌组：<cardlist>")

    @ValueDescription(
        """
        拿牌信息，可用替代项目为:
        <getcard-t> 拿到的牌面（例：♥A）
        <getcard-n> 拿到的牌值（例：1）
        <cardlist> 当前牌组
        <target> @触发人
    """
    )
    val messageAddCard: String by value("<target> 拿到了[<getcard-t>]\n当前牌组：<cardlist>")

    @ValueDescription(
        """
        牌组信息，代替上面的<cardlist>，可用替代项目为:
        <card> 牌
        <total> 总值
    """
    )
    val subMessageCardList: String by value("(<total>)<card>")

    @ValueDescription(
        """
        牌组信息，代替上面的<card>，多张牌会重复多次，可用替代项目为:
        <card-t> 拿到的牌面（例：♥A）
        <card-n> 拿到的牌值（例：1）
    """
    )
    val subMessageCardListMessage: String by value("[<card-t>]")
}