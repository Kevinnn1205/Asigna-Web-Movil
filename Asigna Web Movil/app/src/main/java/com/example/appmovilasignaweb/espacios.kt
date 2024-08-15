package com.example.appmovilasignaweb

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class espacios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_espacios)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun ircambiarcontra(view: View) {
        var intent = Intent(application, Cambiarcontra::class.java)
        startActivity(intent)
    }
    fun volver(view: View) {
        var intent = Intent(application, inicio_sesion::class.java)
        startActivity(intent)
    }
    fun irmodificardatos(view: View) {
        var intent = Intent(application, modificar_datos::class.java)
        startActivity(intent)
    }
    fun irmiperfil(view: View) {
        var intent = Intent(application, miperfil::class.java)
        startActivity(intent)
    }
    fun irreserva(view: View) {
        var intent = Intent(application, Reserva::class.java)
        startActivity(intent)
    }

}