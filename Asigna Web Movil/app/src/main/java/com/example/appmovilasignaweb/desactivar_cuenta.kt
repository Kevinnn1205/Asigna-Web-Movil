package com.example.appmovilasignaweb

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONException

class desactivar_cuenta : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var id_user: String = ""

    // Cola de solicitudes Volley
    private val requestQueue by lazy { Volley.newRequestQueue(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desactivar_cuenta)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MiAppPreferences", MODE_PRIVATE)

        // Obtener los datos del usuario
        obtenerDatosUsuario()

        // Inicializar botones
        val btnDesactivar: Button = findViewById(R.id.btnGuardar)
        val btnVolver: ImageView = findViewById(R.id.imageView3)

        // Configurar el listener del botón de desactivar cuenta
        btnDesactivar.setOnClickListener { irDesactivarCuenta(it) }

        // Configurar el listener del botón de volver
        btnVolver.setOnClickListener { volver() }
    }

    // Método para obtener datos del usuario
    private fun obtenerDatosUsuario() {
        val urlDatosUsuario = config.urlProfile
        val token = sharedPreferences.getString("TOKEN", null)

        if (token.isNullOrEmpty()) {
            Log.e("Error", "Token no encontrado")
            return
        }

        val queue = Volley.newRequestQueue(this)

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, urlDatosUsuario, null,
            Response.Listener { response ->
                try {
                    // Obtener el id_user del objeto JSON
                    id_user = response.optString("id_user", "No disponible")
                    // Almacenar id_user en SharedPreferences si es necesario
                    sharedPreferences.edit().putString("ID_USER", id_user).apply()

                } catch (e: JSONException) {
                    Log.e("Error JSON", "Error al analizar los datos del usuario: ${e.message}")
                    Toast.makeText(this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("Error Volley", "Error al recuperar los datos del usuario: ${error.message}")
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        // Agregar la solicitud a la RequestQueue
        queue.add(jsonObjectRequest)
    }

    // Método para desactivar la cuenta
    private fun desactivarCuenta(token: String) {
        // Usar la URL dinámica desde config
        val url = "${config.urlDesactivarCuenta}/$id_user"

        // Crear la solicitud PUT
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.PUT, url, null,
            Response.Listener { response ->
                try {
                    // Procesar la respuesta del servidor
                    val estado = response.getString("estado")
                    Toast.makeText(this, "Cuenta desactivada: $estado", Toast.LENGTH_LONG).show()

                    // Redirigir al usuario a la pantalla de inicio de sesión o MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish() // Cerrar la actividad actual
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al desactivar la cuenta", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            // Agregar los headers para la autenticación (por ejemplo, token JWT)
            override fun getHeaders(): Map<String, String> {
                return mapOf("Authorization" to "Bearer $token")
            }
        }

        // Añadir la solicitud a la cola
        requestQueue.add(jsonObjectRequest)
    }

    // Método para el botón de desactivar cuenta
    fun irDesactivarCuenta(view: View) {
        val token = sharedPreferences.getString("TOKEN", null)

        if (token != null) {
            if (id_user.isNotEmpty()) {
                desactivarCuenta(token) // Llamar a la función para desactivar la cuenta
            } else {
                Toast.makeText(this, "Error: ID de usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Error: Sesión no válida", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para volver
    private fun volver() {
        finish() // Cierra la actividad actual
    }
}
