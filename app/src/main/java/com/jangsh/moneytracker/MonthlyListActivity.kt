package com.jangsh.moneytracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class MonthlyListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_list)

        // 액션바 홈(뒤로가기) 버튼 + 제목
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)   // 왼쪽 위 화살표 켜기
            setDisplayShowHomeEnabled(true)   // 홈 아이콘 표시
            title = "이번 달 거래 내역"
        }

        recyclerView = findViewById(R.id.recycler_transactions)
        adapter = TransactionAdapter(emptyList())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadData()
    }

    private fun loadData() {
        val today = LocalDate.now()
        val list = TransactionRepository.getTransactionsForMonth(
            year = today.year,
            month = today.monthValue
        )
        adapter.submitList(list)
    }

    // 액션바 홈 버튼 동작(뒤로가기)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
