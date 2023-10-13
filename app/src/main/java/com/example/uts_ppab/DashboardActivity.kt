package com.example.uts_ppab

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import com.example.uts_ppab.databinding.ActivityDashboardBinding
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var inputData: Intent
    private lateinit var textToGet: List<String>
    private lateinit var plannedDates: MutableList<String>
    private var totalPrice: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        textToGet = mutableListOf()
        plannedDates = mutableListOf()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {

            SELAMATPAGI.text = "Selamat Pagi, " + intent.getStringExtra("username")?.replaceFirstChar { it.uppercase() } + "!"

           inputButton.setOnClickListener {
               val intent = Intent(this@DashboardActivity, InputActivity::class.java)
               startActivityForResult(intent, 1)
           }

            dialogCalendar.setOnClickListener {
                showDatePicker()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            inputData = data!!
            updateDashboard(inputData)
        }
    }
    private fun updateDashboard(inputData: Intent) {
        with(binding) {


            val checkStates = inputData.getBooleanArrayExtra("checkedStates")
            dashboardStasiunAwal.text = inputData.getStringExtra("stasiunAwal")
            dashboardStasiunTujuan.text = inputData.getStringExtra("stasiunTujuan")
            dashboardTanggalBerangkat.text = inputData.getStringExtra("tanggalBerangkat")
            plannedDates.add(inputData.getStringExtra("tanggalBerangkat")!!)
            dashboardCalendarDescription.text = "Untuk sekarang, anda memiliki ${plannedDates.size} rencana perjalanan"
            dashboardKelasKereta.text = inputData.getStringExtra("kelasKereta")

            when(inputData.getStringExtra("kelasKereta")) {
                "Ekonomi" -> {
                    totalPrice += 500000
                }
                "Bisnis" -> {
                    totalPrice += 1000000
                }
                "Eksekutif" -> {
                    totalPrice += 1500000
                }
            }

            addedPackages(checkStates)
            dashboardAdditions.text = "Paket yang anda pilih: " + "\n" + textToGet.joinToString(separator = ", ")
            dashboardHargaTiket.text = "Harga Tiket: " + NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(totalPrice)


        }
    }

    private fun addedPackages(checkStates : BooleanArray?) {
        for (i in 0 until (checkStates?.size ?: 0 )) {
            if(checkStates!![i]) {
                when(i) {
                    0 -> {
                        textToGet += resources.getStringArray(R.array.advert_1)[0]
                        totalPrice += resources.getStringArray(R.array.advert_1)[2].toInt()
                    }
                    1 -> {
                        textToGet += resources.getStringArray(R.array.advert_2)[0]
                        totalPrice += resources.getStringArray(R.array.advert_2)[2].toInt()
                    }
                    2 -> {
                        textToGet += resources.getStringArray(R.array.advert_3)[0]
                        totalPrice += resources.getStringArray(R.array.advert_3)[2].toInt()
                    }
                    3 -> {
                        textToGet += resources.getStringArray(R.array.advert_4)[0]
                        totalPrice += resources.getStringArray(R.array.advert_4)[2].toInt()
                    }
                    4 -> {
                        textToGet += resources.getStringArray(R.array.advert_5)[0]
                        totalPrice += resources.getStringArray(R.array.advert_5)[2].toInt()
                    }
                    5 -> {
                        textToGet += resources.getStringArray(R.array.advert_6)[0]
                        totalPrice += resources.getStringArray(R.array.advert_6)[2].toInt()
                    }
                    6 -> {
                        textToGet += resources.getStringArray(R.array.advert_7)[0]
                        totalPrice += resources.getStringArray(R.array.advert_7)[2].toInt()
                    }

                }
            }
        }
    }


    private fun showDatePicker() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.marked_date_picker_dialog)

        val datePicker = dialog.findViewById<DatePicker>(R.id.datePicker)

        dialog.setTitle("Select a date")

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        datePicker.init(currentYear, month, day) { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"

            if(selectedDate in plannedDates) {
                val message = "Ada rencana di $selectedDate"
                val snackbar = Snackbar.make(datePicker, message, Snackbar.LENGTH_SHORT)
                val snackbarView = snackbar.view
                // why 50? god knows why
                snackbarView.layoutParams.width = datePicker.width - 50
                snackbar.show()
            }

        }

        dialog.show()

    }




}
