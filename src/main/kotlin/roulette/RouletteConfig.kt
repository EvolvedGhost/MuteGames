package com.evolvedghost.roulette

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object RouletteConfig : ReadOnlyPluginConfig("roulette") {
    @ValueDescription("自定义指令触发轮盘赌\n请注意，该自定义指令的更改必须重启Mirai")
    val alias: MutableList<String> by value(mutableListOf("轮盘", "俄罗斯轮盘"))

    @ValueDescription("轮盘等待时间，超过后上一个轮盘失效，单位：秒")
    val waitTime: Int by value(600)

    @ValueDescription("当最后数发均为子弹时强制结束游戏")
    val forceEndWhenAllRamin: Boolean by value(true)

    @ValueDescription("一把枪的弹膛数量是否为随机的")
    val randomChamber: Boolean by value(true)

    @ValueDescription("一把枪有多少个弹膛\n如果弹膛数量随机为true则此值为随机的最小值")
    val chamber: Int by value(6)

    @ValueDescription("一把枪最大有多少个弹膛\n如果弹膛数量随机为false则不会有任何作用")
    val chamberMax: Int by value(8)

    @ValueDescription("一把枪上膛的子弹数量是否为随机的")
    val randomBullet: Boolean by value(false)

    @ValueDescription("一把枪有多少发子弹\n如果子弹数量随机为true则此值为随机的最小值")
    val bullet: Int by value(1)

    @ValueDescription("一把枪最多有多少发子弹\n如果子弹数量随机为false则不会有任何作用")
    val bulletMax: Int by value(2)

    @ValueDescription("禁言时间是否随机")
    val randomMute: Boolean by value(true)

    @ValueDescription("禁言多少秒(范围1-2592000)\n如果禁言时间随机为true则此值为随机的最小值")
    val mute: Int by value(1)

    @ValueDescription("禁言最多多少秒(范围1-2592000)\n如果禁言时间随机为false则不会有任何作用")
    val muteMax: Int by value(300)

    @ValueDescription(
        """
        起始消息，可用替代项目为:
        <bullet> 子弹数量
        <chamber> 弹膛数量
        <remain-bullet> 剩余子弹数
        <remain-chamber> 剩余击发数
        <mute-s> 禁言时间（单位秒）（例:5）
        <mute-f> 禁言时间（例:11天4时5分14秒）
        <timeout-s> 超时时间（单位秒）（例:5）
        <timeout-f> 超时时间（例:11天4时5分14秒）
        <target> @触发人
    """
    )
    val messageStart: String by value("新一轮俄罗斯轮盘开启，这是一把<chamber>发的左轮手枪，装填了<bullet>发子弹，击发奖励为禁言<mute-f>，轮盘将于<timeout-f>后超时")

    @ValueDescription(
        """
        结束消息，可用替代项目为:
        <bullet> 子弹数量
        <chamber> 弹膛数量
        <remain-bullet> 剩余子弹数
        <remain-chamber> 剩余击发数
        <mute-s> 禁言时间（单位秒）（例:5）
        <mute-f> 禁言时间（例:11天4时5分14秒）
        <target> @触发人
    """
    )
    val messageEnd: String by value("此轮俄罗斯轮盘结束")

    @ValueDescription(
        """
        超时消息，可用替代项目为:
        <bullet> 子弹数量
        <chamber> 弹膛数量
        <remain-bullet> 剩余子弹数
        <remain-chamber> 剩余击发数
        <mute-s> 禁言时间（单位秒）（例:5）
        <mute-f> 禁言时间（例:11天4时5分14秒）
        <timeout-s> 超时时间（单位秒）（例:5）
        <timeout-f> 超时时间（例:11天4时5分14秒）
    """
    )
    val messageTimeout: String by value("此轮俄罗斯轮盘因超时结束")

    @ValueDescription(
        """
        击发消息，可用替代项目为:
        <bullet> 子弹数量
        <chamber> 弹膛数量
        <remain-bullet> 剩余子弹数
        <remain-chamber> 剩余击发数
        <mute-s> 禁言时间（单位秒）（例:5）
        <mute-f> 禁言时间（例:11天4时5分14秒）
        <target> @触发人
    """
    )
    val messageShot: String by value("<target> 开了一枪，枪响了，被禁言<mute-f>")

    @ValueDescription(
        """
        击发消息但对方是管理员无法禁言，可用替代项目为:
        <bullet> 子弹数量
        <chamber> 弹膛数量
        <remain-bullet> 剩余子弹数
        <remain-chamber> 剩余击发数
        <mute-s> 禁言时间（单位秒）（例:5）
        <mute-f> 禁言时间（例:11天4时5分14秒）
        <target> @触发人
    """
    )
    val messageShotButAdmin: String by value("<target> 开了一枪，枪响了，但对方是管理员，逃掉了<mute-f>的禁言")

    @ValueDescription(
        """
        未击发消息，可用替代项目为:
        <bullet> 子弹数量
        <chamber> 弹膛数量
        <remain-bullet> 剩余子弹数
        <remain-chamber> 剩余击发数
        <mute-s> 禁言时间（单位秒）（例:5）
        <mute-f> 禁言时间（例:11天4时5分14秒）
        <target> @触发人
    """
    )
    val messagePass: String by value("<target> 开了一枪，枪没响，还剩<remain-chamber>轮")

    @ValueDescription(
        """
        剩下全是子弹强制结束消息，可用替代项目为:
        <bullet> 子弹数量
        <chamber> 弹膛数量
        <remain-bullet> 剩余子弹数
        <remain-chamber> 剩余击发数
        <mute-s> 禁言时间（单位秒）（例:5）
        <mute-f> 禁言时间（例:11天4时5分14秒）
        <target> @触发人
    """
    )
    val messageForceEnd: String by value("剩下的发<remain-chamber>全是子弹了，此轮俄罗斯轮盘结束")
}