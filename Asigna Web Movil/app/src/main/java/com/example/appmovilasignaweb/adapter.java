package com.example.appmovilasignaweb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

public class adapter (
        val crearReserva:JSONArray,
        val context:Context
) : RecyclerView.Adapter<AdapterCrearReserva.MyHolder>() {

inner class MyHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
    val nombreEspacio: TextView = itemView.findViewById(R.id.SpinnerNombreEspacio)
}

override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): MyHolder {
    val itemView = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false)
    return MyHolder(itemView)
}

override fun onBindViewHolder(holder: MyHolder, position: Int) {
    val usuario = crearReserva.getJSONObject(position)
    holder.nombreEspacio.text = usuario.getString("nombre_espacio")
}

override fun getItemCount(): Int {
    return crearReserva.length()
}


