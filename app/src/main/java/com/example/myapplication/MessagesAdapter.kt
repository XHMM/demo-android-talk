package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_my.view.*
import kotlinx.android.synthetic.main.message_their.view.*

class MessagesAdapter(private val messageList: MutableList<Message>) :
    RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {
    companion object {
        const val YOU = 0
        const val ME = 1
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var wAccount: TextView? = null
        var wDate: TextView? = null
        lateinit var wMessage: TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = if (viewType == ME)
            layoutInflater.inflate(R.layout.message_my, parent, false) else layoutInflater.inflate(
            R.layout.message_their,
            parent,
            false
        )

        return ViewHolder(view).apply {
            if (viewType == ME) {
                wMessage = view.wMessageMy_message
            }
            else {
                wMessage = view.wMessageTheir_message
                wAccount = view.wMessageTheir_name
                wDate = view.wMessageTheir_date

            }
        }
    }

    override fun onBindViewHolder(holder: MessagesAdapter.ViewHolder, position: Int) {
        messageList[position].run {
            holder.wMessage.text = this.content
            holder.wAccount?.text = this.account
            holder.wDate?.text = this.date
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (messageList[position].isSelf) return ME
        return YOU
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}