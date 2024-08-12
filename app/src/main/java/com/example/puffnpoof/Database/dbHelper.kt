package com.example.puffnpoof.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.puffnpoof.Model.dollModel
import com.example.puffnpoof.Model.transactionModel
import com.example.puffnpoof.Model.userModel

class dbHelper(context: Context) : SQLiteOpenHelper(context, "puffnpoof", null, 2) {
    override fun onCreate(db: SQLiteDatabase?) {
        val queryCreateUser = """
            CREATE TABLE IF NOT EXISTS Users(
                userID INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT,
                password TEXT,
                email TEXT,
                phoneNumber TEXT,
                gender TEXT
            )
        """


        val queryCreateDoll = """
            CREATE TABLE IF NOT EXISTS Dolls (
                dollID INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                price INTEGER,
                size TEXT,
                description TEXT,
                imageLink TEXT,
                rating REAL
            )
        """

        val queryCreateTransaction = """
            CREATE TABLE IF NOT EXISTS Transactions (
                transactionID INTEGER PRIMARY KEY AUTOINCREMENT,
                userID INTEGER,
                dollID INTEGER,
                transactionQuantity INTEGER,
                transactionDate TEXT
            )
        """


        db?.execSQL(queryCreateUser)
        db?.execSQL(queryCreateDoll)
        db?.execSQL(queryCreateTransaction)
        Log.d("dbHelper", "Users table created")
    }

    fun insertUsers(user: userModel){
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", user.username)
            put("password", user.password)
            put("email", user.email)
            put("phoneNumber", user.phoneNumber)
            put("gender", user.gender)
        }
        val newRowId = db.insert("Users", null, values)
        if (newRowId == -1L) {
            Log.e("dbHelper", "Error inserting user: ${user.username}")
        } else {
            Log.d("dbHelper", "User inserted: ${user.username} with userID: $newRowId")
        }
        db.close()
    }

    fun insertDoll(dolls : dollModel){
        val db= writableDatabase
        val values= ContentValues().apply {
            put("name", dolls.name)
            put("price",dolls.price)
            put("size", dolls.size)
            put("description", dolls.description)
            put("imageLink", dolls.imageLink)
            put("rating", dolls.rating)
        }
        val newRowId = db.insert("Dolls",null,values)
        if (newRowId == -1L) {
            Log.e("dbHelper", "Error inserting doll : ${dolls.name}")
        } else {
            Log.d("dbHelper", "Doll inserted: ${dolls.name} with userID: $newRowId")
        }
        db.close()
    }

    fun insertTransaction(transaction: transactionModel){
        val db = writableDatabase
        val values = ContentValues().apply {
            put("userID", transaction.userID)
            put("dollID", transaction.dollID)
            put("transactionQuantity", transaction.transactionQuantity)
            put("transactionDate", transaction.transactionDate)
        }
        val newRowId = db.insert("Transactions", null, values)
        if (newRowId == -1L) {
            Log.e("dbHelper", "Error inserting transaction with dollID : ${transaction.dollID}")
        } else {
            Log.d("dbHelper", "Transaction inserted: ${transaction.dollID} with userID: $newRowId")
        }
        db.close()
    }

    fun getUsers(): ArrayList<userModel> {
        val users = ArrayList<userModel>()
        val db = readableDatabase
        val query = "SELECT * FROM Users"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val userID = cursor.getInt(cursor.getColumnIndexOrThrow("userID"))
                val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
                val gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"))

                val user = userModel(userID, username, password, email, phoneNumber, gender)
                users.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        Log.d("dbHelper", "Fetched ${users.size} users")
        return users
    }

    fun editTransaction(ts: transactionModel){
        val db=writableDatabase
        val values= ContentValues().apply {
            put("transactionQuantity",ts.transactionQuantity)
        }
        db.update("Transactions", values,"transactionID = ?", arrayOf(ts.transactionID.toString()))
        db.close()
    }

    fun deleteTransaction( id:String){
        val db=writableDatabase
        db.delete("Transactions","transactionID =?", arrayOf(id) )
        db.close()
    }

    fun getdollname(dollid:Int?): String{
        var name = ""
        val db = readableDatabase
        val query = "SELECT * FROM Dolls WHERE dollID = ?"
        val cursor = db.rawQuery(query, arrayOf(dollid.toString()))
        cursor.moveToFirst()

        if(cursor.count>0){
            do{
                val doll=dollModel()
                doll.dollID =cursor.getInt(cursor.getColumnIndexOrThrow("dollID"))
                doll.name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                name= doll.name.toString()

            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return name
    }

    fun getTransaction(userid:Int): ArrayList<transactionModel>{
        val trans=ArrayList<transactionModel>()
        val db=readableDatabase
        val query = "SELECT * FROM Transactions WHERE userID = ?"
        val cursor = db.rawQuery(query, arrayOf(userid.toString()))
        cursor.moveToFirst()

        if(cursor.count>0){
            do{
                val transaction=transactionModel()
                transaction.dollID =cursor.getInt(cursor.getColumnIndexOrThrow("dollID"))
                transaction.transactionID =cursor.getInt(cursor.getColumnIndexOrThrow("transactionID"))
                transaction.userID =cursor.getInt(cursor.getColumnIndexOrThrow("userID"))
                transaction.transactionQuantity =cursor.getInt(cursor.getColumnIndexOrThrow("transactionQuantity"))
                transaction.transactionDate = cursor.getString(cursor.getColumnIndexOrThrow("transactionDate"))
                trans.add(transaction)

            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return trans
    }

    fun getDoll() :ArrayList<dollModel>{
        val dolls = ArrayList<dollModel>()
        val db = readableDatabase
        val query ="SELECT * FROM Dolls"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count>0){
            do{
                val doll=dollModel()
                doll.dollID = cursor.getInt(cursor.getColumnIndexOrThrow("dollID"))
                doll.name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                doll.price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
                doll.size = cursor.getString((cursor.getColumnIndexOrThrow("size")))
                doll.description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                doll.imageLink = cursor.getString(cursor.getColumnIndexOrThrow("imageLink"))
                doll.rating = cursor.getDouble(cursor.getColumnIndexOrThrow("rating"))
                dolls.add(doll)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return dolls
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Users")
        db?.execSQL("DROP TABLE IF EXISTS Dolls")
        db?.execSQL("DROP TABLE IF EXISTS Transactions")
        onCreate(db)
        Log.d("dbHelper", "Database upgraded from version $oldVersion to $newVersion")
    }

//    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        db?.execSQL("DROP TABLE IF EXISTS Users")
//        onCreate(db)
//        Log.d("dbHelper", "Database upgraded from version $oldVersion to $newVersion")
//    }

}