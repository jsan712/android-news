package com.example.androidnews

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationResults = findViewById(R.id.locationResults)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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

                    } else {
                        Log.d("MapsActivity", "No results from geocoder!")

                        val toast = Toast.makeText(
                            this@MapsActivity,
                            getString(R.string.geocoder_no_results),
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }
            }
            //Show results for location text
            locationResults.visibility = View.VISIBLE

            //Show sources based on the location
            recyclerView = findViewById(R.id.recyclerView)

            recyclerView.layoutManager = LinearLayoutManager(this)

            val resultsManager = ResultsManager()
            val newsApiKey = getString(R.string.news_api_key)

            doAsync {
                val results: List<Result> = try{
                    resultsManager.retrieveResults(newsApiKey)
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