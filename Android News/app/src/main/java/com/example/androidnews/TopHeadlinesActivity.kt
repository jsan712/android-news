package com.example.androidnews

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import org.jetbrains.anko.doAsync

class TopHeadlinesActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner
    private lateinit var prevButton: MaterialButton
    private lateinit var nextButton: MaterialButton
    private lateinit var pageNum: TextView
    private var currPage = 1
    private var maxPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headlines)

        val savedPreferences = getSharedPreferences("android-news", Context.MODE_PRIVATE)

        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.nextButton)
        recyclerView = findViewById(R.id.headline_recyclerView)
        pageNum = findViewById(R.id.pageNum)

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

        val savedCategory = savedPreferences.getString("category", "Business")!!
        val savedSpinnerPos = savedPreferences.getString("position", "0")!!
        spinner.setSelection(savedSpinnerPos.toInt())



        nextButton.setOnClickListener {
            if(currPage == maxPages){
                nextButton.setBackgroundColor(getColor(R.color.buttonGrey))
            }
            if(currPage < maxPages){
                currPage++
                showPage(spinner.getSelectedItem().toString(), currPage)
            }
        }

        prevButton.setOnClickListener{
            if(currPage == 1){
                prevButton.setBackgroundColor(getColor(R.color.buttonGrey))
            }
            if(currPage > 1){
                currPage--
                showPage(spinner.getSelectedItem().toString(), currPage)
            }
        }
        showPage(savedCategory, currPage)
    }

    private fun getHeadlines(category: String, page: Int){
        val resultsManager = ResultsManager()
        val newsApiKey = getString(R.string.news_api_key)

        doAsync {
            val results: List<Result> = try{
                resultsManager.retrieveHeadlineResults(category, page, newsApiKey)
            }catch(exception: Exception){
                Log.e("TopHeadlinesActivity", "Retrieving results failed!", exception)
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
        val savedPreferences = getSharedPreferences("android-news", MODE_PRIVATE)
        val currPage = 1
        val category = parent.getItemAtPosition(pos).toString()

        //Show page for new spinner item
        showPage(category, currPage)

        if(category != "Business"){
            val editor = savedPreferences.edit()
            editor.putString("category", category)
            editor.putString("position", pos.toString())
            editor.apply()
        }
        getHeadlines(category, currPage)

        //Set the colors of the buttons upon new spinner items being selected
        prevButton.setBackgroundColor(getColor(R.color.buttonGrey))
        if(currPage == maxPages){
            nextButton.setBackgroundColor(getColor(R.color.buttonGrey))
        }
        Log.i("TopHeadlinesActivity", "New spinner item selected!")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        return
    }

    fun showPage(category: String, page: Int){
        getHeadlines(category, page)
        val update = "$currPage / $maxPages"
        pageNum.text = update
    }
}