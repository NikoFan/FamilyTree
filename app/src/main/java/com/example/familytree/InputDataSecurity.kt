package com.example.familytree

class InputDataSecurity {

    fun ReportSQLI(inputData: List<String>): Boolean {
        for (inputString in inputData) {
            println("input string is: $inputString")
            if (
                inputString.contains(";") || // Если содержит недопустимые значения
                inputString.contains("-") ||
                inputString.contains("'") ||
                inputString.length == 0 // Если пустое
            ) return false
        }
        return true
    }
}