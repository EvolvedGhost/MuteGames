package com.evolvedghost.banme

import com.evolvedghost.MuteGames
import com.evolvedghost.banme.BanMeConfig.banMeTime
import com.evolvedghost.banme.BanMeConfig.banMeTimeMax
import com.evolvedghost.banme.BanMeConfig.messageBanMe
import com.evolvedghost.banme.BanMeConfig.randomBanMe
import com.evolvedghost.utils.checkPermit
import com.evolvedghost.utils.messageGenerator
import com.evolvedghost.utils.timeFormatter
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.at
import kotlin.random.Random

object BanMeCommand : SimpleCommand(
    MuteGames,
    primaryName = "banMe",
    secondaryNames = BanMeSecondaryNames,
    description = "自裁指令",
) {
    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true

    @Handler
    suspend fun banMe(sender: CommandSender) {
        val permit = checkPermit(sender)
        if (permit.canReturn) return
        val target = permit.target ?: return
        val isTargetAdmin = permit.isTargetAdmin ?: return
        val mute = if (randomBanMe) {
            Random(System.currentTimeMillis()).nextInt(banMeTimeMax - banMeTime + 1) + banMeTime
        } else {
            banMeTime
        }
        if (!isTargetAdmin) target.mute(mute)
        sender.sendMessage(
            messageGenerator(
                messageBanMe, arrayOf("<target>", "<mute-s>", "<mute-f>"),
                arrayOf(
                    target.at().serializeToMiraiCode(),
                    mute.toString(),
                    timeFormatter(mute),
                )
            )
        )


    }
}