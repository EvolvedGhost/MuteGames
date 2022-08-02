package com.evolvedghost

import com.evolvedghost.admin.AdminCommand
import com.evolvedghost.banme.BanMeCommand
import com.evolvedghost.duel.DuelCommand
import com.evolvedghost.roulette.RouletteCommand
import com.evolvedghost.utils.reloadPlugin
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object MuteGames : KotlinPlugin(
    JvmPluginDescription(
        id = "com.evolvedghost.mutegames",
        name = "MuteGames",
        version = "0.0.1",
    ) {
        author("EvolvedGhost")
        info("禁言小游戏合集")
    }
) {
    override fun onEnable() {
        reloadPlugin()
        AdminCommand.register()
        BanMeCommand.register()
        DuelCommand.register()
        RouletteCommand.register()
        logger.info { "禁言游戏已加载" }
    }

    override fun onDisable() {
        reloadPlugin()
        AdminCommand.unregister()
        BanMeCommand.unregister()
        DuelCommand.unregister()
        RouletteCommand.unregister()
        logger.info { "禁言游戏已退出" }
    }
}