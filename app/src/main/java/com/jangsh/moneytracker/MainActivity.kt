package com.jangsh.moneytracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.card.MaterialCardView
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var textTotalIncome: TextView
    private lateinit var textTotalExpense: TextView
    private lateinit var textExpectedSaving: TextView
    private lateinit var cardMonthSummary: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addButton = findViewById<FloatingActionButton>(R.id.fab_add)

        textTotalIncome = findViewById(R.id.text_total_income)
        textTotalExpense = findViewById(R.id.text_total_expense)
        textExpectedSaving = findViewById(R.id.text_expected_saving)
        cardMonthSummary = findViewById(R.id.card_month_summary)

        // + 버튼 → 거래 추가 화면
        addButton.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

        // 요약 카드 클릭 → 월별 내역 리스트 화면
        cardMonthSummary.setOnClickListener {
            val intent = Intent(this, MonthlyListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateMonthlySummary()
    }

    private fun updateMonthlySummary() {
        val today = LocalDate.now()
        val summary = TransactionRepository.getMonthlySummary(
            year = today.year,
            month = today.monthValue
        )

        textTotalIncome.text = "${summary.totalIncome}원"
        textTotalExpense.text = "${summary.totalExpense}원"
        textExpectedSaving.text = "${summary.expectedSaving}원"
    }
}
