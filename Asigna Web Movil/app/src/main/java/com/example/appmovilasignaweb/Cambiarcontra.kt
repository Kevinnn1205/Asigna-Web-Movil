package com.example.appmovilasignaweb

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.example.appmovilasignaweb.config.config // Importa la clase config para la URL
import com.example.appmovilasignaweb.config.config.Companion.urlCambiarcontra
import java.util.regex.Pattern

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
                cambiarContrasena()
            }
        }
    }

    // Función para validar los campos (misma lógica que el backend)
    private fun validarCampos(): Boolean {
        val passwordActual = editTextPasswordActual.text.toString()
        val nuevaPassword = editTextNuevaPassword.text.toString()
        val confirmarPassword = editTextConfirmarPassword.text.toString()

        if (TextUtils.isEmpty(passwordActual)) {
            editTextPasswordActual.error = "Ingrese su contraseña actual"
            return false
        }

        // Validar que la nueva contraseña no sea igual a la actual
        if (passwordActual == nuevaPassword) {
            editTextNuevaPassword.error = "La nueva contraseña no puede ser igual a la anterior"
            return false
        }

        // Validar que la nueva contraseña tenga al menos 8 caracteres, una mayúscula, un número y un carácter especial
        if (!esContrasenaValida(nuevaPassword)) {
            editTextNuevaPassword.error = "La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula, un número y un carácter especial."
            return false
        }

        // Validar que las contraseñas coincidan
        if (nuevaPassword != confirmarPassword) {
            editTextConfirmarPassword.error = "Las contraseñas no coinciden"
            return false
        }

        return true
    }

    // Validaciones de seguridad similares a las del backend
    private fun esContrasenaValida(contrasena: String): Boolean {
        val longitudMinima = contrasena.length >= 8
        val tieneMayuscula = contrasena.any { it.isUpperCase() }
        val tieneNumero = contrasena.any { it.isDigit() }
        val tieneCaracterEspecial = Pattern.compile("[!@#\$%^&*(),.?\":{}|<>]").matcher(contrasena).find()

        return longitudMinima && tieneMayuscula && tieneNumero && tieneCaracterEspecial
    }

    // Función para enviar la solicitud de cambio de contraseña al backend
    private fun cambiarContrasena() {
        val url = urlCambiarcontra // Utilizamos la URL desde la configuración

        val params = JSONObject()
        params.put("nuevaContrasena", editTextNuevaPassword.text.toString())
        params.put("confirmarContrasena", editTextConfirmarPassword.text.toString())

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.PUT,
            url,
            params,
            Response.Listener { response ->
                val message = response.optString("message", "Contraseña cambiada exitosamente.")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                // Redirigir a la pantalla de inicio de sesión o espacios si es necesario
                val intent = Intent(this, espacios::class.java)
                startActivity(intent)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error al cambiar la contraseña: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                val token = obtenerToken() // Método para obtener el token almacenado
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }

        // Añadir la solicitud a la cola
        val queue = Volley.newRequestQueue(this)
        queue.add(jsonObjectRequest)
    }

    // Función para obtener el token almacenado
    private fun obtenerToken(): String {
        val sharedPreferences = getSharedPreferences("MiAppPreferences", MODE_PRIVATE)
        return sharedPreferences.getString("TOKEN", "") ?: "" // Retorna el token guardado
    }

    // Función para volver a la pantalla de espacios
    fun volver(view: View) {
        val intent = Intent(this, espacios::class.java)
        startActivity(intent)
    }
}
