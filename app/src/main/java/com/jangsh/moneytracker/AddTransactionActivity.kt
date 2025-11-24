package com.jangsh.moneytracker

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var root: LinearLayout
    private lateinit var editAmount: EditText
    private lateinit var editMemo: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var radioGroupType: RadioGroup
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var btnPickDate: Button

    private var selectedDate: LocalDate = LocalDate.now()
    private val dateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        // 액션바에 홈(뒤로가기) 버튼 표시
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "거래 추가"

        root = findViewById(R.id.layout_root)
        editAmount = findViewById(R.id.edit_amount)
        editMemo = findViewById(R.id.edit_memo)
        spinnerCategory = findViewById(R.id.spinner_category)
        radioGroupType = findViewById(R.id.radio_group_type)
        btnSave = findViewById(R.id.button_save)
        btnCancel = findViewById(R.id.button_cancel)
        btnPickDate = findViewById(R.id.button_pick_date)

        // 카테고리 스피너(예시 값)
        val categories = listOf("식비", "교통", "생활", "문화", "기타")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerCategory.adapter = adapter

        // 초기 날짜 텍스트 설정
        updateDateButtonText()

        // 날짜 버튼 클릭 시 DatePickerDialog 띄우기
        btnPickDate.setOnClickListener {
            showDatePicker()
        }

        btnSave.setOnClickListener {
            onSaveClicked()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker() {
        val year = selectedDate.year
        val month = selectedDate.monthValue - 1 // DatePicker는 0부터 시작
        val day = selectedDate.dayOfMonth

        DatePickerDialog(
            this,
            { _, y, m, d ->
                // 선택된 날짜로 LocalDate 갱신
                selectedDate = LocalDate.of(y, m + 1, d)
                updateDateButtonText()
            },
            year,
            month,
            day
        ).show()
    }

    private fun updateDateButtonText() {
        btnPickDate.text = selectedDate.format(dateFormatter)
    }

    private fun onSaveClicked() {
        val amountText = editAmount.text.toString().trim()
        val memoText = editMemo.text.toString().trim()
        val selectedCategory = spinnerCategory.selectedItem.toString()
        val selectedTypeId = radioGroupType.checkedRadioButtonId

        if (amountText.isEmpty()) {
            Snackbar.make(root, "금액을 입력해주세요.", Snackbar.LENGTH_SHORT).show()
            return
        }

        val amount = try {
            amountText.toInt()
        } catch (e: NumberFormatException) {
            Snackbar.make(root, "금액은 숫자로 입력해주세요.", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (selectedTypeId == -1) {
            Snackbar.make(root, "수입/지출을 선택해주세요.", Snackbar.LENGTH_SHORT).show()
            return
        }

        val type =
            if (selectedTypeId == R.id.radio_income) TransactionType.INCOME
            else TransactionType.EXPENSE

        val tx = TransactionRepository.addTransaction(
            type = type,
            category = selectedCategory,
            amount = amount,
            memo = memoText,
            date = selectedDate      // ✅ 선택한 날짜로 저장
        )

        Snackbar.make(
            root,
            "저장되었습니다: [${if (tx.type == TransactionType.INCOME) "수입" else "지출"}] ${tx.category} ${tx.amount}원 (${tx.date})",
            Snackbar.LENGTH_SHORT
        ).show()

        // 저장 후 이전 화면(홈)으로 돌아가기
        finish()
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
