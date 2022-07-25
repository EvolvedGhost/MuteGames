package com.evolvedghost.utils

fun timeFormatter(second: Int): String {
    val sb = java.lang.StringBuilder()
    var time = second
    val day = time / 86400
    time -= day * 86400
    if (day > 0) {
        sb.append(day)
        sb.append("天")
    }
    val hour = time / 3600
    time -= hour * 3600
    if (day > 0 || hour > 0) {
        sb.append(hour)
        sb.append("时")
    }
    val minute = time / 60
    time -= minute * 60
    if (day > 0 || hour > 0 || minute > 0) {
        sb.append(minute)
        sb.append("分")
    }
    sb.append(time)
    sb.append("秒")
    return sb.toString()
}