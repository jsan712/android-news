package com.example.androidnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.os.PersistableBundle
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResultsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        //Get data from the Intent that launched this screen
        val intent: Intent = getIntent()
        val result: String = intent.getStringExtra("RESULT")!!

        //Set the title for the screen when coming from the skip sources button
        val title = getString(R.string.results_title, result)
        setTitle(title)

    }
}