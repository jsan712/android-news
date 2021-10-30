package com.example.androidnews

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ResultsAdapter(val results: List<Result>) : RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {
    //How many rows will be rendered
    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currResult = results[position]
        viewHolder.headline.setText(currResult.headline)
        viewHolder.preview.setText(currResult.preview)
        viewHolder.sourceName.setText(currResult.sourceName)
        val url = currResult.url

        if (currResult.pictureURL.isNotBlank()) {
            Picasso.get().setIndicatorsEnabled(true)

            Picasso
                .get()
                .load(currResult.pictureURL)
                .into(viewHolder.picture)
        }

        //When a card is clicked open the article in the browser
        //The following snippet is adapted from
        // https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
        viewHolder.itemView.setOnClickListener {
            val context: Context = viewHolder.itemView.context
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View = layoutInflater.inflate(R.layout.row_results, parent, false)
        return ViewHolder(rootLayout)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val headline: TextView = itemView.findViewById(R.id.headline)
        val preview: TextView = itemView.findViewById(R.id.preview)
        val sourceName: TextView = itemView.findViewById(R.id.sourceName)
        val picture: ImageView = itemView.findViewById(R.id.picture)
    }
}