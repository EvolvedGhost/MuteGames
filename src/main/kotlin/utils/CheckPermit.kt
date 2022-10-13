package com.evolvedghost.utils

import com.evolvedghost.admin.AdminConfig
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.GroupAwareCommandSender
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.contact.isOwner


data class Permit(
    var canReturn: Boolean,
    var isTargetAdmin: Boolean,
    var group: Group? = null,
    var target: Member? = null
)

/**
 * -1 -> return
 * 1 -> true
 * 0 -> false
 */
fun checkPermit(sender: CommandSender): Permit {
    if (sender !is GroupAwareCommandSender) return Permit(true, false)
    val group = sender.group
    val bot = group.botAsMember
    val target = group.members[sender.user.id]
    if (target == null || !bot.isOperator()) return Permit(true, false)
    val isTargetAdmin = if (bot.isOwner()) false
    else target.isOperator()
    if (isTargetAdmin && !AdminConfig.canAdminPlayIt) return Permit(true, isTargetAdmin)
    return Permit(false, isTargetAdmin, group, target)
}