package com.example.appmovilasignaweb

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config

class desactivar_cuenta : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desactivar_cuenta)

        // Almacenar la ID del usuario en SharedPreferences (por ejemplo, después del inicio de sesión)
        // Esto se hace aquí solo como ejemplo. En tu caso, ya debes haber guardado la ID del usuario después del login.
        val userId = "12345"  // ID del usuario obtenida después del login (ejemplo)
        guardarIdUsuario(userId)
    }

    // Función para guardar la ID del usuario en SharedPreferences
    private fun guardarIdUsuario(userId: String) {
        val prefs = getSharedPreferences("userSession", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("USER_ID", userId)
        editor.apply()
    }

    // Función para obtener la ID del usuario desde SharedPreferences
    private fun obtenerIdUsuario(): String? {
        val prefs = getSharedPreferences("userSession", MODE_PRIVATE)
        return prefs.getString("USER_ID", null)
    }

    // Función para obtener el token desde SharedPreferences
    private fun obtenerToken(): String? {
        val prefs = getSharedPreferences("userSession", MODE_PRIVATE)
        return prefs.getString("TOKEN", null) // Asegúrate de que el token se guarde aquí
    }

    fun volver(view: View) {
        val intent = Intent(application, miperfil::class.java)
        startActivity(intent)
    }

    // Función para desactivar la cuenta
    fun desactivarCuenta(view: View) {
        // Obtener la ID del usuario desde SharedPreferences
        val userId = obtenerIdUsuario()
        val token = obtenerToken()  // Obtener el token

        if (userId != null && token != null) {
            // URL para desactivar la cuenta
            val url = "${config.urlDesactivarCuenta}/$userId"

            // Crear la petición PUT con Volley
            val requestQueue = Volley.newRequestQueue(this)
            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                { response ->
                    // Manejar la respuesta del servidor
                    Toast.makeText(this, "Cuenta desactivada: ${response.getString("estado")}", Toast.LENGTH_LONG).show()

                    // Redirigir al usuario a otra pantalla después de desactivar la cuenta
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                },
                { error ->
                    // Manejar errores
                    Toast.makeText(this, "Error al desactivar cuenta: ${error.message}", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token" // Agrega el token en el encabezado
                    return headers
                }
            }

            // Agregar la petición a la cola
            requestQueue.add(jsonObjectRequest)
        } else {
            Toast.makeText(this, "No se pudo obtener la ID del usuario o el token.", Toast.LENGTH_LONG).show()
        }
    }
}
