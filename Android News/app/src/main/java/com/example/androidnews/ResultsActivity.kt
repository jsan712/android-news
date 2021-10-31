package com.example.androidnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync

class ResultsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        recyclerView = findViewById(R.id.resultsRecyclerView)
        //Sets the scrolling direction to vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

        progressBar = findViewById(R.id.progressBar3)

        //Get data from the Intent that launched this screen
        val intent: Intent = getIntent()
        val resultTerm: String = intent.getStringExtra("RESULT_TERM")!!
        val sourceID: String = intent.getStringExtra("SOURCE_ID")!!

        //Set the title for the screen when coming from the skip sources button
        val title = getString(R.string.results_title, resultTerm)
        setTitle(title)

        val resultsManager = ResultsManager()
        val newsApiKey = getString(R.string.news_api_key)

        doAsync {
            val results: List<Result> = try{
                resultsManager.retrieveSourcesResults(resultTerm, sourceID, newsApiKey)
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
                        this@ResultsActivity,
                        "Failed to retrieve results!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }
}