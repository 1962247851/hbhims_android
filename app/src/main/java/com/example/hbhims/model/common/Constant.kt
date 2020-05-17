package com.example.hbhims.model.common

class Constant {
    companion object {
        private const val HOST = "http://47.100.91.123:415"

        private const val QUERY = "/query"
        private const val QUERY_BY_DATE = "/queryByDate"
        private const val QUERY_RECEIVE = "/queryReceive"
        private const val QUERY_SEND = "/querySend"
        private const val INSERT = "/insert"
        private const val DELETE = "/delete"
        private const val INSERT_OR_REPLACE = "/insertOrReplace"
        private const val DOWNLOAD = "/download"
        private const val UPLOAD = "/upload"

        private const val USER = "/user"
        private const val ROLE = "/role"
        private const val USER_ROLE_RELATION = "/userRoleRelation"
        private const val SPORT = "/sport"
        private const val SLEEP = "/sleep"
        private const val HEIGHT = "/height"
        private const val WEIGHT = "/weight"
        private const val BLOOD_PRESSURE = "/bloodPressure"
        private const val BLOOD_SUGAR = "/bloodSugar"
        private const val BLOOD_OXYGEN = "/bloodOxygen"
        private const val MEDICAL_SUGGESTION = "/medicalSuggestion"
        private const val MEDICAL_SUGGESTION_EVALUATION = "/medicalSuggestionEvaluation"
        private const val MEDICAL_SUGGESTION_REQUEST = "/medicalSuggestionRequest"
        private const val FILE = "/file"

        const val USER_LOGIN = "$HOST$USER/login"
        const val USER_LOGOUT = "$HOST$USER/logout"
        const val USER_QUERY = "$HOST$USER$QUERY"
        const val USER_REGISTER = "$HOST$USER/register"
        const val USER_QUERY_ALL_PROFESSIONAL = "$HOST$USER/queryProfessional"
        const val USER_UPDATE = "$HOST$USER/modify"

        const val ROLE_QUERY = "$HOST$ROLE$QUERY"

        const val USER_ROLE_RELATION_QUERY_BY_USER_ID_AND_ROLE_ID =
            "$HOST$USER_ROLE_RELATION/queryByUserIdAndRoleId"
        const val USER_ROLE_RELATION_INSERT = "$HOST$USER_ROLE_RELATION$INSERT"

        const val CAPTCHA_IMAGE = "$HOST/captchaImage"

        const val SPORT_QUERY = "$HOST$SPORT$QUERY"
        const val SPORT_QUERY_BY_DATE = "$HOST$SPORT$QUERY_BY_DATE"
        const val SPORT_INSERT_OR_REPLACE = "$HOST$SPORT$INSERT_OR_REPLACE"

        const val SLEEP_QUERY = "$HOST$SLEEP$QUERY"
        const val SLEEP_QUERY_BY_DATE = "$HOST$SLEEP$QUERY_BY_DATE"
        const val SLEEP_INSERT_OR_REPLACE = "$HOST$SLEEP$INSERT_OR_REPLACE"

        const val HEIGHT_QUERY = "$HOST$HEIGHT$QUERY"
        const val HEIGHT_INSERT_OR_REPLACE = "$HOST$HEIGHT$INSERT_OR_REPLACE"

        const val WEIGHT_QUERY = "$HOST$WEIGHT$QUERY"
        const val WEIGHT_INSERT = "$HOST$WEIGHT$INSERT"

        const val BLOOD_PRESSURE_QUERY = "$HOST$BLOOD_PRESSURE$QUERY"
        const val BLOOD_PRESSURE_INSERT = "$HOST$BLOOD_PRESSURE$INSERT"

        const val BLOOD_SUGAR_QUERY = "$HOST$BLOOD_SUGAR$QUERY"
        const val BLOOD_SUGAR_INSERT = "$HOST$BLOOD_SUGAR$INSERT"

        const val BLOOD_OXYGEN_QUERY = "$HOST$BLOOD_OXYGEN$QUERY"
        const val BLOOD_OXYGEN_INSERT = "$HOST$BLOOD_OXYGEN$INSERT"

        const val MEDICAL_SUGGESTION_INSERT = "$HOST$MEDICAL_SUGGESTION$INSERT"
        const val MEDICAL_SUGGESTION_QUERY_RECEIVE = "$HOST$MEDICAL_SUGGESTION$QUERY_RECEIVE"
        const val MEDICAL_SUGGESTION_QUERY_SEND = "$HOST$MEDICAL_SUGGESTION$QUERY_SEND"

        const val MEDICAL_SUGGESTION_EVALUATION_INSERT =
            "$HOST$MEDICAL_SUGGESTION_EVALUATION$INSERT"

        const val MEDICAL_SUGGESTION_REQUEST_INSERT = "$HOST$MEDICAL_SUGGESTION_REQUEST$INSERT"
        const val MEDICAL_SUGGESTION_REQUEST_DELETE = "$HOST$MEDICAL_SUGGESTION_REQUEST$DELETE"
        const val MEDICAL_SUGGESTION_REQUEST_QUERY_SEND =
            "$HOST$MEDICAL_SUGGESTION_REQUEST$QUERY_SEND"
        const val MEDICAL_SUGGESTION_REQUEST_QUERY_RECEIVE =
            "$HOST$MEDICAL_SUGGESTION_REQUEST$QUERY_RECEIVE"

        const val FILE_DOWNLOAD = "$HOST$FILE$DOWNLOAD"
        const val FILE_UPLOAD = "$HOST$FILE$UPLOAD"
        const val FILE_UPLOAD_PATH = "/opt/hbhims/healthPictures/"
    }
}