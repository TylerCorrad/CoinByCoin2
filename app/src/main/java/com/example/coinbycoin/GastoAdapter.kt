package com.example.coinbycoin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.coinbycoin.ui.theme.CustomSpinnerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat

class GastoAdapter(private val gastos: List<Gasto>) :
    RecyclerView.Adapter<GastoAdapter.GastoViewHolder>() {

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gasto, parent, false)
        return GastoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GastoViewHolder, position: Int) {
        val currentGasto = gastos[position]
        val numberFormat = NumberFormat.getInstance()
        numberFormat.maximumFractionDigits = 2
        holder.descripcionTextView.text = currentGasto.descripcion
        holder.valorTextView.text = "${numberFormat.format(currentGasto.valor)}$"

        holder.itemView.setOnClickListener {
            listener?.onItemClick(currentGasto)
        }

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    override fun getItemCount() = gastos.size

    inner class GastoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descripcionTextView: TextView = itemView.findViewById(R.id.txtDescripcion)
        val valorTextView: TextView = itemView.findViewById(R.id.txtValor)
    }
}

