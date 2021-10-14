package com.example.androidnews

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar

class MainActivity : AppCompatActivity() {
    private lateinit var searchBox: EditText
    private lateinit var searchButton: Button
    private lateinit var mapButton: Button
    private lateinit var headlinesButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "onCreate called!")

        val preferences: SharedPreferences = getSharedPreferences("android-news", Context.MODE_PRIVATE)

        searchBox= findViewById(R.id.searchBox)
        searchButton= findViewById(R.id.searchButton)
        mapButton= findViewById(R.id.mapButton)
        headlinesButton= findViewById(R.id.headlinesButton)
        progressBar = findViewById(R.id.progressBar)

        //Restore the previous search from SharedPreferences and display it to the user when the screen loads
        //Default to the empty string if there is no saved search term
        val savedSearchTerm = preferences.getString("SEARCH_TERM", "")
        searchBox.setText(savedSearchTerm)

        if(searchBox.text.toString().isBlank()){
            searchButton.isEnabled = false
        }

        searchButton.setOnClickListener{
            Log.d("MainActivity", "Button clicked!")

            //Save the search term to SharedPreferences
            val inputtedTerm = searchBox.getText().toString()
            val editor = preferences.edit()
            editor.putString("SEARCH_TERM", inputtedTerm)
            editor.apply()

            //Set the progress bar to visible when the user clicks search
            progressBar.visibility = View.VISIBLE

            val intent: Intent = Intent(this, SourcesActivity::class.java)
            intent.putExtra("SEARCH", searchBox.text.toString())
            startActivity(intent)
        }
        searchBox.addTextChangedListener(textWatcher)

        mapButton.setOnClickListener {
            Log.d("MainActivity", "Map button clicked!")

            val intent: Intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private val textWatcher: TextWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){
            Log.d("MainActivity", "Text is ${searchBox.getText().toString()}")
            val inputtedText = searchBox.text.toString()
            val enableButton = inputtedText.isNotBlank()
            searchButton.isEnabled = true
        }
        override fun afterTextChanged(p0: Editable?){}
    }

}