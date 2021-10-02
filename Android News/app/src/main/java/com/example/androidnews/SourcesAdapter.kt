package com.example.androidnews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SourcesAdapter(val sources: List<Source>) : RecyclerView.Adapter<SourcesAdapter.ViewHolder>() {
    override fun getItemCount(): Int = sources.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currSource = sources[position]
        viewHolder.name.setText(currSource.name)
        viewHolder.bio.setText(currSource.bio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View = layoutInflater.inflate(R.layout.row_source, parent, false)
        return ViewHolder(rootLayout)
    }

    class ViewHolder(rootLayout: View) : RecyclerView.ViewHolder(rootLayout){
        val name: TextView = rootLayout.findViewById(R.id.name)
        val bio: TextView = rootLayout.findViewById(R.id.bio)
    }


}