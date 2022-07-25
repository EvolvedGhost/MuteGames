package com.evolvedghost.utils

import com.evolvedghost.MuteGames.reload
import com.evolvedghost.admin.AdminConfig
import com.evolvedghost.admin.adminInit
import com.evolvedghost.banme.BanMeConfig
import com.evolvedghost.banme.banMeInit
import com.evolvedghost.duel.DuelConfig
import com.evolvedghost.duel.duelInit
import com.evolvedghost.roulette.RouletteConfig
import com.evolvedghost.roulette.rouletteInit

fun reloadPlugin() {
    AdminConfig.reload()
    adminInit()
    BanMeConfig.reload()
    banMeInit()
    DuelConfig.reload()
    duelInit()
    RouletteConfig.reload()
    rouletteInit()
}