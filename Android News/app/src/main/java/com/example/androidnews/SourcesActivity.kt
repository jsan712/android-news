package com.example.androidnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SourcesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sources)

        val intent: Intent = getIntent()
        val searchTerm: String = intent.getStringExtra("SEARCH")!!

        val title = "Search for: ${searchTerm.toString()}"
        setTitle(title)

        val fakeSources: List<Source> = getFakeSources()
        recyclerView = findViewById(R.id.recyclerView)
        val adapter: SourcesAdapter = SourcesAdapter(fakeSources)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //The following block of code was given by https://developer.android.com/guide/topics/ui/controls/spinner
        spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(this, R.array.temp_array,
            android.R.layout.simple_spinner_item).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    fun getFakeSources(): List<Source>{
        return listOf(
            Source(
                name = "CNN",
                bio = "View the latest news and breaking news today for U.S., world, weather, entertainment, politics and health."
            ),
            Source(
                name = "NBC",
                bio = "View breaking news, videos, and the latest top stories in world news, business, politics, health and pop culture."
            ),
            Source(
                name = "Fox News",
                bio = "View breaking news, videos, and top stories in U.S., World, Entertainment, Health, and Business."
            ),
            Source(
                name = "CBS News",
                bio = "Get the latest, breaking news headlines of the day for national news and world news today."
            ),
            Source(
                name = "AP News",
                bio = "News from The Associated Press, the definitive source for independent journalism from every corner of the globe."
            ),
            Source(
                name = "MSNBC",
                bio = "Breaking news and the latest news for today. Get daily news from local news reporters and world news updates with live audio & video from our team."
            ),
            Source(
                name = "ABC News",
                bio = "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos."
            ),
            Source(
                name = "New York Times",
                bio = "Live news, investigations, opinion, photos and video by the journalists of The New York Times from more than 150 countries around the world."
            ),
            Source(
                name = "The Washington Post",
                bio = "Breaking news and analysis on politics, business, world national news, entertainment more. In-depth DC, Virginia, Maryland news coverage including traffic, weather, and more."
            ),
            Source(
                name = "BBC",
                bio = "Breaking news, sport, TV, radio and a whole lot more. The BBC informs, educates and entertains - wherever you are, whatever your age."
            ),
        )
    }
}