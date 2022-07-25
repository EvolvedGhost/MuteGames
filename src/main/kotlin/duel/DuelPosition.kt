package com.evolvedghost.duel

import com.evolvedghost.utils.debugLogger

class DuelPosition(rand: Int) {
    private var position = String()
    private var mute = -1

    init {
        debugLogger("随机数判断挑战者受伤部位中，随机值为$rand")
        for (i in positionChanceList.indices) {
            if (i == 0 && rand > 0 && rand <= positionChanceList[0]) {
                position = positionNameList[0]
                mute = positionMuteList[0]
                debugLogger("随机数判断挑战者受伤部位中，判断在第 1 个部位")
                break
            } else if (i != 0 && i < positionChanceList.size && rand > positionChanceList[i - 1] && rand <= positionChanceList[i]) {
                position = positionNameList[i]
                mute = positionMuteList[i]
                debugLogger("随机数判断挑战者受伤部位中，判断在第 ${i + 1} 个部位")
                break
            }
        }
    }

    fun getPosition(): String {
        return position
    }

    fun getMute(): Int {
        return mute
    }
}