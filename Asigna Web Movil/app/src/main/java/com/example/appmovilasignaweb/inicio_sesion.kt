package com.example.appmovilasignaweb

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class inicio_sesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_sesion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun volver(view: View) {
        var intent = Intent(application, MainActivity::class.java)
        startActivity(intent)
    }

    fun irrecuperarcontra(view: View) {
        var intent = Intent(application, recuperar_contra::class.java)
        startActivity(intent)
    }

    fun irespacio(view: View) {
        var intent = Intent(application, espacios::class.java)
        startActivity(intent)
    }
}