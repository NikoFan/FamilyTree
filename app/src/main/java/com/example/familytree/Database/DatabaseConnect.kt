package com.example.familytree.Database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseConnectClass(context: Context) : SQLiteOpenHelper(
    context, "TreeDatabase.db", null, 1
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "" +
                    "Create table Users(" +
                    "user_id int primary key not null," +
                    "user_login text not null," +
                    "user_password text not null)"
        )

        db.execSQL("""
            Create table TreeContainer(
            tree_id int primary key not null,
            tree_name text not null,
            tree_body text not null,
            tree_owner int not null,
            foreign key (tree_owner) references Users(user_id) on update cascade
            );
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Users")
        onCreate(db)
    }

    fun getUserAccountID(login: String, password: String): List<String> {
        val users = mutableListOf<String>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            """
            SELECT user_id
            FROM Users
            WHERE user_login = '$login'
            AND
            user_password = '$password'
        """.trimIndent(), null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                users.add("ID: $id")
            } while (cursor.moveToNext())
        }
        cursor.close()
        println("User list data: $users")
        return users
    }

    fun getNewAccountExistStatus(newLogin: String, newPassword: String) : Boolean{
        val users = mutableListOf<String>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            """
            SELECT user_id
            FROM Users
            WHERE user_login = '$newLogin'
            AND
            user_password = '$newPassword'
        """.trimIndent(), null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                users.add("ID: $id")
            } while (cursor.moveToNext())
        }
        cursor.close()
        println("User list data: $users")

        return (users.size == 0)
    }
}