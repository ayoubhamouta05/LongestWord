package com.example.longuestword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.longuestword.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.numberPicker.apply {
            maxValue = 10
            minValue = 1
            value = 5
        }

        binding.btnStartgame.setOnClickListener {
            val intent = Intent(this,PlayActivity::class.java)
            intent.putExtra("numberOfVowels",binding.numberPicker.value.toString())
            startActivity(intent)
        }
    }
}