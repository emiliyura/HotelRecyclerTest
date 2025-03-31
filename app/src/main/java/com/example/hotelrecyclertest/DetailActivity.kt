package com.example.hotelrecyclertest

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val getData = intent.getParcelableExtra<DataClass>("android")
        if (getData != null) {
            findViewById<TextView>(R.id.detailTitle).text = getData.dataTitle
            findViewById<TextView>(R.id.detailDesc).text = getData.dataDesc
            findViewById<ImageView>(R.id.detailImage).setImageResource(getData.dataDetailImage)
        }
    }

    private fun applyTheme() {
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> setTheme(R.style.AppTheme_Dark)
            AppCompatDelegate.MODE_NIGHT_NO -> setTheme(R.style.AppTheme_Light)
            else -> setTheme(R.style.Theme_HotelRecyclerTest)
        }
    }
}