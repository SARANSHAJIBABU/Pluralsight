package com.jwhh.notekeeper

import android.content.Context
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
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val titleTextView = itemView?.findViewById<TextView>(R.id.note_title)
        val descTestView = itemView?.findViewById<TextView>(R.id.note_desc)
    }
}