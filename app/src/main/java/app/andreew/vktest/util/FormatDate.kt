package app.andreew.vktest.util

import java.text.SimpleDateFormat

class FormatDate {
    companion object {
        fun format(string: String): String {
            //2019-02-13T03:04:27Z
            val inputFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val outputFormat: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy' в 'HH:mm:ss")
            val date = inputFormat.parse(string)
            return outputFormat.format(date)
        }
    }
}