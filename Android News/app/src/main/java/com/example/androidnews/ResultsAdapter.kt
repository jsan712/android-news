package com.example.androidnews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResultsAdapter(val results: List<Result>) : RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {
    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(viewHolder: ResultsAdapter.ViewHolder, position: Int) {
        val currResult = results[position]
        viewHolder.headline.setText(currResult.headline)
        viewHolder.preview.setText(currResult.preview)
        viewHolder.sourceName.setText(currResult.sourceName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View = layoutInflater.inflate(R.layout.row_source, parent, false)
        return ResultsAdapter.ViewHolder(rootLayout)
    }

    class ViewHolder(rootLayout: View) : RecyclerView.ViewHolder(rootLayout){
        val headline: TextView = rootLayout.findViewById(R.id.headline)
        val preview: TextView = rootLayout.findViewById(R.id.preview)
        val sourceName: TextView = rootLayout.findViewById(R.id.sourceName)
        val picture: ImageView = rootLayout.findViewById(R.id.picture)
    }
}