package com.example.familytree

import android.app.Application

class MyApp : Application() {

    companion object {
        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Здесь можно инициализировать БД, библиотеки и т.д.
        // Например:
        // Room.databaseBuilder(this, AppDatabase::class.java, "database.db").build()
    }
}