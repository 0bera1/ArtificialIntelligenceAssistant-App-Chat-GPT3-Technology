package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.GONE
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.models.MessageModel

class MessageAdapter(val list : ArrayList<MessageModel>) : Adapter<MessageAdapter.MessageViewHolder>() {

    inner class  MessageViewHolder(view:View) : ViewHolder(view){

        val msgText = view.findViewById<TextView>(R.id.show_message)
        val imageContainer = view.findViewById<LinearLayout>(R.id.imageCard)
        val image = view.findViewById<ImageView>(R.id.image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        var view : View? = null

        var from = LayoutInflater.from(parent.context)
        if(viewType == 0){
            view = from.inflate(R.layout.chatrightitem,parent,false)
        }else {
            view = from.inflate(R.layout.chatleftitem,parent,false)
        }
        return MessageViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val message = list[position]
        return if (message.isUser) 0 else 1
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        val message = list[position]

        if (!message.isUser){
            holder.imageContainer.visibility= GONE
        }
        if (message.isImage){
            holder.imageContainer.visibility= VISIBLE
            Glide.with(holder.itemView.context)
                .load(message.message).into(holder.image)
        }else{
            holder.msgText.text = message.message
        }


    }
}