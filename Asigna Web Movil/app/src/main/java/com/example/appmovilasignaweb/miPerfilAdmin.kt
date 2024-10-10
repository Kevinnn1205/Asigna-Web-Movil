package com.example.appmovilasignaweb

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class miPerfilAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mi_perfil_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Lógica para el clic en "Cerrar sesión"
        val cerrarSesion = findViewById<TextView>(R.id.textView15)
        cerrarSesion.setOnClickListener {
            val intent = Intent(applicationContext, inicio_sesion::class.java) // Asegúrate de que LoginActivity es la pantalla de inicio de sesión
            startActivity(intent)
            finish() // Cierra la actividad actual para evitar que el usuario regrese a esta pantalla
        }
    }
    fun volver(view: View) {
        val intent = Intent(application, moduloInformacionAdmin::class.java)
        startActivity(intent)
    }

    fun irdesactivarcuenta(view: View) {
        val intent = Intent(application, desactivarCuentaAdmin::class.java)
        startActivity(intent)
    }

    fun irCrearCuenta(view: View) {
        val intent = Intent(application, crear_cuenta::class.java)
        startActivity(intent)
    }

    fun irCambiarContrasena(view: View) {
        val intent = Intent(application, CambiarcontraAdmin::class.java)
        startActivity(intent)
    }

}