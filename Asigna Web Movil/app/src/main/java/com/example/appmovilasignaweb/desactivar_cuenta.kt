package com.example.appmovilasignaweb

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONException
import org.json.JSONObject

class desactivar_cuenta : AppCompatActivity() {

    // Cola de solicitudes Volley
    private val requestQueue by lazy { Volley.newRequestQueue(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desactivar_cuenta)

        // Aquí puedes inicializar cualquier vista o lógica que necesites al crear la actividad
    }

    // Método para desactivar la cuenta
    private fun desactivarCuenta(token: String, idUser: String) {
        // Usar la URL dinámica desde config
        val url = config.urlDesactivarCuenta

        // Crear la solicitud PUT
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.PUT, url, null,
            Response.Listener { response ->
                try {
                    // Procesar la respuesta del servidor
                    val estado = response.getString("estado")
                    Toast.makeText(this, "Cuenta desactivada: $estado", Toast.LENGTH_LONG).show()

                    // Redirigir al usuario a la pantalla de inicio de sesión o MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
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
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Header con el token
                return headers
            }
        }

        // Añadir la solicitud a la cola
        requestQueue.add(jsonObjectRequest)
    }

    // Método para el botón de desactivar cuenta
    fun irDesactivarCuenta(view: View) {
        // Obtener el token de SharedPreferences
        val sharedPreferences = getSharedPreferences("MiAppPreferences", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)

        if (token != null) {
            // Obtener el ID del usuario de SharedPreferences o de otra fuente
            val idUser = sharedPreferences.getString("ID_USER", null)
            if (idUser != null) {
                desactivarCuenta(token, idUser) // Llamar a la función para desactivar la cuenta
            } else {
                Toast.makeText(this, "Error: ID de usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Error: Sesión no válida", Toast.LENGTH_SHORT).show()
        }
    }
}
