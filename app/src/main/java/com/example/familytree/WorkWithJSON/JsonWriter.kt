package com.example.familytree.WorkWithJSON

import androidx.compose.ui.platform.LocalContext
import org.json.JSONObject
import com.example.familytree.Database.DatabaseConnectClass

class JsonWriter {

    // Конвертер строки в JSON объект
    fun StringToJsonConverter(stringValue: String): JSONObject {
        return JSONObject(stringValue)
    }

    // Подсчет колчиества ID в персонах
    fun countIds(jsonString: String, objectStringName: String): Int {
        return try {
            val jsonObject = JSONObject(jsonString)
            val personsObject = jsonObject.getJSONObject(objectStringName)
            personsObject.length() + 1 // Возвращает количество ключей в объекте
        } catch (e: Exception) {
            println("Ошибка при парсинге JSON: ${e.message}")
            1
        }
    }

    // Создание стартового JSON с 1 группами
    fun CreateStartJSONView(
        jsonStringBefore: String,
        inputPersonData: List<String>,
        lineGroupId: Int = -1,
        underGroupId: Int = -1,
        lineGroupCreateApprove: Boolean = false,
        underGroupCreateApprove: Boolean = false

    ): String {
        // Исходная JSON строка
        val emptyJson = jsonStringBefore
        var newPersonId: Int = countIds(
            emptyJson,
            "persons"
        )


        // Создание основного JSON объект
        val mainObject = StringToJsonConverter(emptyJson)

        // Создание объекта persons с вложенными данными
        val personsObject = JSONObject().apply {
            put("ID: $newPersonId", JSONObject().apply {
                put("name", inputPersonData[0])
                put("full-name", inputPersonData[1])
                put("sex", inputPersonData[2])
                put("birthday", inputPersonData[3])
                put("underGroupId-(FK)", underGroupId)
                put("lineGroupId-(FK)", lineGroupId)
                put("fatherId-(FK)", -1)
                put("motherId-(FK)", -1)
            })
        }

        // Добавление полей
        mainObject.put("persons", personsObject)
        mainObject.put("lineGroup", JSONObject())
        mainObject.put("underGroup", JSONObject())

        println(mainObject.toString(2))

        return mainObject.toString(2)

    }
}