package com.evolvedghost

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
        info("""A Mirai-Console Plugin""")
    }
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}