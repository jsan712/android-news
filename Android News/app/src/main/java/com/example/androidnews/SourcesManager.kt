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

class SourcesManager {
    val okHttpClient: OkHttpClient

    init{
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClientBuilder.addInterceptor(loggingInterceptor)

        okHttpClient = okHttpClientBuilder.build()
    }

    fun retrieveSources(apiKey: String): List<Source>{
        val sources: MutableList<Source> = mutableListOf()
        val category: String = "Hockey"

        val request: Request = Request.Builder()
            .url("https://newsapi.org/v2/top-headlines/sources?category=sports&apiKey=$apiKey")
            .get()
            .build()

        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if(response.isSuccessful && !responseBody.isNullOrBlank()){
            val json: JSONObject = JSONObject(responseBody)
            val statuses: JSONArray = json.getJSONArray("sources")

            for(i in 0 until statuses.length()){
                val curr: JSONObject = statuses.getJSONObject(i)
                val name: String = curr.getString("name")
                val bio: String = curr.getString("description")

                val source: Source = Source(
                    name = name,
                    bio = bio
                )

                sources.add(source)
            }
        }
        return sources
    }
}