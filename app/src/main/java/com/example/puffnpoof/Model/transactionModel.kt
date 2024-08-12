package com.example.puffnpoof.Model

data class transactionModel(
    var transactionID: Int? = null,
    var userID: Int? = null,
    var dollID: Int? = null,
    var transactionQuantity: Int? = null,
    var transactionDate: String? = null
):java.io.Serializable

//data class transactionModel(
//    val transactionID: Int,
//    val userID: Int,
//    val dollID: Int,
//    var transactionQuantity: Int,
//    val transactionDate: String
//)
