package com.example.androidnews

import android.util.Base64
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class ResultsManager {
    val okHttpClient: OkHttpClient

    init{
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClientBuilder.addInterceptor(loggingInterceptor)

        okHttpClient = okHttpClientBuilder.build()
    }

    /*fun retrieveUrl(apiKey: String): String{
        val urls: MutableList<String> = mutableListOf()
        //val searchLocation: String =


        val request: Request = Request.Builder()
            .url("https://newsapi.org/v2/everything?qInTitle=+'Washington+D.C.'&apiKey=$apiKey")
            .get()
            .build()

        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if(response.isSuccessful && !responseBody.isNullOrBlank()) {
            val json: JSONObject = JSONObject(responseBody)
            val articles: JSONArray = json.getJSONArray("articles")

            for (i in 0 until articles.length()) {
                val curr: JSONObject = articles.getJSONObject(i)
                val url: String = curr.getString("url")
                urls.add(url)
            }
        }
        return urls[0]
    }*/

    fun retrieveResults(location: String, apiKey: String): List<Result>{
        val results: MutableList<Result> = mutableListOf()

        val request: Request = Request.Builder()
            .url("https://newsapi.org/v2/everything?qInTitle='$location'&apiKey=$apiKey")
            .get()
            .build()

        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if(response.isSuccessful && !responseBody.isNullOrBlank()){
            val json: JSONObject = JSONObject(responseBody)
            val articles: JSONArray = json.getJSONArray("articles")

            for(i in 0 until articles.length()){
                val curr: JSONObject = articles.getJSONObject(i)

                val title: String = curr.getString("title")
                val preview: String = curr.getString("description")
                val pictureUrl: String = curr.getString("urlToImage")

                val source: JSONObject = curr.getJSONObject("source")
                val name: String = source.getString("name")

                val result: Result = Result(
                    headline = title,
                    preview = preview,
                    sourceName = name,
                    pictureURL = pictureUrl
                )

                results.add(result)
            }
        }
        return results
    }
}