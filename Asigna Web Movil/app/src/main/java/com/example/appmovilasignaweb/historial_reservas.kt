package com.example.appmovilasignaweb

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class historial_reservas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial_reservas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun irModuloInformacion(view: View) {
        val intent = Intent(this, espacios::class.java)
        startActivity(intent)
    }

    fun volver(view: View) {
        val intent = Intent(this, espacios::class.java)
        startActivity(intent)
    }

    fun irAgregarReserva(view: View) {
        val intent = Intent(this, Contenedor_crear_reserva::class.java)
        startActivity(intent)
    }

    fun irMiPerfil(view: View) {
        val intent = Intent(this, miperfil::class.java)
        startActivity(intent)
    }
}