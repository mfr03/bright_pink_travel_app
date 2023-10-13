package com.example.uts_ppab

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.example.uts_ppab.databinding.ActivityInputBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar

class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInputBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val cardViews: List<CardView> = listOf(createCardView(), createCardView(), createCardView(), createCardView(),
            createCardView(), createCardView(), createCardView())
        editCardView(cardViews[0], resources.getStringArray(R.array.advert_1))
        editCardView(cardViews[1], resources.getStringArray(R.array.advert_2))
        editCardView(cardViews[2], resources.getStringArray(R.array.advert_3))
        editCardView(cardViews[3], resources.getStringArray(R.array.advert_4))
        editCardView(cardViews[4], resources.getStringArray(R.array.advert_5))
        editCardView(cardViews[5], resources.getStringArray(R.array.advert_6))
        editCardView(cardViews[6], resources.getStringArray(R.array.advert_7))
        val checkStates = BooleanArray(cardViews.size)

        with(binding) {

            tanggalBerangkat.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_UP) {
                    val rightDrawable = (v as AutoCompleteTextView).compoundDrawables[2]
                    if(event.rawX >= (v.right - rightDrawable.bounds.width())) {
                        showDatePickerDialog(tanggalBerangkat)
                        return@setOnTouchListener true
                    }
                }
                false
            }


            val adapterAwal = ArrayAdapter(this@InputActivity, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.spinner_stasiun_awal))
            stasiunAwal.setAdapter(adapterAwal)

            stasiunAwal.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_UP) {
                    val rightDrawable = (v as AutoCompleteTextView).compoundDrawables[2]
                    if(event.rawX >= (v.right - rightDrawable.bounds.width())) {
                        stasiunAwal.showDropDown()
                        return@setOnTouchListener true
                    }
                }
                false
            }

            val adapterTujuan = ArrayAdapter(this@InputActivity, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.spinner_stasiun_tujuan))
            stasiunTujuan.setAdapter(adapterTujuan)

            stasiunTujuan.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_UP) {
                    val rightDrawable = (v as AutoCompleteTextView).compoundDrawables[2]
                    if(event.rawX >= (v.right - rightDrawable.bounds.width())) {
                        stasiunTujuan.showDropDown()
                        return@setOnTouchListener true
                    }
                }
                false
            }

            val adapterKelas = ArrayAdapter(this@InputActivity, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.spinner_kelas_kereta))
            kelasKereta.setAdapter(adapterKelas)

            kelasKereta.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_UP) {
                    val rightDrawable = (v as AutoCompleteTextView).compoundDrawables[2]
                    if(event.rawX >= (v.right - rightDrawable.bounds.width())) {
                        kelasKereta.showDropDown()
                        return@setOnTouchListener true
                    }
                }
                false
            }

            carouselViewPager.adapter = CardPagerAdapter(cardViews)

            homeButton.setOnClickListener {
                finish()
            }

            submitButton.setOnClickListener {
                if (tanggalBerangkat.text.toString().isEmpty() || stasiunAwal.text.toString().isEmpty() || stasiunTujuan.text.toString().isEmpty() || kelasKereta.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, "Mohon isi bagan yang kosong", Snackbar.LENGTH_SHORT).setAnchorView(submitButton).show()
                } else {
                    val intent = Intent()
                    intent.putExtra("tanggalBerangkat", tanggalBerangkat.text.toString())
                    intent.putExtra("stasiunAwal", stasiunAwal.text.toString())
                    intent.putExtra("stasiunTujuan", stasiunTujuan.text.toString())
                    intent.putExtra("kelasKereta", kelasKereta.text.toString())
                    intent.putExtra("checkedStates", (carouselViewPager.adapter as CardPagerAdapter).checkedStates)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    private fun showDatePickerDialog(element: AutoCompleteTextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = dateFormat.parse("$day/${month + 1}/$year")

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
            val selectedDateParsed = dateFormat.parse(selectedDate)
            val comparedResult = selectedDateParsed.compareTo(currentDate)
            if(comparedResult < 0) {
                Snackbar.make(element, "Tanggal yang anda pilih sudah lewat", Snackbar.LENGTH_SHORT).setAnchorView(binding.homeButton).show()
            } else {
                element.setText(selectedDate)
            }
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun createCardView(): CardView {
        val context = applicationContext
        val cardView = CardView(context)

        val inflater = LayoutInflater.from(context)
        val cardViewLayout = inflater.inflate(R.layout.card_view, cardView, false)

        cardView.addView(cardViewLayout)

        return cardView
    }

    private fun editCardView(cardView: CardView, stringArray: Array<String>) {
        val title = cardView.findViewById<TextView>(R.id.card_title)
        title.text = stringArray[0]
        val description = cardView.findViewById<TextView>(R.id.card_description)
        description.text = stringArray[1]
    }

    class CardPagerAdapter(private val cardViews: List<CardView>) : PagerAdapter() {

        val checkedStates = BooleanArray(cardViews.size)

        override fun getCount(): Int {
            return cardViews.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val cardView = cardViews[position]

            val toggleButton = cardView.findViewById<ToggleButton>(R.id.toggleButton)

            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                checkedStates[position] = isChecked
            }

            cardView.cardElevation = 0f
            cardView.setCardBackgroundColor(Color.TRANSPARENT)
            container.addView(cardView)
            return cardView
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View)
        }

    }
}