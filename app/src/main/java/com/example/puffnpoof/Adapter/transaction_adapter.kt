package com.example.puffnpoof.Adapter

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.puffnpoof.Database.dbHelper
import com.example.puffnpoof.Model.transactionModel
import com.example.puffnpoof.R

class transaction_adapter(
    private var trans_list: ArrayList<transactionModel>,
    private var currID: Int,
    private var dbHelper: dbHelper
    ): RecyclerView.Adapter<transaction_adapter.showList>() {

        class showList(ItemView: View): RecyclerView.ViewHolder(ItemView){
            val transID_: TextView = itemView.findViewById(R.id.transID)
            val dollname_: TextView = itemView.findViewById(R.id.transdollName)
            val quantity_: TextView = itemView.findViewById(R.id.Quantity)
            val date_: TextView = itemView.findViewById(R.id.transDate)
            val apus: ImageView = itemView.findViewById(R.id.deleteButtonTransaction)
            val editJumlah: ImageView = itemView.findViewById(R.id.editButtonQuantity)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): showList {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.transaction_list_design,parent,false)
        return showList(itemView)
    }

    override fun getItemCount(): Int {
        return trans_list.filter { it.userID == currID }.size
    }

    override fun onBindViewHolder(holder: showList, position: Int) {
        val curr_trans = trans_list[position]
        holder.transID_.text = "PF${String.format("%04d",curr_trans.transactionID)}"
        holder.dollname_.text = dbHelper.getdollname(curr_trans.dollID)
        holder.quantity_.text = curr_trans.transactionQuantity.toString()
        holder.date_.text = curr_trans.transactionDate.toString()

        holder.editJumlah.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Edit Transaction Quantity")

            val customLayout = LayoutInflater.from(holder.itemView.context).inflate(R.layout.custom_edit_button_pop_up, null)
            builder.setView(customLayout)

            val input = customLayout.findViewById<EditText>(R.id.editTextQuantity)
            input.requestFocus()

            builder.setPositiveButton("OK") { dialog, which ->
                val newQuantity = input.text.toString().toIntOrNull()
                if (newQuantity != null && newQuantity > 0) {
                    curr_trans.transactionQuantity = newQuantity
                    notifyDataSetChanged()
                    dbHelper.editTransaction(curr_trans)
                } else {
                    Toast.makeText(holder.itemView.context, "Invalid quantity", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        holder.apus.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Delete Transaction")
            builder.setMessage("Are you sure you want to delete this transaction?")

            builder.setPositiveButton("Delete") { dialog, which ->
                trans_list.removeAt(position)
                notifyDataSetChanged()
                dbHelper.deleteTransaction(curr_trans.transactionID.toString())
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        Log.d("tag", "dollname_ = ${dbHelper.getdollname(curr_trans.dollID)}")
        Log.d("tag", "TransID_ = ${curr_trans.transactionID}")
        Log.d("tag", "quantity_ = ${curr_trans.transactionQuantity}")
        Log.d("tag", "date_ = ${curr_trans.transactionDate}")
    }

}

