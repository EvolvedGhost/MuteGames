package com.evolvedghost.utils

import com.evolvedghost.MuteGames
import com.evolvedghost.admin.AdminConfig.debug
import net.mamoe.mirai.utils.info

fun debugLogger(message: String) {
    if (debug) {
        MuteGames.logger.info { message }
    }
}