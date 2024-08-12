package com.example.puffnpoof.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.puffnpoof.Model.dollModel
import com.example.puffnpoof.R
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class home_adapter(
    private var doll_list: ArrayList<dollModel>,
    private val onItemClick: (dollModel) -> Unit
) : RecyclerView.Adapter<home_adapter.showList>() {

    class showList(ItemView: View): RecyclerView.ViewHolder(ItemView){
        val pic: ImageView = itemView.findViewById(R.id.doll_image)
        val name: TextView = itemView.findViewById(R.id.Dollname)
        val price: TextView = itemView.findViewById(R.id.Price)
        val ID: TextView = itemView.findViewById(R.id.DollID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): showList {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.doll_list_design,parent,false)
        return showList(itemView)
    }

    override fun getItemCount(): Int {
        return doll_list.size
    }

    override fun onBindViewHolder(holder: showList, position: Int) {
        val current_doll = doll_list[position]
        holder.name.text = current_doll.name
        holder.ID.text = "BB${String.format("%03d", current_doll.dollID)}"

        Picasso.get().load(current_doll.imageLink).into(holder.pic)
        holder.price.text = "$ ${current_doll.price}"
        holder.itemView.setOnClickListener {
            onItemClick(current_doll)
        }
    }

    fun filterList(text: String) {
        doll_list.clear()
        if (text.isEmpty()) {
            doll_list.addAll(doll_list)
        } else {
            val search = text.toLowerCase(Locale.getDefault())
            doll_list.addAll(doll_list.filter {
                it.name?.toLowerCase(Locale.getDefault())?.contains(search) ?: false
            })
        }
        notifyDataSetChanged()
    }
}