package com.example.familytree

object StaticStorage {

    private var activeUserCurrentId: Int? = null
    private var activeTreeIdNumber: Int? = null

    fun setId(userId: Int) {
        activeUserCurrentId = userId
    }

    fun getId(): Int? {
        return activeUserCurrentId
    }

    fun setTreeId(newTreeIdNumber: Int) {
        activeTreeIdNumber = newTreeIdNumber
    }

    fun getTreeId(): Int? {
        return activeTreeIdNumber
    }
}