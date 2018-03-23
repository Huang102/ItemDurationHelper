package com.kisen.itemdurationhelper.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * description: ${TODO}
 * author: KisenHuang
 * date: 2018/3/23 10:42
 * version: ${VERSION}
 */
object TimeUtil {

    @SuppressLint("SimpleDateFormat")
    private fun getSimple(format: String): SimpleDateFormat {
        return SimpleDateFormat(format)
    }

    fun formatTime(time: Long, format: String): String? = getSimple(format).format(Date(time))
}