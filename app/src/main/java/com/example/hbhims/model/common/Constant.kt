package com.example.hbhims.model.common

class Constant {
    companion object {
        private const val HOST = "http://47.100.91.123:415"
        private const val USER = "/user"
        private const val SPORT = "/sport"
        private const val SLEEP = "/sleep"

        const val USER_LOGIN = "$HOST$USER/login"
        const val CAPTCHA_IMAGE = "$HOST/captchaImage"

        const val SPORT_QUERY = "$HOST$SPORT/query"
        const val SPORT_QUERY_BETWEEN = "$HOST$SPORT/queryBetween"
        const val SPORT_INSERT = "$HOST$SPORT/insert"

        const val SLEEP_QUERY = "$HOST$SLEEP/query"
        const val SLEEP_QUERY_BY_DATE = "$HOST$SLEEP/queryByDate"
        const val SLEEP_INSERT_OR_REPLACE = "$HOST$SLEEP/insertOrReplace"
    }
}