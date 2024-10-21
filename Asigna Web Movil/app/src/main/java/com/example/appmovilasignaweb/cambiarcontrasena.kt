package com.example.appmovilasignaweb

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONObject

class cambiarcontrasena : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiarcontrasena)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MiAppPreferences", MODE_PRIVATE)

        val nuevaContrasenaInput = findViewById<EditText>(R.id.nuevaContrasena)
        val confirmarContrasenaInput = findViewById<EditText>(R.id.confirmarContrasena)
        val botonCambiar = findViewById<Button>(R.id.btnGuardar)

        botonCambiar.setOnClickListener {
            val nuevaContrasena = nuevaContrasenaInput.text.toString()
            val confirmarContrasena = confirmarContrasenaInput.text.toString()

            if (nuevaContrasena.isNotEmpty() && confirmarContrasena.isNotEmpty()) {
                CambiarContrasena(nuevaContrasena, confirmarContrasena)
            } else {
                Toast.makeText(this, "Por favor llena ambos campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun CambiarContrasena(nuevaContrasena: String, confirmarContrasena: String) {
        // Usar la URL desde config
        val url = config.urlCambiarContrasena

        val params = JSONObject()
        params.put("nuevaContrasena", nuevaContrasena)
        params.put("confirmarContrasena", confirmarContrasena)

        val request = object : JsonObjectRequest(
            Request.Method.PUT,
            url,
            params,
            { response ->
                // Mostrar alerta de éxito
                mostrarAlertaExito()
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {

            // Aquí agregamos el token en la cabecera
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                val token = obtenerToken() // Método para obtener el token almacenado
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }

    private fun obtenerToken(): String {
        // Obtener el token almacenado en SharedPreferences
        return sharedPreferences.getString("token", "") ?: ""
    }

    private fun mostrarAlertaExito() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Éxito")
        builder.setMessage("Contraseña cambiada exitosamente.")
        builder.setPositiveButton("Aceptar") { dialog: DialogInterface, _: Int ->
            // Redirigir a la pantalla de espacios
            val intent = Intent(this, espacios::class.java)
            startActivity(intent)
            finish()
        }
        builder.setCancelable(false)
        builder.show()
    }
}
