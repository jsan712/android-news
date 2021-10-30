package com.example.androidnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.location.Address
import android.os.PersistableBundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync

class ResultsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        //Get data from the Intent that launched this screen
        val intent: Intent = getIntent()
        val source: String = intent.getStringExtra("SOURCE")!!
        val term: String = intent.getStringExtra("term")!!
        val sourceID: String = intent.getStringExtra("SOURCEID")!!

        //Set the title for the screen when coming from the skip sources button
        val title = getString(R.string.results_title, term)
        setTitle(title)

        val resultsManager = ResultsManager()
        val newsApiKey = getString(R.string.news_api_key)

        doAsync {
            val results: List<Result> = try{
                resultsManager.retrieveHeadlineResults(term, newsApiKey)
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