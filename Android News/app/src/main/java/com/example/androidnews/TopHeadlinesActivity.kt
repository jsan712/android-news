package com.example.androidnews

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync

class TopHeadlinesActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headlines)

        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.nextButton)

        recyclerView = findViewById(R.id.headline_recyclerView)

        //Sets the scrolling direction to vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

        //The following block of code was given by https://developer.android.com/guide/topics/ui/controls/spinner
        spinner = findViewById(R.id.headlineSpinner)

        ArrayAdapter.createFromResource(this, R.array.categories,
            android.R.layout.simple_spinner_item).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this
    }

    private fun getHeadlines(category: String){
        val resultsManager = ResultsManager()
        val newsApiKey = getString(R.string.news_api_key)

        doAsync {
            val results: List<Result> = try{
                resultsManager.retrieveHeadlineResults(category, newsApiKey)
            }catch(exception: Exception){
                Log.e("ResultsActivity", "Retrieving results failed!", exception)
                listOf<Result>()
            }

            runOnUiThread {
                if(results.isNotEmpty()){
                    val adapter: ResultsAdapter = ResultsAdapter(results)
                    recyclerView.adapter = adapter
                }
                else{
                    Toast.makeText(
                        this@TopHeadlinesActivity,
                        "Failed to retrieve results!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    //The following functions provided by https://developer.android.com/guide/topics/ui/controls/spinner
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id:Long){
        val category = parent.getItemAtPosition(pos).toString()
        getHeadlines(category)
        Log.i("TopHeadlinesActivity", "New spinner item selected!")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        return
    }
}