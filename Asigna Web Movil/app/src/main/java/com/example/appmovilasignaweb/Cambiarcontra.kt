package com.example.appmovilasignaweb

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Cambiarcontra : AppCompatActivity() {

    private lateinit var editTextPasswordActual: EditText
    private lateinit var editTextNuevaPassword: EditText
    private lateinit var editTextConfirmarPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cambiarcontra)

        // Configuración de los insets para la vista
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de los campos de contraseña
        editTextPasswordActual = findViewById(R.id.editTextTextPassword1)
        editTextNuevaPassword = findViewById(R.id.editTextTextPassword)
        editTextConfirmarPassword = findViewById(R.id.editTextTextPassword2)

        // Configuración del botón enviar
        val btnGuardar = findViewById<View>(R.id.btnGuardar)
        btnGuardar.setOnClickListener {
            if (validarCampos()) {
                // Aquí puedes agregar la lógica para guardar la nueva contraseña
                Toast.makeText(this, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para validar los campos
    private fun validarCampos(): Boolean {
        val passwordActual = editTextPasswordActual.text.toString()
        val nuevaPassword = editTextNuevaPassword.text.toString()
        val confirmarPassword = editTextConfirmarPassword.text.toString()

        if (TextUtils.isEmpty(passwordActual)) {
            editTextPasswordActual.error = "Ingrese su contraseña actual"
            return false
        }

        if (TextUtils.isEmpty(nuevaPassword)) {
            editTextNuevaPassword.error = "Ingrese una nueva contraseña"
            return false
        }

        if (nuevaPassword.length < 6) {
            editTextNuevaPassword.error = "La contraseña debe tener al menos 6 caracteres"
            return false
        }

        if (!nuevaPassword.equals(confirmarPassword)) {
            editTextConfirmarPassword.error = "Las contraseñas no coinciden"
            return false
        }

        return true
    }

    // Función para volver a la pantalla de espacios
    fun volver(view: View) {
        val intent = Intent(this, espacios::class.java)
        startActivity(intent)
    }
}
