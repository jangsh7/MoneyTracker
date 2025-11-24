package com.jangsh.moneytracker

import java.time.LocalDate

data class Transaction(
    val id: Long,
    val type: TransactionType,
    val category: String,
    val amount: Int,
    val memo: String,
    val date: LocalDate
)
