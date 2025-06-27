package com.example.familytree.Database

import android.R
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.ui.platform.LocalContext
import com.example.familytree.MainPage
import com.example.familytree.StaticStorage

class DatabaseConnectClass(context: Context) : SQLiteOpenHelper(
    context, "TreeDatabase.db", null, 1
) {

    private val db_reader = readableDatabase
    private val db_creater = writableDatabase


    override fun onCreate(
        db: SQLiteDatabase
    ) {
        db.execSQL(
            """
            Create table IF NOT EXISTS Users(
            user_id int primary key not null,
            user_login text not null,
            user_password text not null)
        """.trimIndent()
        )
        db.execSQL(
            """
            Create table IF NOT EXISTS TreeContainer(
            tree_id int primary key not null,
            tree_name text not null,
            tree_body text not null,
            tree_owner int not null,
            foreign key (tree_owner) references Users(user_id) on update cascade
            );
        """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS Users")
        onCreate(db)
    }

    fun getUserAccountID(
        login: String,
        password: String
    ): Int? {
        // ID для авторизации пользователя
        var signInUserCurrentId: Int = -1
        // val db_reader = readableDatabase
        val cursor: Cursor = db_reader.rawQuery(
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
            StaticStorage.setId(signInUserCurrentId)
        }

        return signInUserCurrentId
    }

    fun getBiggestId(
        tableName: String,
        columnName: String
    ): Int {
        // ID для новой записи
        var theBiggestTableIdForNewRow: Int = 1
        // val db_reader = readableDatabase
        val cursor: Cursor = db_reader.rawQuery(
            """
            SELECT $columnName
            FROM $tableName
            Order by $columnName ASC
        """.trimIndent(), null
        )

        if (cursor.moveToFirst()) {
            do {
                theBiggestTableIdForNewRow = cursor.getInt(0).toInt()
            } while (cursor.moveToNext())
        }
        cursor.close()
        println("last ID = $theBiggestTableIdForNewRow")

        return theBiggestTableIdForNewRow + 1
    }

    fun createUserAccount(
        userLogin: String,
        userPassword: String
    ): Boolean {
        try {
            println("create account")
            // var db_creater = writableDatabase
            // Готовый ID для нового аккаунта
            var newAccountId: Int = getBiggestId("Users", "user_id")
            db_creater.execSQL(
                """
            INSERT INTO Users
            VALUES (${newAccountId}, '$userLogin', '$userPassword')
        """.trimIndent()
            )

            // После создания аккаунта - поместить ID в статический класс
            StaticStorage.setId(newAccountId)
            return true

        } catch (e: Exception) {

            println("ОШИБКА createUserAccount :: $e")
            return false
        }


    }

    fun getNewAccountExistStatus(
        newLogin: String,
        newPassword: String
    ): Boolean {
        val users = mutableListOf<String>()
        // val db_reader = readableDatabase
        val cursor: Cursor = db_reader.rawQuery(
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

    // Получение списка имен уже созданнх деревьев
    fun getUserTreeNames(
        newNameForTree: String
    ): Boolean {
        var treeName: String? = ""
        var activeUserId: Int? = StaticStorage.getId()
        var cursor: Cursor = db_reader.rawQuery(
            """
            SELECT *
            FROM TreeContainer
            WHERE tree_owner = $activeUserId
                AND tree_name = '$newNameForTree'
        """.trimIndent(), null
        )
        if (cursor.moveToFirst()) {
            do {
                treeName = cursor.getString(0)
            } while (cursor.moveToNext())
        }
        cursor.close()
        println("name of tree not exist status: ${treeName == ""}")

        return (treeName == "")
    }

    // функция для создания нового имени для древа
    fun createNewTree(
        newTreeName: String
    ) {
        println(
            """
            INSERT INTO TreeContainer
            VALUES(${
                getBiggestId(
                    "TreeContainer",
                    "tree_id"
                )
            }, '$newTreeName', '{}', ${StaticStorage.getId()})
        """
        )
        db_creater.execSQL(
            """
            INSERT INTO TreeContainer
            VALUES(${
                getBiggestId(
                    "TreeContainer",
                    "tree_id"
                )
            }, '$newTreeName', '{}', ${StaticStorage.getId()})
        """.trimIndent()
        )
    }

    // Получение списка деревьев пользователя
    fun getUserTreesArray(): List<List<Any>> {
        val treesArray = mutableListOf<List<Any>>()
        val cursor: Cursor = db_reader.rawQuery(
            """
                select *
                from TreeContainer
                where tree_owner = ${StaticStorage.getId()}
            """.trimIndent(), null
        )
        if (cursor.moveToFirst()) {
            do {
                var treeId = cursor.getInt(0)
                var treeName = cursor.getString(1)
                val treeIconRes = when {
                    cursor.getInt(0) % 5 == 0 -> com.example.familytree.R.drawable.tree_5
                    cursor.getInt(0) % 3 == 0 -> com.example.familytree.R.drawable.tree_3
                    cursor.getInt(0) % 2 == 0 -> com.example.familytree.R.drawable.tree_2
                    else -> com.example.familytree.R.drawable.tree_absolute
                }

                treesArray.add(listOf(treeId, treeName, treeIconRes))
            } while (cursor.moveToNext())
        }
        cursor.close()

        return treesArray
    }


    // Удаление древа из БД
    fun deleteUserTreeFromTableByIdNumber(
        idNumber: Int
    ) {
        db_creater.execSQL("""
            DELETE FROM TreeContainer
            WHERE tree_id = $idNumber
        """.trimIndent())
    }

    // Определение наличия людей в древе
    fun detectIsTreeEmpty() : Boolean{
        var treeIdNumber: Int? = StaticStorage.getTreeId()
        var userIdNumber: Int? = StaticStorage.getId()
        var treeBodyData: String = "{}"
        var cursor: Cursor = db_reader.rawQuery(
            """
                select tree_body
                from TreeContainer
                Where tree_owner = $userIdNumber
                    and
                    tree_id = $treeIdNumber
            """.trimIndent(), null
        )
        if (cursor.moveToFirst()) {
            treeBodyData = cursor.getString(0)
        }
        cursor.close()

        println("Тело древа: $treeBodyData")

        return (treeBodyData == "{}")
    }

    // Получения древа
    fun getTreeBody() : String{
        var treeIdNumber: Int? = StaticStorage.getTreeId()
        var userIdNumber: Int? = StaticStorage.getId()
        var treeBodyData: String = "{}"
        var cursor: Cursor = db_reader.rawQuery(
            """
                select tree_body
                from TreeContainer
                Where tree_owner = $userIdNumber
                    and
                    tree_id = $treeIdNumber
            """.trimIndent(), null
        )
        if (cursor.moveToFirst()) {
            treeBodyData = cursor.getString(0)
        }
        cursor.close()

        println("Тело древа: $treeBodyData")

        return treeBodyData
    }

    // Обновление tree_body
    fun UpdateTreeBody(newTreeBodyValue: String) {
        var treeIdNumber: Int? = StaticStorage.getTreeId()
        db_creater.execSQL("""
            UPDATE  TreeContainer
            SET tree_body = '$newTreeBodyValue'
            WHERE tree_id = $treeIdNumber
        """.trimIndent())
    }
}