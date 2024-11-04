package com.example.boo345word.ui.word

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.boo345word.databinding.ActivityWordListBinding

class WordListActivity  : AppCompatActivity(){

    private lateinit var binding : ActivityWordListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWordListBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
    }
    
    companion object{
        
        fun start(context: Context){
            val intent = Intent(context, WordListActivity::class.java)

            context.startActivity(intent)
        }
    }
}