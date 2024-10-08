package com.example.appmovilasignaweb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appmovilasignaweb.R

class EspacioAdapter(private val espacioList: MutableList<Espacio>) : RecyclerView.Adapter<EspacioAdapter.EspacioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspacioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_espacio, parent, false)
        return EspacioViewHolder(view)
    }

    override fun onBindViewHolder(holder: EspacioViewHolder, position: Int) {
        val espacio = espacioList[position]
        holder.textViewNombreEspacio.text = espacio.nombre
        holder.textViewClasificacionEspacio.text = espacio.clasificacion
        holder.textViewCapacidadEspacio.text = espacio.capacidad
        holder.textViewDescripcionEspacio.text = espacio.descripcion
    }

    override fun getItemCount(): Int {
        return espacioList.size
    }

    class EspacioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNombreEspacio: TextView = itemView.findViewById(R.id.textViewNombreEspacio)
        val textViewClasificacionEspacio: TextView = itemView.findViewById(R.id.textViewClasificacionEspacio)
        val textViewCapacidadEspacio: TextView = itemView.findViewById(R.id.textViewCapacidadEspacio)
        val textViewDescripcionEspacio: TextView = itemView.findViewById(R.id.textViewDescripcionEspacio)
    }
}
