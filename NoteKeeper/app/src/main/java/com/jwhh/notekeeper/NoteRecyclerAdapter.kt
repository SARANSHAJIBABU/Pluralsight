package com.jwhh.notekeeper

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by Saran on 22/7/19.
 */
class NoteRecyclerAdapter(private val context: Context, private val noteList: List<NoteInfo>): RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.note_list_item,parent,false)
        //attachToRoot - true= After creation, immediately attach to parent
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return noteList.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noteInfo = noteList.get(position)
        holder.titleTextView?.text = noteInfo.title
        holder.descTestView?.text = noteInfo.text
        holder.notePosition = position
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val titleTextView = itemView?.findViewById<TextView>(R.id.note_title)
        val descTestView = itemView?.findViewById<TextView>(R.id.note_desc)
        var notePosition = 0

        init {
            //
            // Recyclerview provides flexibility to set ClickListener
            // ClickListener can be set on whole view
            // or can set seperate listeners for different view items like imgview,textview..
            itemView?.setOnClickListener {
                val intent = Intent(context,NoteActivity::class.java)
                intent.putExtra(NOTE_POSITION,notePosition)
                context.startActivity(intent)
            }
        }
    }
}