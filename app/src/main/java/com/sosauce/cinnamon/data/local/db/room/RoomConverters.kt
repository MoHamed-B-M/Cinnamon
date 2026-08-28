package com.sosauce.cinnamon.data.local.db.room

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class RoomConverters {

    @TypeConverter
    fun convertListToString(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun convertStringToList(string: String): List<String> {
        return Json.Default.decodeFromString(string)
    }

    @TypeConverter
    fun convertListIntToString(list: List<Int>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun convertStringToListInt(string: String): List<Int> {
        return Json.Default.decodeFromString(string)
    }
}