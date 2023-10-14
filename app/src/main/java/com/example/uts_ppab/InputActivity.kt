package com.example.uts_ppab

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.example.uts_ppab.databinding.ActivityInputBinding
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding
    private var totalPrices: Int = 0

    private var stasiunAwalBefore: String = ""
    private var stasiunTujuanBefore: String = ""
    private var kelasKeretaBefore: String = ""

    private var priceCalcBefore: Int = 0
    private var pricePerUnit: Int = 100000
    private var pastTollBefore: Int = 0

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


        with(binding) {

            carouselViewPager.adapter = CardPagerAdapter(cardViews)


            cardViews.forEachIndexed { index, cardView ->
                cardView.findViewById<ToggleButton>(R.id.toggleButton).setOnCheckedChangeListener { _, isChecked ->
                    val checkedStates =  (carouselViewPager.adapter as CardPagerAdapter).checkedStates
                    checkedStates[index] = isChecked
                    if(isChecked) {
                        totalPrices += cardView.findViewById<TextView>(R.id.card_price).text.toString().toInt()
                    } else {
                        totalPrices -= cardView.findViewById<TextView>(R.id.card_price).text.toString().toInt()
                    }
                    totalPrice.text = convertPrice(totalPrices)
                }
            }

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

            stasiunAwal.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedItem = adapterAwal.getItem(position)
                val stasiunAwalNow = selectedItem.toString()

                if(stasiunAwalBefore == "") {
                    stasiunAwalBefore = stasiunAwalNow
                }
                Log.d("stasiunAwalBefore", stasiunAwalBefore)
                if (stasiunAwalBefore != stasiunAwalNow) {
                    if(stasiunTujuanBefore != "" && kelasKereta.text.toString() != "") {

                        priceCalcBefore = weightedGraph(stasiunAwalBefore, stasiunTujuanBefore) * pricePerUnit
                        totalPrices -= priceCalcBefore
                        stasiunAwalBefore = stasiunAwalNow

                        priceCalcBefore = weightedGraph(stasiunAwalBefore, stasiunTujuanBefore) * pricePerUnit
                        totalPrices += priceCalcBefore
                        totalPrice.text = convertPrice(totalPrices)
                    }
                } else {
                    if(stasiunTujuanBefore != "" && kelasKereta.text.toString() != "") {
                        priceCalcBefore = weightedGraph(stasiunAwalBefore, stasiunTujuanBefore) * pricePerUnit
                        totalPrices += priceCalcBefore
                        totalPrice.text = convertPrice(totalPrices)
                    }
                }
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

            stasiunTujuan.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedItem = adapterTujuan.getItem(position)
                val stasiunTujuanNow = selectedItem.toString()

                if(stasiunTujuanBefore == "") {
                    stasiunTujuanBefore = stasiunTujuanNow
                }
                Log.d("stasiunTujuanBefore", stasiunTujuanBefore)
                if (stasiunTujuanBefore != stasiunTujuanNow) {
                    if(stasiunAwalBefore != "") {

                        priceCalcBefore = weightedGraph(stasiunAwalBefore, stasiunTujuanBefore) * pricePerUnit
                        totalPrices -= priceCalcBefore
                        stasiunTujuanBefore = stasiunTujuanNow

                        priceCalcBefore = weightedGraph(stasiunAwalBefore, stasiunTujuanBefore) * pricePerUnit
                        totalPrices += priceCalcBefore
                        if(pastTollBefore != 0) {
                            totalPrice.text = convertPrice(totalPrices)
                        }
                    }
                } else {
                    if(stasiunAwalBefore != "") {
                        priceCalcBefore = weightedGraph(stasiunAwalBefore, stasiunTujuanBefore) * pricePerUnit
                        totalPrices += priceCalcBefore
                        if(pastTollBefore != 0) {
                            totalPrice.text = convertPrice(totalPrices)
                        }
                    }
                }
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

            kelasKereta.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedItem = adapterKelas.getItem(position)
                val kelasKeretaNow = selectedItem.toString()
                var currentToll: Int = 0

                if(kelasKeretaNow == "Ekonomi") {
                    currentToll = 500000
                } else if(kelasKeretaNow == "Bisnis") {
                    currentToll = 1000000
                } else if(kelasKeretaNow == "Eksekutif") {
                    currentToll = 1500000
                }

                if(kelasKeretaBefore == "") {
                    kelasKeretaBefore = kelasKeretaNow
                    totalPrices += currentToll
                    pastTollBefore = currentToll
                    totalPrice.text = convertPrice(totalPrices)
                }

                if(kelasKeretaBefore != kelasKeretaNow) {
                    if(stasiunAwalBefore != "" && stasiunTujuanBefore != "") {
                        var pastToll: Int = 0
                        if(kelasKeretaBefore == "Ekonomi") {
                            pastToll = 500000
                        } else if(kelasKeretaBefore == "Bisnis") {
                            pastToll = 1000000
                        } else if(kelasKeretaBefore == "Eksekutif") {
                            pastToll = 1500000
                        }
                        totalPrices += (currentToll - pastToll)
                        totalPrice.text = convertPrice(totalPrices)
                        kelasKeretaBefore = kelasKeretaNow
                        pastTollBefore = currentToll
                    }
                }
            }

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
                    intent.putExtra("hargaTiket", totalPrices)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    fun convertPrice(price: Int): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(price)
    }

    fun weightedGraph(stasiunAwal: String, stasiunTujuan: String): Int {
        when(stasiunAwal) {
            "Stasiun A" -> {
                when(stasiunTujuan) {
                    "Stasiun Z" -> { return 2 }
                    "Stasiun X" -> { return 5 }
                    "Stasiun Y" -> { return 3 }
                }
            }
            "Stasiun B" -> {
                when(stasiunTujuan) {
                    "Stasiun Z" -> { return 4 }
                    "Stasiun X" -> { return 2 }
                    "Stasiun Y" -> { return 5 }
                }
            }
            "Stasiun C" -> {
                when(stasiunTujuan) {
                    "Stasiun Z" -> { return 3 }
                    "Stasiun X" -> { return 7 }
                    "Stasiun Y" -> { return 1 }
                }
            }
        }
        return 0
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
        val price = cardView.findViewById<TextView>(R.id.card_price)
        price.text = stringArray[2]
    }

    class CardPagerAdapter(private val cardViews: List<CardView>) : PagerAdapter() {

        val checkedStates = BooleanArray(cardViews.size)
        var totalBill = IntArray(cardViews.size)

        override fun getCount(): Int {
            return cardViews.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val cardView = cardViews[position]

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