package com.evolvedghost.utils

import com.evolvedghost.MuteGames.reload
import com.evolvedghost.MuteGames.save
import com.evolvedghost.admin.AdminConfig
import com.evolvedghost.admin.adminInit
import com.evolvedghost.banme.BanMeConfig
import com.evolvedghost.banme.banMeInit
import com.evolvedghost.blackjack.BlackjackConfig
import com.evolvedghost.blackjack.blackjackInit
import com.evolvedghost.duel.DuelConfig
import com.evolvedghost.duel.duelInit
import com.evolvedghost.roulette.RouletteConfig
import com.evolvedghost.roulette.rouletteInit

fun reloadPlugin() {
    AdminConfig.reload()
    adminInit()
    AdminConfig.save()
    BanMeConfig.reload()
    banMeInit()
    BanMeConfig.save()
    DuelConfig.reload()
    duelInit()
    DuelConfig.save()
    RouletteConfig.reload()
    rouletteInit()
    RouletteConfig.save()
    BlackjackConfig.reload()
    blackjackInit()
    BlackjackConfig.save()
}