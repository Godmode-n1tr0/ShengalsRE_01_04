package com.bignerdranch.android.bank

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bignerdranch.android.bank.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class Kredit : AppCompatActivity() {

    private lateinit var backArrow: ImageView
    private lateinit var amountSeekBar: SeekBar
    private lateinit var selectedAmount: TextView
    private lateinit var loanTermEditText: TextInputEditText
    private lateinit var calculateButton: AppCompatButton
    private lateinit var monthlyPaymentResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kredit)

        initializeViews()
        setupEventListeners()
        updateAmountDisplay()
    }

    private fun initializeViews() {
        backArrow = findViewById(R.id.backArrow)
        amountSeekBar = findViewById(R.id.amountSeekBar)
        loanTermEditText = findViewById(R.id.loanTermEditText)
        calculateButton = findViewById(R.id.calculateButton)
        monthlyPaymentResult = findViewById(R.id.monthlyPaymentResult)
    }

    private fun setupEventListeners() {
        backArrow.setOnClickListener {
            finish()
        }

        amountSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateAmountDisplay()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        calculateButton.setOnClickListener {
            calculateMonthlyPayment()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateAmountDisplay() {
        val progress = amountSeekBar.progress
        val amount = 30000 + (progress * 1000)
        selectedAmount.text = "${amount.toLocaleString()} ₽"
    }

    @SuppressLint("SetTextI18n")
    private fun calculateMonthlyPayment() {
        val termText = loanTermEditText.text.toString().trim()

        if (termText.isEmpty()) {
            Toast.makeText(this, "Введите срок кредита", Toast.LENGTH_SHORT).show()
            return
        }

        val term = termText.toIntOrNull()
        if (term == null || term <= 0) {
            Toast.makeText(this, "Введите корректный срок кредита", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = 30000 + (amountSeekBar.progress * 1000)
        val monthlyPayment = calculateAnnuityPayment(amount, term)

        monthlyPaymentResult.text = "ежемесячный платёж: ${monthlyPayment.toLocaleString()} ₽"
    }

    private fun calculateAnnuityPayment(amount: Int, term: Int): Int {
        val annualRate = 0.10 // 10% годовых
        val monthlyRate = annualRate / 12

        val coefficient = (monthlyRate * Math.pow(1 + monthlyRate, term.toDouble())) /
                (Math.pow(1 + monthlyRate, term.toDouble()) - 1)

        val payment = amount * coefficient
        return payment.toInt()
    }

    private fun Int.toLocaleString(): String {
        return String.format("%,d", this).replace(',', ' ')
    }
}