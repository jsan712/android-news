package com.example.androidnews

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

        if (currResult.pictureURL.isNotBlank()) {
            Picasso.get().setIndicatorsEnabled(true)

            Picasso
                .get()
                .load(currResult.pictureURL)
                .into(viewHolder.picture)
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

//        init{
//            itemView.setOnClickListener(this)
//        }

//        override fun onClick(view: View?) {
//            val position: Int = adapterPosition
//            if(position != RecyclerView.NO_POSITION){
//                listener.onItemClick(position)
//            }
//        }
    }

//    interface OnItemClickListener{
//        fun onItemClick(position: Int)
//    }
}