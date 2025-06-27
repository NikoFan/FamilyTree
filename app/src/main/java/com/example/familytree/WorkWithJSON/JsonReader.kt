package com.example.familytree.WorkWithJSON

import com.example.familytree.StaticStorage
import org.json.JSONObject

object JsonReader {

    // Конвертер строки в JSON объект
    fun StringToJsonConverter(stringValue: String): JSONObject {
        return JSONObject(stringValue)
    }

    // Получение гендера персоны
    fun GetActivePersonGender(
        treeBodyJSONString: String
    ): String {


        println("PERSON ID: --- ${StaticStorage.getPersonId()}\n")
        val mainObject = StringToJsonConverter(treeBodyJSONString)
        val personsObject = mainObject.getJSONObject("persons")
        val currentPersonObject = personsObject.getJSONObject("ID: ${StaticStorage.getPersonId()}")
        val personGender = currentPersonObject.getString("sex")


        return personGender
    }

    // Получение имени персоны
    fun GetActivePersonName(
        treeBodyJSONString: String
    ): String {

        val mainObject = StringToJsonConverter(treeBodyJSONString)
        val personsObject = mainObject.getJSONObject("persons")
        val currentPersonObject = personsObject.getJSONObject("ID: ${StaticStorage.getPersonId()}")
        val personGender = currentPersonObject.getString("name")


        return personGender
    }
}