package com.example.uts_ppab

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
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
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.example.uts_ppab.databinding.ActivityInputBinding
import java.util.Calendar

class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInputBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val cardViews: List<CardView> = listOf(createCardView(), createCardView(), createCardView())
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

    private fun createCardView(): CardView {
        val context = applicationContext
        val cardView = CardView(context)

        val inflater = LayoutInflater.from(context)
        val cardViewLayout = inflater.inflate(R.layout.card_view, cardView, false)

        cardView.addView(cardViewLayout)

        return cardView
    }

    private fun editCardView(cardView: CardView) {
        val textview = cardView.findViewById<TextView>(R.id.card_title)
        textview.text = "CardView"
    }

    class CardPagerAdapter(private val cardViews: List<CardView>) : PagerAdapter() {

        private val checkedStates = BooleanArray(cardViews.size)

        override fun getCount(): Int {
            return cardViews.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val cardView = cardViews[position]

            val checkBox = cardView.findViewById<CheckBox>(R.id.checkBox)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
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