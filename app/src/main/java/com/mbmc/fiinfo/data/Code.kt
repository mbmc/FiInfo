package com.mbmc.fiinfo.data

import androidx.annotation.StringRes
import com.mbmc.fiinfo.R

/*
    "347977" // ?
    "34777" // sprint -> doesn't work
    "347986" // ?
    "34866" // tmobile
    "347987" // ?
    "34326" // USCC
    "34872" // USCC
    "3474666" // Three UK
    "347942" // ?
    "349826" // Three AT
    "346398" // Next
    "342886" // Auto
    "344636" // Info
    "34963" // repair
    "347626" // cell settings
    "3478272886" // multisim
    "34798677" // ?
    "3462646" // sprint
    "34798624" // ?
    "34243779" // ?
    "3444" // sign in
    "34284" // send report
 */
enum class Code(
    @StringRes val labelId: Int,
    val code: String
) {
    AUTO(R.string.auto, "342886"),
    INFO(R.string.info, "344636"),
    MULTI_SIM(R.string.multi_sim, "3478272886"),
    NEXT(R.string.next, "346398"),
    REPAIR(R.string.repair, "34963"),
    REPORT(R.string.report, "34284"),
    SIGN_IN(R.string.sign_in, "3444"),
    SETTINGS(R.string.settings, "347626"),
    SPRINT(R.string.sprint, "34777"),
    SPRINT_2(R.string.sprint_2, "3462646"),
    T_MOBILE(R.string.t_mobile, "34866"),
    THREE_AT(R.string.three_at, "349826"),
    THREE_UK(R.string.three_uk, "3474666"),
    US_CELLULAR(R.string.us_cellular, "34326"),
    US_CELLULAR_2(R.string.us_cellular_2, "34872");

    companion object {
        val MAIN =
            setOf(SPRINT, T_MOBILE, THREE_AT, THREE_UK, US_CELLULAR, AUTO, REPAIR, NEXT, INFO)
    }
}