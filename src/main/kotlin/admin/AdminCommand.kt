package com.evolvedghost.admin

import com.evolvedghost.MuteGames
import com.evolvedghost.duel.duelMap
import com.evolvedghost.roulette.rouletteMap
import com.evolvedghost.utils.reloadPlugin
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.GroupAwareCommandSender

object AdminCommand : CompositeCommand(
    MuteGames,
    primaryName = "mg",
    secondaryNames = adminMainSecondaryNames,
    description = "MuteGams管理指令",
) {

    @SubCommand("reload", "重载")
    @Description("重载本插件的配置项")
    suspend fun reload(sender: CommandSender) {
        reloadPlugin()
        sender.sendMessage("重载完成")
    }

    @SubCommand("rouletteStop", "停止轮盘")
    @Description("停止本群的俄罗斯轮盘")
    suspend fun rouletteStop(sender: CommandSender) {
        if (sender is GroupAwareCommandSender) {
            if (rouletteMap[sender.group.id] != null && !rouletteMap[sender.group.id]!!.isFinished()) {
                rouletteMap[sender.group.id]!!.endByAdmin()
                sender.sendMessage("已强制结束轮盘")
            } else {
                sender.sendMessage("没有进行中的轮盘")
            }
        }
    }

    @SubCommand("dualStop", "停止决斗")
    @Description("停止本群的决斗")
    suspend fun dualStop(sender: CommandSender) {
        if (sender is GroupAwareCommandSender) {
            if (duelMap[sender.group.id] != null && !duelMap[sender.group.id]!!.isFinished()) {
                duelMap[sender.group.id]!!.endByAdmin()
                sender.sendMessage("已强制结束决斗")
            } else {
                sender.sendMessage("没有进行中的决斗")
            }
        }
    }
}