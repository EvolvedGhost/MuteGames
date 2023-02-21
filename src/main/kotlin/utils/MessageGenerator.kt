package com.evolvedghost.utils

import net.mamoe.mirai.message.code.MiraiCode.deserializeMiraiCode
import net.mamoe.mirai.message.data.MessageChain

fun messageGenerator(text: String, keywords: Array<String>, targets: Array<String>): MessageChain {
    return messageGenerator(messageGeneratorString(text, keywords, targets))
}

fun messageGenerator(text: String): MessageChain {
    return text.deserializeMiraiCode()
}

fun messageGeneratorString(text: String, keywords: Array<String>, targets: Array<String>): String {
    return if (keywords.size == targets.size) {
        var finalText = text
        for (i in keywords.indices) {
            finalText = finalText.replace(keywords[i], targets[i])
        }
        finalText
    } else "错误"
}