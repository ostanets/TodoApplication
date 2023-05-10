package com.ostanets.todoapp.models

enum class TimeOffset(val minuets: Int) {
    FIVE_MINUTES(5),
    FIFTEEN_MINUTES(15),
    THIRTY_MINUTES(30),
    ONE_HOUR(60),
    ONE_DAY(1440)
}