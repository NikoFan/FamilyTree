package com.example.familytree

data class StaticStorage(val empty: String? = null){

    private var activeUserCurrentId: Int? = null

    fun setId(userId: Int) {
        activeUserCurrentId = userId
    }

    fun getId() : Int?{
        return activeUserCurrentId
    }
}