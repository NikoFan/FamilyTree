package com.example.familytree

object StaticStorage {

    private var activeUserCurrentId: Int? = null
    private var activeTreeIdNumber: Int? = null
    private var activePersonJsonIdNumber: Int? = 1

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

    fun setPersonId(newPersonIdNumber: Int) {
        activePersonJsonIdNumber = newPersonIdNumber
    }

    fun getPersonId(): Int? {
        return activePersonJsonIdNumber
    }


}