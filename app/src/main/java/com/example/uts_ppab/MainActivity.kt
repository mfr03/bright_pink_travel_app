package com.example.uts_ppab

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import com.example.uts_ppab.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import org.apache.commons.validator.routines.EmailValidator
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {

            inputTanggalLahir.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_UP) {
                    val rightDrawable = (v as AutoCompleteTextView).compoundDrawables[2]
                    if(event.rawX >= (v.right - rightDrawable.bounds.width())) {
                        showDatePickerDialog(inputTanggalLahir)
                        return@setOnTouchListener true
                    }
                }
                false
            }

            registerButton.setOnClickListener {
                if (inputEmail.text.toString().isEmpty() || inputUsername.text.toString().isEmpty() ||inputPassword.text.toString().isEmpty() || inputTanggalLahir.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, "Mohon isi bagan yang kosong", Snackbar.LENGTH_SHORT).setAnchorView(registerButton).show()
                } else {
                    if(checkValidity(inputEmail.text.toString(), inputTanggalLahir.text.toString())) {
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        intent.putExtra("username", inputUsername.text.toString())
                        intent.putExtra("password", inputPassword.text.toString())
                        startActivity(intent)
                        finish()
                    }
                }
            }

        }
    }

    private fun showDatePickerDialog(element: AutoCompleteTextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
            element.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun checkValidity(email : String, tanggalLahir : String) : Boolean {
        if(EmailValidator.getInstance().isValid(email)) {
            if(checkAge(tanggalLahir)) {
                return true
            }
        } else {
            Snackbar.make(binding.root, "Email yang anda masukkan tidak valid", Snackbar.LENGTH_SHORT).setAnchorView(binding.registerButton).show()
        }
        return false
    }

    private fun checkAge(tanggalLahir: String) : Boolean {

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val currentDate = "$day/${month + 1}/$year"

        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = dateFormat.parse(currentDate)
        val birthCalendar = Calendar.getInstance()
        birthCalendar.time = dateFormat.parse(tanggalLahir)

        val umur = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

        if(umur >= 15) {
            return true
        } else {
            Snackbar.make(binding.root, "Anda berumur kurang dari 15", Snackbar.LENGTH_SHORT).setAnchorView(binding.registerButton).show()
        }
        return false
    }
}

