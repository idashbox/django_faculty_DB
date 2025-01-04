package com.example.faculty_app.ui.teachers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.faculty_app.R

class FilterActivity : AppCompatActivity() {

    private lateinit var minAgeEditText: EditText
    private lateinit var maxAgeEditText: EditText
    private lateinit var minYearStartEditText: EditText
    private lateinit var maxYearStartEditText: EditText
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton
    private lateinit var applyFiltersButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        // Инициализация виджетов
        minAgeEditText = findViewById(R.id.editTextMinAge)
        maxAgeEditText = findViewById(R.id.editTextMaxAge)
        minYearStartEditText = findViewById(R.id.editTextMinYearStart)
        maxYearStartEditText = findViewById(R.id.editTextMaxYearStart)
        maleRadioButton = findViewById(R.id.radioButtonMale)
        femaleRadioButton = findViewById(R.id.radioButtonFemale)
        applyFiltersButton = findViewById(R.id.buttonApplyFilters)

        applyFiltersButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra("minAge", minAgeEditText.text.toString().toIntOrNull())
            intent.putExtra("maxAge", maxAgeEditText.text.toString().toIntOrNull())
            intent.putExtra("minYearStart", minYearStartEditText.text.toString().toIntOrNull())
            intent.putExtra("maxYearStart", maxYearStartEditText.text.toString().toIntOrNull())
            intent.putExtra("sex", when {
                maleRadioButton.isChecked -> "Male"
                femaleRadioButton.isChecked -> "Female"
                else -> null
            })
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
