package com.jangsh.moneytracker

import java.time.LocalDate

object TransactionRepository {

    private val transactions = mutableListOf<Transaction>()
    private var nextId = 1L

    data class MonthlySummary(
        val totalIncome: Int,
        val totalExpense: Int,
        val expectedSaving: Int
    )

    fun addTransaction(
        type: TransactionType,
        category: String,
        amount: Int,
        memo: String,
        date: LocalDate = LocalDate.now()
    ): Transaction {
        val tx = Transaction(
            id = nextId++,
            type = type,
            category = category,
            amount = amount,
            memo = memo,
            date = date
        )
        transactions.add(tx)
        return tx
    }

    fun getTransactionsForMonth(year: Int, month: Int): List<Transaction> {
        return transactions.filter { tx ->
            tx.date.year == year && tx.date.monthValue == month
        }
    }

    fun getMonthlySummary(year: Int, month: Int): MonthlySummary {
        val list = getTransactionsForMonth(year, month)

        var income = 0
        var expense = 0

        for (tx in list) {
            if (tx.type == TransactionType.INCOME) {
                income += tx.amount
            } else {
                expense += tx.amount
            }
        }

        // 아주 단순한 로직: 예상 저축 가능 금액 = 수입 - 지출
        val saving = income - expense

        return MonthlySummary(
            totalIncome = income,
            totalExpense = expense,
            expectedSaving = saving
        )
    }
}
