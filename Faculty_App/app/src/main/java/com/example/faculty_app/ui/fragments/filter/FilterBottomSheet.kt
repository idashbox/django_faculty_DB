package com.example.faculty_app.ui.fragments.filter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.faculty_app.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet(private val listener: OnFilterApplyListener) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "FilterBottomSheet"
    }

    interface OnFilterApplyListener {
        fun onFilterApplied(
            minAge: Int?,
            maxAge: Int?,
            minYearStart: Int?,
            maxYearStart: Int?,
            sex: String?
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter_bottom_sheet, container, false)

        val minAgeEditText: EditText = view.findViewById(R.id.editTextMinAge)
        val maxAgeEditText: EditText = view.findViewById(R.id.editTextMaxAge)
        val minYearStartEditText: EditText = view.findViewById(R.id.editTextMinYearStart)
        val maxYearStartEditText: EditText = view.findViewById(R.id.editTextMaxYearStart)
        val maleRadioButton: RadioButton = view.findViewById(R.id.radioButtonMale)
        val femaleRadioButton: RadioButton = view.findViewById(R.id.radioButtonFemale)
        val applyFiltersButton: Button = view.findViewById(R.id.buttonApplyFilters)

        applyFiltersButton.setOnClickListener {
            val minAge = minAgeEditText.text.toString().toIntOrNull()
            val maxAge = maxAgeEditText.text.toString().toIntOrNull()
            val minYearStart = minYearStartEditText.text.toString().toIntOrNull()
            val maxYearStart = maxYearStartEditText.text.toString().toIntOrNull()
            val sex = when {
                maleRadioButton.isChecked -> "Male"
                femaleRadioButton.isChecked -> "Female"
                else -> null
            }

            listener.onFilterApplied(minAge, maxAge, minYearStart, maxYearStart, sex)
            dismiss()
        }

        return view
    }
}
