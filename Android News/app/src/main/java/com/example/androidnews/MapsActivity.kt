package com.example.androidnews

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Browser
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.androidnews.databinding.ActivityMapsBinding
import org.jetbrains.anko.doAsync

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var locationResults: TextView
    private lateinit var progressBar: ProgressBar
    private var currentAddress: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationResults = findViewById(R.id.locationResults)
        progressBar = findViewById(R.id.progressBar2)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    fun updateCurrentAddress(address: Address) {
        currentAddress = address
        locationResults.text = address.getAddressLine(0)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Restore previous pin if one exists
        val savedPreferences = getSharedPreferences("android-news", Context.MODE_PRIVATE)

        val savedLat = savedPreferences.getString("lat", "0.0")!!
        val savedLon = savedPreferences.getString("lon", "0.0")!!
        val savedPost = savedPreferences.getString("post", "false")!!
        val savedLocation = savedPreferences.getString("location", "false")!!
        Log.d("MapsActivity", "Retrieved values")

        //Show sources based on the location
        recyclerView = findViewById(R.id.recyclerView)

        /* Code to make recyclerView horizontal came from https://www.youtube.com/watch?v=EFZkktBOFF8
         * which proved to be faulty and AndroidStudio suggested a fix that worked
         */
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        //Display the results from the saved location
        if (savedLocation != "false") {
            val coords: LatLng = LatLng(savedLat.toDouble(), savedLon.toDouble())

            googleMap.addMarker(MarkerOptions().position(coords).title(savedPost))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 10.0f))

            val resultsManager = ResultsManager()
            val newsApiKey = getString(R.string.news_api_key)

            doAsync {
                val results: List<Result> = try{
                    resultsManager.retrieveMapResults(savedLocation, newsApiKey)
                }catch(exception: Exception){
                    Log.e("MapsActivity", "Retrieving results failed!", exception)
                    listOf<Result>()
                }

                runOnUiThread {
                    if(results.isNotEmpty()){
                        val adapter: ResultsAdapter = ResultsAdapter(results)
                        recyclerView.adapter = adapter
                    }
                    else{
                        Toast.makeText(
                            this@MapsActivity,
                            "Failed to retrieve results!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        googleMap.setOnMapClickListener { coords: LatLng ->
            googleMap.clear()

            doAsync {
                val geocoder: Geocoder = Geocoder(this@MapsActivity)
                val results: List<Address> = try {
                    geocoder.getFromLocation(coords.latitude, coords.longitude, 10)
                } catch (exception: Exception) {
                    Log.e("MapsActivity", "Geocoding failed", exception)
                    listOf()
                }

                runOnUiThread {
                    if (results.isNotEmpty()) {
                        // Potentially, we could show all results to the user to choose from,
                        // but for our usage it's sufficient enough to just use the first result.
                        // The Geocoder's first result is often the "best" one in terms of its accuracy / confidence.
                        val firstResult: Address = results[0]
                        val postalAddress: String = firstResult.getAddressLine(0)

                        Log.d("MapsActivity", "First result: $postalAddress")

                        googleMap.addMarker(
                            MarkerOptions().position(coords).title(postalAddress)
                        )

                        // Add a map marker where the user tapped and pan the camera over
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 10.0f))

                        updateCurrentAddress(firstResult)

                        //Save the location
                        val editor = savedPreferences.edit()
                        editor.putString("lat", coords.latitude.toString())
                        editor.putString("lon", coords.longitude.toString())
                        editor.putString("post", postalAddress)
                        editor.putString("location", firstResult.adminArea)
                        editor.apply()
                        Log.d("MapsActivity", "Coords saved")

                    } else {
                        Log.d("MapsActivity", "No results from geocoder!")

                        val toast = Toast.makeText(
                            this@MapsActivity,
                            getString(R.string.geocoder_no_results),
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                    //Show results for location text
                    locationResults.visibility = View.VISIBLE
                    locationResults.text = getString(R.string.results_for_location, currentAddress!!.getAddressLine(0))

                    val resultsManager = ResultsManager()
                    val newsApiKey = getString(R.string.news_api_key)

                    doAsync {
                        val results: List<Result> = try{
                            resultsManager.retrieveMapResults(currentAddress!!.adminArea, newsApiKey)
                        }catch(exception: Exception){
                            Log.e("MapsActivity", "Retrieving results failed!", exception)
                            listOf<Result>()
                        }

                        runOnUiThread {
                            if(results.isNotEmpty()){
                                val adapter: ResultsAdapter = ResultsAdapter(results)
                                recyclerView.adapter = adapter
                            }
                            else{
                                Toast.makeText(
                                    this@MapsActivity,
                                    "Failed to retrieve results!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }
}