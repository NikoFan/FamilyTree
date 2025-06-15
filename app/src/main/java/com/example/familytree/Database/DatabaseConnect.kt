package com.example.familytree.Database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.familytree.StaticStorage

class DatabaseConnectClass(context: Context) : SQLiteOpenHelper(
    context, "TreeDatabase.db", null, 1
) {


    public var storage: StaticStorage = StaticStorage()
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            Create table Users(
            user_id int primary key not null,
            user_login text not null,
            user_password text not null)
        """.trimIndent()


        )

        db.execSQL(
            """
            Create table TreeContainer(
            tree_id int primary key not null,
            tree_name text not null,
            tree_body text not null,
            tree_owner int not null,
            foreign key (tree_owner) references Users(user_id) on update cascade
            );
        """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Users")
        onCreate(db)
    }

    fun getUserAccountID(login: String, password: String): Int? {
        // ID для авторизации пользователя
        var signInUserCurrentId: Int = -1
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
                signInUserCurrentId = cursor.getInt(0).toInt()

            } while (cursor.moveToNext())
        }
        cursor.close()
        println("User id data: $signInUserCurrentId")
        if (signInUserCurrentId != -1) {
            storage.setId(signInUserCurrentId)
        }

        return signInUserCurrentId
    }

    fun getUserBiggestId(): Int {
        // ID для нового аккаунта
        var currentUserIdToAddNewAccount: Int = 1
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            """
            SELECT user_id
            FROM Users
            Order by user_id ASC
        """.trimIndent(), null
        )

        if (cursor.moveToFirst()) {
            do {
                currentUserIdToAddNewAccount = cursor.getInt(0).toInt()
            } while (cursor.moveToNext())
        }
        cursor.close()
        println("last userID = $currentUserIdToAddNewAccount")

        return currentUserIdToAddNewAccount + 1
    }

    fun createUserAccount(userLogin: String, userPassword: String): Boolean {
        try {
            println("create account")
            var db = writableDatabase
            // Готовый ID для нового аккаунта
            var newAccountId: Int = getUserBiggestId()
            db.execSQL(
                """
            INSERT INTO Users
            VALUES (${newAccountId}, '$userLogin', '$userPassword')
        """.trimIndent()
            )

            // После создания аккаунта - поместить ID в статический класс
            storage.setId(newAccountId)
            return true

        } catch (e: Exception) {

            println("ОШИБКА createUserAccount :: $e")
            return false
        }


    }

    fun getNewAccountExistStatus(newLogin: String, newPassword: String): Boolean {
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